package com.calendar.util;

public class Info {
	public static final String TODOLIST_DB = "todolist_db";
	public static final String TABLE = "todolist_t";
	public static final String USER_TABLE = "user_t";
	
	public static final String ID = "id";
	public static final String OPTYPE = "opType";
	public static final String SYN = "syn";
	public static final String TASKID = "taskId";
	public static final String UID = "uid";
	public static final String NICKNAME = "nickName";
	public static final String TASKNAME = "taskName";
	public static final String TASKTAG = "taskTag";
	public static final String TASKDETAIL = "taskDetail";
	public static final String TASKCREATETIME = "taskCreateTime";
	public static final String TASKSTARTTIME = "taskStartTime";
	public static final String TASKENDTIME = "taskEndTime";
	public static final String TASKEDITTIME = "taskEditTime";
	public static final String TASKIMPORTANT = "taskImportant";
	public static final String TASKCOMPLETE = "taskComplete";
	public static final String TASKSTATUS = "taskStatus";
	public static final String TASKTYPE = "taskType";
	public static final String ALARM = "alarm";
	public static final String ALARMTYPE = "alarmType";
	public static final String ALARMCYCLE = "alarmCycle";
	public static final String ALARMTIME = "alarmTime";
	
	public static final String LASTSYNTIME = "lastSynTime";
	public static final String TOKEN = "token";
	public static final String PWD = "password";
	
	
	public static final int OP_LOGIN = 0;
	public static final int OP_ADD = 1;
	public static final int OP_EDIT = 2;
	public static final int OP_DELETE = 3;
	public static final int OP_PERIOD = 4;
	public static final int OP_SOMEDAY = 5;
	public static final int OP_SYN = 6;
	public static final int OP_MARK = 7;
	
	
	public static final int YES_SYN = 1;
	public static final int NO_SYN = 0;
	public static final int YES_COMPLETE = 1;
	public static final int NO_COMPLETE = 0;
	public static final int YES_DELETE = -1;
	public static final int YES_ALARM = 1;
}
