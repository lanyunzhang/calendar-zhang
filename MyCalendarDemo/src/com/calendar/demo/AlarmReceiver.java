package com.calendar.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("the set time");
		//�������þ�����յ��㲥�¼�
		
		intent.setClass(context, DilogActivity.class);
		context.startActivity(intent);
		
	}

}
