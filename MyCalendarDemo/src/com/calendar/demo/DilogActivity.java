package com.calendar.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;


public class DilogActivity extends Activity {
	private MediaPlayer mp;
	protected void onCreate(Bundle bundle){
		
		super.onCreate(bundle);
		
		mp = MediaPlayer.create(DilogActivity.this,R.raw.rain);
		
		mp.start();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(DilogActivity.this);
		
		builder.setIcon(R.drawable.alarm_press);  

	    builder.setTitle("你确定要离开吗？");  

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  

	      public void onClick(DialogInterface dialog, int whichButton) {  

	              //这里添加点击确定后的逻辑 
	    	  	   mp.stop();
	               //showDialog("你选择了确定");  

	           }  

	       });  

	       builder.create().show();  
	}
	
	
	private void showDialog(String str) {  

		new AlertDialog.Builder(DilogActivity.this)  

		     .setMessage(str)  

		     .show();  

	}
	

}
