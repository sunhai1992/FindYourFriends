package com.haisun.findyourfriends.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.utils.Exit;
import com.haisun.findyourfriends.utils.LoginUtil;
import com.haisun.findyourfriends.R;
import com.haisun.findyourfriends.utils.ShareUtil;
import com.haisun.findyourfriends.utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class ShareSituationActivity extends Activity {
	
	ListView listView;
	Timer timer = new Timer();//保证实时监测网上的动态更新
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		listView=(ListView)findViewById(R.id.sharesituationlistview);
 
		LoginUtil logutil=new LoginUtil(this);
		ShareUtil shareutil=new ShareUtil(this,listView);
		timer.schedule(task, 1000 * 1, 1000 * 5); 	 
		listView.setEnabled(true); 
		listView.setOnItemClickListener(new ItemClickListener());
		
		Exit.add(this);
	}
	
	private final class ItemClickListener implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			 View curr = parent.getChildAt((int) id);
			 TextView c = (TextView)curr.findViewById(R.id.name);
			 TextView d = (TextView)curr.findViewById(R.id.exit);
			 c.setOnClickListener(new nameOnClickListener());
			 c.setOnClickListener(new exitOnClickListener()); 
			 Utils.toast(getApplicationContext(), c.getText().toString() + "o");
			// TODO Auto-generated method stub
		}	
	}
	
	public final class nameOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 TextView c = (TextView)v;
			 String playerChanged = c.getText().toString();
			 Utils.toast(getApplicationContext(), playerChanged+"o");
		}
	}
	
	public final class exitOnClickListener implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 TextView c = (TextView)v;
			 String playerChanged = c.getText().toString();
		}
	}
 

	boolean isclick=false;
	 @SuppressLint("NewApi")
	 String playerChanged="";
	 TextView clickName ;
	 
	
	
	public void nameclick(View v)
	 {
		if(isclick){
			clickName.setBackgroundColor(Color.TRANSPARENT);
		 }
			TextView clickName1= (TextView)v;
			clickName1.setBackgroundColor(Color.GREEN); 
			playerChanged =clickName1.getText().toString();
			isclick=true;
			clickName=clickName1;
	 }
	

	public void enterin(View v)
	{
         if(isclick==false)
        	 return ;
		 Utils.friendon=1;
		 Intent intent=new Intent(ShareSituationActivity.this,LoginActivity.class);
		 AVObject shareperson=new AVObject(); 
		 AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		 AVQuery<AVObject> queryy = new AVQuery<AVObject>("Group");
		 try {
			 shareperson=query.get(ShareUtil.id(playerChanged));//�����ھͻ��׳��쳣
			 ShareUtil.shareperson=shareperson;
		} catch (AVException e) {
			 Utils.friendon=2;
			 try {
				ShareUtil.sharegroup=queryy.get(ShareUtil.groupid(playerChanged));
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//跳转界面则停止timer
		 if (timer != null) {
	            timer.cancel();
	            timer = null;
	     }
		 startActivity(intent); 
	}
	
	 public void exit(View v)
	 { 
		 if(isclick==false)
        	 return ;  
		 AVObject shareperson=new AVObject(); 
		 AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		 AVQuery<AVObject> queryy = new AVQuery<AVObject>("Group");
		 try {
			 shareperson=query.get(ShareUtil.id(playerChanged));//�����ھͻ��׳��쳣
			 ShareUtil.deleteMember(playerChanged);
		} catch (AVException e) { 
			 try {
				ShareUtil.sharegroup=queryy.get(ShareUtil.groupid(playerChanged));
				ShareUtil.deleteMemberFromGroup(playerChanged);
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	 }
	  
		 
	TimerTask task = new TimerTask() {
	    public void run() {
	    	Message message = new Message();
	        message.what = 1;	            
	        handler.sendMessage(message);
	    }
	};
 
 
	private boolean first=false;
	 final Handler handler = new Handler() {
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case 1:
	                update();
	                break;
	            }
	            super.handleMessage(msg);
	        }
	        void update() {
	        	LoginUtil.invitedSituation();
	        	ShareUtil.init();
	        	if(isclick)
	        	{
	        		clickName.setBackgroundColor(Color.GREEN);
	        	}
	        }
	    };
	

	public void invite(View v)
	{ 
		 if (timer != null) {// ֹͣtimer
	            timer.cancel();
	            timer = null;
	        }
		Intent intent=new Intent(ShareSituationActivity.this,SearchActivity.class);
		startActivity(intent);
	}

	public void returnback(View v)
	{
		Utils.friendon=0;
		if (timer != null) {// ֹͣtimer
	            timer.cancel();
	            timer = null;
	        }
		Intent intent=new Intent(ShareSituationActivity.this,LoginActivity.class);
		startActivity(intent);
	}
	
	public void exitall(View v)
	{
		//finish();
		Exit.close();
		System.exit(0);
	}
	
    @Override
    protected void onDestroy() {
        if (timer != null) {// ֹͣtimer
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}


