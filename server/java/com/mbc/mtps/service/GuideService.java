package com.mbc.mtps.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.mtps.dao.GuideDao;
import com.mbc.mtps.dto.CarStatusDto;


@Service
@Transactional
public class GuideService {

    final GuideDao dao;

    GuideService(GuideDao dao) {
        this.dao = dao;
    }
    
    public List<CarStatusDto> searchCar(String carNum) {
    	return dao.searchCar(carNum);
    }   	
    
}