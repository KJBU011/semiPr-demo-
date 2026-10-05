package com.mbc.mtps.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mbc.mtps.dao.MemberDao;
import com.mbc.mtps.dao.SpaceDao;
import com.mbc.mtps.dto.CarStatusDto;
import com.mbc.mtps.dto.MemberDto;

@Service
@Transactional
public class MemberService {

    final MemberDao dao;
    final PasswordEncoder passwordEncoder; 
    final SpaceService spaceService;

    MemberService(MemberDao dao, PasswordEncoder passwordEncoder, SpaceService spaceService) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
        this.spaceService = spaceService;
    }

//=============================== 로그인/회원가입 페이지 =================================
    
    // 아이디 중복체크
    public boolean idCheck(String id) {
        return dao.getMember(id) != null ? true : false; 
    }
    
    // 회원가입
    public boolean addMember(MemberDto dto) {
    	
        dto.setPw(passwordEncoder.encode(dto.getPw()));
    	int count = dao.addMember(dto);
        return count>0;
    }
    
    // 로그인
    public MemberDto login(MemberDto dto) {
    	
    	MemberDto member = dao.login(dto.getId());   // ← service.login()에 dto 전체가 아니라 id만 전달

        if (member == null) {
            return null;   // 아이디 없음
        }
        if (!passwordEncoder.matches(dto.getPw(), member.getPw())) {   // ← 입력한 비밀번호와 암호화된 값 비교
            return null;   // 비밀번호 틀림
        }

        member.setPw(null);   // ← 응답에 암호화된 비밀번호가 노출되지 않도록 제거
    	
        return member;
    }

    
