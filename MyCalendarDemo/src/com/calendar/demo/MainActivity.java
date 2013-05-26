package com.calendar.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.calendar.util.NumMonthOfYear;
import com.calendar.util.util;
import com.canlendar.demo.view.NoteTaking;

/**
 * Android实现日历控件
 * @Description: Android实现日历控件

 * @File: MainActivity.java

 * @Package com.calendar.demo

 * @Author zhanglanyun 

 * @Date 2013-5-21 

 * @Version V1.0
 * 
 * 
 *   1. 修复每个月日历的动态显示5或者6行
 *   2. 实现日历的阴历显示
 *   3. 实现日历的滑动
 *   4. 添加记事时只显示当前周，其它周隐藏,便于去记事，不会因滑动而失去焦点
 *   5. 动态的添加记事功能
 */
public class MainActivity extends Activity{
	// 生成日历，外层容器
	private LinearLayout layContent = null; //外层日历主体
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
	private ArrayList<View> layrow = new ArrayList<View>();

	// 日期变量
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	// 当前操作日期
	private int iMonthViewCurrentMonth = 0;
	private int iMonthViewCurrentYear = 0;
	private int iFirstDayOfWeek = Calendar.MONDAY;
	private int iDay = 0;
	private int numOfDay = 0;
	private int currow = -1;
	private int prerow = -1;
	private int selectday = -1;
	
	private int Calendar_Width = 0;
	private int Cell_Width = 0;

	// 页面控件
	TextView Top_Date = null;
	Button btn_pre_month = null;
	Button btn_next_month = null;
	TextView arrange_text = null;
	LinearLayout mainLayout = null;
	LinearLayout arrange_layout = null;
	
	//--------------listview----------------
	ListView listview = null;
	MyAdapter adapter = null;
	
	

	// 数据源
	ArrayList<String> Calendar_Source = null;
	Hashtable<Integer, Integer> calendar_Hashtable = new Hashtable<Integer, Integer>();
	Boolean[] flag = null;
	Calendar startDate = null;
	Calendar endDate = null;
	int dayvalue = -1;

	public static int Calendar_WeekBgColor = 0;
	public static int Calendar_DayBgColor = 0;
	public static int isHoliday_BgColor = 0;
	public static int unPresentMonth_FontColor = 0;
	public static int isPresentMonth_FontColor = 0;
	public static int isToday_BgColor = 0;
	public static int special_Reminder = 0;
	public static int common_Reminder = 0;
	public static int Calendar_WeekFontColor = 0;

	String UserName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 7 + 1;
		util.setWidth(Cell_Width,Calendar_Width);
		
