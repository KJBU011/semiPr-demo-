package com.mbc.mtps.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.CarDto;
import com.mbc.mtps.service.CarService;

@RestController
public class CarController {
	
	final CarService service;

	public CarController(CarService service) {
		this.service = service;
	}
	
	// 차량번호 뒤 4자리 검색
	@GetMapping("getCarNum")
	public List<CarDto> getCarNum(String car_num){
		System.out.println("CarController getCarNum()" + new Date());
		return service.getCarNum(car_num);
	}
	
	// 할인 등록
	@PostMapping("applyDiscount")
	public boolean applyDiscount(Timestamp ent_time) {
		System.out.println("CarController getCarNum()" + new Date());
		return service.applyDiscount(ent_time);
	}
	
	// 할인 취소
	@PostMapping("cancelDiscount")
	public boolean cancelDiscount(Timestamp ent_time) {
		System.out.println("CarController cancelDiscount()" + new Date());
		return service.cancelDiscount(ent_time);
	}
	
	// 할인 지급 내역 리스트
	@GetMapping("getDiscountList")
	public List<CarDto> getDiscountList(){
		System.out.println("CarController getDiscountList()" + new Date());
		return service.getDiscountList();
	}
	
	
}
