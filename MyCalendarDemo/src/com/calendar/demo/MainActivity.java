package com.calendar.demo;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import com.calendar.demo.view.widget.OnWheelClickedListener;
import com.calendar.demo.view.widget.OnWheelScrollListener;
import com.calendar.demo.view.widget.WheelView;
import com.calendar.demo.view.widget.adapters.ArrayWheelAdapter;
import com.calendar.demo.view.widget.adapters.NumericWheelAdapter;
import com.calendar.util.DB;
import com.calendar.util.Info;
import com.calendar.util.NumMonthOfYear;
import com.calendar.util.Record;
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
 *   15. 在添加事件界面，不能去点击日历的左右切换和日历文本选择逻辑
 *   16. 默认不区分计划和备忘，所以默认隐藏viewpager，只有当设置中打开开关之后，才区分计划和备忘
 *   17. 上班以及假期的计算方法
 *   18. 时间插入数据库，从数据库中得到结果,删除数据，更新数据
 *   19. back键的监控，标记所在页面
 *   
 *   20. 切换备忘，计划,备忘和计划分别在数据库中查找，如何区分，插入的时候区分备忘和计划
 *   21. 有事件就显示黑色的小角，当时间都删除后，黑色的小边角消失
 */
public class MainActivity extends Activity{
	

	// 生成日历，外层容器
	private LinearLayout layContent = null; //外层日历主体
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
	private ArrayList<View> layrow = new ArrayList<View>();
	public ArrayList<Record> arr=null;

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
	private int selectday = -1;
	private int curtime = 0;
	private int curmin = 0;
	
	private int Calendar_Width = 0;
	private int Cell_Width = 0;
	private boolean isFiveRowExist = false;
	private boolean isOff = true;
	private boolean isPopup = false;
	private boolean timeScrolled = false;
	private boolean isAddOrUpdate = true;
	private boolean isInCalendarActivity = true;
	// 页面控件
	private TextView Top_Date = null;
	private Button btn_pre_month = null;
	private Button btn_next_month = null;
	private TextView arrange_text = null;
	private RelativeLayout mainLayout = null;
	private LinearLayout arrange_layout = null;
	private NoteTaking nt = null;
	private EditText addeventcontent = null;
	private TextView save = null;
	private ImageView iv = null;
	private ImageView ivs = null;
	private ImageButton b_date = null;
	private ImageButton b_alarm = null;
	private DatePickerDialog mDialog = null;
	private ViewPager mPager;//页卡内容
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    private TextView t1, t2;// 页卡头标
    private View memoView,planView;
    private PopupWindow  popupWindow;
    private ListView memolistview = null;
    private ListView planlistview = null;
    
	private ListView listview = null;
	private ListView alarmlistview = null;
	private List<Map<String,String>> alarmitem = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> noteitem = new ArrayList<Map<String,String>>();
	private MyAdapter adapter = null;
	private MyAlarmAdapter maa = null;
	private TextView cancel = null;

	// 数据源
	ArrayList<String> Calendar_Source = null;
	Hashtable<Integer, Integer> calendar_Hashtable = new Hashtable<Integer, Integer>();
	Boolean[] flag = null;
	Calendar startDate = null;
	Calendar endDate = null;
	int dayvalue = -1;
	private Record record = null;
	
