package com.calendar.demo;

import com.calendar.util.Lunar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity{
	
	private TextView tv = null;
	private Message msg = null;
	private Handler handler = null;
	
	private EditText et_year;
	private EditText et_month;
	private EditText et_day;
	private Button  toTheDate;
	private Lunar lunar;
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.set);
		
		
		tv = (TextView)findViewById(R.id.set);
		et_year = (EditText) findViewById(R.id.year_edittext);
		et_month =  (EditText) findViewById(R.id.month_edittext);
		et_day = (EditText) findViewById(R.id.day_edittext);
		toTheDate = (Button) findViewById(R.id.toTheDate);
		
		if(APP.getpreferences().getMemoPlan())
			tv.setText("on");
		else
			tv.setText("off");
		
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
	
		
		toTheDate.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				String year = et_year.getText().toString();
				String month = et_month.getText().toString();
				String day = et_day.getText().toString();
				
				int Year = Integer.valueOf(year).intValue();
				int Month = Integer.valueOf(month).intValue();
				int Day = Integer.valueOf(day).intValue();
				int dayCount = Lunar.getLunarMonthDays(Year, Month);
				
				if(Day > dayCount){
					System.out.println("ERROR");
				}else{
					
					//将农历日期转化为阳历日期，然后切换过去
					System.out.println(Lunar.getLunarDay(Year, Month, Day));
					
					Message msg = handler.obtainMessage();
					msg.arg1 = MainActivity.TO_MONTH_YEAR;
					msg.arg2 = Month;
					msg.what = Year;
					handler.sendMessage(msg);
					finish();
				}
			}});
		
	}

}
