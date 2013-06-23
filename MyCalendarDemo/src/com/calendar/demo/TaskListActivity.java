package com.calendar.demo;

import java.util.ArrayList;  
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  

import com.calendar.demo.DetailDialog.ListenerThree;
import com.calendar.util.DB;
import com.calendar.util.Info;
import com.calendar.util.Record;


import android.R.integer;
import android.app.Activity;  
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;  
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;  
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
  

public class TaskListActivity extends Activity {  
  
	
	
    private List<Map<String, Object>> mylist = new ArrayList<Map<String, Object>>();  
    private List<Map<String, Object>> splitList = new ArrayList<Map<String, Object>>();  
    private ArrayList<Record> records;
    private Button backButton;
    private Button findButton;
    private EditText findEditText;
    private TaskListAdapter adapter;
    private int choosePos;

    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        
		setContentView(R.layout.tasklistactivity);
        final ListView list = (ListView) findViewById(R.id.MyListView);  
  
        backButton = (Button)findViewById(R.id.backToMain);
        findButton = (Button)findViewById(R.id.searchDone);
        findEditText = (EditText)findViewById(R.id.taskSearchText);
        backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TaskListActivity.this.finish();
			}
		});

		findButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (findEditText.getText().length() == 0) {
					//
					refresh();

				} else {

					String str = findEditText.getText().toString();
					adapter.findTaskByString(str);
					adapter.notifyDataSetChanged();

					if (getTheFirstPositon(str) != -1) {
						list.setSelectionFromTop(getTheFirstPositon(str), 5);
					}
				}

			}
		});
//        setData();
        getData();
        adapter = new TaskListAdapter(this, mylist, splitList); 
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, // 各项的意义：arg1是当前item的view，
																				// arg2是当前item的ID。这个id根据你在适配器中的写法可以自己定义。
																				// arg3是当前的item在listView中的相对位置！
					long arg3) {
				showDetail(arg2);

			}
		});

       
    }  
    
	private void showDetail(int pos) {

		choosePos = pos;

		final DetailDialog dialog3 = new DetailDialog(TaskListActivity.this,
				R.style.MyDialog);

		ListenerThree listener3 = new ListenerThree() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.dialog_button_ok:
					choosePos = choosePos - 1;
					break;
				case R.id.dialog_button_cancle:
					choosePos = choosePos + 1;
					break;
				}
				dialog3.setDetail((String) mylist.get(choosePos).get(
						"taskDetail"));
			}

		};
		dialog3.SetListener(listener3);
		// 创建Dialog并设置样式主题
		// 这个是将Dialog移动到指定位置
		Window window = dialog3.getWindow();
		android.view.WindowManager.LayoutParams para = new WindowManager.LayoutParams();
		para.x = 80;// 设置x坐标
		para.y = -100;// 设置y坐标
		window.setAttributes(para);
		dialog3.setCanceledOnTouchOutside(true);
		// 设置点击Dialog外部任意区域关闭Dialog
		dialog3.show();
		dialog3.setDetail((String) mylist.get(choosePos).get("taskDetail"));

	}

    private void refresh() {
    	mylist.clear();
    	splitList.clear();
    	getData();
    	adapter.notifyDataSetChanged();
	}
    
	private int getTheFirstPositon(String str) {
		int pos = -1;
		for (int i = 0; i < mylist.size(); i++) {
			if (mylist.get(i).get("taskDetail").toString().contains(str)) {

				pos = i;
				break;
			}

		}

		return pos;
	}

//	private void getDataBySearchKeyWord(String keyWord) {
//
//		DB db = DB.getDB(MainActivity.this);
//		records = db.getRecordsByTaskDetailKeyWords(keyWord);
//		if (records != null) {
//			mylist.clear();
//			splitList.clear();
//			Map<String, Object> map = null;
//			for (Record r : records) {
//				if (r.getTaskStatus() == 0) {
//					map = new HashMap<String, Object>();
//					map.put("taskId", r.getTaskId());
//					map.put("isTaskComplete", r.getTaskComplete());
//					map.put("alarmTime", r.getAlarmTime());
//					map.put("taskDetail", r.getTaskDetail());
//					map.put("needAlarm", r.getAlarm());
//					mylist.add(map);
//
//				}
//			}
//			adapter.notifyDataSetChanged();
//		}
//
//	}
   
	private void getData() {

		Map<String, Object> mp = null;
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.MONTH, -3);
		Calendar c2 = Calendar.getInstance();
		c2.add(Calendar.MONTH, 1);
		long dayCount =  ((c2.getTimeInMillis() - c1.getTimeInMillis())/(1000*60*60*24));
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
				"yyyy-MM-dd");
	     for(int i = 0; i < dayCount; i++)
		{
	    	Calendar dcCalendar = c1;
	    	dcCalendar.add(Calendar.DAY_OF_MONTH, +1);
			Date date = dcCalendar.getTime();
			DB db = DB.getDB(TaskListActivity.this);
//			records = db.getPeriodRecords(1, start, end);
			records = db.getPeriodRecordsByDate(1, date, Info.PLAN);
			if (records != null) {
		    	mp = new HashMap<String, Object>();
				mp.put("taskDetail", format.format(dcCalendar.getTime()));
				mylist.add(mp);
				splitList.add(mp);
				Map<String, Object> map = null;
				for (Record r : records) {
					if (r.getTaskStatus() == 0) {
						map = new HashMap<String, Object>();
						map.put("taskId", r.getTaskId());
						map.put("isTaskComplete", r.getTaskComplete());
						map.put("alarmTime", r.getAlarmTime());
						map.put("taskDetail", r.getTaskDetail());
						map.put("needAlarm", r.getAlarm());
						mylist.add(map);
					}
				}
			}
			
			records = db.getPeriodRecordsByDate(1, date, Info.NOTE);
			if (records != null) {
				Map<String, Object> map = null;
				for (Record r : records) {
					if (r.getTaskStatus() == 0) {
						map = new HashMap<String, Object>();
						map.put("taskId", r.getTaskId());
						map.put("isTaskComplete", r.getTaskComplete());
						map.put("alarmTime", r.getAlarmTime());
						map.put("taskDetail", r.getTaskDetail());
						map.put("needAlarm", r.getAlarm());
						mylist.add(map);
					}
				}
			}
		}
	}

	}

