package com.calendar.demo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.calendar.demo.view.NoteTaking;
import com.calendar.demo.view.widget.OnWheelChangedListener;
import com.calendar.demo.view.widget.WheelView;
import com.calendar.demo.view.widget.adapters.ArrayWheelAdapter;
import com.calendar.demo.view.widget.adapters.NumericWheelAdapter;
import com.calendar.util.NumMonthOfYear;
import com.calendar.util.util;

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
 *   3. 实现日历的滑动,坡敏跟进这个问题吧
 *   4. 添加记事时只显示当前周，其它周隐藏,便于去记事，不会因滑动而失去焦点
 *   5. 动态的添加记事功能
 *   6. 记事分别添加在每天的下面，点击日历表格切换
 *   7. 阴历显示正确，但是节气显示不对
 *   8. 添加点击日期弹出改变日期对话框，只显示日和月
 *   9. 可以正常调整日期，还要做闹钟，重要性，是计划还是备忘，以备以后计算
 *   10. 保存之后会显示第六行,跳转到下个月之后，默认还是的日子是当天的日子不变
 *   11. 点击下个月之后，选择日期，所在的行显示不正确，这里有bug，晚上修复吧
 *   12. 实现键盘向上移动的时，可以动态变化输入框的行数
 *   13. 计划和备忘的切换开关实现 
 *   14. 添加闹钟逻辑，可以选择多个闹钟，同时列出多个闹钟的时间
 *   
 *   15. 在添加事件界面，不能去点击日历的左右切换和日历文本选择逻辑
 */
public class MainActivity extends Activity{
	// 生成日历，外层容器
	private LinearLayout layContent = null; //外层日历主体
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
	private ArrayList<View> layrow = new ArrayList<View>();
	
	public ArrayList<String> arr;

	// 日期变量
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	// 当前操作日期
	private int iMonthViewCurrentMonth_Dialog = 0;
	private int iMonthViewCurrentYear_Dialog = 0;
	
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
	private boolean isFiveRowExist = false;
	private boolean isOff = false;

	// 页面控件
	TextView Top_Date = null;
	Button btn_pre_month = null;
	Button btn_next_month = null;
	TextView arrange_text = null;
	RelativeLayout mainLayout = null;
	LinearLayout arrange_layout = null;
	NoteTaking nt = null;
	View  addnote = null;
	EditText addeventcontent = null;
	TextView save = null;
	ImageView iv = null;
	ImageButton b_date = null;
	ImageButton b_alarm = null;
	DatePickerDialog mDialog = null;
	
	
	//--------------listview----------------
	ListView listview = null;
	//SimpleAdapter sa = null;
	List<Map<String,String>> noteitem = new ArrayList<Map<String,String>>();
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

		
		System.out.println(getApplicationContext().getFilesDir().getAbsolutePath());
		
		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 7 + 1;
		util.setWidth(Cell_Width,Calendar_Width);
		
