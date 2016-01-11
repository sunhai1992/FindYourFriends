package com.haisun.findyourfriends.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.haisun.findyourfriends.utils.Exit;
import com.haisun.findyourfriends.utils.LoginUtil;
import com.haisun.findyourfriends.R;
import com.haisun.findyourfriends.utils.ShareUtil;
import com.haisun.findyourfriends.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchedContactinfoActivity extends Activity {
	
	TextView sex;
	TextView username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searched_person_info);
		sex=(TextView) findViewById(R.id.sex);
		username=(TextView) findViewById(R.id.username);
		sex.setText(String.valueOf(ShareUtil.invitingPerson.getBoolean("sex")));
		username.setText(ShareUtil.invitingPerson.getString("username"));
		
		Exit.add(this);
	}
	

	public void returnback(View v)
	{
		Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
		startActivity(intent);
	}
	

	public void invite(View v){ 
		//获取邀请人的好友列表
		try {
			LoginUtil.userinfo.refresh();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(LoginUtil.userinfo.getString("phonenumber").equals(ShareUtil.invitingPerson.getString("phonenumber")))
		{
			Utils.toast(getApplicationContext(), "��������Լ�Ϊ����");
			return;
		}
		String singleshareda=LoginUtil.userinfo.getString("singlesharedidsstr");
		List<String> singlesharedaids;
		//判断被邀请人是否已经是邀请人的好友
		boolean flag=false;
		if(singleshareda!=null&&singleshareda.length()!=0)
		{
			String tmp=ShareUtil.invitingPerson.getObjectId();
			singlesharedaids=Utils.strParse(singleshareda);
			for(String id:singlesharedaids)
			{
				if(id.equals(tmp))
				{
					flag=true;
					break;
				}
			}
		}
		if(flag==true)
		{
			Utils.toast(getApplicationContext(), "已添加为好友了");
			return;
		} 
		//获取被邀请者的被邀请的列表,如果已经在被邀请列表中则也不再添加
		String singleinvitedstr=ShareUtil.invitingPerson.getString("singleinvitedstr");
		List<String> singleinvited; 
		if(singleinvitedstr!=null&&singleinvitedstr.length()!=0)
		{
			singleinvited=Utils.strParse(singleinvitedstr);
			String tmp=LoginUtil.userinfo.getObjectId();
			boolean flag1=false;
			for(String id:singleinvited)
			{
				if(id.equals(tmp))
				{
					flag1=true;
					return;
				}
			}
			if(flag1==false)
			{
				singleinvited.add(LoginUtil.userinfo.getObjectId().toString());
			}
	    }
		else
		{
			singleinvited=new ArrayList<String>();
			singleinvited.add(LoginUtil.userinfo.getObjectId());
		}
		ShareUtil.invitingPerson.put("singleinvitedstr", Utils.strCombine(singleinvited));
	 	 
		ShareUtil.invitingPerson.saveInBackground();	
		Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
		startActivity(intent);
	}
	
}
