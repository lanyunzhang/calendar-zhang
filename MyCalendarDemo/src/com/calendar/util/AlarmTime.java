package com.calendar.util;



public class AlarmTime  {

	private long time;
	private int  tag; // 类型带定义

	public AlarmTime(long time, int tag)
	{
		this.time = time;
		this.tag = tag;
		
	}
	
	public long getTime()
	{
		return time;
		
	}
	
	public void setTime(long time)
	{
		this.time = time;
		
	}
	
	public int getTag()
	{
		return tag;
	}
	
	public void setTag(int tag)
	{
		
		this.tag = tag;
	}
}
