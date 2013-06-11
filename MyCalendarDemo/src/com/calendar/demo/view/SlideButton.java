package com.calendar.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SlideButton extends View implements OnTouchListener{

	private Bitmap bg_plan,bg_memo, slide_btn;
	private Rect btn_plan,btn_memo;
	private boolean nowChoose = false;
	private boolean slideState = false;
	float downX,nowX;
	private boolean isSetOnChangeListener = false;
	private OnChangedListener listener;
	
	public SlideButton(Context context) {
		super(context);
		init();
	}

	public SlideButton(Context context,AttributeSet attr){
		super(context,attr);
		init();
	}
	
	public void init(){
		bg_plan = BitmapFactory.decodeResource(getResources(), 0);
		bg_memo = BitmapFactory.decodeResource(getResources(), 0);
		slide_btn = BitmapFactory.decodeResource(getResources(),0);
		
		btn_memo = new Rect(0,0,slide_btn.getWidth(),slide_btn.getHeight());
		btn_plan = new Rect(bg_plan.getWidth()-slide_btn.getWidth(),0,bg_plan.getWidth(),slide_btn.getHeight());
		setOnTouchListener(this);
		
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			if(event.getX() > bg_memo.getWidth() || event.getY() > bg_memo.getHeight())
				return false;
			slideState = true;
			downX = event.getX();
			nowX = downX;
			break;
		case MotionEvent.ACTION_MOVE:
			nowX = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			slideState =false;
			boolean lastChoose = nowChoose;
			if(event.getX()>bg_memo.getWidth()/2)
				nowChoose = true;
			else
				nowChoose = false;
			if(isSetOnChangeListener && (nowChoose != lastChoose))
				listener.OnChanged(nowChoose);
			break;
		default:
			break;
		}
		invalidate();
		return true;
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//这里控制按钮的变化
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x;
		
		if(nowX < (bg_plan.getWidth()/2))
			canvas.drawBitmap(bg_memo, matrix, paint);
		else
			canvas.drawBitmap(bg_plan,matrix,paint);
		
		if(slideState){
			if(nowX >= bg_plan.getWidth())
				x = bg_plan.getWidth() - slide_btn.getWidth()/2;
			else
				x = nowX -slide_btn.getWidth()/2;
		}else{
			if(nowChoose)
				x = btn_memo.left;
			else
				x = btn_plan.left;
		}
		if(x < 0)
			x = 0;
		else if(x>bg_plan.getWidth() - slide_btn.getWidth())
			x = bg_plan.getWidth() - slide_btn.getWidth();
		
		canvas.drawBitmap(slide_btn, x, 0,paint);
	}
	public void setOnChangedListener(OnChangedListener listener){
		this.listener = listener;
		isSetOnChangeListener = true;
	}
	public interface OnChangedListener{
		public void OnChanged(boolean CheckState);
	}

}
