package com.calendar.demo;

import com.calendar.demo.MainActivity.MyHandler;

import android.app.Application;
/**
 * preference �Ѿ������handler�����������ã�����activity��������ı�������
 * @author zhanglanyun
 *
 */
public class APP extends Application {
	private MyHandler myhandler = null;
	
	public MyHandler getHandler(){
		return myhandler;
	}
	
	public void setHandler(MyHandler myhandler){
		this.myhandler = myhandler;
	}
}