		// 制定布局文件，并设置属性
		mainLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.calendar_main, null);
		// mainLayout.setPadding(2, 0, 2, 0);
		setContentView(mainLayout);

		listview = (ListView)mainLayout.findViewById(R.id.listview);
		//listview = (ListView)getLayoutInflater().inflate(R.layout.list, null);
		adapter = new MyAdapter(MainActivity.this);
		listview.setAdapter(adapter);
		listview.setDividerHeight(0);
		listview.setCacheColorHint(Color.TRANSPARENT);
		
		
		// 声明控件，并绑定事件
		Top_Date = (TextView) findViewById(R.id.Top_Date);
		btn_pre_month = (Button) findViewById(R.id.btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.btn_next_month);
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());

		// 计算本月日历中的第一天(一般是上月的某天)，并更新日历
		//mainLayout.addView(generateCalendarMain());
		generateCalendarMain();
		//添加listview并且添加随手记两笔textview
		//mainLayout.addView(listview);
		nt = new NoteTaking(MainActivity.this,Calendar_Width,Cell_Width);
		nt.setOnClickListener(new tvClicklistener());
		nt.setData(getString(R.string.writeit));
		nt.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_event_edit_bg));
		//mainLayout.addView(nt);
		
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
		//这里初始化数据源，并且设置adapter
		
	/*	initListData();
		listview.setAdapter(sa);*/
		
		iv = (ImageView)findViewById(R.id.iv);
		iv.setOnClickListener(new tvClicklistener());
		//mainLayout.addView(iv);
		
		initAddView();
		//选择月份的监听
		Top_Date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mDialog = new CustomerDatePickerDialog(MainActivity.this ,
						listener ,iMonthViewCurrentYear,iMonthViewCurrentMonth,
						calToday.get(Calendar.DAY_OF_MONTH)
						);
				//mDialog.setTitle(calToday.get(Calendar.YEAR )+"年"+(calToday.get(Calendar.MONTH )+1)+"月");
				mDialog.setTitle(iMonthViewCurrentYear + "年"+(iMonthViewCurrentMonth+1)+"月");
				mDialog.show();
		        
			}
		});
		
		selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
		System.out.println("onCreate---"+selectday);
		
	}
	
	class CustomerDatePickerDialog extends DatePickerDialog {

        public CustomerDatePickerDialog(Context context,
                OnDateSetListener callBack, int year, int monthOfYear,
                int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            super.onDateChanged(view, year, month, day);
            mDialog.setTitle(year + "年" + (month+1) + "月");
            iMonthViewCurrentMonth_Dialog = month;
            iMonthViewCurrentYear_Dialog = year;
            
          
        }
       
        @Override
		public void onClick(DialogInterface dialog, int which) {
			//在这里得到年月，点击完成后更新年月，这里只是滑动，应该不会越界。但是阴历有上下界
        	if(which == DialogInterface.BUTTON1){
				if(iMonthViewCurrentMonth_Dialog != iMonthViewCurrentMonth || 
						iMonthViewCurrentYear_Dialog != iMonthViewCurrentYear){
					
					System.out.println(iMonthViewCurrentMonth_Dialog);
					System.out.println(iMonthViewCurrentYear_Dialog);
					toMonthYear(iMonthViewCurrentMonth_Dialog,iMonthViewCurrentYear_Dialog);
					
				}
        	}
       }
        
        @Override
    	public void show() {
    		// TODO Auto-generated method stub
    		super.show();
    		 DatePicker dp = findDatePicker((ViewGroup) this.getWindow().getDecorView());
    	        if (dp != null) {
    	        	Class c=dp.getClass();
    	        	Field f;
    				try {
    						if(Build.VERSION.SDK_INT>14){
    							f = c.getDeclaredField("mDaySpinner");
    							f.setAccessible(true );  
    							LinearLayout l= (LinearLayout)f.get(dp);   
    							l.setVisibility(View.GONE);
    						}else{
    							f = c.getDeclaredField("mDayPicker");
    							f.setAccessible(true );  
    							LinearLayout l= (LinearLayout)f.get(dp);   
    							l.setVisibility(View.GONE);
    						}
    				} catch (SecurityException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (NoSuchFieldException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (IllegalArgumentException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (IllegalAccessException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}  
    	        	
    	        } 
    	}
    }
	//从当前dialog中查找DatePicker对象
	private DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    } 
	
	 //跳转到month 和  year 特定的年月份
    public void toMonthYear(int month,int year){

			calSelected.setTimeInMillis(0);
			iMonthViewCurrentMonth = month;
			iMonthViewCurrentYear  = year;
			
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
			selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
			System.out.println("toMonthYear-----"+selectday);
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
		//layContent = createLayout(LinearLayout.VERTICAL);
		layContent = (LinearLayout)mainLayout.findViewById(R.id.lly);
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
		selectday = GetNumFromDate(calToday, GetStartDate());
		

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
		System.out.println(currow);
		if (currow == 5){
			layrow.get(5).setVisibility(View.GONE);
			isFiveRowExist = false;
		}
		else if (currow == 6){
			layrow.get(5).setVisibility(View.VISIBLE);
			isFiveRowExist = true;
		}
		
	}

	// 更新日历,点击也要更新日历？
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
			selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
			System.out.println("prev_month---"+selectday);
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
					int day = 5;
					
					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();

			updateCalendar();
			//在这里计算selectday
			selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
			System.out.println("next_month---"+selectday);
			
		}
	}
	// 点击日历，触发事件
	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
			int day = GetNumFromDate(calSelected, startDate);
			selectday = day+1;
			System.out.println("onClick----"+selectday);
			item.setSelected(true);
			updateCalendar();
			//这里改变日历数据，查询数据库-------------------------------------
			arr.clear();
			arr.add(selectday+"");
			adapter.notifyDataSetChanged();
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
		
		System.out.println("in setViewGone " + selectday);
		int row = (selectday-1) / 7 ;
		//隐藏不是row的所有行
		for(int i=0;i<layrow.size();i++){
			if(i != row)
				layrow.get(i).setVisibility(View.GONE);
		}
	}
	
	public void setViewVisble(){
		int row = (selectday-1) / 7;
		for(int i=0;i<layrow.size();i++){
			if(i != row )
				layrow.get(i).setVisibility(View.VISIBLE);
		}
		if(!isFiveRowExist)
			layrow.get(5).setVisibility(View.GONE);
	}
	//listview的adapter设置,修改为只有一个textview显示
	private class MyAdapter extends BaseAdapter {  
        private Context context;  
        private LayoutInflater inflater;  
          
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
                view = inflater.inflate(R.layout.item_events, null);  
            }
            final TextView tv = (TextView)view.findViewById(R.id.text);
            /*final EditText edit = (EditText) view.findViewById(R.id.edit); 
            edit.setText(arr.get(position));    //在重构adapter的时候不至于数据错乱  
            Button del = (Button) view.findViewById(R.id.del);  */
            tv.setText(arr.get(position));
            tv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					System.out.println("textview clicked!");
				}
			});
            return view;  
        }  
        
    }
	
	public void clickText(){
		//首先nt隐藏，显示添加界面，日历同样隐藏相应的部分
		iv.setVisibility(View.GONE);
		listview.setVisibility(View.GONE);
		addNote();
		setViewGone();
		
		addeventcontent.setVisibility(View.VISIBLE);
		addeventcontent.setText(null);
		save.setVisibility(View.VISIBLE);
		b_date.setVisibility(View.VISIBLE);
		b_alarm.setVisibility(View.VISIBLE);
		//自动弹出软键盘
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
		imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT); 
	}
	
	public void addNote(){
		//处理添加事件
		save.setOnClickListener(new addnoteclicklistener());
	}
	public class tvClicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//添加相应事件
			clickText();
		}
		
	}
	
	public class addnoteclicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			String content = addeventcontent.getText().toString();
			if(content.length() == 0 || content.equals("") || content == null || 
					content.trim().length() == 0){
				Toast.makeText(MainActivity.this, getString(R.string.contentisnull),
						Toast.LENGTH_SHORT).show();
			}else{
				//不为空的话，保存字符串，并且日历显示，随手记显示，listview添加相应的内容
				//addnote.setVisibility(View.GONE);
				addeventcontent.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
				b_date.setVisibility(View.GONE);
				b_alarm.setVisibility(View.GONE);
				
				listview.setVisibility(View.VISIBLE);
				iv.setVisibility(View.VISIBLE);
				setViewVisble();
				String text = content.trim();
				arr.add(text);
				adapter.notifyDataSetChanged();
				//关掉软键盘
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
			}
		}
		
	}
	/**
	 * 这是对添加事件的一些控件进行初始化
	 */
	public void initAddView(){
		addeventcontent = (EditText)findViewById(R.id.add_event_content);
		save = (TextView)findViewById(R.id.save);
		b_date = (ImageButton)findViewById(R.id.b_mp);
		b_date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				if(!isOff){
					b_date.setBackgroundDrawable(
							getResources().getDrawable(R.drawable.setting_switch_default_off));
					isOff = true;
				}else{
					b_date.setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_switch_default_on));
					isOff = false;
				}
			}
			
		});
		b_alarm = (ImageButton)findViewById(R.id.b_alarm);
		b_alarm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				initPopUpWindow();
			}
			
		});
		addeventcontent.setVisibility(View.GONE);
		save.setVisibility(View.GONE);
		b_date.setVisibility(View.GONE);
		b_alarm.setVisibility(View.GONE);
		
	}
	
	private void initPopUpWindow(){
		View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.pop_up_date, null);
		final PopupWindow  popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.showAsDropDown(layContent,((layContent.getWidth()-popupView.getWidth())/4),0);
		
		// set the view of alarm
		Calendar calendar = Calendar.getInstance();

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
        String months[] = new String[] {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"};
        month.setViewAdapter(new DateArrayAdapter(this, months, curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
    
        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(this, curYear, curYear + 10, 0));
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);
        
        //day
        updateDays(year, month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
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
    
	/**
	 * 自定义handler事件，发送消息来进行
	 * 
	 */
	public class MyHandler extends Handler{
		
		public void handleMessage(Message msg){
		//这里主要是用来对设置开关进行修改的，动态的改变所需要的view	
		}

	}
	//日期选择对话框
	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){  //
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			System.out.println("点击之后触发！");
		}
	};
	
}