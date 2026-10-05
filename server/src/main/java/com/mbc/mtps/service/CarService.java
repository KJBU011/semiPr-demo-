package com.mbc.mtps.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
	
//================================== 점주 페이지 ======================================
	
	// 차량번호 뒤 4자리 검색
    public List<CarDto> getCarNum(String carNum) {
    	
    	List<CarDto> list = dao.getCarNum(carNum);
    	
    	// 현재 주차 시간(분) 계산해서 각 차량에 세팅
    	for (CarDto dto : list) {
    		if (dto.getEntTime() != null) {
    			LocalDateTime entranceTime = dto.getEntTime().toLocalDateTime();
    			LocalDateTime currentTime = LocalDateTime.now();
    			long totalMinutes = Duration.between(entranceTime, currentTime).toMinutes();
    			
    			if (totalMinutes < 0) {
    				totalMinutes = 0;
    			}
    			
    			dto.setParkingMinutes((int) totalMinutes);
    		}
    	}
    	
    	return list;
    }
	
    // 할인 등록
    public boolean applyDiscount(int carId, String ownerId) {
    	int count = dao.applyDiscount(carId, ownerId);
    	dao.decreaseOwnerTime(ownerId);
    	return count > 0;
    }
    
    // 점주의 할인 가능 시간(discnt_time) 2시간 차감
    public boolean decreaseOwnerTime(String ownerId) {
    	int count = dao.decreaseOwnerTime(ownerId);
    	return count > 0;
    }
    
    // 할인 지급 내역 리스트 (discnt_at 있는 차량만)
    public List<CarDto> getDiscountList() {
    	return dao.getDiscountList();
    }
    
    // 금일 가게 방문 차량 수 (해당 점주가 오늘 할인해준 차량 리스트)
    public List<CarDto> getTodayVisitCount(String ownerId) {
    	return dao.getTodayVisitCount(ownerId);
    }
   
//================================== 관리자 페이지 ======================================
    
    // 금일 입/출차 수
    public Map<String, Object> getTodayInOutCount() {
    	return dao.getTodayInOutCount();
    }

    // 출차 차량 제외 차량 리스트
    public List<CarDto> carList(int pageNum, String category, String keyword) {
    	
    	List<CarDto> list = dao.carList(pageNum, category, keyword);
    	
    	// 현재 주차 시간(분) 및 예상 주차요금(currentCost) 계산해서 각 차량에 세팅
    	for (CarDto dto : list) {
    		if (dto.getEntTime() != null) {
    			LocalDateTime entranceTime = dto.getEntTime().toLocalDateTime();
    			LocalDateTime currentTime = LocalDateTime.now();
    			long totalMinutes = Duration.between(entranceTime, currentTime).toMinutes();
    			
    			if (totalMinutes < 0) {
    				totalMinutes = 0;
    			}
    			
    			dto.setParkingMinutes((int) totalMinutes);
    			
    			// 점주 차량은 주차요금 무조건 0원 (-1 값으로 전달 요청받음)
    			if (dto.getAuth() == 2) {
    				dto.setCurrentCost(-1);
    				continue;
    			}
    			
    			// 관리자 차량은 주차요금 무조건 0원 (-2 값으로 전달 요청받음)
    			if (dto.getAuth() == 1) {
    				dto.setCurrentCost(-2);
    				continue;
    			}
    			
    			
    			// 입차 후 15분 이내면 무료(회차 차량)
    			if (totalMinutes <= 15) {
    				dto.setCurrentCost(0);
    				continue;
    			}
    			
    			long chargeMinutes = totalMinutes;
    			
    			// 할인 적용 시 2시간 차감
    			if (dto.getDiscntAt() != null) {
    				chargeMinutes = chargeMinutes - 120;
    			}
    			
    			// 할인 후 유료시간이 없으면 0원
    			if (chargeMinutes <= 0) {
    				dto.setCurrentCost(0);
    				continue;
    			}
    			
    			// 남은 시간을 1시간 단위로 올림, 시간당 2,000원
    			int chargeHours = (int) Math.ceil(chargeMinutes / 60.0);
    			dto.setCurrentCost(chargeHours * 2000);
    		}
    	}
    	
    	return list;
    }
    
    // 출차 차량 제외 차량 수
    public int carCount(String category, String keyword) {
    	return dao.carCount(category, keyword);
    }
    
    // 출차 차량 리스트
    public List<CarDto> exitCarList(int pageNum, String category, String keyword) {
    	
    	List<CarDto> list = dao.exitCarList(pageNum, category, keyword);
    	
    	// 입차~출차 시간으로 정산 금액(cost) 계산 (2시간까지 무료, 이후 1시간당 2,000원)
    	for (CarDto dto : list) {
    		if (dto.getEntTime() != null && dto.getExTime() != null) {
    			LocalDateTime entranceTime = dto.getEntTime().toLocalDateTime();
    			LocalDateTime exitTime = dto.getExTime().toLocalDateTime();
    			long totalMinutes = Duration.between(entranceTime, exitTime).toMinutes();
    			
    			if (totalMinutes < 0) {
    				totalMinutes = 0;
    			}
    			
    			// 2시간(120분)까지 무료
    			long chargeMinutes = totalMinutes - 120;
    			
    			if (chargeMinutes <= 0) {
    				dto.setCost(0);
    				continue;
    			}
    			
    			// 남은 시간을 1시간 단위로 올림, 시간당 2,000원
    			int chargeHours = (int) Math.ceil(chargeMinutes / 60.0);
    			dto.setCost(chargeHours * 2000);
    		}
    	}
    	
    	return list;
    }
    
    // 출차 차량 수
    public int exitCarCount(String category, String keyword) {
    	return dao.exitCarCount(category, keyword);
    }
    
    // 시간별 차량 방문 통계 (전날, 0~23시 1시간 단위, 오늘 제외)
    public List<Map<String, Object>> hourStat(String date) {
    	return dao.hourStat(date);
    }
    
    // 일별 차량 방문 통계 (최근 7일, 날짜/요일별, 오늘 제외)
    public List<Map<String, Object>> dayStat() {
    	
    	List<Map<String, Object>> list = dao.dayStat();
    	
    	// stat_date(Timestamp)를 'yyyy-MM-dd' 형태의 문자열로 변환 (hourStat의 date 파라미터와 형식 통일)
    	for (Map<String, Object> row : list) {
    		Object statDate = row.get("stat_date");
    		if (statDate instanceof Timestamp) {
    			row.put("stat_date", statDate.toString().substring(0, 10));
    		}
    	}
    	return list;
    }
 
}