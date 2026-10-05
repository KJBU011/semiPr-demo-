package com.mbc.mtps.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.CarDto;

@Mapper
@Repository
public interface CarDao {

	// 차량번호 뒤 4자리 검색
	List<CarDto> getCarNum(String car_num);
	
	// 할인 등록 (discnt_at = NOW())
	int applyDiscount(Timestamp ent_time);
	
	// 할인 취소
	int cancelDiscount(Timestamp ent_time);
	
	// 할인 지급 내역 리스트 (discnt_at 있는 차량만)
	List<CarDto> getDiscountList();
	
}
