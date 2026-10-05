package com.mbc.mtps.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class CarDto implements Serializable{

    private int carId;           		// 입출차 내역 PK (SERIAL)
    private Timestamp entTime;   		// 입차 시간
    private String id;           		// 회원이면 member.id, 비회원이면 NULL
    private int carStat;         		// 상태 (0=입차, 1=주차, 2=출차, 3=정산)
    private Timestamp exTime;    		// 출차 시간
    private int cost;        			// 요금
    private String spcNo;        		// 주차된 자리
    private Timestamp discntAt;			// 할인적용시간 (NULL=미적용, 값 있으면 2시간 고정 적용)
    private String discntOwnerId;		// 할인을 부여한 점주 id
    private String carNum;				// 입차 당시 차량번호
    private int parkingMinutes;			// 현재 주차 시간(분)
    private String name;				// 회원 이름, 비회원이면 "비회원"
    private int carType;				// 차량 종류 (0=일반, 1=전기차)
    private int currentCost;			// 현재 예상 주차요금
    private Integer auth;				// 회원 권한, 비회원이면 NULL
	
    public CarDto() {
	
	}

	public CarDto(int carId, Timestamp entTime, String id, int carStat, Timestamp exTime, int cost, String spcNo,
			Timestamp discntAt, String discntOwnerId, String carNum, int parkingMinutes, String name, int carType,
			int currentCost, Integer auth) {
		super();
		this.carId = carId;
		this.entTime = entTime;
		this.id = id;
		this.carStat = carStat;
		this.exTime = exTime;
		this.cost = cost;
		this.spcNo = spcNo;
		this.discntAt = discntAt;
		this.discntOwnerId = discntOwnerId;
		this.carNum = carNum;
		this.parkingMinutes = parkingMinutes;
		this.name = name;
		this.carType = carType;
		this.currentCost = currentCost;
		this.auth = auth;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public Timestamp getEntTime() {
		return entTime;
	}

	public void setEntTime(Timestamp entTime) {
		this.entTime = entTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCarStat() {
		return carStat;
	}

	public void setCarStat(int carStat) {
		this.carStat = carStat;
	}

	public Timestamp getExTime() {
		return exTime;
	}

	public void setExTime(Timestamp exTime) {
		this.exTime = exTime;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getSpcNo() {
		return spcNo;
	}

	public void setSpcNo(String spcNo) {
		this.spcNo = spcNo;
	}

	public Timestamp getDiscntAt() {
		return discntAt;
	}

	public void setDiscntAt(Timestamp discntAt) {
		this.discntAt = discntAt;
	}

	public String getDiscntOwnerId() {
		return discntOwnerId;
	}

	public void setDiscntOwnerId(String discntOwnerId) {
		this.discntOwnerId = discntOwnerId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public int getParkingMinutes() {
		return parkingMinutes;
	}

	public void setParkingMinutes(int parkingMinutes) {
		this.parkingMinutes = parkingMinutes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public int getCurrentCost() {
		return currentCost;
	}

	public void setCurrentCost(int currentCost) {
		this.currentCost = currentCost;
	}

	public Integer getAuth() {
		return auth;
	}

	public void setAuth(Integer auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "CarDto [carId=" + carId + ", entTime=" + entTime + ", id=" + id + ", carStat=" + carStat
				+ ", exTime=" + exTime + ", cost=" + cost + ", spcNo=" + spcNo + ", discntAt=" + discntAt
				+ ", discntOwnerId=" + discntOwnerId + ", carNum=" + carNum + ", parkingMinutes="
				+ parkingMinutes + ", name=" + name + ", carType=" + carType + ", currentCost=" + currentCost
				+ ", auth=" + auth + "]";
	}
    
}