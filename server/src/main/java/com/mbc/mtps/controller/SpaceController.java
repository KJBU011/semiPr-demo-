package com.mbc.mtps.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.mtps.service.SpaceService;

@RestController
public class SpaceController {
	
	final SpaceService service;

	public SpaceController(SpaceService service) {
		this.service = service;
	}
	
	// 정산 처리 시 사용 중(1)이던 자리를 빈 자리(0)로 반납
	@PostMapping("release")
	public boolean release(String spcNo) {
		System.out.println("SpaceController release()" + new Date());
		return service.release(spcNo);
	}
	
	// 주차 안내/마이페이지 접속 시, 자리 상태 동기화 (프론트에서 페이지 진입할 때마다 호출)
	@PostMapping("sync")
	public boolean sync() {
		System.out.println("SpaceController sync()" + new Date());
		service.sync();
		return true;
	}
}