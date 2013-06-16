package com.calendar.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DB {
	private static SQLiteDatabase db;
	public DB(Context context) {
		if(db == null) {
			db = context.openOrCreateDatabase(Info.TODOLIST_DB, Context.MODE_PRIVATE, null);
			String sql = "CREATE TABLE IF NOT EXISTS "
					+ Info.TABLE
					+ " (" 
					+ Info.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Info.OPTYPE + " INTEGER,"
					+ Info.SYN + " INTEGER,"
					+ Info.TASKID + " INTEGER,"
					+ Info.UID + " INTEGER,"
					+ Info.NICKNAME + " CHAR(20),"
					+ Info.TASKNAME + " CHAR(50),"
					+ Info.TASKTAG + " CHAR(50),"
					+ Info.TASKDETAIL + " TEXT,"
					+ Info.TASKCREATETIME + " INTEGER,"
					+ Info.TASKSTARTTIME + " INTEGER,"
					+ Info.TASKENDTIME + " INTEGER,"     
					+ Info.TASKEDITTIME + " INTEGER,"
					+ Info.TASKIMPORTANT + " INTEGER,"
					+ Info.TASKCOMPLETE + " INTEGER,"
					+ Info.TASKSTATUS + " INTEGER,"
					+ Info.TASKTYPE + " INTEGER,"
					+ Info.ALARM + " INTEGER,"
					+ Info.ALARMTYPE + " INTEGER,"
					+ Info.ALARMCYCLE + " INTEGER,"
					+ Info.ALARMTIME + " INTEGER)";
			db.execSQL(sql);
			
//			sql = "CREATE TABLE IF NOT EXISTS "
//					+ Info.USER_TABLE + " ( "
//					+ Info.UID + " INTEGER PRIMARY KEY,"
//					+ Info.NICKNAME + " CHAR(20),"
//					+ Info.PWD + " CHAR(128),"
//					+ Info.LASTSYNTIME + " INTEGER DEFAULT 0,"
//					+ Info.TOKEN + " CHAR(128))";
//			db.execSQL(sql);
		}
	}
	
	private static DB instance;
	
	public static synchronized DB getDB(Context context) {
		if(instance == null)
			instance = new DB(context);
		return instance;
	}
	
	public synchronized long add(Record r) {
		
		ContentValues cv = new ContentValues();
		cv.put(Info.OPTYPE, Info.OP_ADD);
		cv.put(Info.UID, r.getUid());
		cv.put(Info.TASKID, r.getTaskId());
		cv.put(Info.SYN, r.getSyn());
		cv.put(Info.NICKNAME, r.getNickName());
		cv.put(Info.TASKNAME, r.getTaskName());
		cv.put(Info.TASKTAG, r.getTaskTag());
		cv.put(Info.TASKDETAIL, r.getTaskDetail());
		cv.put(Info.TASKCREATETIME, r.getTaskCreateTime());
		cv.put(Info.TASKSTARTTIME, r.getTaskStartTime());
		cv.put(Info.TASKEDITTIME, r.getTaskEditTime());
		cv.put(Info.TASKENDTIME, r.getTaskEndTime());
		cv.put(Info.TASKIMPORTANT, r.getTaskImportant());
		cv.put(Info.TASKCOMPLETE, r.getTaskComplete());
		cv.put(Info.TASKSTATUS, r.getTaskStatus());
		cv.put(Info.TASKTYPE, r.getTaskType());
		cv.put(Info.ALARM, r.getAlarm());
		cv.put(Info.ALARMTYPE, r.getAlarmType());
		cv.put(Info.ALARMCYCLE, r.getAlarmCycle());
		cv.put(Info.ALARMTIME, r.getAlarmTime());
		
		return db.insert(Info.TABLE, null, cv);
	}
	
	public synchronized int update(Record r) {
		ContentValues cv = new ContentValues();
		cv.put(Info.OPTYPE, r.getOptype());
		cv.put(Info.UID, r.getUid());
		cv.put(Info.TASKID, r.getTaskId());
		cv.put(Info.SYN, r.getSyn());
		cv.put(Info.NICKNAME, r.getNickName());
		cv.put(Info.TASKNAME, r.getTaskName());
		cv.put(Info.TASKTAG, r.getTaskTag());
		cv.put(Info.TASKDETAIL, r.getTaskDetail());
		cv.put(Info.TASKCREATETIME, r.getTaskCreateTime());
		cv.put(Info.TASKSTARTTIME, r.getTaskStartTime());
		cv.put(Info.TASKEDITTIME, r.getTaskEditTime());
		cv.put(Info.TASKENDTIME, r.getTaskEndTime());
		cv.put(Info.TASKIMPORTANT, r.getTaskImportant());
		cv.put(Info.TASKCOMPLETE, r.getTaskComplete());
		cv.put(Info.TASKSTATUS, r.getTaskStatus());
		cv.put(Info.TASKTYPE, r.getTaskType());
		cv.put(Info.ALARM, r.getAlarm());
		cv.put(Info.ALARMTYPE, r.getAlarmType());
		cv.put(Info.ALARMCYCLE, r.getAlarmCycle());
		cv.put(Info.ALARMTIME, r.getAlarmTime());
		
		return db.update(Info.TABLE, cv, Info.ID + "=?", new String[]{String.valueOf(r.getId())});
	}
	
	public synchronized int updateByTaskId(Record r) {
		ContentValues cv = new ContentValues();
//		cv.put(Info.OPTYPE, Info.OP_ADD);
		cv.put(Info.OPTYPE, r.getOptype());
		cv.put(Info.UID, r.getUid());
		cv.put(Info.SYN, r.getSyn());
		cv.put(Info.NICKNAME, r.getNickName());
		cv.put(Info.TASKNAME, r.getTaskName());
		cv.put(Info.TASKTAG, r.getTaskTag());
		cv.put(Info.TASKDETAIL, r.getTaskDetail());
		cv.put(Info.TASKCREATETIME, r.getTaskCreateTime());
		cv.put(Info.TASKSTARTTIME, r.getTaskStartTime());
		cv.put(Info.TASKEDITTIME, r.getTaskEditTime());
		cv.put(Info.TASKENDTIME, r.getTaskEndTime());
		cv.put(Info.TASKIMPORTANT, r.getTaskImportant());
		cv.put(Info.TASKCOMPLETE, r.getTaskComplete());
		cv.put(Info.TASKSTATUS, r.getTaskStatus());
		cv.put(Info.TASKTYPE, r.getTaskType());
		cv.put(Info.ALARM, r.getAlarm());
		cv.put(Info.ALARMTYPE, r.getAlarmType());
		cv.put(Info.ALARMCYCLE, r.getAlarmCycle());
		cv.put(Info.ALARMTIME, r.getAlarmTime());
		
		return db.update(Info.TABLE, cv, Info.TASKID + "=?", new String[]{String.valueOf(r.getTaskId())});
	}
	
	public synchronized int delete(long id) {
		return db.delete(Info.TABLE, Info.ID + "=?", new String[]{String.valueOf(id)});
	}
	
	public synchronized long addUser(long uid, String name, String pwd, String token) {
		ContentValues cv = new ContentValues();
		cv.put(Info.UID, uid);
		cv.put(Info.NICKNAME, name);
		cv.put(Info.PWD, pwd);
		cv.put(Info.TOKEN, token);
		
		return db.insert(Info.USER_TABLE, null, cv);
	}
	
	public synchronized boolean hasTask(long taskId) {
		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + Info.TASKID + " =?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(taskId)});
		boolean i = false;
		if(cursor.getCount() > 0)
			i = true;
		cursor.close();
		return i;
	}
	
	public synchronized HashMap<String, String> getPwdLastSynTimeToken(long uid) {
		String sql = "SELECT * FROM " + Info.USER_TABLE + " WHERE " + Info.UID + " =?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(uid)});
		
		if(cursor.moveToNext()) {
			String pwd = cursor.getString(cursor.getColumnIndex(Info.PWD));
			long lastSynTime = cursor.getLong(cursor.getColumnIndex(Info.LASTSYNTIME));
			String token = cursor.getString(cursor.getColumnIndex(Info.TOKEN));
			cursor.close();
			HashMap<String, String> ans = new HashMap<String, String>();
			ans.put(Info.PWD, pwd);
			ans.put(Info.LASTSYNTIME, String.valueOf(lastSynTime));
			ans.put(Info.TOKEN, token);
			return ans;
		}
		cursor.close();
		return null;
	}
	
	public synchronized int updateLastSynTimeAndToken(long uid, long lastSynTime, String token) {
		ContentValues cv = new ContentValues();
		cv.put(Info.LASTSYNTIME, lastSynTime);
		cv.put(Info.TOKEN, token);
		return db.update(Info.USER_TABLE, cv, Info.UID + "=?", new String[]{String.valueOf(uid)});
	}
	
	public synchronized int updateLastSynTime(long uid, long lastSynTime) {
		ContentValues cv = new ContentValues();
		cv.put(Info.LASTSYNTIME, lastSynTime);
		return db.update(Info.USER_TABLE, cv, Info.UID + "=?", new String[]{String.valueOf(uid)});
	}
	
	public synchronized int updateToken(long uid, String token) {
		ContentValues cv = new ContentValues();
		cv.put(Info.TOKEN, token);
		return db.update(Info.USER_TABLE, cv, Info.UID + "=?", new String[]{String.valueOf(uid)});
	}
	
	public synchronized ArrayList<String> getUserNames() {
		String sql = "SELECT * FROM " + Info.USER_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<String> res = null;
		if(cursor.getCount() > 0) {
			res = new ArrayList<String>();
			while(cursor.moveToNext()) {
				res.add(cursor.getString(cursor.getColumnIndex(Info.NICKNAME)));
			}
		}
		cursor.close();
		return res;
	}
	public synchronized ArrayList<String> getUserPwds() {
		String sql = "SELECT * FROM " + Info.USER_TABLE;
		Cursor cursor = db.rawQuery(sql, null);
		ArrayList<String> res = null;
		if(cursor.getCount() > 0) {
			res = new ArrayList<String>();
			while(cursor.moveToNext()) {
				res.add(cursor.getString(cursor.getColumnIndex(Info.PWD)));
			}
		}
		cursor.close();
		return res;
	}
	
	public synchronized boolean hasUser(long uid) {
		String sql = "SELECT * FROM " + Info.USER_TABLE + " WHERE " + Info.UID + " =?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(uid)});
		boolean ans = false;
		if(cursor.getCount() == 1)
			ans = true;
		cursor.close();
		return ans;
	}
	
	public synchronized ArrayList<Record> getPeriodRecordsByDate(long uid, Date date,int planOrNote) {
		System.out.println(date.getTime());
		long start;
		long end;
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		start = date.getTime();
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		end = date.getTime();
		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + Info.UID + " =? and " + Info.ALARM + " =? and " + Info.ALARMTIME + " >= ? and "
				+ Info.ALARMTIME + " <= ? and " + Info.TASKSTATUS + " != ?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(uid), String.valueOf(planOrNote),String.valueOf(start), String.valueOf(end), String.valueOf(Info.YES_DELETE)});
//		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + Info.UID + " =?";
//		Cursor cursor = db.rawQuery(sql, new String[]{"1"});
		ArrayList<Record> records = null;
		if(cursor.getCount() > 0) {
			records = new ArrayList<Record>();
			
			while(cursor.moveToNext()) {
				records.add(new Record(
						cursor.getLong(cursor.getColumnIndex(Info.ID)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKID)),
						cursor.getInt(cursor.getColumnIndex(Info.UID)),
						cursor.getInt(cursor.getColumnIndex(Info.OPTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.SYN)),
						cursor.getString(cursor.getColumnIndex(Info.NICKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKTAG)),
						cursor.getString(cursor.getColumnIndex(Info.TASKDETAIL)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKCREATETIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKSTARTTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKENDTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKEDITTIME)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKIMPORTANT)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKCOMPLETE)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKSTATUS)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARM)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMCYCLE)),
						cursor.getLong(cursor.getColumnIndex(Info.ALARMTIME))
						));
			}
		}
		cursor.close();
		return records;
	}
	
	public synchronized ArrayList<Record> getPeriodRecords(long uid, long start, long end) {
		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + Info.UID + " =? and " + Info.ALARMTIME + " >= ? and "
				+ Info.ALARMTIME + " <= ? and " + Info.TASKSTATUS + " != ?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(uid), String.valueOf(start), String.valueOf(end), String.valueOf(Info.YES_DELETE)});
//		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + Info.UID + " =?";
//		Cursor cursor = db.rawQuery(sql, new String[]{"1"});
		ArrayList<Record> records = null;
		if(cursor.getCount() > 0) {
			records = new ArrayList<Record>();
			
			while(cursor.moveToNext()) {
				records.add(new Record(
						cursor.getLong(cursor.getColumnIndex(Info.ID)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKID)),
						cursor.getInt(cursor.getColumnIndex(Info.UID)),
						cursor.getInt(cursor.getColumnIndex(Info.OPTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.SYN)),
						cursor.getString(cursor.getColumnIndex(Info.NICKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKTAG)),
						cursor.getString(cursor.getColumnIndex(Info.TASKDETAIL)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKCREATETIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKSTARTTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKENDTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKEDITTIME)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKIMPORTANT)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKCOMPLETE)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKSTATUS)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARM)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMCYCLE)),
						cursor.getLong(cursor.getColumnIndex(Info.ALARMTIME))
						));
			}
		}
		cursor.close();
		return records;
	}
	
	public synchronized ArrayList<Record> getCacheRecords(long uid) {
		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + Info.UID + " =? and " + Info.SYN + " =?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(uid), String.valueOf(Info.NO_SYN)});
		ArrayList<Record> records = null;
		if(cursor.getCount() > 0) {
			records = new ArrayList<Record>();
			
			while(cursor.moveToNext()) {
				records.add(new Record(
						cursor.getLong(cursor.getColumnIndex(Info.ID)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKID)),
						cursor.getInt(cursor.getColumnIndex(Info.UID)),
						cursor.getInt(cursor.getColumnIndex(Info.OPTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.SYN)),
						cursor.getString(cursor.getColumnIndex(Info.NICKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKTAG)),
						cursor.getString(cursor.getColumnIndex(Info.TASKDETAIL)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKCREATETIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKSTARTTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKENDTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKEDITTIME)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKIMPORTANT)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKCOMPLETE)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKSTATUS)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARM)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMCYCLE)),
						cursor.getLong(cursor.getColumnIndex(Info.ALARMTIME))
						));
			}
		}
		cursor.close();
		return records;
	}
	
	
	
	public synchronized ArrayList<Record> getRecordsByTaskDetailKeyWords(String keyWord) {
		String sql = "SELECT * FROM " + Info.TABLE + " WHERE "  + Info.TASKDETAIL + " LIKE '%" + keyWord +  "%'";
		Cursor cursor = db.rawQuery(sql, new String[]{});
		ArrayList<Record> records = null;
		if(cursor.getCount() > 0) {
			records = new ArrayList<Record>();
			
			while(cursor.moveToNext()) {
				records.add(new Record(
						cursor.getLong(cursor.getColumnIndex(Info.ID)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKID)),
						cursor.getInt(cursor.getColumnIndex(Info.UID)),
						cursor.getInt(cursor.getColumnIndex(Info.OPTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.SYN)),
						cursor.getString(cursor.getColumnIndex(Info.NICKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKNAME)),
						cursor.getString(cursor.getColumnIndex(Info.TASKTAG)),
						cursor.getString(cursor.getColumnIndex(Info.TASKDETAIL)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKCREATETIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKSTARTTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKENDTIME)),
						cursor.getLong(cursor.getColumnIndex(Info.TASKEDITTIME)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKIMPORTANT)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKCOMPLETE)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKSTATUS)),
						cursor.getInt(cursor.getColumnIndex(Info.TASKTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARM)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMTYPE)),
						cursor.getInt(cursor.getColumnIndex(Info.ALARMCYCLE)),
						cursor.getLong(cursor.getColumnIndex(Info.ALARMTIME))
						));
			}
		}
		cursor.close();
		return records;
	}
	
	public synchronized Record getRecord(String idType, long id) {
		String sql = "SELECT * FROM " + Info.TABLE + " WHERE " + idType + " =?";
		Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});
		
		Record r = null;
		if(cursor.moveToNext()) {
			r = new Record(
					cursor.getLong(cursor.getColumnIndex(Info.ID)),
					cursor.getLong(cursor.getColumnIndex(Info.TASKID)),
					cursor.getInt(cursor.getColumnIndex(Info.UID)),
					cursor.getInt(cursor.getColumnIndex(Info.OPTYPE)),
					cursor.getInt(cursor.getColumnIndex(Info.SYN)),
					cursor.getString(cursor.getColumnIndex(Info.NICKNAME)),
					cursor.getString(cursor.getColumnIndex(Info.TASKNAME)),
					cursor.getString(cursor.getColumnIndex(Info.TASKTAG)),
					cursor.getString(cursor.getColumnIndex(Info.TASKDETAIL)),
					cursor.getLong(cursor.getColumnIndex(Info.TASKCREATETIME)),
					cursor.getLong(cursor.getColumnIndex(Info.TASKSTARTTIME)),
					cursor.getLong(cursor.getColumnIndex(Info.TASKENDTIME)),
					cursor.getLong(cursor.getColumnIndex(Info.TASKEDITTIME)),
					cursor.getInt(cursor.getColumnIndex(Info.TASKIMPORTANT)),
					cursor.getInt(cursor.getColumnIndex(Info.TASKCOMPLETE)),
					cursor.getInt(cursor.getColumnIndex(Info.TASKSTATUS)),
					cursor.getInt(cursor.getColumnIndex(Info.TASKTYPE)),
					cursor.getInt(cursor.getColumnIndex(Info.ALARM)),
					cursor.getInt(cursor.getColumnIndex(Info.ALARMTYPE)),
					cursor.getInt(cursor.getColumnIndex(Info.ALARMCYCLE)),
					cursor.getLong(cursor.getColumnIndex(Info.ALARMTIME))
					);
		}
		cursor.close();
		return r;
	}
}
