package com.mbc.mtps.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.mbc.mtps.dto.CarStatusDto;

@Service
public class ChatService {

    final RestTemplate restTemplate;
    final ParkingService parkingService;
    final MemberService memberService; // [추가] 개인 주차 상태 조회용

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.api.model:gemini-flash-latest}")
    private String model;

    // [추가 2026-08-12] Gemini API가 503(UNAVAILABLE, 서버 과부하)일 때 재시도 관련 설정
    private static final int MAX_RETRIES = 3;          // 최초 시도 포함 총 3회
    private static final long RETRY_DELAY_MS = 1500L;  // 재시도 사이 대기시간 (1.5초)

    // 잘 안 바뀌는 정보(정책, 사이트 소개)는 서버 시작 시 한 번만 읽어서 보관
    private final String staticPrompt;

    public ChatService(RestTemplate restTemplate, ParkingService parkingService, MemberService memberService) {
        this.restTemplate = restTemplate;
        this.parkingService = parkingService;
        this.memberService = memberService;
        this.staticPrompt = loadSystemPrompt();
    }

    // resources 폴더의 chatbot-prompt.txt를 읽어서 문자열로 반환
    private String loadSystemPrompt() {
        try {
            ClassPathResource resource = new ClassPathResource("chatbot-prompt.txt");
            try (InputStream is = resource.getInputStream()) {
                return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            System.out.println("프롬프트 파일 로드 실패: " + e.getMessage());
            return "당신은 주차장 안내 챗봇입니다.";
        }
    }

    // 질문이 들어올 때마다, DB에서 지금 이 순간의 전체 주차 현황을 조회해서 텍스트로 만듦
    private String buildRealtimeInfo() {
        Map<String, Object> total = parkingService.getTotalParkingCount();
        List<Map<String, Object>> floors = parkingService.getFloorCount();

        StringBuilder sb = new StringBuilder();
        sb.append("\n[실시간 주차 현황 - 지금 이 순간 기준]\n");
        sb.append("전체: ").append(total).append("\n");
        sb.append("층별 빈자리: ").append(floors).append("\n");
        sb.append("이 실시간 정보를 참고해서 '지금 빈자리 있어요?' 같은 질문에 답하세요.\n");

        return sb.toString();
    }

    // [추가] 로그인한 회원의 현재 주차 상태/요금을 실시간으로 조회해서 텍스트로 만듦
    private String buildMyParkingInfo(String id) {
        if (id == null || id.isEmpty()) {
            return "\n[로그인 상태] 로그인하지 않은 사용자입니다. 개인 요금 문의는 로그인 후 이용해달라고 안내하세요.\n";
        }

        CarStatusDto dto = memberService.getCarStatus(id);

        int displayCost = dto.getCurrentCost();
        if (dto.getCarStat() == 3) {
            displayCost = dto.getCost();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n[이 사용자의 현재 주차 정보 - 실시간]\n");
        sb.append("상태: ").append(dto.getCarStatus()).append("\n");
        sb.append("현재 요금: ").append(displayCost).append("원\n");
        sb.append("이 정보를 참고해서 '내 요금 얼마야', '나 지금 주차 중이야?' 같은 질문에 답하세요.\n");

        return sb.toString();
    }

    // [수정] id 파라미터 추가
    // [추가 2026-08-12] 구간별 소요시간 로그 (프롬프트 구성 / DB 조회 / Gemini API 호출 어디서 지연되는지 확인용)
    public String askChatbot(String userMessage, String id) {
        long startTime = System.currentTimeMillis();

        // 키 미설정 시 API를 때리지 않고 안내 (서버는 정상 부팅됨)
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("ChatService: GEMINI_API_KEY 미설정 — 챗봇 비활성화 상태로 응답");
            return "지금 챗봇 기능이 준비 중입니다. 주차 조회·안내 메뉴를 이용해주세요.";
        }

        String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model + ":generateContent";

        List<Map<String, Object>> contents = new ArrayList<>();

        // 정적 정책 텍스트 + 전체 실시간 현황 + 이 사용자의 개인 주차 정보를 합쳐서 프롬프트로 사용
        String realtimeInfo = buildRealtimeInfo();
        String myParkingInfo = buildMyParkingInfo(id);
        long afterDbTime = System.currentTimeMillis(); // [추가 2026-08-12] DB 조회(buildRealtimeInfo + buildMyParkingInfo) 완료 시점

        String fullPrompt = staticPrompt + realtimeInfo + myParkingInfo;

        contents.add(buildMessage("user", fullPrompt));
        contents.add(buildMessage("model", "네, 안내 원칙과 현재 현황을 확인했습니다. 무엇을 도와드릴까요?"));
        contents.add(buildMessage("user", userMessage));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // [수정 2026-08-12] 503(서버 과부하) 시 자동 재시도하는 헬퍼 메서드로 교체
            Map<String, Object> response = callGeminiApiWithRetry(url, request);
            long afterApiTime = System.currentTimeMillis(); // [추가 2026-08-12] Gemini API 응답 수신 완료 시점

            String result = extractText(response);
            long endTime = System.currentTimeMillis(); // [추가 2026-08-12] 응답 파싱까지 전체 완료 시점

            // [추가 2026-08-12] 구간별 소요시간 로그 출력
            System.out.println("========== ChatService askChatbot 소요시간 ==========");
            System.out.println("프롬프트 길이(글자수): " + fullPrompt.length());
            System.out.println("DB 조회(주차 현황 + 개인 정보): " + (afterDbTime - startTime) + "ms");
            System.out.println("Gemini API 호출: " + (afterApiTime - afterDbTime) + "ms");
            System.out.println("응답 파싱: " + (endTime - afterApiTime) + "ms");
            System.out.println("전체 소요시간: " + (endTime - startTime) + "ms");
            System.out.println("=====================================================");

            return result;
        } catch (Exception e) {
            System.out.println("ChatService askChatbot 에러: " + e.getMessage());
            return "죄송합니다, 지금 챗봇 응답을 가져오지 못했습니다. 잠시 후 다시 시도해주세요.";
        }
    }

    // [추가 2026-08-12] Gemini API가 503(UNAVAILABLE, 일시적 서버 과부하)을 반환하면
    // 짧은 대기 후 최대 MAX_RETRIES회까지 자동으로 재시도한다.
    // 503이 아닌 다른 예외(401 인증오류, 400 잘못된 요청 등)는 재시도해도 의미가 없으므로 즉시 던진다.
    @SuppressWarnings("unchecked")
    private Map<String, Object> callGeminiApiWithRetry(String url, HttpEntity<Map<String, Object>> request) {
        int attempt = 0;

        while (true) {
            attempt++;
            try {
                return restTemplate.postForObject(url, request, Map.class);
            } catch (HttpServerErrorException e) {
                boolean isServiceUnavailable = e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE;
                boolean hasRetriesLeft = attempt < MAX_RETRIES;

                if (isServiceUnavailable && hasRetriesLeft) {
                    System.out.println("Gemini API 503(과부하) 응답, " + attempt + "번째 시도 실패. "
                            + RETRY_DELAY_MS + "ms 후 재시도합니다.");
                    sleep(RETRY_DELAY_MS);
                    continue; // 다음 시도로
                }
                // 503이 아니거나, 재시도 횟수를 다 썼으면 그대로 던져서 바깥 catch에서 처리
                throw e;
            }
        }
    }

    // [추가 2026-08-12] 재시도 대기용 sleep (인터럽트 발생 시 상태 복구 후 진행)
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private Map<String, Object> buildMessage(String role, String text) {
        Map<String, Object> part = new HashMap<>();
        part.put("text", text);

        Map<String, Object> message = new HashMap<>();
        message.put("role", role);
        message.put("parts", List.of(part));
        return message;
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<String, Object> response) {
        if (response == null) {
            return "응답을 받지 못했습니다.";
        }
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
        if (candidates == null || candidates.isEmpty()) {
            return "답변을 생성하지 못했습니다.";
        }
        Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
        return (String) parts.get(0).get("text");
    }
}