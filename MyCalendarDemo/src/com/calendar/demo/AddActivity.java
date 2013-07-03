package com.calendar.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.calendar.demo.view.widget.OnWheelChangedListener;
import com.calendar.demo.view.widget.OnWheelClickedListener;
import com.calendar.demo.view.widget.OnWheelScrollListener;
import com.calendar.demo.view.widget.WheelView;
import com.calendar.demo.view.widget.adapters.ArrayWheelAdapter;
import com.calendar.demo.view.widget.adapters.NumericWheelAdapter;
import com.calendar.util.AlarmTime;
import com.calendar.util.AlarmToString;
import com.calendar.util.DB;
import com.calendar.util.Record;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

/**
 * 
 * @author zhanglanyun
 * 接受一个时间日期，并且判断是更新还是添加
 */
public class AddActivity extends Activity  implements OnClickListener{
	
	private ImageView  back = null;
	private TextView   cancel = null;
	private EditText   addEventContent = null;
	private TextView   save = null;
	private ImageButton b_alarm = null;
	private ImageView b_mp = null;
	private View popupView = null;
	private TextView tv1 = null;
	private TextView tv2 = null;
	private TextView tv3 = null;
	private TextView tv4 = null;
	private TextView tv5 = null;
	private PopupWindow popupWindow = null;
	private ArrayList<Time> time = null;
	private ListView alarmlist = null;
	private MyAlarmAdapter maa = null;
	private Date date = null;
	private DB db = null;
	private Record recordintent = null;
	private TextView addNoteDate = null;
	private ArrayList<Integer> alarmRequestCode = null;
	
	private boolean isPopup = false;
	private boolean timeScrolled = false;
	private boolean isAddOrUpdate = false;
	private boolean isOff = true;
	
