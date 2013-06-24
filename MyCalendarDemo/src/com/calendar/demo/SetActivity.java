package com.calendar.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
					//在这些月份中间可以转化
					if(Month>=1 && Month<=10){
						try {
							perl(Year,Month,Day);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}});
		
	}
	
	private void perl(int year ,int month ,int day) throws IOException{
		String msg =execPerl("perl/subroutine_10.pl");
		System.out.println(msg);
		
	/*	BufferedReader br= null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("perl/T2013e.txt"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(br==null);
		
		for(String line = br.readLine(); line != null; line = br.readLine()) {
		  System.out.println(line);
		}
		br.close();*/
		
	}
	
	private static String execPerl(String fileName){
		String cmd = "";
		String msg = "";
		String brs = "";
		cmd = "perl " + fileName;
		try {
			Process pro = Runtime.getRuntime().exec(cmd);
			InputStream ins = pro.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(ins));
			while((brs = br.readLine()) != null){
				msg += brs;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

}
