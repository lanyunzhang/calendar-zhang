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

	    builder.setTitle("��ȷ��Ҫ�뿪��");  

        builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {  

	      public void onClick(DialogInterface dialog, int whichButton) {  

	              //������ӵ��ȷ������߼� 
	    	  	   mp.stop();
	               //showDialog("��ѡ����ȷ��");  

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
