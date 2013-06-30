package com.calendar.util;

import java.util.ArrayList;

import android.R.integer;
public class AlarmToString  {

	private ArrayList<AlarmTime> alarmTimes = new ArrayList<AlarmTime>();
	private String alarmTimeString = new String();

	public ArrayList<AlarmTime> getAlarmTimes() {
		return alarmTimes;

	}

	public void setAlarmTimes(ArrayList<AlarmTime> s1) {
		this.alarmTimes = s1;

	}

	public String getalarmTimeString() {
		return alarmTimeString;
	}

	public void setAlarmTimeString(String alarmTimeString) {

		this.alarmTimeString = alarmTimeString;
	}
	
	public void AlarmsToString() {
		if (alarmTimes.size() != 0) {
			for (int i = 0; i < alarmTimes.size(); i++) {
				String s1 = "|" + String.valueOf(alarmTimes.get(i).getTime()) + "#"
						+ String.valueOf(alarmTimes.get(i).getTag());
				alarmTimeString = alarmTimeString + s1;

			}

		}

	}
	
	public void StringToAlarms() {
		if (alarmTimeString.length() != 0) {
			String a[] = alarmTimeString.split("\\|");
			for (int i = 1; i < a.length; i++) {
				String b[] = a[i].split("#");
				
				AlarmTime time = new AlarmTime(Long.valueOf(b[0]),Integer.parseInt(b[1]));
				alarmTimes.add(time);

			}

		}

	}
	
}
