package com.calendar.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;


public class SettingActivity extends Activity {
	private boolean flag = false;
	private ImageView iv = null;
	private ImageView back = null;
	private Message msg = null;
	private Handler handler = null;
	private TextView plan_analysis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_main);
        iv = (ImageView) findViewById(R.id.off);
        back = (ImageView) findViewById(R.id.return_normal);
        plan_analysis = (TextView) findViewById(R.id.plan_analysis);
        
        
        back.setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View view) {
				finish();
			}});
        
        
        handler = APP.getHandler();
        flag = APP.getpreferences().getMemoPlan();
        if(flag){
        	iv.setImageDrawable(getResources().getDrawable(R.drawable.on));
        	plan_analysis.setTextColor(Color.rgb(0, 0, 0));
        }
        else{
        	iv.setImageDrawable(getResources().getDrawable(R.drawable.off));
        	plan_analysis.setTextColor(Color.rgb(171, 171, 171));
        }
    }
    
    public void switchOnOff(View source)
    {
    	
    	msg = handler.obtainMessage();
    	if(!flag){
    		iv.setImageDrawable(getResources().getDrawable(R.drawable.on));    		
    		flag=true;
    		APP.getpreferences().putMemoPlan(true);
			msg.arg1 = MainActivity.SET_ON;
			handler.sendMessage(msg);
			plan_analysis.setTextColor(Color.rgb(0, 0, 0));
    	}else{
    		iv.setImageDrawable(getResources().getDrawable(R.drawable.off));
    		flag=false;
			msg.arg1 = MainActivity.SET_OFF; 
			APP.getpreferences().putMemoPlan(false);
			handler.sendMessage(msg);
			plan_analysis.setTextColor(Color.rgb(171, 171, 171));
    	}
    	
    	
    }
    
}
