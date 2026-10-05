package com.mbc.mtps.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder; 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.CarStatusDto;
import com.mbc.mtps.dto.MemberDto;
import com.mbc.mtps.service.MemberService;

@RestController
public class MemberController {

    final MemberService service;
    final PasswordEncoder passwordEncoder;                              // ← ① 필드 추가

    MemberController(MemberService service, PasswordEncoder passwordEncoder) {  // ← ② 파라미터 추가
        this.service = service;
        this.passwordEncoder = passwordEncoder;                         // ← ③ 추가
    }
    
//================================== 로그인/회원가입 페이지 ======================================    
    
    // 아이디 중복체크
    @PostMapping("idcheck")
    public boolean idCheck(String id) {
        System.out.println("MemberController idCheck " + new Date());
        return service.idCheck(id);
    }

    // 회원가입
    @PostMapping("addmember")
    public boolean addMember(MemberDto dto) {
        System.out.println("MemberController addmember " + new Date());
        System.out.println(dto.getEmail());
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
    @GetMapping("getmember")
    public MemberDto getMember(String id) {
        System.out.println("MemberController getMember " + new Date());
        return service.getMember(id);
    }


    // 현재 차량 상태와 주차 위치 조회
    @GetMapping("getcarstatus")
    public CarStatusDto getCarStatus(String id) {
    	System.out.println("MemberController getCarStatus " + new Date());
        return service.getCarStatus(id);
        /*
          "입차"
          "주차"
          "출차"
          "정산"
          "상태 확인 불가"
         
         <예시>
         
         "carStat": 1,							주차상태 
		 "carStatus": "주차",
		 "auth": 3,								권한(일반회원)
		 "floor": 1,							주차층
		 "spcNo": "A-03",						주차 자리번호
		 "entTime": "2026-07-31T14:00:00",		입차시간
		 "discntAt": null,						할인 적용시간(미적용)
		 "parkingMinutes": 72,					현재까지 주차한 시간(분)
		 "currentCost": 4000,					현재 예상 주차요금
		 "exTime": null,						출차시간
		 "cost": null,							확정 주차비
		 "carNum": "23나4567"					차량번호
         
         */
    }


    // [수정] 내 차량 이용내역 (진짜 페이징)
    @GetMapping("getparkinghistory")
    public List<CarStatusDto> getParkingHistory(String id, int pageNum) {
        System.out.println("MemberController getParkingHistory " + new Date());
        return service.getParkingHistory(id, pageNum);
    }

    // [추가] 내 차량 이용내역 전체 건수
    @GetMapping("getparkinghistorycount")
    public int getParkingHistoryCount(String id) {
        System.out.println("MemberController getParkingHistoryCount " + new Date());
        return service.getParkingHistoryCount(id);
    }
    
    // 주차 상태 변경 (정산: carStat=3, 자동출차: carStat=2)
    @PostMapping("updatecarstat")
    public boolean updateCarStat(String id, int carStat) {
        System.out.println("MemberController updateCarStat " + new Date());
        return service.updateCarStat(id, carStat);
    }


    // 비밀번호 변경
    @PostMapping("updatepw")
    public boolean updatePw(String id, String currentPw, String newPw) {
        System.out.println("MemberController updatePw " + new Date());
        return service.updatePw(id,currentPw, newPw);
    }


    // 전화번호 변경
    @PostMapping("updatephone")
    public boolean updatePhone(MemberDto dto) {
        System.out.println( "MemberController updatePhone " + new Date());
        return service.updatePhone(dto);
        
    }


    // 이메일 변경
    @PostMapping("updateemail")
    public boolean updateEmail(MemberDto dto) {
        System.out.println( "MemberController updateEmail " + new Date());
        return service.updateEmail(dto);
     
    }


    // 차량정보 변경
    @PostMapping("updatecar")
    public boolean updateCar(MemberDto dto) {
        System.out.println( "MemberController updateCar " + new Date());
        return service.updateCar(dto);
       
    }
    
//================================== 점주 페이지 =================================
    
//================================== 관리자 페이지 =================================  
    
    // 점주 목록 조회
    @GetMapping("ownerlist")
    public List<MemberDto> OwnerList(String category, String keyword, int pageNum) {
        System.out.println("MemberController OwnerList " + new Date());
        return service.OwnerList(category, keyword, pageNum);
    }
    
    // 일반 회원 목록 조회
    @GetMapping("memberlist")
    public List<MemberDto> MemberList(String category, String keyword, int pageNum) {
        System.out.println("MemberController MemberList " + new Date());
        return service.MemberList(category, keyword, pageNum);
    }
    
    // 점주 회원 수
    @GetMapping("ownercount")
    public int ownerCount(String category, String keyword) {
        System.out.println("MemberController ownerCount" + new Date());
        return service.ownerCount(category, keyword);
    }
    
    // 일반 회원 수
    @GetMapping("membercount")
    public int memberCount(String category, String keyword) {
        System.out.println("MemberController memberCount" + new Date());
        return service.memberCount(category, keyword);
    }
    
    // 관리자에 의한 회원정보 수정
    @PostMapping("updatememberbyadmin")
    public boolean updateMemberByAdmin(MemberDto dto) {
        System.out.println("MemberController updateMemberByAdmin " + new Date());
        return service.updateMemberByAdmin(dto);
    }
    
    // 회원 삭제 (일반회원/점주 공용)
    @PostMapping("deletemember")
    public boolean deleteMember(String id) {
        System.out.println("MemberController deleteMember" + new Date());
        return service.deleteMember(id);
    }
    
}