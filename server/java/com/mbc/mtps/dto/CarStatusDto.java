package com.mbc.mtps.dto;

import java.sql.Timestamp;
// 마이페이지 파트
public class CarStatusDto {

    private int car_stat;              // 0=입차, 1=주차, 2=출차, 3=정산
    private String car_status;         // 프론트에 전달할 상태 문자열

    private int auth;                  // 1=관리자, 2=점주, 3=일반회원

    private Integer floor;             // 주차 층
    private String spc_no;             // 주차 자리번호

    private Timestamp ent_time;        // 입차시간
    private Timestamp discnt_at;       // 할인 적용시간, null이면 미적용

    private int parking_minutes;       // 현재까지 주차한 시간(분)
    private int current_cost;          // 현재 예상 주차요금

    private Timestamp ex_time;         // 출차시간
    private Integer cost;              // 확정 주차비

    private String car_num;            // 차량번호


    public CarStatusDto() {
    }


    public int getCar_stat() {
        return car_stat;
    }

    public void setCar_stat(int car_stat) {
        this.car_stat = car_stat;
    }

    public String getCar_status() {
        return car_status;
    }

    public void setCar_status(String car_status) {
        this.car_status = car_status;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getSpc_no() {
        return spc_no;
    }

    public void setSpc_no(String spc_no) {
        this.spc_no = spc_no;
    }

    public Timestamp getEnt_time() {
        return ent_time;
    }

    public void setEnt_time(Timestamp ent_time) {
        this.ent_time = ent_time;
    }

    public Timestamp getDiscnt_at() {
        return discnt_at;
    }

    public void setDiscnt_at(Timestamp discnt_at) {
        this.discnt_at = discnt_at;
    }

    public int getParking_minutes() {
        return parking_minutes;
    }

    public void setParking_minutes(int parking_minutes) {
        this.parking_minutes = parking_minutes;
    }

    public int getCurrent_cost() {
        return current_cost;
    }

    public void setCurrent_cost(int current_cost) {
        this.current_cost = current_cost;
    }

    public Timestamp getEx_time() {
        return ex_time;
    }

    public void setEx_time(Timestamp ex_time) {
        this.ex_time = ex_time;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getCar_num() {
        return car_num;
    }

    public void setCar_num(String car_num) {
        this.car_num = car_num;
    }


    @Override
    public String toString() {
        return "CarStatusDto [car_stat=" + car_stat
                + ", car_status=" + car_status
                + ", auth=" + auth
                + ", floor=" + floor
                + ", spc_no=" + spc_no
                + ", ent_time=" + ent_time
                + ", discnt_at=" + discnt_at
                + ", parking_minutes=" + parking_minutes
                + ", current_cost=" + current_cost
                + ", ex_time=" + ex_time
                + ", cost=" + cost
                + ", car_num=" + car_num + "]";
    }
}