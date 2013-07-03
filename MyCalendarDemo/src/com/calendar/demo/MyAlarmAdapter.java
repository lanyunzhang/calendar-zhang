package com.calendar.demo;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * �����б��������
 * @author zhanglanyun
 *
 */
public class MyAlarmAdapter extends BaseAdapter {  
    private Context context;  
    private LayoutInflater inflater;  
    private ArrayList<String> arr;
    private ArrayList<Integer> alarmRequestCode;
    
    public MyAlarmAdapter(Context context) {  
        super();  
        this.context = context;  
        inflater = LayoutInflater.from(context);  
        arr = new ArrayList<String>();  
        alarmRequestCode = new ArrayList<Integer>();
        
    }  
    
    public ArrayList<String> getArrayList(){
    	return arr;
    }
    
    public ArrayList<Integer> getAlarmRequestCode(){
    	return alarmRequestCode;
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
        edit.setText(arr.get(position));    //���ع�adapter��ʱ���������ݴ���  
        Button del = (Button) view.findViewById(R.id.del);  */
        tv.setText(arr.get(position));
        tv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				System.out.println("textview clicked!");
			}
		});
        
        tv.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View view) {
				((AddActivity)context).dialogAlarm(position);
				return false;
			}});
        return view;  
    }
    
    
    
}