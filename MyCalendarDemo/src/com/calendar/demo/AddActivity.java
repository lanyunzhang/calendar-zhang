package com.calendar.demo;

import android.app.Activity;
import android.app.PendingIntent.CanceledException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AddActivity extends Activity  implements OnClickListener{
	
	private ImageView  back = null;
	private TextView   cancel = null;
	private EditText   addEventContent = null;
	private TextView   save = null;
	private ImageButton b_alarm = null;
	private ImageButton b_mp = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnote);
		
		initLayout();
		initListener();
	}
	
	private void initLayout(){
		cancel = (TextView) findViewById(R.id.cancel);
		back = (ImageView) findViewById(R.id.back);
		addEventContent = (EditText) findViewById(R.id.add_event_content);
		save = (TextView) findViewById(R.id.save);
		b_alarm = (ImageButton) findViewById(R.id.b_alarm);
		b_mp = (ImageButton) findViewById(R.id.b_mp);
		
	}
	
	private void initListener(){
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
		addEventContent.setOnClickListener(this);
		save.setOnClickListener(this);
		b_alarm.setOnClickListener(this);
		b_mp.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view == cancel){
			finish();
		}
	}
	

}
