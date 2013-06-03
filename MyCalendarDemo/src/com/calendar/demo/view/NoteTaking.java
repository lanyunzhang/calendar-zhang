package com.calendar.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.calendar.demo.MainActivity;
import com.calendar.demo.R;
import com.calendar.util.util;

public class NoteTaking extends View{

	Paint pt = new Paint();
	private int fTextSize = 30;
	private String text ;
	public NoteTaking(Context context,int iWidth,int iHeight) {
		super(context);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
	}
	public void setData(String text){
		this.text = text;
	}
	
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		//画一个矩形，填充 随后记两笔
		
		pt.setColor(Color.BLACK);
		pt.setStyle(Paint.Style.STROKE);
		Rect rect = new Rect();
		rect.set(0, 0,util.screen_width,util.cell_width);
		float iPosX =rect.left;
	    float iPosY = rect.top + (rect.bottom - rect.top -getTextHeight())/2 + pt.getFontMetrics().bottom;
	   	canvas.drawRect(rect,pt);
	   	
	   	pt.setColor(Color.GRAY);
	   	pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(fTextSize);
		pt.setUnderlineText(false);
		canvas.drawText(text,iPosX, iPosY, pt);
		
	}

	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}
}
