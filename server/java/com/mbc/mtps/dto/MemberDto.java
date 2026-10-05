package com.mbc.mtps.dto;

import java.io.Serializable;

public class MemberDto implements Serializable {

    private String id;          // 아이디
    private String pw;          // 비밀번호
    private String name;        // 이름
    private int discnt_time;     // 점주 할인 가능 시간
    private String phone;       // 전화번호
    private String email;       // 이메일
    private int auth;           // 1: 관리자, 2: 점주, 3: 일반회원
    private String car_num;      // 차량번호
    private int car_type;        // 0: 일반차, 1: 전기차

    public MemberDto() {
	
	}

	public MemberDto(String id, String pw, String name, int discnt_time, String phone, String email, int auth,
			String car_num, int car_type) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.discnt_time = discnt_time;
		this.phone = phone;
		this.email = email;
		this.auth = auth;
		this.car_num = car_num;
		this.car_type = car_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDiscnt_time() {
		return discnt_time;
	}

	public void setDiscnt_time(int discnt_time) {
		this.discnt_time = discnt_time;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAuth() {
		return auth;
	}

	public void setAuth(int auth) {
		this.auth = auth;
	}

	public String getCar_num() {
		return car_num;
	}

	public void setCar_num(String car_num) {
		this.car_num = car_num;
	}

	public int getCar_type() {
		return car_type;
	}

	public void setCar_type(int car_type) {
		this.car_type = car_type;
	}

	@Override
	public String toString() {
		return "MemberDto [id=" + id + ", pw=" + pw + ", name=" + name + ", discnt_time=" + discnt_time + ", phone="
				+ phone + ", email=" + email + ", auth=" + auth + ", car_num=" + car_num + ", car_type=" + car_type
				+ "]";
	}
    
    
}