	private int curyear = 0;
	private int curmonth = 0;
	private int curday = 0;
	private int curhour = 0;
	private int curmin = 0;
	private int currIndex = -1;
	private int currentyear = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addnote);
		
		initLayout();
		initListener();
		getData();
		setData();
	}
	
	private void setData(){
		if(!isAddOrUpdate){
			addEventContent.setText(recordintent.getTaskDetail());
			//同时查询相应的闹钟，添加闹钟
			String alarmcode = recordintent.getAlarmTimes();
			System.out.println("alarmcode"+alarmcode);
			if(alarmcode.length() > 1){
				AlarmToString alarmtostring = new AlarmToString(alarmcode);
				alarmtostring.StringToAlarms();
				ArrayList<AlarmTime> alarmtime = alarmtostring.getAlarmTimes();
				Date date = new Date();
				maa.getArrayList().clear();
				alarmRequestCode.clear();
				for(AlarmTime alarm :alarmtime){
					date.setTime( alarm.getTime());
					Time timeo = new Time(date.getYear(),date.getMonth(),date.getDate(),
							                            date.getHours(),date.getMinutes());
					time.add(timeo);
					maa.getArrayList().add(date.getYear() + 1900 +"-" +date.getMonth() +"-" + date.getDate() +"-"+
							date.getHours() +"-" +date.getMinutes());
					
					alarmRequestCode.add(alarm.getTag());
					
				}
			}
			maa.notifyDataSetChanged();
		}
		//设置文本显示时间
		addNoteDate.setText((date.getYear() + 1900) +"-" +(date.getMonth()+1) +"-" + date.getDate());
		
	}
	
	private void initLayout(){
		cancel = (TextView) findViewById(R.id.cancel);
		back = (ImageView) findViewById(R.id.back);
		addEventContent = (EditText) findViewById(R.id.add_event_content);
		save = (TextView) findViewById(R.id.save);
		b_alarm = (ImageButton) findViewById(R.id.b_alarm);
		b_mp = (ImageView) findViewById(R.id.b_mp);
		alarmlist = (ListView) findViewById(R.id.alarmlist);
		addNoteDate = (TextView)findViewById(R.id.date);
		
		time = new ArrayList<Time>();
		maa = new MyAlarmAdapter(AddActivity.this);
		alarmlist.setAdapter(maa);
		alarmlist.setCacheColorHint(Color.TRANSPARENT);
		b_mp.setVisibility(View.GONE);
		
	}
	
	private void initListener(){
		back.setOnClickListener(this);
		cancel.setOnClickListener(this);
		addEventContent.setOnClickListener(this);
		save.setOnClickListener(this);
		b_alarm.setOnClickListener(this);
		b_mp.setOnClickListener(this);
	}

	private void getData(){
		Bundle bundle = getIntent().getExtras();
		isAddOrUpdate = bundle.getBoolean("ADD");
		currIndex = bundle.getInt("INDEX");
		date = (Date) bundle.getSerializable("DATE");
		recordintent = (Record) bundle.getSerializable("UPDATE");
		db = APP.getDatabase();
		
		if(currIndex == 1){
			b_mp.setVisibility(View.VISIBLE);
			b_alarm.setVisibility(View.VISIBLE);
			isOff = false;
			b_mp.setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_switch_default_on));
		}else if (currIndex == 0){
			b_alarm.setVisibility(View.GONE);
			b_mp.setVisibility(View.VISIBLE);
			
		}
		
		alarmRequestCode = maa.getAlarmRequestCode(); 
		
	}
	
	@Override
	public void onClick(View view) {
		if(view == cancel || view == back){
			finish();
		}
		
		if(view == b_alarm){
			clickAlarm();
		}
		
		if(view == save)
			saveNote();
		
		if(view == b_mp){
			switchmemoplan();
		}
	}
	
	private void switchmemoplan(){
		
		if(isOff){
			isOff = false;
			b_mp.setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_switch_default_on));
			b_alarm.setVisibility(View.VISIBLE);
		}else{
			isOff = true;
			b_mp.setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_switch_default_off));
			b_alarm.setVisibility(View.GONE);
		}
	}
	
	private void saveNote(){
		String content = addEventContent.getText().toString();
		if(content.length() == 0 || content.equals("") || content == null || 
				content.trim().length() == 0){
			Toast.makeText(AddActivity.this, getString(R.string.contentisnull),
					Toast.LENGTH_SHORT).show();
		}else{
			//不为空的话，保存字符串，并且日历显示，随手记显示，listview添加相应的内容
			
			String text = content.trim();
			Message msg = new Message();
			Record record = new Record();
			
			if(maa.getArrayList().size() > 0){
				record.setAlarm(1);
			}
			if(isAddOrUpdate){
				
				record.setTaskDetail(text);
				record.setUid(1);
				record.setAlarmTime(date.getTime());
				String alarmcode = setAlarm(record);
				System.out.println(alarmcode+"*************");
				record.setAlarmTimes(alarmcode);
				record.setId(db.add(record));
				
				msg.arg1 = MainActivity.ADD_LIST; 
				msg.obj = record;
				APP.getHandler().sendMessage(msg);
				
			}else{
				recordintent.setTaskDetail(text);
				String alarmcode = "|";
				if(time.size() != 0){ //有一个或者多个闹铃
					record.setAlarm(1);
					
					for(Time oneTime:time){
						int alarm = oneTime.getTag();
						Calendar alarmtime = Calendar.getInstance();
						alarmtime.set(Calendar.YEAR, oneTime.getYear());
						alarmtime.set(Calendar.MONTH, oneTime.getMonth());
						alarmtime.set(Calendar.DAY_OF_MONTH, oneTime.getDay());
						alarmtime.set(Calendar.HOUR_OF_DAY, oneTime.getHour());
						alarmtime.set(Calendar.MINUTE, oneTime.getMinutes());
						alarmtime.set(Calendar.SECOND, 0);
						
						alarmcode  = alarmcode + alarmtime.getTimeInMillis(); 
						alarmcode  = alarmcode + "#" + alarm;
						alarmcode  = alarmcode + "|";
			
					}
					
					System.out.println(alarmcode+"----------------------");
					recordintent.setAlarmTimes(alarmcode);
					db.update(recordintent);
				}else{
					recordintent.setAlarmTimes(alarmcode);
					db.update(recordintent);
				}
				
				
				msg.arg1 = MainActivity.UPDATE;
				msg.obj = recordintent;
				APP.getHandler().sendMessage(msg);
				
			}
			//普通模式
			if(!APP.getpreferences().getMemoPlan()){
				msg.arg2 = MainActivity.NOMEMOPLAN;
			}else{ // 计划和备忘的插入和更新
		 		
		 		if(currIndex == 0){ //备忘
		 			msg.arg2 = MainActivity.MEMO;
		 		}else if (currIndex == 1){ //计划
		 			
		 			msg.arg2 = MainActivity.PLAN;
		 		}
			}
			
			//关掉软键盘
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.hideSoftInputFromWindow(save.getWindowToken(), 0);
			//添加闹钟，并且为每一个闹钟设置requestcode
			//区分是添加还是更新，从入口区分
			//为什么不执行datasetchange也可以呢？
			finish();
			
		}
		
	}


	private void clickAlarm(){
		if(!isPopup){
			initPopUpWindow();
			//关掉软键盘
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.hideSoftInputFromWindow(b_alarm.getWindowToken(), 0);
		}
	}
	
	private void initPopUpWindow(){
		isPopup = true;
		// set current time
	    Calendar c = Calendar.getInstance();
	    curyear = 100;
	    currentyear = c.get(Calendar.YEAR);
	    curmonth = c.get(Calendar.MONTH);
	    curday = c.get(Calendar.DAY_OF_MONTH);
		curhour = c.get(Calendar.HOUR_OF_DAY);
		curmin = c.get(Calendar.MINUTE);
		
		popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_up_date, null);
		tv1 = (TextView) popupView.findViewById(R.id.tv1);
		tv2 = (TextView) popupView.findViewById(R.id.tv2);
		tv3 = (TextView) popupView.findViewById(R.id.tv3);
		tv4 = (TextView) popupView.findViewById(R.id.tv4);
		tv5 = (TextView) popupView.findViewById(R.id.tv5);
		
		popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		//popupWindow.showAsDropDown(layContent,((layContent.getWidth()-popupView.getWidth())/4),0);
		popupWindow.showAtLocation(addEventContent, Gravity.CENTER, 0, 0);
		
		// set the view of alarm
		Calendar calendar = Calendar.getInstance();
		Button date_cancel = (Button) popupView.findViewById(R.id.date_cancel);
		Button date_ok = (Button)popupView.findViewById(R.id.date_ok);
		date_cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				isPopup = false;
		}});
		date_ok.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				isPopup = false;
				if(!timeScrolled){
					System.out.println(curyear+" "+curmonth+" "+curday+" "+curhour+" "+curmin);
					//添加定闹钟逻辑，如果设定成功闹钟的颜色要有变化-----------
					//setAlarm(curyear+1913,curmonth,curday,curhour,curmin);,将闹钟添加到一个list中，并不真的进行更新
					addTimeList(curyear + currentyear-100,curmonth,curday,curhour,curmin);
					popupWindow.dismiss();
				}
		}});
        
        final WheelView month = (WheelView) popupView.findViewById(R.id.month);
        final WheelView year = (WheelView) popupView.findViewById(R.id.year);
        final WheelView day = (WheelView) popupView.findViewById(R.id.day);
	        
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };

        // month
        int curMonth = calendar.get(Calendar.MONTH);
        String months[] = new String[] {"1", "2", "3", "4", "5",
                "6", "7", "8", "9", "10", "11", "12"};
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
    
        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(this, currentyear-100, currentyear + 100, 100));
        year.setCurrentItem(100);
        year.addChangingListener(listener);
        
        //day
        updateDays(year, month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        
	        
		final WheelView hours = (WheelView) popupView.findViewById(R.id.hour);
		DateNumericAdapter nwa_h = new DateNumericAdapter(this,0,23,curhour);
		hours.setViewAdapter(nwa_h);
		hours.setCyclic(true);
		
	
		final WheelView mins = (WheelView) popupView.findViewById(R.id.mins);
		DateNumericAdapter nwa_m = new DateNumericAdapter(this,0,59,curmin);
		mins.setViewAdapter(nwa_m);
		mins.setCyclic(true);
	
		hours.setCurrentItem(curhour);
		mins.setCurrentItem(curmin);
	
		// add listeners
		addChangingListener(mins, "min");
		addChangingListener(hours, "hour");
		
	
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					curhour= hours.getCurrentItem();
					curmin = mins.getCurrentItem();
					curmonth = month.getCurrentItem();
					curyear = year.getCurrentItem();
					curday = day.getCurrentItem()+1;
					
				}
			}
		};
		year.addChangingListener(wheelListener);
		month.addChangingListener(wheelListener);
		day.addChangingListener(wheelListener);
		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);
		
		OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);
        mins.addClickingListener(click);
        month.addClickingListener(click);
        year.addClickingListener(click);
        day.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				curhour = hours.getCurrentItem();
				curmin = mins.getCurrentItem();
				curmonth = month.getCurrentItem();
				curyear = year.getCurrentItem();
				curday = day.getCurrentItem()+1;
			}
		};
		
		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		month.addScrollingListener(scrollListener);
		year.addScrollingListener(scrollListener);
		day.addScrollingListener(scrollListener);
		
	}
	
	
	  /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(this, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }
    
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        
        /**
         * Constructor
         */
        public DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(16);
        }
        
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}
	
	
	private void addTimeList(int year,int month,int day,int hour,int minutes){
		Time alarmtime = new Time(year,month,day,hour,minutes);
		time.add(alarmtime);
		
		maa.getArrayList().add(year+"-"+month+"-"+day+"-"+hour+"-"+minutes);
		maa.notifyDataSetChanged();
	}
	

	public String setAlarm(Record record){
		//先得到年月日时分
		String alarmcode ="|";
		if(time.size() != 0){ //有一个或者多个闹铃
			
			record.setAlarm(1);
			for(Time oneTime:time){
				
				int alarm = APP.getpreferences().getAlarm();//设置唯一闹铃的requestcode
				Calendar alarmtime = Calendar.getInstance();
				alarmtime.set(Calendar.YEAR, oneTime.getYear());
				alarmtime.set(Calendar.MONTH, oneTime.getMonth());
				alarmtime.set(Calendar.DAY_OF_MONTH, oneTime.getDay());
				alarmtime.set(Calendar.HOUR_OF_DAY, oneTime.getHour());
				alarmtime.set(Calendar.MINUTE, oneTime.getMinutes());
				alarmtime.set(Calendar.SECOND, 0);
				
				Intent intent = new Intent("com.calendar.demo.alarm");
				intent.setClass(AddActivity.this, AlarmReceiver.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("ALARM", record.getTaskDetail());
				PendingIntent pi=PendingIntent.getBroadcast(this, alarm, intent,0);
				oneTime.setTag(alarm);
				APP.getpreferences().putAlarm(alarm+1);
				
				alarmcode  = alarmcode + alarmtime.getTimeInMillis(); 
				alarmcode  = alarmcode + "#" + alarm;
				alarmcode  = alarmcode + "|";
				
				AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
			    am.set(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pi);
			    
			    /**
			     * 删除闹钟有两种方式，一种是PendingIntent完全一样
			     * 另外就是requestcode 和 接收器要一样,这样也可以新建一个PendingIntent然后取消
			     */
			    //在Record中更新或添加闹铃，同时还要注意把requestcode加进去
			  
			}
		}else{
			//没有闹铃
			record.setAlarm(0);
		}
		return alarmcode;
	}
	
	public void dialogAlarm(final int position){
		
		  final int tags =alarmRequestCode.get(position);
		  System.out.println(tags);
		  AlertDialog.Builder builder = new Builder(AddActivity.this);
		  builder.setMessage(getString(R.string.isDelete));
	  	  builder.setTitle(getString(R.string.tip));
	  	  builder.setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//删除闹铃的逻辑有问题
				Intent intent = new Intent("com.calendar.demo.alarm");
				intent.setClass(AddActivity.this, AlarmReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(AddActivity.this, tags, intent, 0);
				AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				am.cancel(pi);
				
				alarmRequestCode.remove(position);
				String timestring1 =maa.getArrayList().remove(position);
				
				for(Time times :time){
					String timestring2 = "";
					timestring2 = (times.getYear()+1900)+"-" +times.getMonth() +"-" + times.getDay() +"-"+
							times.getHour() +"-" +times.getMinutes();
					if(timestring2.equals(timestring1)){
						time.remove(times);
						break;
					}
				}
				maa.notifyDataSetChanged();
				
				//更新数据库的alarmcode
			
				
			}});
	  	  builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}});

	  	  builder.create().show();
	}
	

}
