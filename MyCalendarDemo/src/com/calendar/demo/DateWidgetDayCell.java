package com.calendar.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

import com.calendar.util.DB;
import com.calendar.util.Lunar;

/**
 * �����ؼ���Ԫ�������
 * 
 * @Description: �����ؼ���Ԫ�������

 * @FileName: DateWidgetDayCell.java 

 * @Package com.calendar.demo 

 * @Author  zhanglanyun

 * @Date    2013-05-22 

 * @Version V1.0
 */
public class DateWidgetDayCell extends View {
	// �����С --ũ���빫��������
	private static final int fTextSize = 33;
	private static final int lTextSize = 14;
	
	// ����Ԫ��
	private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private RectF bitmapRect = new RectF();
	private RectF rectbackground = new RectF();
	private String sDate = "";
	private String sLundarDate = "";

	// ��ǰ����
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;
	private  Date date;
	private Lunar l ;
	// ��������
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bTouchedDown = false;
	private boolean bHoliday = false;
	private boolean hasRecord = false;
	private boolean bMove = false;
	private boolean blackpoint = false;
	
	public static int ANIM_ALPHA_DURATION = 100;
    private DB db = null;
	public interface OnItemClick {
		public void OnClick(DateWidgetDayCell item);
	}

	// ���캯��
	public DateWidgetDayCell(Context context, int iWidth, int iHeight) {
		super(context);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
		
		db = APP.getDatabase();
		System.out.println("db"+ db == null);
	}

	// ȡ����ֵ
	public Calendar getDate() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

	// ���ñ���ֵ
	public void setData(int iYear, int iMonth, int iDay, Boolean bToday,
			Boolean bHoliday, int iActiveMonth, boolean hasRecord) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;

		this.sDate = Integer.toString(iDateDay);
		//�����iDateMonth��0-11������Lunar�е���1-12����������Ҫ��һ��1
		this.sLundarDate = Lunar.getLunarDay(iDateYear, iDateMonth+1, iDateDay);
		this.bIsActiveMonth = (iDateMonth == iActiveMonth);
		this.bToday = bToday;
		this.bHoliday = bHoliday;
		this.hasRecord = hasRecord;
		
		 SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		  try {
		 date = myFormatter.parse(iDateYear+"-"+(iDateMonth+1)+"-"+iDateDay);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		l = new Lunar(date);
	}

	
	// ���ػ��Ʒ���
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		rect.set(0, 0, this.getWidth(), this.getHeight());
		bitmapRect.set((this.getWidth()*2)/3, 0, this.getWidth(), (this.getHeight())/3);
		rectbackground.set(0,0,this.getWidth()/5,this.getHeight()/5);
		rect.inset(1, 1);

		final boolean bFocused = IsViewFocused();
		drawDayView(canvas, bFocused);
		drawDayNumber(canvas);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	// ������������
	private void drawDayView(Canvas canvas, boolean bFocused) {

		if (bSelected || bFocused) {
			LinearGradient lGradBkg = null;

		/*	if (bFocused) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xffaf53ff, 0xffaf53ff, Shader.TileMode.CLAMP);
			}*/
			

			/*if (bSelected) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xffaf53ff, 0xffaf53ff, Shader.TileMode.CLAMP);
			}*/

			/*if (lGradBkg != null) {
				pt.setShader(lGradBkg);*/
			    pt.setColor(Color.rgb(175, 53, 255));
				canvas.drawRect(rect, pt);
				pt.setShader(null);

		} else {
			pt.setColor(getColorBkg(bHoliday, bToday));
			canvas.drawRect(rect, pt);
			
		}

		if (hasRecord) {
			CreateReminder(canvas, MainActivity.special_Reminder);
		}
		// else if (!hasRecord && !bToday && !bSelected) {
		// CreateReminder(canvas, Calendar_TestActivity.Calendar_DayBgColor);
		// }
	}

	// ���������е�����,������Ӷ���������ʾ
	public void drawDayNumber(Canvas canvas) {
		// draw day number
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(fTextSize);
		pt.setColor(MainActivity.isPresentMonth_FontColor);
		pt.setUnderlineText(false);
		
		if (!bIsActiveMonth)
			pt.setColor(MainActivity.unPresentMonth_FontColor);

		if (bToday)
			pt.setColor(Color.rgb(55, 193, 0));
			//pt.setUnderlineText(true);

		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDate) >> 1);

