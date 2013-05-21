package com.calendar.util;

/*
 * 瑞年 和 平年的一个月的天数，以及判断一年是不是瑞年
 */
public class NumMonthOfYear {
	
	
	public static int [] leapyear = {31,29,31,30,31,30,31,31,30,31,30,31};
	public static int [] averyear = {31,28,31,30,31,30,31,31,30,31,30,31};
	
	
	public static boolean isLeapYear(int year){
		return (year%400 == 0) || (year % 4 == 0 && year % 100 != 0 );
	}
	
	
	public static int dayOfMonth(int year ,int month){
		if(isLeapYear(year))
			return leapyear[month];
		return averyear[month];
	}
	
	
}
