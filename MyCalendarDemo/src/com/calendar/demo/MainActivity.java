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
 * Androidʵ�������ؼ�
 * @Description: Androidʵ�������ؼ�

 * @File: MainActivity.java

 * @Package com.calendar.demo

 * @Author zhanglanyun 

 * @Date 2013-5-21 

 * @Version V1.0
 * 
 *   1. �޸�ÿ���������Ķ�̬��ʾ5����6��
 *   2. ʵ��������������ʾ
 *   3. ʵ�������Ļ���,����������������
 *   4. ��Ӽ���ʱֻ��ʾ��ǰ�ܣ�����������,����ȥ���£������򻬶���ʧȥ����
 *   5. ��̬����Ӽ��¹���
 *   6. ���·ֱ������ÿ������棬�����������л�
 *   7. ������ʾ��ȷ�����ǽ�����ʾ����
 *   8. ��ӵ�����ڵ����ı����ڶԻ���ֻ��ʾ�պ���
 *   9. ���������������ڣ���Ҫ�����ӣ���Ҫ�ԣ��Ǽƻ����Ǳ������Ա��Ժ����
 *   10. ����֮�����ʾ������,��ת���¸���֮��Ĭ�ϻ��ǵ������ǵ�������Ӳ���
 *   11. ����¸���֮��ѡ�����ڣ����ڵ�����ʾ����ȷ��������bug�������޸���
 *   12. ʵ�ּ��������ƶ���ʱ�����Զ�̬�仯����������
 *   13. �ƻ��ͱ������л�����ʵ�� 
 *   14. ��������߼�������ѡ�������ӣ�ͬʱ�г�������ӵ�ʱ��
 *   15. ������¼����棬����ȥ��������������л��������ı�ѡ���߼�
 *   16. Ĭ�ϲ����ּƻ��ͱ���������Ĭ������viewpager��ֻ�е������д򿪿���֮�󣬲����ּƻ��ͱ���
 *   17. �ϰ��Լ����ڵļ��㷽��
 *   18. ʱ��������ݿ⣬�����ݿ��еõ����,ɾ�����ݣ���������
 *   19. back���ļ�أ��������ҳ��
 *   
 *   20. �л��������ƻ�,�����ͼƻ��ֱ������ݿ��в��ң�������֣������ʱ�����ֱ����ͼƻ�
 *   21. ���¼�����ʾ��ɫ��С�ǣ���ʱ�䶼ɾ���󣬺�ɫ��С�߽���ʧ
 */
public class MainActivity extends Activity{
	

	// �����������������
	private LinearLayout layContent = null; //�����������
	private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
	private ArrayList<View> layrow = new ArrayList<View>();
	public ArrayList<Record> arr=null;

	// ���ڱ���
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	// ��ǰ��������
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
	// ҳ��ؼ�
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
	private ViewPager mPager;//ҳ������
    private List<View> listViews; // Tabҳ���б�
    private ImageView cursor;// ����ͼƬ
    private TextView t1, t2;// ҳ��ͷ��
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

	// ����Դ
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
    private int offset = 0;// ����ͼƬƫ����
    private int currIndex = 0;// ��ǰҳ�����
    private int bmpW;// ����ͼƬ���
    
    //���ݿ����
    DB db = APP.getDatabase();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new APP().setHandler(new MyHandler());
		
		// �����Ļ��͸ߣ���Ӌ�����Ļ���ȷ��ߵȷݵĴ�С
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 7 + 1;
		util.setWidth(Cell_Width,Calendar_Width);
		