//=============================== 마이 페이지 =================================
    
    // 마이페이지 회원정보
    public MemberDto getMember(String id) {

        return dao.getMember(id);
    }


    // 현재 차량 상태와 주차 위치 조회
    public CarStatusDto getCarStatus(String id) {

        CarStatusDto dto = dao.getCarStatus(id);

        // 입출차 기록이 한 번도 없으면 기본 상태를 출차로 설정
        if (dto == null) {
            dto = new CarStatusDto();
            dto.setCarStat(2);
            dto.setCarStatus("출차");
            dto.setFloor(null);
            dto.setSpcNo(null);
            dto.setEntTime(null);
            dto.setDiscntAt(null);
            dto.setParkingMinutes(0);
            dto.setCurrentCost(0);
            return dto;
        }

        // 차량 상태 문자열 설정
        if (dto.getCarStat() == 0) {
            dto.setCarStatus("입차");
        } else if (dto.getCarStat() == 1) {
            dto.setCarStatus("주차");
        } else if (dto.getCarStat() == 2) {
            dto.setCarStatus("출차");
            dto.setFloor(null);
            dto.setSpcNo(null);
            dto.setParkingMinutes(0);
            dto.setCurrentCost(0);
            return dto;
        } else if (dto.getCarStat() == 3) {
            dto.setCarStatus("정산");
        } else {
            dto.setCarStatus("상태 확인 불가");
            dto.setFloor(null);
            dto.setSpcNo(null);
            dto.setParkingMinutes(0);
            dto.setCurrentCost(0);
            return dto;
        }

        // 점주 차량은 정기차량이므로 예상 주차요금 0원
        if (dto.getAuth() == 2) {
            dto.setCurrentCost(0);

            if (dto.getEntTime() != null) {
                LocalDateTime entranceTime = dto.getEntTime().toLocalDateTime();
                LocalDateTime currentTime = LocalDateTime.now();
                long totalMinutes = Duration.between(entranceTime, currentTime).toMinutes();

                if (totalMinutes < 0) {
                    totalMinutes = 0;
                }

                dto.setParkingMinutes((int) totalMinutes);
            }

            return dto;
        }

        Timestamp entTime = dto.getEntTime();

        // 입차시간이 없으면 주차시간과 요금 0
        if (entTime == null) {
            dto.setParkingMinutes(0);
            dto.setCurrentCost(0);
            return dto;
        }

        // 현재까지 총 주차시간 계산
        LocalDateTime entranceTime = entTime.toLocalDateTime();
        LocalDateTime currentTime = LocalDateTime.now();
        long totalMinutes = Duration.between(entranceTime, currentTime).toMinutes();

        if (totalMinutes < 0) {
            totalMinutes = 0;
        }

        dto.setParkingMinutes((int) totalMinutes);

        
        if(dto.getDiscntAt() == null || dto.getDiscntAt().equals("")){
        	dto.setCurrentCost(Math.round((dto.getParkingMinutes() - 15) / 60 * 20) * 100);
            } else {
              int totalCost = Math.round((dto.getParkingMinutes() - 120 - 15) / 60 * 20) * 100;
              if(totalCost > 0){
                dto.setCurrentCost(totalCost);
              } else{
            	  dto.setCurrentCost(0);
              }
            }

        return dto;
    }


    // 내 차량 이용내역
    // [수정] 내 차량 이용내역 (진짜 페이징)
    public List<CarStatusDto> getParkingHistory(String id, int pageNum) {

        return dao.getParkingHistory(id, pageNum);
    }

    // [추가] 내 차량 이용내역 전체 건수
    public int getParkingHistoryCount(String id) {

        return dao.getParkingHistoryCount(id);
    }
    
    // [8/7 추가] 주차 상태 변경. carStat 값에 따라 정산 처리 또는 자동 출차 처리로 내부 분기.
    // (정산: carStat=3, 자동출차: carStat=2)
    /* MemberController에서 부를 API 엔드포인트를 settleParking, autoExitParking 두 개로 따로 만들지 않고, 
		updateCarStat 하나로 합친 이 메소드가 바로 그 "합쳐진 진입점" 역할을 한다.
		Dao는 여전히 2개(settleParking, autoExitParking)로 나눠져 있지만, 바깥에서 부르는 창구는 이 메소드 하나로 통일*/
    
    // CarStat 값으로 분기 요약 : API 1개(updateCarStat)로 정산/출차 두 기능을 처리.
    public boolean updateCarStat(String id, int carStat) {

    	// 정산 처리
    	if (carStat == 3) {
    		// 왜 새로 요금을 계산 안 하고 getCarStatus()를 재사용했는지?
    		// getCarStatus() 안에는 이미 복잡한 요금 계산 로직이 다 구현되어 있어, 그 계산 로직을 또 새로 짜지 않고 그대로 재사용
    		
    		// getCarStatus(id) 재사용 요약 : 	요금 계산 로직 중복 방지, 정책 변경 시 한 곳만 수정하면 되게
    		CarStatusDto dto = getCarStatus(id); // 이미 있는 조회+요금계산 로직 재사용

    		// 조회가 안 되거나 입차/주차 상태가 아니면 정산 대상 없음
    		// "정산할 자격이 있는 상태인지" 검증하는 방어 코드.
    		// null이면 애초에 입출차 기록 자체가 없으니 정산할 게 없음.
    		// CarStat가 0(입차)도 1(주차)도 아니면(이미 출차 or 이미 정산) : 중복 정산이나 잘못된 상태 전환을 막기 위해 여기서 걸러냄.
    		// 이게 없으면, 예를 들어 이미 정산 완료(3)된 걸 실수로 또 정산 API를 부르면 "금액이 또 덮어써지는 이상힌 일"이 생길 수 있다.
    		
    		// dto == null || CarStat 검증 요약 : 잘못된 상태에서 중복/오류 정산 방지
    		if (dto == null || (dto.getCarStat() != 0 && dto.getCarStat() != 1)) {
    			return false;
    		}
    		//검증을 통과하면, getCarStat()가 계산해준 currentCost(현재 예상 요금)를 그대로 settleParking에 넘겨서 확정 금액으로 저장함.
    		return dao.settleParking(id, dto.getCurrentCost()) > 0;

    	// 자동 출차 처리
    	} else if (carStat == 2) {
    		// 별도의 검증 없이 바로 autoExitParking(id)를 호출하도록 되어있다.
    		// 왜 정산처럼 미리 조건을 체크 안했냐면, autoExitParking의 SQL자체가 where car_stat = 3을 조건으로 걸고 있어서,
    		// "정산 상태가 아니면 애초에 SQL에서 아무 행도 안걸려서 자동으로 count = 0이 나오게 되어있다." 
    		// 즉 SQL쪽에서 이미 방어가 되어 있어서, 자바 코드에서 또 검증할 필요가 없었던 것.

    		// autoExitParking으로 car_stat이 2로 바뀌면 getCarStatus()가 spcNo를 null 처리하므로,
    		// 자리 반납에 쓸 spcNo는 상태가 바뀌기 전(car_stat=3)에 미리 조회해둔다.
    		CarStatusDto dto = getCarStatus(id);

    		int count = dao.autoExitParking(id);
    		System.out.println("spcNo : " + dto.getSpcNo());
    		// 출차 성공 시, 사용 중이던 자리(space)를 빈 자리로 반납
    		if (count > 0 && dto != null && dto.getSpcNo() != null) {
    			
    			spaceService.release(dto.getSpcNo());
    		}

    		return count > 0;
    	}

    	// 그 외 값이면 처리 대상 아님
    	// 방어적 기본값
    	// carStat으로 3도 2도 아닌 엉뚱한 값이 들어오면, 아무것도 처리하지 않고 그냥 실패로 응답하게 해두는 안전장치
    	
    	// return false 요약 : 예상 못한 값이 들어와도 안전하게 실패 처리
    	return false;
    }

    // 비밀번호 변경
    public boolean updatePw(String id, String currentPw, String newPw) {
    	// 1. 현재 회원 정보(암호화된 pw 포함) 조회
        MemberDto member = dao.login(id);   // login에서 만든 id-only 조회 메소드 재사용 가능

        if (member == null) {
            return false;   // 회원 없음
        }

        // 2. 입력한 현재 비밀번호가 맞는지 확인
        if (!passwordEncoder.matches(currentPw, member.getPw())) {
            return false;   // 현재 비밀번호 불일치 → 변경 거부
        }

        // 3. 검증 통과하면 새 비밀번호 암호화해서 업데이트
        MemberDto dto = new MemberDto();
        dto.setId(id);
        dto.setPw(passwordEncoder.encode(newPw));

        int count = dao.updatePw(dto);

        return count > 0;
    }

    // 전화번호 변경
    public boolean updatePhone(MemberDto dto) {

        int count = dao.updatePhone(dto);
        
        return count > 0 ? true:false;
    }


    // 이메일 변경
    public boolean updateEmail(MemberDto dto) {
    	
    	 int count = dao.updateEmail(dto);
    	
    	 return count > 0 ? true:false;
    }


    // 차량정보 변경
    public boolean updateCar(MemberDto dto) {
    	
    	int count = dao.updateCar(dto);
    	
    	return count > 0 ? true:false;
    }
    