	public static int Calendar_WeekBgColor = 0;
	public static int Calendar_DayBgColor = 0;
	public static int isHoliday_BgColor = 0;
	public static int unPresentMonth_FontColor = 0;
	public static int isPresentMonth_FontColor = 0;
	public static int isToday_BgColor = 0;
	public static int special_Reminder = 0;
	public static int common_Reminder = 0;
	public static int Calendar_WeekFontColor = 0;
	public static final int UPDATE_LIST = 0;
	public static final int DELETE_LIST = 1;
	public static final int SET_OFF = 2;
	public static final int SET_ON = 3;
	public static final int NOMEMOPLAN = 0;
	public static final int MEMOPLAN = 1;
	public String UserName = "";
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    
    //数据库对象
    DB db = APP.getDatabase();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new APP().setHandler(new MyHandler());
		
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
		setContentView(mainLayout);
		listview = (ListView)mainLayout.findViewById(R.id.listview);
		alarmlistview= (ListView)mainLayout.findViewById(R.id.alarmlist);
		adapter = new MyAdapter(MainActivity.this);
		arr = adapter.getArrayList();
		listview.setAdapter(adapter);
		listview.setDividerHeight(0);
		listview.setCacheColorHint(Color.TRANSPARENT);
		maa = new MyAlarmAdapter(MainActivity.this);
		alarmlistview.setAdapter(maa);
		alarmlistview.setCacheColorHint(Color.TRANSPARENT);
		// 声明控件，并绑定事件
		Top_Date = (TextView) findViewById(R.id.Top_Date);
		btn_pre_month = (Button) findViewById(R.id.btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.btn_next_month);
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());
		ivs = (ImageView)findViewById(R.id.ivs);
		ivs.setOnClickListener(new ivsClicklistener());
		
		// 计算本月日历中的第一天(一般是上月的某天)，并更新日历
		generateCalendarMain();
		//添加listview并且添加随手记两笔textview
		nt = new NoteTaking(MainActivity.this,Calendar_Width,Cell_Width);
		nt.setOnClickListener(new tvClicklistener());
		nt.setData(getString(R.string.writeit));
		nt.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_event_edit_bg));
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
		
		iv = (ImageView)findViewById(R.id.iv);
		iv.setOnClickListener(new tvClicklistener());
		initAddView();
		//选择月份的监听
		Top_Date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mDialog = new CustomerDatePickerDialog(MainActivity.this ,
						listener ,iMonthViewCurrentYear,iMonthViewCurrentMonth,
						calToday.get(Calendar.DAY_OF_MONTH)
						);
				mDialog.setTitle(iMonthViewCurrentYear + "年"+(iMonthViewCurrentMonth+1)+"月");
				mDialog.show();
		        
			}
		});
		selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
        InitImageView();
 		InitTextView();
 		InitViewPager();
 		
 	
 		
 		//第一次进入，读取今天的数据
 		arr.clear();
		ArrayList<Record> record = db.getPeriodRecordsByDate(1, getDate(),Info.NOTE);
		if(record != null){
			for(Record records:record){
				arr.add(records);
			}
			adapter.notifyDataSetChanged();
		}
		
		setTest();
		delMemoPlan();
		b_alarm.setVisibility(View.GONE);
		
	}
	
	/**
	 * 监听back键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK && !isInCalendarActivity ){
			addeventcontent.setVisibility(View.GONE);
			save.setVisibility(View.GONE);
			b_date.setVisibility(View.GONE);
			b_alarm.setVisibility(View.GONE);
			alarmlistview.setVisibility(View.GONE);
			
			listview.setVisibility(View.VISIBLE);
			iv.setVisibility(View.VISIBLE);
			setViewVisble();
			
			isInCalendarActivity = true;
			return false;
		}
		return super.onKeyDown(keyCode, event);
		
	}
	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		memoView = mInflater.inflate(R.layout.lay1, null);
		memolistview = (ListView) memoView.findViewById(R.id.memo);
		memolistview.setAdapter(adapter);
		listViews.add(memoView);
		planView = mInflater.inflate(R.layout.lay2, null);
		planlistview = (ListView)planView.findViewById(R.id.plan);
		listViews.add(planView);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		memolistview.setDividerHeight(0);
		memolistview.setCacheColorHint(Color.TRANSPARENT);
		planlistview.setDividerHeight(0);
		planlistview.setCacheColorHint(Color.TRANSPARENT);
		
	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	/**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };
    
    /**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
/**
 * 	
 * 日期选取器
 */
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
    					e.printStackTrace();
    				} catch (NoSuchFieldException e) {
    					e.printStackTrace();
    				} catch (IllegalArgumentException e) {
    					e.printStackTrace();
    				} catch (IllegalAccessException e) {
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
			refreshMonthData();
    }

    private void refreshMonthData(){
    	arr.clear();
		ArrayList<Record> records =db.getPeriodRecordsByDate(1, getDate(),Info.NOTE);
		if(records != null){
			for(Record record : records){
				arr.add(record);
			}
		}
		adapter.notifyDataSetChanged();
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
		layContent = (LinearLayout)mainLayout.findViewById(R.id.lly);
		layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));
		layContent.addView(generateCalendarHeader());
		days.clear();
		
		for (int iRow = 0; iRow < 6; iRow++) {
			View view = generateCalendarRow();
			layContent.addView(view);
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
		
		int currow = rowOfMonth(calStartDate);
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
			
			refreshMonthData();
		}

	}

	// 点击下月按钮，触发事件
	class Next_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
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
			selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
			refreshMonthData();
		}
	}
	// 点击日历，触发事件
	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
			int day = GetNumFromDate(calSelected, startDate);
			selectday = day+1;
			System.out.println("onClick----"+(selectday-iDay));
			item.setSelected(true);
			updateCalendar();
			
			//从数据库中读取数据，然后填充到arr中
			arr.clear();
			ArrayList<Record> record = db.getPeriodRecordsByDate(1, getDate(),Info.NOTE);
			if(record != null){
				for(Record records:record){
					arr.add(records);
				}
				adapter.notifyDataSetChanged();
			}else{
				//还没有记事，添加记事的背景图片
				System.out.println("null");
				arr.clear();
				adapter.notifyDataSetChanged();
			}
			
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
		Calendar endDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		endDate.add(Calendar.DAY_OF_MONTH, 41);
		return endDate;
	}
	
	public int rowOfMonth(Calendar startDate){
		
		int day_num = NumMonthOfYear.dayOfMonth(iMonthViewCurrentYear, iMonthViewCurrentMonth) + iDay;
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
	
	public void clickText(Record record,int arg){
		
		if(arg == NOMEMOPLAN){
			
		}else if(arg == MEMOPLAN){
			
	    	mPager.setVisibility(View.GONE);
	 		memoView.setVisibility(View.GONE);
	 		planView.setVisibility(View.GONE);
	 		t1.setVisibility(View.GONE);
	 		t2.setVisibility(View.GONE);
	 		cursor.setVisibility(View.GONE);
		}
		ivs.setVisibility(View.GONE);
		iv.setVisibility(View.GONE);
		isInCalendarActivity = false;
		listview.setVisibility(View.GONE);
		addNote();
		setViewGone();
		this.record = record;
		addeventcontent.setVisibility(View.VISIBLE);
		if(record != null)
			addeventcontent.setText(record.getTaskDetail());
		else
			addeventcontent.setText(null);
		
		cancel.setVisibility(View.VISIBLE);
		save.setVisibility(View.VISIBLE);
		b_date.setVisibility(View.VISIBLE);
		//b_alarm.setVisibility(View.VISIBLE);
		//自动弹出软键盘
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
		imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT); 
	}
	
	
	public void addNote(){
		//处理添加事件
		save.setOnClickListener(new addnoteclicklistener());
	}
	/**
	 * 普通添加监听
	 *
	 */
	public class tvClicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//添加相应事件
			isAddOrUpdate = true;
			clickText(null,0);
		}
		
	}
	/**
	 * 切换之后的添加按钮监听
	 *
	 */
	public class ivsClicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			clickText(null,1);
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
				addeventcontent.setVisibility(View.GONE);
				save.setVisibility(View.GONE);
				b_date.setVisibility(View.GONE);
				b_alarm.setVisibility(View.GONE);
				alarmlistview.setVisibility(View.GONE);
				
				if(!APP.getpreferences().getMemoPlan()){
					listview.setVisibility(View.VISIBLE);
					iv.setVisibility(View.VISIBLE);
				}else{
					ivs.setVisibility(View.VISIBLE);
					mPager.setVisibility(View.VISIBLE);
			 		memoView.setVisibility(View.VISIBLE);
			 		planView.setVisibility(View.VISIBLE);
			 		t1.setVisibility(View.VISIBLE);
			 		t2.setVisibility(View.VISIBLE);
			 		cursor.setVisibility(View.VISIBLE);
				}
				setViewVisble();
				String text = content.trim();
				//关掉软键盘
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				
				//区分是添加还是更新，从入口区分
				//为什么不执行datasetchange也可以呢？
				if(isAddOrUpdate){
					Date date = getDate();
					Record record = new Record();
					record.setTaskDetail(text);
					record.setUid(1);
					record.setAlarmTime(date.getTime());
					record.setId(db.add(record));
					arr.add(record);
					
				}else{
					record.setTaskDetail(text);
					db.update(record);
				}
			}
			isInCalendarActivity = true;
		}
		
	}
	/**
	 * 得到日期对象
	 */
	
	private Date getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf.parse(iMonthViewCurrentYear +"-"+(iMonthViewCurrentMonth+1)+"-"+(selectday-iDay));
			date.setHours(c.get(Calendar.HOUR_OF_DAY));
			date.setMinutes(c.get(Calendar.MINUTE));
			date.setSeconds(c.get(Calendar.SECOND));
			
			System.out.println(iMonthViewCurrentYear +"-"+(iMonthViewCurrentMonth+1)+"-"+(selectday-iDay)+"-"
					+date.getHours()+"-"+date.getMinutes()+"-"+date.getSeconds());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 这是对添加事件的一些控件进行初始化
	 */
	public void initAddView(){
		addeventcontent = (EditText)findViewById(R.id.add_event_content);
		save = (TextView)findViewById(R.id.save);
		cancel = (TextView)findViewById(R.id.cancel);
		b_date = (ImageButton)findViewById(R.id.b_mp);
		b_date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				if(!isOff){
					b_date.setBackgroundDrawable(
							getResources().getDrawable(R.drawable.setting_switch_default_on));
					isOff = true;
					b_alarm.setVisibility(View.GONE);
					if(isPopup){
						popupWindow.dismiss();
						isPopup = false;
					}
				}else{
					b_date.setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_switch_default_off));
					isOff = false;
					b_alarm.setVisibility(View.VISIBLE);
					
				}
			}
			
		});
		b_alarm = (ImageButton)findViewById(R.id.b_alarm);
		b_alarm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				if(!isPopup)
					initPopUpWindow();
			}
		});
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				System.out.println("HAHA");
			}});
		cancel.setVisibility(View.GONE);
		addeventcontent.setVisibility(View.GONE);
		save.setVisibility(View.GONE);
		b_date.setVisibility(View.GONE);
		b_alarm.setVisibility(View.GONE);
		
	}
    //处理闹钟的弹出菜单	
	private void initPopUpWindow(){
		isPopup = true;
		// set current time
	    Calendar c = Calendar.getInstance();
		curtime = c.get(Calendar.HOUR_OF_DAY);
		curmin = c.get(Calendar.MINUTE);
		
		View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.time_layout, null);
		popupWindow = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.showAsDropDown(layContent,((layContent.getWidth()-popupView.getWidth())/4),0);
		
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
					System.out.println(curtime+" "+curmin);
					//添加定闹钟逻辑，如果设定成功闹钟的颜色要有变化
					setAlarm(curtime,curmin);
					popupWindow.dismiss();
				}
		}});
        
		final WheelView hours = (WheelView) popupView.findViewById(R.id.hour);
		NumericWheelAdapter nwa_h = new NumericWheelAdapter(this,0,23);
		nwa_h.setTextSize(16);
		hours.setViewAdapter(nwa_h);
		hours.setCyclic(true);
	
		final WheelView mins = (WheelView) popupView.findViewById(R.id.mins);
		NumericWheelAdapter nwa_m = new NumericWheelAdapter(this,0,59,"%02d");
		nwa_m.setTextSize(16);
		mins.setViewAdapter(nwa_m);
		mins.setCyclic(true);
	
		hours.setCurrentItem(curtime);
		mins.setCurrentItem(curmin);
	
		// add listeners
		addChangingListener(mins, "min");
		addChangingListener(hours, "hour");
	
		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				if (!timeScrolled) {
					curtime= hours.getCurrentItem();
					curmin = mins.getCurrentItem();
				}
			}
		};
		hours.addChangingListener(wheelListener);
		mins.addChangingListener(wheelListener);
		
		OnWheelClickedListener click = new OnWheelClickedListener() {
            public void onItemClicked(WheelView wheel, int itemIndex) {
                wheel.setCurrentItem(itemIndex, true);
            }
        };
        hours.addClickingListener(click);
        mins.addClickingListener(click);

		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				timeScrolled = true;
			}
			public void onScrollingFinished(WheelView wheel) {
				timeScrolled = false;
				curtime = hours.getCurrentItem();
				curmin = mins.getCurrentItem();
			}
		};
		
		hours.addScrollingListener(scrollListener);
		mins.addScrollingListener(scrollListener);
		
		
	}
	
	private void addChangingListener(final WheelView wheel, final String label) {
		wheel.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				//wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}
	
	/**
	 * 设定闹钟
	 */
	public void setAlarm(int hour,int min){
		//先得到年月日时分
		System.out.println(iMonthViewCurrentMonth);
		System.out.println(iMonthViewCurrentYear); 
		System.out.println(selectday - iDay);
		
		Calendar alarmtime = Calendar.getInstance();
		alarmtime.set(Calendar.YEAR, iMonthViewCurrentYear);
		alarmtime.set(Calendar.MONTH, iMonthViewCurrentMonth);
		alarmtime.set(Calendar.DAY_OF_MONTH, selectday-iDay);
		alarmtime.set(Calendar.HOUR_OF_DAY, hour);
		alarmtime.set(Calendar.MINUTE, min);
		alarmtime.set(Calendar.SECOND, 0);
		
		 
		Intent intent = new Intent("com.calendar.demo.alarm");
		intent.setClass(MainActivity.this, AlarmReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, intent,0);
		
		AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
	    am.set(AlarmManager.RTC_WAKEUP, alarmtime.getTimeInMillis(), pi);
	    
	    maa.getArrayList().add(iMonthViewCurrentYear+"-"+iMonthViewCurrentMonth+"-"+(selectday-iDay)
	    		+"-"+hour+"-"+min);
	    maa.notifyDataSetChanged();
	    
	    b_alarm.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic__alarm__on));
	}
	/**
	 * 更新事件
	 */
	private void updateNote(Record record){
		clickText(record,0);
		isAddOrUpdate = false;
	}
	/**
	 * 自定义handler事件，发送消息来进行
	 * 
	 */
	public class MyHandler extends Handler{
		
		public void handleMessage(Message msg){
		//这里主要是用来对设置开关进行修改的，动态的改变所需要的view	
			switch(msg.arg1){
				case UPDATE_LIST:
					updateNote((Record)msg.obj);
					break;
				case DELETE_LIST:
					dialog((Record)msg.obj,msg.arg2);
					break;
				case SET_ON:
					setMemoPlan();
					break;
				case SET_OFF:
					delMemoPlan();
					break;
				default:
					break;
			}
		}

	}
	//日期选择对话框
	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){  //
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			System.out.println("点击之后触发！");
		}
	};
	
	//删除对话框
    protected void dialog(Record record, final int position) {
      final long id = record.getId();
  	  AlertDialog.Builder builder = new Builder(MainActivity.this);
  	  builder.setMessage(getString(R.string.isDelete));
  	  builder.setTitle(getString(R.string.tip));
  	  builder.setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//删除之后，更新arr
			db.delete(id);
			arr.remove(position);
			adapter.notifyDataSetChanged();
			
		}});
  	  builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}});

  	  builder.create().show();
  	 }
    
    /**
     * 计划备忘切换视图
     */
    
    private  void setMemoPlan(){
    	
    	listview.setVisibility(View.GONE);
    	
    	ivs.setVisibility(View.VISIBLE);
    	mPager.setVisibility(View.VISIBLE);
 		memoView.setVisibility(View.VISIBLE);
 		planView.setVisibility(View.VISIBLE);
 		t1.setVisibility(View.VISIBLE);
 		t2.setVisibility(View.VISIBLE);
 		cursor.setVisibility(View.VISIBLE);
    }
    
    /**
     * 取消计划备忘视图
     */
    
    private void delMemoPlan(){
    	ivs.setVisibility(View.GONE);
    	mPager.setVisibility(View.GONE);
 		memoView.setVisibility(View.GONE);
 		planView.setVisibility(View.GONE);
 		t1.setVisibility(View.GONE);
 		t2.setVisibility(View.GONE);
 		cursor.setVisibility(View.GONE);
 		
 		iv.setVisibility(View.VISIBLE);
 		listview.setVisibility(View.VISIBLE);
    }
    
    private void setTest(){
    	Button b = (Button) findViewById(R.id.setting);
    	b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Intent i = new Intent();
				i.setClass(MainActivity.this, SetActivity.class);
				startActivity(i);
			}
    		
    	});
    }
	
}