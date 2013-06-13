package com.calendar.demo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
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
    private ArrayList<String> arr = null;
      
    public MyAdapter(Context context) {  
        super();  
        this.context = context;  
        inflater = LayoutInflater.from(context);  
        arr = new ArrayList<String>();  
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
    
    public ArrayList<String> getArrayList(){
    	return arr;
    }
}