package com.calendar.demo;

import java.util.List;
import java.util.Map;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class TaskListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
 
	private String findString  = new String();
	private List<Map<String, Object>> listData;

	private List<Map<String, Object>> splitData;

	public TaskListAdapter(Context context, List<Map<String, Object>> listData,
			List<Map<String, Object>> splitData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
		this.splitData = splitData;
	}
	
	@SuppressLint("NewApi")
	public void findTaskByString(String str)
	{
		if(str.length() > 0)
		{
			findString = str;	
		}
	}
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		if (splitData.contains(listData.get(position))) {
			return false;
		}
		return super.isEnabled(position);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		int tag;
		if (splitData.contains(listData.get(position))) {

			convertView = mInflater.inflate(R.layout.list_items_tag, null);
			tag = 1;
		} else {
			convertView = mInflater.inflate(R.layout.list_items, null);
			tag = 0;
		}

		TextView textView = (TextView) convertView.findViewById(R.id.itemTitle);
		String strs = listData.get(position).get("taskDetail").toString();

		if (tag == 1) {
			textView.setText(strs);
		} else {
			if (findString.length() > 0) {
				int start = strs.indexOf(findString);
				int end = start + findString.length();
				if (start != -1) {
					start = strs.indexOf(findString);
					SpannableStringBuilder style = new SpannableStringBuilder(
							strs);
					style.setSpan(new BackgroundColorSpan(Color.RED), start,
							end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					textView.setText(style);
				}
				else {
					textView.setText(strs);
				}
			} else {
				textView.setText(strs);

			}
		}
		return convertView;
	}

}