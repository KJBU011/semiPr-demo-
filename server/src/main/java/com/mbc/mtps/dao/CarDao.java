package com.mbc.mtps.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.CarDto;

@Mapper
@Repository
public interface CarDao {

//================================== 점주 페이지 ======================================
	
	// 차량번호 뒤 4자리 검색
	List<CarDto> getCarNum(String carNum);
	
	// 할인 등록 (discnt_at = NOW())
	int applyDiscount(@Param("carId") int carId, @Param("ownerId") String ownerId);
	
	// 점주의 할인 가능 시간(discnt_time) 2시간 차감
	int decreaseOwnerTime(String ownerId);
	
	// 할인 지급 내역 리스트 (discnt_at 있는 차량만)
	List<CarDto> getDiscountList();
	
	// 금일 가게 방문 차량 수 (해당 점주가 오늘 할인해준 차량 리스트)
	List<CarDto> getTodayVisitCount(String ownerId);

//================================== 관리자 페이지 ======================================	
	
	// 금일 입/출차 수
	Map<String, Object> getTodayInOutCount();
	
	// 출차 차량 제외 차량 리스트
	List<CarDto> carList(int pageNum, String category, String keyword);
	
	// 출차 차량 제외 차량 수
	int carCount(String category, String keyword);
	
	// 출차 차량 리스트
	List<CarDto> exitCarList(int pageNum, String category, String keyword);
		
	// 출차 차량 수
	int exitCarCount(String category, String keyword);
	
	// 시간별 차량 방문 통계 (전날, 0~23시 1시간 단위, 오늘 제외)
	List<Map<String, Object>> hourStat(String date);
	
	// 일별 차량 방문 통계 (최근 7일, 날짜/요일별, 오늘 제외)
	List<Map<String, Object>> dayStat();
	
}