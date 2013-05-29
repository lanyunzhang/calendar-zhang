package com.calendar.demo;

import com.calendar.demo.MainActivity.MyHandler;

import android.app.Application;
/**
 * preference 已经共享的handler都在这里设置，所有activity共享这里的变量设置
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
