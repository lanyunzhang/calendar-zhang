package com.calendar.demo;

import java.util.ArrayList;
import java.util.Calendar;

import com.calendar.demo.view.widget.OnWheelChangedListener;
import com.calendar.demo.view.widget.OnWheelClickedListener;
import com.calendar.demo.view.widget.OnWheelScrollListener;
import com.calendar.demo.view.widget.WheelView;
import com.calendar.demo.view.widget.adapters.ArrayWheelAdapter;
import com.calendar.demo.view.widget.adapters.NumericWheelAdapter;

import android.app.Activity;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
	private ImageButton b_mp = null;
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
	
	private boolean isPopup = false;
	private boolean timeScrolled = false;
	
	private int curyear = 0;
	private int curmonth = 0;
	private int curday = 0;
	private int curhour = 0;
	private int curmin = 0;
	
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
		alarmlist = (ListView) findViewById(R.id.alarmlist);
		
		time = new ArrayList<Time>();
		maa = new MyAlarmAdapter(AddActivity.this);
		alarmlist.setAdapter(maa);
		alarmlist.setCacheColorHint(Color.TRANSPARENT);
		
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
		if(view == cancel || view == back){
			finish();
		}
		
		if(view == b_alarm){
			clickAlarm();
		}
		
		if(view == save){
			saveNote();
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
			/*addEventContent.setVisibility(View.GONE);
			save.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			b_date.setVisibility(View.GONE);
			b_alarm.setVisibility(View.GONE);
			//alarmlistview.setVisibility(View.GONE);
			
			setViewVisble();*/
			String text = content.trim();
			//普通模式
			if(!APP.getpreferences().getMemoPlan()){
				/*listview.setVisibility(View.VISIBLE);
				iv.setVisibility(View.VISIBLE);*/
				
				if(isAddOrUpdate){
					
					Date date = getDate();
					Record record = new Record();
					record.setTaskDetail(text);
					record.setUid(1);
					record.setAlarmTime(date.getTime());
					if(!isOff){
						record.setAlarm(1);
					}
					record.setId(db.add(record));
					arr.add(record);
					adapter.notifyDataSetChanged();
					
				}else{
					record.setTaskDetail(text);
					db.update(record);
				}
				
			}else{ // 计划和备忘的插入和更新
				ivs.setVisibility(View.VISIBLE);
				mPager.setVisibility(View.VISIBLE);
		 		memoView.setVisibility(View.VISIBLE);
		 		planView.setVisibility(View.VISIBLE);
		 		t1.setVisibility(View.VISIBLE);
		 		t2.setVisibility(View.VISIBLE);
		 		cursor.setVisibility(View.VISIBLE);
		 		cursorLayout.setVisibility(View.VISIBLE);
		 		
		 		if(currIndex == 0){ //备忘
			 		if(isAddOrUpdate){
			 			Date date = getDate();
			 			Record record = new Record();
			 			record.setTaskDetail(text);
			 			record.setUid(1);
			 			record.setAlarm(0);
			 			record.setAlarmTime(date.getTime());
			 			record.setId(db.add(record));
			 			arrMemoList.add(record);
			 			
			 		}else{
			 			record.setTaskDetail(text);
			 			db.update(record);
			 		}
		 		}else if (currIndex == 1){ //计划
		 			if(isAddOrUpdate){
		 				Date date = getDate();
			 			Record record = new Record();
			 			record.setTaskDetail(text);
			 			record.setUid(1);
			 			record.setAlarm(1);
			 			record.setAlarmTime(date.getTime());
			 			record.setId(db.add(record));
			 			arrPlanList.add(record);
		 			}else{
		 				record.setTaskDetail(text);
		 				db.update(record);
		 			}
		 		}
			}
			
			//关掉软键盘
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			//添加闹钟，并且为每一个闹钟设置requestcode
			setAlarm();
			
			//区分是添加还是更新，从入口区分
			//为什么不执行datasetchange也可以呢？
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
					System.out.println((curyear+1913)+" "+curmonth+" "+curday+" "+curhour+" "+curmin);
					//添加定闹钟逻辑，如果设定成功闹钟的颜色要有变化-----------
					//setAlarm(curyear+1913,curmonth,curday,curhour,curmin);,将闹钟添加到一个list中，并不真的进行更新
					addTimeList(curyear+1913,curmonth,curday,curhour,curmin);
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
        year.setViewAdapter(new DateNumericAdapter(this, curYear-100, curYear + 100, 100));
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
	

}
