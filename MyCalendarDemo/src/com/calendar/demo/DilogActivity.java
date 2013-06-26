package com.calendar.demo;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;


public class DilogActivity extends Activity {
	private MediaPlayer mp;
	private WakeLock mWakelock;
	protected void onCreate(Bundle bundle){
		
		super.onCreate(bundle);
		
		 final Window win = getWindow();
		 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				 | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
				 win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				 
		mp = MediaPlayer.create(DilogActivity.this,R.raw.shine);
		mp.start();
		
		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP 
				|PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
	    mWakelock.acquire();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(DilogActivity.this);
		
		builder.setIcon(R.drawable.alarm_press);  

	    builder.setTitle("你确定要离开吗？");  

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  

	      public void onClick(DialogInterface dialog, int whichButton) {  

	              //这里添加点击确定后的逻辑 
	    	  	   mp.stop();

	           }  

	       });  

	       builder.create().show();  
	       
	}
	

	@Override
	protected void onPause() {
		mWakelock.release();
		super.onPause();
	}
	

}