//================================== 점주 페이지 ===================================
    
//================================== 관리자 페이지 ===================================  
    
    // 점주 목록 조회
    public List<MemberDto> OwnerList(String category, String keyword, int pageNum) {
        return dao.OwnerList(category, keyword, pageNum);
    }
    
    // 일반 회원 목록 조회
    public List<MemberDto> MemberList(String category, String keyword, int pageNum) {
        return dao.MemberList(category, keyword, pageNum);
    }
    
    // 점주 회원 수
    public int ownerCount(String category, String keyword) {
        return dao.ownerCount(category, keyword);
    }
    
    // 일반 회원 수
    public int memberCount(String category, String keyword) {
        return dao.memberCount(category, keyword);
    }
    
    // 회원정보 수정
    public boolean updateMemberByAdmin(MemberDto dto) {

        int count = dao.updateMemberByAdmin(dto);
        
        return count > 0 ? true : false;
    }
    
    // 회원 삭제 (일반회원/점주 공용) : 소프트 삭제
    // id, pw를 각각 무작위 문자열(UUID)로 교체(탈퇴한 아이디 재사용 허용, 로그인 불가 처리) + auth=0
    public boolean deleteMember(String id) {

        String del = UUID.randomUUID().toString();

        Map<String, String> params = new HashMap<>();
        params.put("old", id);
        params.put("new", del);

        int count = dao.deleteMember(params);
        return count > 0 ? true : false;
    }
    
    
}