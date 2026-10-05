package com.mbc.mtps.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.CarDao;
import com.mbc.mtps.dto.CarDto;

@Service
@Transactional
public class CarService {
	
	final CarDao dao;

	public CarService(CarDao dao) {
		this.dao = dao;
	}
	
	// 차량번호 뒤 4자리 검색
    public List<CarDto> getCarNum(String car_num) {
    	
    	return dao.getCarNum(car_num);
    }
	
    // 할인 등록
    public boolean applyDiscount(Timestamp ent_time) {
    	int count = dao.applyDiscount(ent_time);
    	return count > 0;
    }
    
    // 할인 취소
    public boolean cancelDiscount(Timestamp ent_time) {
    	int count = dao.cancelDiscount(ent_time);
    	return count > 0;
    }
    
    // 할인 지급 내역 리스트 (discnt_at 있는 차량만)
    public List<CarDto> getDiscountList() {
    	return dao.getDiscountList();
    }

}
