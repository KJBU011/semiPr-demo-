package com.mbc.mtps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.CarStatusDto;
import com.mbc.mtps.dto.MemberDto;

@Mapper
@Repository
public interface MemberDao {

	// 마이페이지 회원정보
    MemberDto getMember(String id);

    // 현재 차량 상태와 주차 위치 조회
    CarStatusDto getCarStatus(String id);

    // 내 차량 이용내역
    List<CarStatusDto> getParkingHistory(String id);

    // 비밀번호 변경
    int updatePw(MemberDto dto);

    // 전화번호 변경
    int updatePhone(MemberDto dto);

    // 이메일 변경
    int updateEmail(MemberDto dto);

    // 차량번호와 전기차 여부 변경
    int updateCar(MemberDto dto);
    
    // 아이디 중복체크
    boolean idCheck(String id);
    
    // 회원가입
    int addMember(MemberDto dto);

    // 로그인
    MemberDto login(MemberDto dto);
    
    
}