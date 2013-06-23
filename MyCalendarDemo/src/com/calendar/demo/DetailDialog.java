package com.calendar.demo;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailDialog extends Dialog implements OnClickListener{
		
	Context context;    
	private Button ok,cancle;
	private ListenerThree listener;
	private TextView detail;
	public DetailDialog(Context context) 
	{        
		super(context);        
		// TODO Auto-generated constructor stub        
		this.context = context;    
	}    
	public DetailDialog(Context context, int theme,ListenerThree listener)
	{       
		super(context, theme);       
		this.context = context;
		this.listener = listener;
	}    
	
	public DetailDialog(Context context, int theme)
	{       
		super(context, theme);       
		this.context = context;
		Log.i("daili", "constract");
		
	}
   	
	public interface ListenerThree{ 
        public void onClick(View view); 
    }
	//设置应该响应onClick事件的接口
	public void SetListener(ListenerThree listener){
		this.listener = listener;
	}
	
	protected void onCreate(Bundle savedInstanceState) 
	{        
		// TODO Auto-generated method stub     
		Log.i("daili", "create");
		super.onCreate(savedInstanceState);        
		this.setContentView(R.layout.dialog);   
		init();
	}
	
	
	public void setDetail(String msg)
	{
		detail.setText(msg);
	}
	public void init()
	{	
		ok = (Button) findViewById(R.id.dialog_button_ok);	    
		cancle = (Button) findViewById(R.id.dialog_button_cancle);
		detail = (TextView)findViewById(R.id.dialog_detail);
		ok.setOnClickListener(this);
		cancle.setOnClickListener(this);
	}
	
	public void onClick(View v) { 
		//调用本类里面的接口中的onClick,就是所谓的接口回调吧
        listener.onClick(v); 
    }
}


