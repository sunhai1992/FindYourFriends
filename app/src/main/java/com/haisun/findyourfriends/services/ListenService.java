package com.haisun.findyourfriends.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.haisun.findyourfriends.utils.LoginUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ���������Ƿ����˻�����Ⱥ�����Լ�����λ�ã�ÿ5�����һ��
 * @author lenovo
 *
 */
public class ListenService extends Service {
private static final String TAG = "RemindService";
	protected static final int UPDATE_TEXT = 0;
	Timer timer;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}  
	private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	switch (msg.what){
        	 case UPDATE_TEXT:
        		 LoginUtil.invitedSituation();
 	           	if(LoginUtil.flag==true)
     			{
             		//ShareUtil.init();
             		LoginUtil.flag=false;
     			}
     			break;
        	} 
        }
    };
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		timer = new Timer();
		
		timer.schedule(new TimerTask() {
		   int cnt=0;
			@Override
			public void run() {
				//ִ������
				//LoginUtil.invitedSituation();
				
				mHandler.sendEmptyMessage(UPDATE_TEXT);
			}
		},  1000, 5000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		Log.i(TAG, "RemindService is destroy");
	}
	
}
  