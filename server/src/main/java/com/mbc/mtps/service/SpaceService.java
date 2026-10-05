package com.mbc.mtps.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.SpaceDao;

@Service
@Transactional
public class SpaceService {
	
	final SpaceDao dao;

	public SpaceService(SpaceDao dao) {
		this.dao = dao;
	}
	
	// 정산 처리 시 사용 중(1)이던 자리를 빈 자리(0)로 반납
	public boolean release(String spcNo) {
		int count = dao.release(spcNo);
		return count > 0;
	}
	
	// 주차 안내/마이페이지 접속 시, 실제 차량 입출차 상태(car)와 자리 상태(space)를 동기화
	public void sync() {
		dao.markUsed();
		dao.markFree();
	}

}