//		final int iPosY = (int) (this.getHeight()
//				- (this.getHeight() - getTextHeight()) / 2 - pt
//				.getFontMetrics().bottom);
		
		final int iPosY = (int)(this.getHeight() - 2*(this.getHeight() - getTextHeight())/3)
				-2*((int)pt.getFontMetrics().bottom);
		
		final int iPosXX = (int)rect.left + ((int)rect.width() >> 1)
				- ((int) pt.measureText(sLundarDate)>>1)/2;

		final int iPosYY = (int)(this.getHeight() - (this.getHeight() - getTextHeight())/3)
				-((int)pt.getFontMetrics().bottom);
		
		if(iDateYear == 2013){
			if(l.findWorkOrHoliday(iDateMonth+1, iDateDay) == 1){
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.date_bg_jia),
						null, bitmapRect, null);
				
			}else if (l.findWorkOrHoliday(iDateMonth+1, iDateDay)== 2)
				canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.date_bg_ban),null,
						bitmapRect, null);
		}
		
		canvas.drawText(sDate, iPosX, iPosY, pt);
		pt.setTextSize(lTextSize);
		pt.setUnderlineText(false);
		
		//��ӹ��������Լ�ũ������
		String sF = Lunar.findSFestivals(iDateMonth+1 , iDateDay);
		String lF = Lunar.findLFestivals(l.getLunarMonth(), l.getLunarDay());
		pt.setColor(Color.rgb(167, 204, 174));
		if(sF != null){
			
			canvas.drawText(sF, iPosXX, iPosYY, pt);
		}
		else if(lF != null){
			canvas.drawText(lF, iPosXX, iPosYY, pt);
			System.out.println(lF);
		}
		else{
			pt.setColor(Color.rgb(188, 188, 188));
			canvas.drawText(sLundarDate, iPosXX, iPosYY, pt);
			
		}
		
		blackpoint = false;
		
		if((db.getPeriodRecordsByDate(1, getDates(),0))!=null){
			if(db.getPeriodRecordsByDate(1, getDates(),0).size()>0)
				blackpoint = true;
			
		}
		
		if((db.getPeriodRecordsByDate(1, getDates(),1))!=null){
			if(db.getPeriodRecordsByDate(1, getDates(),1).size()>0 )
				blackpoint = true;
		}
		if(blackpoint)
			canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.noteflag),
				null, rectbackground, null);
		
	}

	private Date getDates(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf.parse(iDateYear +"-"+(iDateMonth+1)+"-"+(iDateDay));
			date.setHours(c.get(Calendar.HOUR_OF_DAY));
			date.setMinutes(c.get(Calendar.MINUTE));
			date.setSeconds(c.get(Calendar.SECOND));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	// �õ�����߶�
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// �����������ز�ͬ��ɫֵ
	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return MainActivity.isToday_BgColor;
		 if (bHoliday) //������ĩ�����ⱳ��ɫ����ȥ��ע��
		 return MainActivity.isHoliday_BgColor;
		return MainActivity.Calendar_DayBgColor;
	}

	// �����Ƿ�ѡ��
	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			//this.invalidate();
			this.postInvalidate();
		}
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// ����¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		float startPosx  = 0;
		float startPosy  = 0;
		float endPosx = 0;
		float endPosy = 0;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = false;
			startPosx = event.getX();
			startPosy = event.getY();
			
			//startAlphaAnimIn(DateWidgetDayCell.this);
			//doItemClick();
			System.out.println("CELL ACTION_DOWN");
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			System.out.println("CELL ACTION_CANCEL");
			//invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			
			System.out.println("UPUPUPUP");
			//invalidate();
			bTouchedDown = false;
			
			if(!bMove)
				doItemClick();
			else
				bMove = false;
			
			System.out.println("CELL ACTION_UP");
		}
		
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			System.out.println("CELL ACTION_MOVE");
			//bMove = true;
			endPosx = event.getX();
			endPosy = event.getY();
			
			if(Math.abs(endPosx - startPosx)+Math.abs(endPosy-startPosy) > 100){
				bMove = true;
			}else{
				bTouchedDown = true;
			}
		}
		return bHandled;
	}

	// ����¼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	// ��͸���Ƚ���
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(ANIM_ALPHA_DURATION);
		anim.startNow();
		view.startAnimation(anim);
	}

	public void CreateReminder(Canvas canvas, int Color) {
		pt.setStyle(Paint.Style.FILL_AND_STROKE);
		pt.setColor(Color);
		Path path = new Path();
		path.moveTo(rect.right - rect.width() / 4, rect.top);
		path.lineTo(rect.right, rect.top);
		path.lineTo(rect.right, rect.top + rect.width() / 4);
		path.lineTo(rect.right - rect.width() / 4, rect.top);
		path.close();
		canvas.drawPath(path, pt);
	}
}