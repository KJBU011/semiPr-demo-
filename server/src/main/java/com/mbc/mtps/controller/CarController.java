package com.mbc.mtps.controller;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.CarDto;
import com.mbc.mtps.service.CarService;

@RestController
public class CarController {
	
	final CarService service;

	public CarController(CarService service) {
		this.service = service;
	}
	
//================================== 점주 페이지 ======================================
	
	// 차량번호 뒤 4자리 검색
	@GetMapping("getcarnum")
	public List<CarDto> getCarNum(String carNum){
		System.out.println("CarController getCarNum()" + new Date());
		return service.getCarNum(carNum);
	}
	
	// 할인 등록
	@PostMapping("applydiscount")
	public boolean applyDiscount(int carId, String ownerId) {
		System.out.println("CarController applyDiscount()" + new Date());
		return service.applyDiscount(carId, ownerId);
	}
	
	// 점주의 할인 가능 시간(discnt_time) 2시간 차감
	@PostMapping("decreaseownertime")
	public boolean decreaseOwnerTime(String ownerId) {
		System.out.println("CarController decreaseOwnerTime()" + new Date());
		return service.decreaseOwnerTime(ownerId);
	}
	
	// 할인 지급 내역 리스트
	@GetMapping("getdiscountlist")
	public List<CarDto> getDiscountList(){
		System.out.println("CarController getDiscountList()" + new Date());
		return service.getDiscountList();
	}
	
	// 금일 가게 방문 차량 수 (해당 점주가 오늘 할인해준 차량 리스트)
	@GetMapping("gettodayvisitcount")
	public List<CarDto> getTodayVisitCount(String ownerId) {
		System.out.println("CarController getTodayVisitCount()" + new Date());
	    return service.getTodayVisitCount(ownerId);
	}
	
//================================== 관리자 페이지 ======================================
	
	// 금일 입/출차 수
	@GetMapping("gettodayinoutcount")
	public Map<String, Object> getTodayInOutCount() {
		System.out.println("CarController getTodayInOutCount()" + new Date());
		return service.getTodayInOutCount();
	}
	
	// 출차 차량 제외 차량 리스트
	@GetMapping("carlist")
	public List<CarDto> carList(int pageNum, String category, String keyword){
		System.out.println("CarController carList()" + new Date());
		return service.carList(pageNum, category, keyword);
	}
	
	// 출차 차량 제외 차량 수
	@GetMapping("carcount")
	public int carCount(String category, String keyword){
		System.out.println("CarController carCount()" + new Date());
		return service.carCount(category, keyword);
	}
	
	// 출차 차량 리스트
	@GetMapping("exitcarlist")
	public List<CarDto> exitCarList(int pageNum, String category, String keyword){
		System.out.println("CarController exitCarList()" + new Date());
		return service.exitCarList(pageNum, category, keyword);
	}
	
	// 출차 차량 수
	@GetMapping("exitcarcount")
	public int exitCarCount(String category, String keyword){
		System.out.println("CarController exitCarCount()" + new Date());
		return service.exitCarCount(category, keyword);
	}
	
	// 시간별 차량 방문 통계
	@GetMapping("hourstat")
	public List<Map<String, Object>> hourStat(String date){
		System.out.println("CarController hourStat()" + new Date());
		return service.hourStat(date);
	}
	
	// 일별 차량 방문 통계
	@GetMapping("daystat")
	public List<Map<String, Object>> dayStat(){
		System.out.println("CarController dayStat()" + new Date());
		return service.dayStat();
	}
	
}