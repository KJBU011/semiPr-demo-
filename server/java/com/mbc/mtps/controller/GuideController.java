package com.mbc.mtps.controller;

import java.util.Date;
import java.util.List;

import org.apache.catalina.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.dto.CarStatusDto;
import com.mbc.mtps.service.GuideService;

@RestController
public class GuideController {

	private final GuideService service;

	GuideController(GuideService service) {
		this.service = service;
	}
	
	@GetMapping("searchCar")
	public List<CarStatusDto> searchCar(String carNum) {
		System.out.println("GuideController searchCar" + new Date());
		
		
		
		return service.searchCar(carNum);
	}
	
}
