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
	
	/**
	 * ������һҳ
	 * @param v
	 */
	public void returnback(View v)
	{
		Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
		startActivity(intent);
	}
	
	/**
	 * ���±������ߵ�������
	 * @param v
	 */
	public void invite(View v){ 
		//��ȡ�����˵ĺ����б�
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
		//�жϱ��������Ƿ��Ѿ��������˵ĺ���
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
			Utils.toast(getApplicationContext(), "�����Ϊ������");
			return;
		} 
		//��ȡ�������ߵı�������б�,����Ѿ��ڱ������б�����Ҳ�������
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
