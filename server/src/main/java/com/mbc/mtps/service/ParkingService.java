package com.mbc.mtps.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.ParkingDao;
import com.mbc.mtps.dto.SpaceDto;

@Service
@Transactional
public class ParkingService {
	
	final ParkingDao dao;

	public ParkingService(ParkingDao dao) {
		this.dao = dao;
	}

//=============================== 주차 안내 페이지 =================================
	
	// 주차 가능 대수
	public Map<String, Object> getTotalParkingCount(){
    	return dao.getTotalParkingCount();
    }
    
	// 층별 유형별 주차 가능 대수
    public List<Map<String, Object>> getFloorCount(){
    	return dao.getFloorCount();
    }
    
    // 주차현황 지도
    public List<SpaceDto> parkingMap(){
    	return dao.parkingMap();
    }

}
