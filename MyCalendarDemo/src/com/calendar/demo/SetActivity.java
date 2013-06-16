package com.calendar.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SetActivity extends Activity{
	
	private TextView tv = null;
	private Message msg = null;
	private Handler handler = null;
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		tv = new TextView(this);
		if(APP.getpreferences().getMemoPlan())
			tv.setText("on");
		else
			tv.setText("off");
		
		setContentView(tv);
		handler = APP.getHandler();
		msg = handler.obtainMessage();
		tv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				
				if(tv.getText().equals("off")){
					tv.setText("on");
					APP.getpreferences().putMemoPlan(true);
					msg.arg1 = MainActivity.SET_ON;
					handler.sendMessage(msg);
					finish();
				}else{
					tv.setText("off");
					msg.arg1 = MainActivity.SET_OFF; 
					APP.getpreferences().putMemoPlan(false);
					handler.sendMessage(msg);
					finish();
				}
			}
		});
	}

}
