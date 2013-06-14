package com.calendar.util;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.http.params.HttpParams;

public class Record implements Serializable {
	private long id = 1;
	private long taskId = -1;
	private long uid = 1;
	private int optype = 0;
	private int syn = 0;  // 0表示未同步，1表示已同步
	private String nickName = ""; // 未使用
	private String taskName = ""; // 未使用 
	private String taskTag = ""; // 未使用
	private String taskDetail = "";
	private long taskCreateTime; // 未使用
	private long taskStartTime; // 未使用
	private long taskEndTime; // 未使用
	private long taskEditTime;
	private int taskImportant; // 未使用
	private int taskComplete; //0表示未完成，1表示已完成
	private int taskStatus; //-1表示删除
	private int taskType; // 未使用
	private int alarm; //0表示不要提醒，1表示要提醒
	private int alarmType; // 未使用
	private int alarmCycle; // 未使用
	private long alarmTime;
	
	
	public Record(){}


	public Record(long id, long taskId, long uid, int optype, int syn,
			String nickName, String taskName, String taskTag,
			String taskDetail, long taskCreateTime, long taskStartTime,
			long taskEndTime, long taskEditTime, int taskImportant,
			int taskComplete, int taskStatus, int taskType, int alarm,
			int alarmType, int alarmCycle, long alarmTime) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.uid = uid;
		this.optype = optype;
		this.syn = syn;
		this.nickName = nickName;
		this.taskName = taskName;
		this.taskTag = taskTag;
		this.taskDetail = taskDetail;
		this.taskCreateTime = taskCreateTime;
		this.taskStartTime = taskStartTime;
		this.taskEndTime = taskEndTime;
		this.taskEditTime = taskEditTime;
		this.taskImportant = taskImportant;
		this.taskComplete = taskComplete;
		this.taskStatus = taskStatus;
		this.taskType = taskType;
		this.alarm = alarm;
		this.alarmType = alarmType;
		this.alarmCycle = alarmCycle;
		this.alarmTime = alarmTime;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getTaskId() {
		return taskId;
	}


	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}


	public long getUid() {
		return uid;
	}


	public void setUid(long uid) {
		this.uid = uid;
	}


	public int getOptype() {
		return optype;
	}


	public void setOptype(int optype) {
		this.optype = optype;
	}


	public int getSyn() {
		return syn;
	}


	public void setSyn(int syn) {
		this.syn = syn;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getTaskTag() {
		return taskTag;
	}


	public void setTaskTag(String taskTag) {
		this.taskTag = taskTag;
	}


	public String getTaskDetail() {
		return taskDetail;
	}


	public void setTaskDetail(String taskDetail) {
		this.taskDetail = taskDetail;
	}


	public long getTaskCreateTime() {
		return taskCreateTime;
	}


	public void setTaskCreateTime(long taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}


	public long getTaskStartTime() {
		return taskStartTime;
	}


	public void setTaskStartTime(long taskStartTime) {
		this.taskStartTime = taskStartTime;
	}


	public long getTaskEndTime() {
		return taskEndTime;
	}


	public void setTaskEndTime(long taskEndTime) {
		this.taskEndTime = taskEndTime;
	}


	public long getTaskEditTime() {
		return taskEditTime;
	}


	public void setTaskEditTime(long taskEditTime) {
		this.taskEditTime = taskEditTime;
	}


	public int getTaskImportant() {
		return taskImportant;
	}


	public void setTaskImportant(int taskImportant) {
		this.taskImportant = taskImportant;
	}


	public int getTaskComplete() {
		return taskComplete;
	}


	public void setTaskComplete(int taskComplete) {
		this.taskComplete = taskComplete;
	}


	public int getTaskStatus() {
		return taskStatus;
	}


	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}


	public int getTaskType() {
		return taskType;
	}


	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}


	public int getAlarm() {
		return alarm;
	}


	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}


	public int getAlarmType() {
		return alarmType;
	}


	public void setAlarmType(int alarmType) {
		this.alarmType = alarmType;
	}


	public int getAlarmCycle() {
		return alarmCycle;
	}


	public void setAlarmCycle(int alarmCycle) {
		this.alarmCycle = alarmCycle;
	}


	public long getAlarmTime() {
		return alarmTime;
	}


	public void setAlarmTime(long alarmTime) {
		this.alarmTime = alarmTime;
	}
	
	
	
}