		// 制定布局文件，并设置属性
		mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.calendar_main, null);
		// mainLayout.setPadding(2, 0, 2, 0);
		setContentView(mainLayout);

		listview = (ListView)getLayoutInflater().inflate(R.layout.list, null);
		adapter = new MyAdapter(MainActivity.this);
		listview.setAdapter(adapter);
		
		// 声明控件，并绑定事件
		Top_Date = (TextView) findViewById(R.id.Top_Date);
		btn_pre_month = (Button) findViewById(R.id.btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.btn_next_month);
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());

		// 计算本月日历中的第一天(一般是上月的某天)，并更新日历
		mainLayout.addView(generateCalendarMain());
		
		
		//添加listview并且添加随手记两笔textview
		mainLayout.addView(listview);
		NoteTaking nt = new NoteTaking(MainActivity.this,Calendar_Width,Cell_Width);
		nt.setOnClickListener(new tvClicklistener());
		nt.setData(getString(R.string.writeit));
		mainLayout.addView(nt);
		
		calStartDate = getCalendarStartDate();
		DateWidgetDayCell daySelected = updateCalendar();

		if (daySelected != null)
			daySelected.requestFocus();

		LinearLayout.LayoutParams Param1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		ScrollView view = new ScrollView(this);
		arrange_layout = createLayout(LinearLayout.VERTICAL);
		arrange_layout.setPadding(5, 2, 0, 0);
		arrange_text = new TextView(this);
		mainLayout.setBackgroundColor(Color.WHITE);
		arrange_text.setTextColor(Color.BLACK);
		arrange_text.setTextSize(18);
		arrange_layout.addView(arrange_text);

		startDate = GetStartDate();
		calToday = GetTodayDate();

		endDate = GetEndDate(startDate);
		view.addView(arrange_layout, Param1);
		
		//mainLayout.addView(view);

		// 新建线程
		new Thread() {
			@Override
			public void run() {
				int day = GetNumFromDate(calToday, startDate);
				
				if (calendar_Hashtable != null
						&& calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}
			
		}.start();

		Calendar_WeekBgColor = this.getResources().getColor(
				R.color.Calendar_WeekBgColor);
		Calendar_DayBgColor = this.getResources().getColor(
				R.color.Calendar_DayBgColor);
		isHoliday_BgColor = this.getResources().getColor(
				R.color.isHoliday_BgColor);
		unPresentMonth_FontColor = this.getResources().getColor(
				R.color.unPresentMonth_FontColor);
		isPresentMonth_FontColor = this.getResources().getColor(
				R.color.isPresentMonth_FontColor);
		isToday_BgColor = this.getResources().getColor(R.color.isToday_BgColor);
		special_Reminder = this.getResources()
				.getColor(R.color.specialReminder);
		common_Reminder = this.getResources().getColor(R.color.commonReminder);
		Calendar_WeekFontColor = this.getResources().getColor(
				R.color.Calendar_WeekFontColor);
	}

	protected String GetDateShortString(Calendar date) {
		String returnString = date.get(Calendar.YEAR) + "/";
		returnString += date.get(Calendar.MONTH) + 1 + "/";
		returnString += date.get(Calendar.DAY_OF_MONTH);
		
		return returnString;
	}

	// 得到当天在日历中的序号
	private int GetNumFromDate(Calendar now, Calendar returnDate) {
		Calendar cNow = (Calendar) now.clone();
		Calendar cReturnDate = (Calendar) returnDate.clone();
		setTimeToMidnight(cNow);
		setTimeToMidnight(cReturnDate);
		
		long todayMs = cNow.getTimeInMillis();
		long returnMs = cReturnDate.getTimeInMillis();
		long intervalMs = todayMs - returnMs;
		int index = millisecondsToDays(intervalMs);
		
		return index;
	}

	private int millisecondsToDays(long intervalMs) {
		return Math.round((intervalMs / (1000 * 86400)));
	}

	private void setTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	// 生成布局
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		
		return lay;
	}

	// 生成日历头部
	private View generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		// layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));
		
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayHeader day = new DateWidgetDayHeader(this, Cell_Width,
					35);
			
			final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}
		
		return layRow;
	}

	// 生成日历主体
	private View generateCalendarMain() {
		layContent = createLayout(LinearLayout.VERTICAL);
		// layContent.setPadding(1, 0, 1, 0);
		layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));
		layContent.addView(generateCalendarHeader());
		days.clear();
		
		for (int iRow = 0; iRow < 6; iRow++) {
			View view = generateCalendarRow();
			layContent.addView(view);
			//把view放到一个集合中去，然后隐藏或者显示最后一个view
			layrow.add(view);
		}
		
		return layContent;
	}

	// 生成日历中的一行，仅画矩形
	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		
		for (int iDay = 0; iDay < 7; iDay++) {
			DateWidgetDayCell dayCell = new DateWidgetDayCell(this, Cell_Width,
					Cell_Width);
			dayCell.setItemClick(mOnDayCellClick);
			days.add(dayCell);
			layRow.addView(dayCell);
		}
		
		return layRow;
	}

	// 设置当天日期和被选中日期
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);

		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}
		
		UpdateStartDateForMonth();
		return calStartDate;
	}

	// 由于本日历上的日期都是从周一开始的，此方法可推算出上月在本月日历中显示的天数
	private void UpdateStartDateForMonth() {
		iMonthViewCurrentMonth = calStartDate.get(Calendar.MONTH);
		iMonthViewCurrentYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		// update days for week
		UpdateCurrentMonthDisplay();
		int iDay = 0;
		int iStartDay = iFirstDayOfWeek;
		
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		this.iDay=iDay;
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
		
		
		//在这里更改calStartDate----------------------------------------------
		int currow = rowOfMonth(calStartDate);
		if ( (prerow == -1 && currow == 5) || (prerow == 6 && currow == 5) )
			layrow.get(5).setVisibility(View.GONE);
		else if (prerow == 5 && currow == 6){
			layrow.get(5).setVisibility(View.VISIBLE);
		}
		
		prerow = currow;
		//------------------------------------------------------------------
	}

	// 更新日历
	private DateWidgetDayCell updateCalendar() {
		DateWidgetDayCell daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
		
		for (int i = 0; i < days.size(); i++) {
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			DateWidgetDayCell dayCell = days.get(i);

			// 判断是否当天
			boolean bToday = false;
			
			if (calToday.get(Calendar.YEAR) == iYear) {
				if (calToday.get(Calendar.MONTH) == iMonth) {
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
						selectday = GetNumFromDate(calSelected, GetStartDate());
						bToday = true;
					}
				}
			}

			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;

			// 是否被选中
			bSelected = false;
			
			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}
			
			dayCell.setSelected(bSelected);

			// 是否有记录
			boolean hasRecord = false;
			
			if (flag != null && flag[i] == true && calendar_Hashtable != null
					&& calendar_Hashtable.containsKey(i)) {
				// hasRecord = flag[i];
				hasRecord = Calendar_Source.get(calendar_Hashtable.get(i))
						.contains(UserName);
			}

			if (bSelected)
				daySelected = dayCell;

			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday,
					iMonthViewCurrentMonth, hasRecord);

			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		//在重绘之前判断是否要显示几行
		
		layContent.invalidate();
		
		return daySelected;
	}

	// 更新日历标题上显示的年月
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + "年"
				+ (calStartDate.get(Calendar.MONTH) + 1) + "月";
		Top_Date.setText(date);
	}

	// 点击上月按钮，触发事件
	class Pre_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			arrange_text.setText("");
			calSelected.setTimeInMillis(0);
			iMonthViewCurrentMonth--;
			
			if (iMonthViewCurrentMonth == -1) {
				iMonthViewCurrentMonth = 11;
				iMonthViewCurrentYear--;
			}
			
			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			calStartDate.set(Calendar.HOUR_OF_DAY, 0);
			calStartDate.set(Calendar.MINUTE, 0);
			calStartDate.set(Calendar.SECOND, 0);
			calStartDate.set(Calendar.MILLISECOND, 0);
			
			
			UpdateStartDateForMonth();
			startDate = (Calendar) calStartDate.clone();
			endDate = GetEndDate(startDate);

			// 新建线程
			new Thread() {
				@Override
				public void run() {

					int day = GetNumFromDate(calToday, startDate);
					
					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();

			updateCalendar();
		}

	}

	// 点击下月按钮，触发事件
	class Next_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			arrange_text.setText("");
			calSelected.setTimeInMillis(0);
			iMonthViewCurrentMonth++;
			
			if (iMonthViewCurrentMonth == 12) {
				iMonthViewCurrentMonth = 0;
				iMonthViewCurrentYear++;
			}
			
			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, iMonthViewCurrentMonth);
			calStartDate.set(Calendar.YEAR, iMonthViewCurrentYear);
			UpdateStartDateForMonth();
			startDate = (Calendar) calStartDate.clone();
			endDate = GetEndDate(startDate);

			// 新建线程
			new Thread() {
				@Override
				public void run() {
					int day = 5;
					
					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();

			updateCalendar();
		}
	}

	// 点击日历，触发事件
	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
			int day = GetNumFromDate(calSelected, startDate);
			selectday = day;
			
			if (calendar_Hashtable != null
					&& calendar_Hashtable.containsKey(day)) {
				arrange_text.setText(Calendar_Source.get(calendar_Hashtable
						.get(day)));
			} else {
				arrange_text.setText("暂无数据记录");
			}
			
			item.setSelected(true);
			updateCalendar();
		}
	};

	public Calendar GetTodayDate() {
		Calendar cal_Today = Calendar.getInstance();
		cal_Today.set(Calendar.HOUR_OF_DAY, 0);
		cal_Today.set(Calendar.MINUTE, 0);
		cal_Today.set(Calendar.SECOND, 0);
		cal_Today.setFirstDayOfWeek(Calendar.MONDAY);

		return cal_Today;
	}

	// 得到当前日历中的第一天
	public Calendar GetStartDate() {
		int iDay = 0;
		Calendar cal_Now = Calendar.getInstance();
		cal_Now.set(Calendar.DAY_OF_MONTH, 1);
		cal_Now.set(Calendar.HOUR_OF_DAY, 0);
		cal_Now.set(Calendar.MINUTE, 0);
		cal_Now.set(Calendar.SECOND, 0);
		cal_Now.setFirstDayOfWeek(Calendar.MONDAY);

		iDay = cal_Now.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		
		if (iDay < 0) {
			iDay = 6;
		}
		this.iDay = iDay;
		
		cal_Now.add(Calendar.DAY_OF_WEEK, -iDay);
		
		return cal_Now;
	}

	public Calendar GetEndDate(Calendar startDate) {
		// Calendar end = GetStartDate(enddate);
		Calendar endDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		//row = rowOfMonth(startDate) / 7;
		
		endDate.add(Calendar.DAY_OF_MONTH, 41);
		return endDate;
	}
	
	public int rowOfMonth(Calendar startDate){
		
		int day_num = NumMonthOfYear.dayOfMonth(iMonthViewCurrentYear, iMonthViewCurrentMonth) + iDay;
//		System.out.println(iMonthViewCurrentYear+ " " + iMonthViewCurrentMonth + " " + iDay+" ");
//		System.out.println("day_num" + day_num);
		if(day_num < 36)
			numOfDay = 5;
		else
			numOfDay = 6;
		System.out.println("rowofnum="+numOfDay);
		return numOfDay;
	}
	
	public void setViewGone(){
		int row = selectday / 7;
		//隐藏不是row的所有行
		for(int i=0;i<layrow.size();i++){
			if(i != row)
				layrow.get(i).setVisibility(View.GONE);
		}
	}
	public void setViewVisble(){
		int row = selectday / 7;
		for(int i=0;i<layrow.size();i++){
			if(i != row)
				layrow.get(i).setVisibility(View.VISIBLE);
		}
	}
	//listview的adapter设置
	private class MyAdapter extends BaseAdapter {  
		  
        private Context context;  
        private LayoutInflater inflater;  
        public ArrayList<String> arr;  
        public MyAdapter(Context context) {  
            super();  
            this.context = context;  
            inflater = LayoutInflater.from(context);  
            arr = new ArrayList<String>();  
        }  
        @Override  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return arr.size();  
        }  
        @Override  
        public Object getItem(int arg0) {  
            // TODO Auto-generated method stub  
            return arg0;  
        }  
        @Override  
        public long getItemId(int arg0) {  
            // TODO Auto-generated method stub  
            return arg0;  
        }  
        @Override  
        public View getView(final int position, View view, ViewGroup arg2) {  
            // TODO Auto-generated method stub  
            if(view == null){  
                view = inflater.inflate(R.layout.listview_item, null);  
            }  
            final EditText edit = (EditText) view.findViewById(R.id.edit); 
            
            edit.setText(arr.get(position));    //在重构adapter的时候不至于数据错乱  
            Button del = (Button) view.findViewById(R.id.del);  
            
            edit.setOnFocusChangeListener(new OnFocusChangeListener() {  
                @Override  
                public void onFocusChange(View v, boolean hasFocus) {  
                    // TODO Auto-generated method stub  
                	if(arr.size()>0){  
                		
                		//ionFocusChange(position,edit,hasFocus);
                    } 
                }  
            });  
            del.setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View arg0){  
                    // TODO Auto-generated method stub  
                    //从集合中删除所删除项的EditText的内容  
                    arr.remove(position);  
                    adapter.notifyDataSetChanged();  
                }  
            });  
            return view;  
        }  
        
        public void ionFocusChange(int position,EditText edit,boolean hasFocus){
        	if(position == arr.size()-1){
        		
        		if(hasFocus){
        			edit.setText("");
        			setViewGone();
        		}else{
        			if(edit.getText().toString() == "" || edit.getText().toString()==null){
        				edit.setText(R.string.writeit);
        				edit.setTextColor(getResources().getColorStateList(R.color.edittextcolor));
        			}else{
        				arr.add(position, edit.getText().toString());
        				arr.remove(position+1);
        				arr.add(getString(R.string.writeit));
        			}
        			setViewVisble();
        		}
        	}else{
        		
        		if(hasFocus){
        			setViewGone();
        		}
        		
        	}
        }
    }
	
	public void clickText(){
		
	}
	
	public class tvClicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//添加相应事件
			clickText();
		}
		
	}
	
	
}