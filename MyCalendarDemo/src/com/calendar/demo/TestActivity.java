package com.calendar.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.calendar.util.CalendarView;

public class TestActivity extends Activity{

	private CalendarView view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_calendar);
		
	}

}