		// �ƶ������ļ�������������
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
		// �����ؼ��������¼�
		Top_Date = (TextView) findViewById(R.id.Top_Date);
		btn_pre_month = (Button) findViewById(R.id.btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.btn_next_month);
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());
		ivs = (ImageView)findViewById(R.id.ivs);
		ivs.setOnClickListener(new ivsClicklistener());
		
		// ���㱾�������еĵ�һ��(һ�������µ�ĳ��)������������
		generateCalendarMain();
		//���listview����������ּ�����textview
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
		
		// �½��߳�
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
		//�����ʼ������Դ����������adapter
		
		iv = (ImageView)findViewById(R.id.iv);
		iv.setOnClickListener(new tvClicklistener());
		initAddView();
		//ѡ���·ݵļ���
		Top_Date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mDialog = new CustomerDatePickerDialog(MainActivity.this ,
						listener ,iMonthViewCurrentYear,iMonthViewCurrentMonth,
						calToday.get(Calendar.DAY_OF_MONTH)
						);
				mDialog.setTitle(iMonthViewCurrentYear + "��"+(iMonthViewCurrentMonth+1)+"��");
				mDialog.show();
		        
			}
		});
		selectday =iDay + calToday.get(Calendar.DAY_OF_MONTH);
        InitImageView();
 		InitTextView();
 		InitViewPager();
 		
 	
 		
 		//��һ�ν��룬��ȡ���������
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
	 * ����back��
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
	 * ��ʼ��ͷ��
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
	}

	/**
	 * ��ʼ��ViewPager
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
	 * ��ʼ������
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 2 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
	}
	/**
     * ͷ��������
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
	 * ҳ���л�����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

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
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
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
 * ����ѡȡ��
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
            mDialog.setTitle(year + "��" + (month+1) + "��");
            iMonthViewCurrentMonth_Dialog = month;
            iMonthViewCurrentYear_Dialog = year;
            
          
        }
       
        @Override
		public void onClick(DialogInterface dialog, int which) {
			//������õ����£������ɺ�������£�����ֻ�ǻ�����Ӧ�ò���Խ�硣�������������½�
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
	//�ӵ�ǰdialog�в���DatePicker����
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
	
	 //��ת��month ��  year �ض������·�
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

			// �½��߳�
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

	// �õ������������е����
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

	// ���ɲ���
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		
		return lay;
	}

	// ��������ͷ��
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

	// ������������
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

	// ���������е�һ�У���������
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

	// ���õ������ںͱ�ѡ������
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

	// ���ڱ������ϵ����ڶ��Ǵ���һ��ʼ�ģ��˷���������������ڱ�����������ʾ������
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

	// ��������,���ҲҪ����������
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

			// �ж��Ƿ���
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

			// �Ƿ�ѡ��
			bSelected = false;
			
			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}
			
			dayCell.setSelected(bSelected);

			// �Ƿ��м�¼
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
		//���ػ�֮ǰ�ж��Ƿ�Ҫ��ʾ����
		layContent.invalidate();
		
		return daySelected;
	}

	// ����������������ʾ������
	private void UpdateCurrentMonthDisplay() {
		String date = calStartDate.get(Calendar.YEAR) + "��"
				+ (calStartDate.get(Calendar.MONTH) + 1) + "��";
		Top_Date.setText(date);
	}

	// ������°�ť�������¼�
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

			// �½��߳�
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

	// ������°�ť�������¼�
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

			// �½��߳�
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
	// ��������������¼�
	private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
		public void OnClick(DateWidgetDayCell item) {
			calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
			int day = GetNumFromDate(calSelected, startDate);
			selectday = day+1;
			System.out.println("onClick----"+(selectday-iDay));
			item.setSelected(true);
			updateCalendar();
			
			//�����ݿ��ж�ȡ���ݣ�Ȼ����䵽arr��
			arr.clear();
			ArrayList<Record> record = db.getPeriodRecordsByDate(1, getDate(),Info.NOTE);
			if(record != null){
				for(Record records:record){
					arr.add(records);
				}
				adapter.notifyDataSetChanged();
			}else{
				//��û�м��£���Ӽ��µı���ͼƬ
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

	// �õ���ǰ�����еĵ�һ��
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
		//���ز���row��������
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
		//�Զ����������
		InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
		imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT); 
	}
	
	
	public void addNote(){
		//��������¼�
		save.setOnClickListener(new addnoteclicklistener());
	}
	/**
	 * ��ͨ��Ӽ���
	 *
	 */
	public class tvClicklistener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			//�����Ӧ�¼�
			isAddOrUpdate = true;
			clickText(null,0);
		}
		
	}
	/**
	 * �л�֮�����Ӱ�ť����
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
				//��Ϊ�յĻ��������ַ���������������ʾ�����ּ���ʾ��listview�����Ӧ������
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
				//�ص������
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				
				
				//��������ӻ��Ǹ��£����������
				//Ϊʲô��ִ��datasetchangeҲ�����أ�
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
	 * �õ����ڶ���
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
	 * ���Ƕ�����¼���һЩ�ؼ����г�ʼ��
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
    //�������ӵĵ����˵�	
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
					//��Ӷ������߼�������趨�ɹ����ӵ���ɫҪ�б仯
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
	 * �趨����
	 */
	public void setAlarm(int hour,int min){
		//�ȵõ�������ʱ��
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
	 * �����¼�
	 */
	private void updateNote(Record record){
		clickText(record,0);
		isAddOrUpdate = false;
	}
	/**
	 * �Զ���handler�¼���������Ϣ������
	 * 
	 */
	public class MyHandler extends Handler{
		
		public void handleMessage(Message msg){
		//������Ҫ�����������ÿ��ؽ����޸ĵģ���̬�ĸı�����Ҫ��view	
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
	//����ѡ��Ի���
	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){  //
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			System.out.println("���֮�󴥷���");
		}
	};
	
	//ɾ���Ի���
    protected void dialog(Record record, final int position) {
      final long id = record.getId();
  	  AlertDialog.Builder builder = new Builder(MainActivity.this);
  	  builder.setMessage(getString(R.string.isDelete));
  	  builder.setTitle(getString(R.string.tip));
  	  builder.setPositiveButton(getString(R.string.YES), new DialogInterface.OnClickListener(){
		@Override
		public void onClick(DialogInterface dialog, int which) {
			//ɾ��֮�󣬸���arr
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
     * �ƻ������л���ͼ
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
     * ȡ���ƻ�������ͼ
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