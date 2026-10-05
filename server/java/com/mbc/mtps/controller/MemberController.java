package com.mbc.mtps.controller;

import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.CarStatusDto;
import com.mbc.mtps.dto.MemberDto;
import com.mbc.mtps.service.MemberService;

@RestController
public class MemberController {

    final MemberService service;

    MemberController(MemberService service) {
        this.service = service;
    }
    
//================================== 마이페이지 ======================================    
    
    // 아이디 중복체크
    @PostMapping("idcheck")
    public boolean idcheck(String id) {
        System.out.println("MemberController idcheck " + new Date());
        return service.idCheck(id);
    }

    // 회원가입
    @PostMapping("addMember")
    public boolean addMember(MemberDto dto) {
        System.out.println("MemberController addmember " + new Date());

        return service.addMember(dto);
    }

 // 로그인
    @PostMapping("login")
    public MemberDto login(MemberDto dto) {
        System.out.println("MemberController login " + new Date());
        return service.login(dto);
    }

//================================== 마이페이지 ======================================
 // 마이페이지 회원정보
    @GetMapping("getMember")
    public MemberDto getMember(String id) {
        System.out.println("MemberController getMember " + new Date());
        return service.getMember(id);
    }


    // 현재 차량 상태와 주차 위치 조회
    @GetMapping("getCarStatus")
    public CarStatusDto getCarStatus(String id) {

        CarStatusDto carStatusDto =
                service.getCarStatus(id);

        /*
         * 입출차 기록이 한 번도 없으면
         * 기본 상태를 출차로 설정
         */
        if (carStatusDto == null) {

            carStatusDto = new CarStatusDto();

            carStatusDto.setCar_stat(2);
            carStatusDto.setCar_status("출차");

            carStatusDto.setFloor(null);
            carStatusDto.setSpc_no(null);
            carStatusDto.setEnt_time(null);
            carStatusDto.setDiscnt_at(null);

            carStatusDto.setParking_minutes(0);
            carStatusDto.setCurrent_cost(0);

            return carStatusDto;
        }

        // 0 = 입차
        if (carStatusDto.getCar_stat() == 0) {

            carStatusDto.setCar_status("입차");

        // 1 = 주차
        } else if (carStatusDto.getCar_stat() == 1) {

            carStatusDto.setCar_status("주차");

        // 2 = 출차
        } else if (carStatusDto.getCar_stat() == 2) {

            carStatusDto.setCar_status("출차");

            carStatusDto.setFloor(null);
            carStatusDto.setSpc_no(null);

            carStatusDto.setParking_minutes(0);
            carStatusDto.setCurrent_cost(0);

        // 3 = 정산
        } else if (carStatusDto.getCar_stat() == 3) {

            carStatusDto.setCar_status("정산");

        } else {

            carStatusDto.setCar_status("상태 확인 불가");

            carStatusDto.setFloor(null);
            carStatusDto.setSpc_no(null);

            carStatusDto.setParking_minutes(0);
            carStatusDto.setCurrent_cost(0);
        }

        return carStatusDto;
    }


    // 내 차량 이용내역
    @GetMapping("getParkingHistory")
    public List<CarStatusDto> getParkingHistory(String id) {

        /*
         * ent_time  : 입차시간 내역
         * ex_time   : 출차시간 내역
         * cost      : 확정 주차비
         * discnt_at : 할인 적용시간
         */

        return service.getParkingHistory(id);
    }


    // 비밀번호 변경
    @PostMapping("updatePw")
    public boolean updatePw(MemberDto dto) {

        System.out.println(
                "MemberController updatePw " + new Date());

        int count = service.updatePw(dto);

        return count > 0;
    }


    // 전화번호 변경
    @PostMapping("updatePhone")
    public boolean updatePhone(MemberDto dto) {

        System.out.println(
                "MemberController updatePhone " + new Date());

        int count = service.updatePhone(dto);

        return count > 0;
    }


    // 이메일 변경
    @PostMapping("updateEmail")
    public boolean updateEmail(MemberDto dto) {

        System.out.println(
                "MemberController updateEmail " + new Date());

        int count = service.updateEmail(dto);

        return count > 0;
    }


    // 차량정보 변경
    @PostMapping("updateCar")
    public boolean updateCar(MemberDto dto) {

        System.out.println(
                "MemberController updateCar " + new Date());

        int count = service.updateCar(dto);

        return count > 0;
    }
    
//================================== 점주 페이지 =================================
    
    
    
}