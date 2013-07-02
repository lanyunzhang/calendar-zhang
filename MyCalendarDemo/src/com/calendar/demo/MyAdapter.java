package com.calendar.demo;

import java.util.ArrayList;

import com.calendar.util.Record;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 添加记事事件的adapter
 * @author zhanglanyun
 *
 */
public class MyAdapter extends BaseAdapter {  
    private Context context;  
    private LayoutInflater inflater;
    private ArrayList<Record> arr = null;
      
    public MyAdapter(Context context) {  
        super();  
        this.context = context;  
        inflater = LayoutInflater.from(context);  
        arr = new ArrayList<Record>();  
    }  
    @Override  
    public int getCount() {  
        return arr.size();  
    }  
    @Override  
    public Object getItem(int arg0) {  
        return arg0;  
    }  
    @Override  
    public long getItemId(int arg0) {  
        return arg0;  
    }  
    @Override  
    public View getView(final int position, View view, ViewGroup arg2) {  
        if(view == null){  
            view = inflater.inflate(R.layout.item_events, null);  
        }
        final TextView tv = (TextView)view.findViewById(R.id.text);
        tv.setText(arr.get(position).getTaskDetail());
        tv.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//点击之后进入修改页面
				System.out.println("textview clicked!");
				if(APP.getHandler() != null){
					Message msg = new Message();
					msg.obj = arr.get(position);
					msg.arg1 = MainActivity.UPDATE_LIST;
					APP.getHandler().sendMessage(msg);
				}
				
			}
		});
        tv.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View view) {
				//弹出一个对话框，点击确定删除
				if(APP.getHandler()!=null){
					Message msg = new Message();
					msg.obj = arr.get(position);
					msg.arg1 = MainActivity.DELETE_LIST;
					msg.arg2 = position;
					APP.getHandler().sendMessage(msg);
					
				}
				return false;
			}});
        return view;  
    }  
    
    
    public ArrayList<Record> getArrayList(){
    	return arr;
    }
    

}