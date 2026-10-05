package com.mbc.mtps.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.mtps.dto.CarStatusDto;

@Mapper
@Repository
public interface GuideDao {

	List<CarStatusDto> searchCar(String carNum);
	
}
