package com.calendar.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Preference {
	
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;
	private static Preference mPreferences;
	private static final String FIRSTLOCK ="FIRSTLOCK";
	private static final String ISMEMOPLAN ="ISMEMOPLAN";
	private static final String ALARM = "ALARM";
	
	public  Preference(Context context){
		mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		mEditor = mSharedPreferences.edit();
	}
	
	public static synchronized Preference getPreferences(Context context) {
		if (mPreferences == null) {
			mPreferences = new Preference(context);
		}
		return mPreferences;
	}
	
	public void putMemoPlan(boolean flag){
		mEditor.putBoolean(ISMEMOPLAN, flag);
		mEditor.commit();
	}
	
	public boolean getMemoPlan(){
		return mSharedPreferences.getBoolean(ISMEMOPLAN, false);
	}
	
	public void putAlarm(int num){
		mEditor.putInt(ALARM, num);
		mEditor.commit();
	}
	
	public int getAlarm(){
		return mSharedPreferences.getInt(ALARM, 0);
	}
}
