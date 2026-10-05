package com.mbc.mtps.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SpaceDao {

	// 정산 처리 시 사용 중(1)이던 자리를 빈 자리(0)로 반납
	int release(String spcNo);
	
	// 현재 주차 중인 차량이 있는 자리를 사용 중(1)으로 동기화
	int markUsed();
	
	// 주차 중인 차량이 없는 자리를 빈 자리(0)로 동기화
	int markFree();

}