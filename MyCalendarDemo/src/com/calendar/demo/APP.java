package com.calendar.demo;

import com.calendar.demo.MainActivity.MyHandler;
import com.calendar.util.DB;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * preference �Ѿ������handler�����������ã�����activity��������ı�������
 * @author zhanglanyun
 *
 */
public class APP extends Application {
	private static MyHandler myhandler = null;
	private static DB db = null;
	private static APP app= null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
	}

	public static MyHandler getHandler(){
		return myhandler;
	}
	
	public void setHandler(MyHandler myhandler){
		this.myhandler = myhandler;
	}
	
	public static DB getDatabase(){
		if(db == null)
			db = new DB(app);
		return db;
	}
	
	 
}
