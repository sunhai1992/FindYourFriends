package com.haisun.findyourfriends.models;

import java.io.Serializable;

/**
 * �û���Ϣ��
 */
public class UserIno implements Serializable  {
	private String username;
	private String phonenumber;
	private String password;
	private Double longtitude;
	private Double latitude; 
	private boolean on;
	public UserIno(){}
	
	public UserIno(String username, String phonenumber, String password,Double longtitude,Double latitude,boolean on) {
		this.username = username;
		this.phonenumber = phonenumber;
		this.password = password;
		this.longtitude=longtitude;
		this.latitude=latitude; 
		this.on=on;
	}

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean on) {
		this.on = on;
	}

	public Double getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(Double longtitude) {
		this.longtitude = longtitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserIno [username=" + username + ", phonenumber=" + phonenumber
				+ ", password=" + password + ", longtitude=" + longtitude
				+ ", latitude=" + latitude + "]";
	}
}
