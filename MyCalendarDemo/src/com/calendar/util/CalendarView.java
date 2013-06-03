package com.calendar.util;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import java.util.Calendar;

import com.calendar.demo.DateWidgetDayCell;
import com.calendar.demo.DateWidgetDayHeader;
import com.calendar.demo.DayStyle;
import com.calendar.demo.MainActivity;
import com.calendar.demo.R;
import com.calendar.demo.MainActivity.addnoteclicklistener;
import com.calendar.demo.MainActivity.tvClicklistener;
import com.calendar.demo.view.NoteTaking;
/**
 * Ҫʵ�ֵ�����ʾ��������LinearLayout
 * Ӧ��ViewGroup������View�ٷֿ����ֱ���ʾ�����������
 * @author zhanglanyun
 *
 */
public class CalendarView extends LinearLayout{

		private Context context;
	
		// �����������������
		private LinearLayout layContent = null; //�����������
		private ArrayList<DateWidgetDayCell> days = new ArrayList<DateWidgetDayCell>();
		private ArrayList<View> layrow = new ArrayList<View>();
		
		public ArrayList<String> arr;

		// ���ڱ���
		public static Calendar calStartDate = Calendar.getInstance();
		private Calendar calToday = Calendar.getInstance();
		private Calendar calCalendar = Calendar.getInstance();
		private Calendar calSelected = Calendar.getInstance();

		// ��ǰ��������
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
		
		//LinearLayout mainLayout = null;
		LinearLayout arrange_layout = null;

		// ����Դ
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
		
		public void initView(Context context){
			this.context = context;
			//LayoutInflater.from(context).inflate(R.layout.calendar_main, this,true);
			
			WindowManager windowManager = ((Activity)context).getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			int screenWidth = display.getWidth();
			Calendar_Width = screenWidth;
			Cell_Width = Calendar_Width / 7 + 1;
			util.setWidth(Cell_Width,Calendar_Width);
			
			// �ƶ������ļ�������������
			/*mainLayout = (LinearLayout) activity.getLayoutInflater().inflate(
					R.layout.calendar_main, null);*/
			// ���㱾�������еĵ�һ��(һ�������µ�ĳ��)������������
			addView(generateCalendarMain());
		
			calStartDate = getCalendarStartDate();
			DateWidgetDayCell daySelected = updateCalendar();

			if (daySelected != null)
				daySelected.requestFocus();
			
			setBackgroundColor(Color.WHITE);
			startDate = GetStartDate();
			calToday = GetTodayDate();

			endDate = GetEndDate(startDate);
			
			/*// �½��߳�
			new Thread() {
				@Override
				public void run() {
					int day = GetNumFromDate(calToday, startDate);
					
					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
				
			}.start();*/
			
			

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
			LinearLayout lay = new LinearLayout(context);
			lay.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			lay.setOrientation(iOrientation);
			
			return lay;
		}

		// ��������ͷ��
		private View generateCalendarHeader() {
			LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
			// layRow.setBackgroundColor(Color.argb(255, 207, 207, 205));
			
			for (int iDay = 0; iDay < 7; iDay++) {
				DateWidgetDayHeader day = new DateWidgetDayHeader(context, Cell_Width,
						35);
				
				final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
				day.setData(iWeekDay);
				layRow.addView(day);
			}
			
			return layRow;
		}

		// ������������
		private View generateCalendarMain() {
			layContent = createLayout(LinearLayout.VERTICAL);
			// layContent.setPadding(1, 0, 1, 0);
			layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));
			layContent.addView(generateCalendarHeader());
			days.clear();
			
			for (int iRow = 0; iRow < 6; iRow++) {
				View view = generateCalendarRow();
				layContent.addView(view);
				//��view�ŵ�һ��������ȥ��Ȼ�����ػ�����ʾ���һ��view
				layrow.add(view);
			}
			return layContent;
		}

		// ���������е�һ�У���������
		private View generateCalendarRow() {
			LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
			
			for (int iDay = 0; iDay < 7; iDay++) {
				DateWidgetDayCell dayCell = new DateWidgetDayCell(context, Cell_Width,
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
			//UpdateCurrentMonthDisplay();
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
			
			
			//���������calStartDate----------------------------------------------
			int currow = rowOfMonth(calStartDate);
			if ( (prerow == -1 && currow == 5) || (prerow == 6 && currow == 5) )
				layrow.get(5).setVisibility(View.GONE);
			else if (prerow == 5 && currow == 6){
				layrow.get(5).setVisibility(View.VISIBLE);
				isFiveRowExist = true;
			}
			
			prerow = currow;
			//------------------------------------------------------------------
		}

		// ��������
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
			layContent.invalidate();
			
			return daySelected;
		}

		// ����������������ʾ������
		/*private void UpdateCurrentMonthDisplay() {
			String date = calStartDate.get(Calendar.YEAR) + "��"
					+ (calStartDate.get(Calendar.MONTH) + 1) + "��";
			Top_Date.setText(date);
		}*/

		/*// ������°�ť�������¼�
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
			}

		}

		// ������°�ť�������¼�
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
			}
		}*/

		// ��������������¼�
		private DateWidgetDayCell.OnItemClick mOnDayCellClick = new DateWidgetDayCell.OnItemClick() {
			public void OnClick(DateWidgetDayCell item) {
				/*calSelected.setTimeInMillis(item.getDate().getTimeInMillis());
				int day = GetNumFromDate(calSelected, startDate);
				selectday = day;
				
				if (calendar_Hashtable != null
						&& calendar_Hashtable.containsKey(day)) {
					arrange_text.setText(Calendar_Source.get(calendar_Hashtable
							.get(day)));
				} else {
					arrange_text.setText("�������ݼ�¼");
				}
				item.setSelected(true);
				updateCalendar();*/
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
			// Calendar end = GetStartDate(enddate);
			Calendar endDate = Calendar.getInstance();
			endDate = (Calendar) startDate.clone();
			//row = rowOfMonth(startDate) / 7;
			
			endDate.add(Calendar.DAY_OF_MONTH, 41);
			return endDate;
		}
		
		public int rowOfMonth(Calendar startDate){
			
			int day_num = NumMonthOfYear.dayOfMonth(iMonthViewCurrentYear, iMonthViewCurrentMonth) + iDay;
//			System.out.println(iMonthViewCurrentYear+ " " + iMonthViewCurrentMonth + " " + iDay+" ");
//			System.out.println("day_num" + day_num);
			if(day_num < 36)
				numOfDay = 5;
			else
				numOfDay = 6;
			System.out.println("rowofnum="+numOfDay);
			return numOfDay;
		}
		
		public void setViewGone(){
			int row = selectday / 7;
			//���ز���row��������
			for(int i=0;i<layrow.size();i++){
				if(i != row)
					layrow.get(i).setVisibility(View.GONE);
			}
		}
		public void setViewVisble(){
			int row = selectday / 7;
			for(int i=0;i<layrow.size();i++){
				if(i != row )
					layrow.get(i).setVisibility(View.VISIBLE);
			}
			if(!isFiveRowExist)
				layrow.get(5).setVisibility(View.GONE);
		}

	public CalendarView(Context context) {
		this(context,null);
		initView(context);
	}
	
	public CalendarView(Context context ,AttributeSet attrs){
		super(context,attrs);
	}
	

}
