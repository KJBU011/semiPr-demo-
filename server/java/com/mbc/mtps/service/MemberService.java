package com.mbc.mtps.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.MemberDao;
import com.mbc.mtps.dto.CarStatusDto;
import com.mbc.mtps.dto.MemberDto;

@Service
@Transactional
public class MemberService {

    final MemberDao dao;

    MemberService(MemberDao dao) {
        this.dao = dao;
    }

//=============================== 로그인/회원가입 페이지 =================================
    
    // 아이디 중복체크
    public boolean idCheck(String id) {
        return dao.getMember(id) == null; 
    }
    
    // 회원가입
    public boolean addMember(MemberDto dto) {
    	
    	int count = dao.addMember(dto);
        return count>0;
    }
    
    // 로그인
    public MemberDto login(MemberDto dto) {
    	
    	MemberDto login = dao.login(dto);
    	
    	/*
    	if (login != null) {
            login.setPw(null); // 비밀번호는 응답에 노출하지 않음
        }
        */
        return login;
    }

//=============================== 마이 페이지 =================================
    
 // 마이페이지 회원정보
    public MemberDto getMember(String id) {

        return dao.getMember(id);
    }


    // 현재 차량 상태와 주차 위치 조회
    public CarStatusDto getCarStatus(String id) {

        CarStatusDto dto =
                dao.getCarStatus(id);

        /*
         * 입출차 기록이 없으면
         * Controller에서 출차 상태로 처리
         */
        if (dto == null) {
            return null;
        }


        /*
         * 점주 차량은 정기차량이므로
         * 주차시간과 할인 여부에 관계없이
         * 예상 주차요금 0원
         */
        if (dto.getAuth() == 2) {

            dto.setCurrent_cost(0);

            /*
             * 출차 상태가 아니고
             * 입차시간이 있으면 현재 주차시간 계산
             */
            if (dto.getEnt_time() != null
                    && dto.getCar_stat() != 2) {

                LocalDateTime entranceTime =
                        dto.getEnt_time()
                           .toLocalDateTime();

                LocalDateTime currentTime =
                        LocalDateTime.now();

                long totalMinutes =
                        Duration.between(
                                entranceTime,
                                currentTime)
                                .toMinutes();

                if (totalMinutes < 0) {
                    totalMinutes = 0;
                }

                dto.setParking_minutes(
                        (int) totalMinutes);
            }

            return dto;
        }


        /*
         * 출차 상태는 현재 주차 중이 아니므로
         * 현재 주차시간과 예상요금 0
         */
        if (dto.getCar_stat() == 2) {

            dto.setParking_minutes(0);
            dto.setCurrent_cost(0);

            return dto;
        }


        Timestamp entTime =
                dto.getEnt_time();

        // 입차시간이 없는 경우
        if (entTime == null) {

            dto.setParking_minutes(0);
            dto.setCurrent_cost(0);

            return dto;
        }


        /*
         * 입차시간부터 현재시간까지
         * 총 주차시간 계산
         */
        LocalDateTime entranceTime =
                entTime.toLocalDateTime();

        LocalDateTime currentTime =
                LocalDateTime.now();

        long totalMinutes =
                Duration.between(
                        entranceTime,
                        currentTime)
                        .toMinutes();

        if (totalMinutes < 0) {
            totalMinutes = 0;
        }

        dto.setParking_minutes(
                (int) totalMinutes);


        /*
         * 입차 후 15분 이내면
         * 회차 차량으로 요금 0원
         */
        if (totalMinutes <= 15) {

            dto.setCurrent_cost(0);

            return dto;
        }


        long chargeMinutes =
                totalMinutes;

        /*
         * discnt_at에 값이 있으면
         * 점주가 2시간 할인을 적용한 상태
         */
        if (dto.getDiscnt_at() != null) {

            chargeMinutes =
                    chargeMinutes - 120;
        }


        // 할인 후 유료시간이 없는 경우
        if (chargeMinutes <= 0) {

            dto.setCurrent_cost(0);

            return dto;
        }


        /*
         * 남은 시간을 1시간 단위로 올림
         *
         * 1~60분   = 1시간
         * 61~120분 = 2시간
         */
        int chargeHours =
                (int) Math.ceil(
                        chargeMinutes / 60.0);


        // 시간당 2,000원
        int currentCost =
                chargeHours * 2000;

        dto.setCurrent_cost(currentCost);

        return dto;
    }


    // 내 차량 이용내역
    public List<CarStatusDto> getParkingHistory(
            String id) {

        return dao.getParkingHistory(id);
    }


    // 비밀번호 변경
    public int updatePw(MemberDto dto) {

        return dao.updatePw(dto);
    }


    // 전화번호 변경
    public int updatePhone(MemberDto dto) {

        return dao.updatePhone(dto);
    }


    // 이메일 변경
    public int updateEmail(MemberDto dto) {

        return dao.updateEmail(dto);
    }


    // 차량정보 변경
    public int updateCar(MemberDto dto) {

        return dao.updateCar(dto);
    }
    
//================================== 점주 페이지 ===================================
    
    
}