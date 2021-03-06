package com.calendar.demo;

public class Time {
	
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minutes;
	private int tag;
	private boolean isSetAlarm = false;
	
	public boolean getIsSetAlarm(){
		return isSetAlarm;
	}
	public void setIsSetAlarm(boolean isSetAlarm){
		this.isSetAlarm = isSetAlarm;
	}
	public int getTag(){
		return tag;
	}
	public void setTag(int tag){
		this.tag = tag;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	
	public Time(int year ,int month , int day ,int hour,int minutes){
		this.year = year;
		this.minutes = minutes;
		this.month = month;
		this.day = day;
		this.hour = hour;
	}
	
}
