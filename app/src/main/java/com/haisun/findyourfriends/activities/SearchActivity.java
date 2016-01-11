package com.haisun.findyourfriends.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.utils.Exit;
import com.haisun.findyourfriends.utils.LoginUtil;
import com.haisun.findyourfriends.R;
import com.haisun.findyourfriends.utils.ShareUtil;
import com.haisun.findyourfriends.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
	
	EditText phonenumber;
	EditText groupname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		phonenumber=(EditText) findViewById(R.id.searchphonumber);
		groupname=(EditText) findViewById(R.id.groupname);
		
		Exit.add(this);
	}
	
	static String tableName = "UserInfo";
	public static AVObject userinfo=new AVObject();
	List<AVObject> avObjects; 
	AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
	
	
	public void searchContact(View v)
	{
		ShareUtil util=new ShareUtil();
	 	query.whereEqualTo("phonenumber", phonenumber.getText().toString());
	 	try {
		    avObjects = query.find();
			//Toast.makeText(getApplicationContext(), String.valueOf(avObjects.size()), 1).show();
			if(avObjects.size()==0)
			{
				Utils.toast(getApplicationContext(), "不存在此用户");
			}
			else {  
			 	  userinfo=avObjects.get(0);
			 	  util.setAVObject(userinfo); 
			 	  Intent intent=new Intent(SearchActivity.this,SearchedContactinfoActivity.class);
			 	  startActivity(intent);
			}
		} catch (AVException e) { 
			e.printStackTrace();
		}
	} 
	
	public void returnback(View v)
	{
		Intent intent=new Intent(SearchActivity.this,ShareSituationActivity.class);
	 	startActivity(intent);
	}
	

	String groupstr;
	public void selectedFromContacts(View v)
	{
	    groupstr=groupname.getText().toString();
		if(groupstr==null||groupstr.equals(""))
		{
			Utils.toast(getApplicationContext(), "请输入群名");
			return;
		}
		
		if(ShareUtil.isNameLeagal(groupstr)==false)
		{
			Utils.toast(getApplicationContext(), "此群名已经存在");
			return;
		}
		
		Utils.getPhoneContacts(this);
		ArrayList<String> mContactsName=Utils.mContactsName;
		//�����Ի��� 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("复选框");
		final boolean [] defaultSelectedStatus=new boolean[mContactsName.size()];
	    String [] str=(String[])mContactsName.toArray(new String[mContactsName.size()]); 
		

		builder.setMultiChoiceItems(str, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener()
        {
             @Override
              public void onClick(DialogInterface dialog, int which, boolean isChecked)
              { 
            	  defaultSelectedStatus[which]=isChecked;
	               }
        });

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
			List<AVObject> allavObjects=Utils.allavObjects;
			List<AVObject> addavObjects=new ArrayList<AVObject>();
			List<String> groupids=new ArrayList<String>();
             @Override
             public void onClick(DialogInterface dialog, int which)
             {
            	 AVObject group=new AVObject("Group");
            	 groupids.add(LoginUtil.userinfo.getObjectId());//将群主id添加至群中
            	  
            	 for(int i=0;i<defaultSelectedStatus.length;i++)
            	 {
            		 if(defaultSelectedStatus[i])
            		 {
            			 groupids.add(allavObjects.get(i).getObjectId());  //群的所有人的id
            			 addavObjects.add(allavObjects.get(i));
            		 } 
            	 }
            	 group.put("groupname", groupstr);
            	 group.put("groupowner", LoginUtil.userinfo.getObjectId());
            	 //group.put("memberids", Utils.strCombine(groupids));
            	 group.put("memberids", LoginUtil.userinfo.getObjectId());
            	 try {
					group.save();
				} catch (AVException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	 
            	 String groupid=group.getObjectId();
            	 
            	 for(AVObject avobject:addavObjects)
            	 {
            		 try {
						avobject.refresh();
					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		 String groupinvited=avobject.getString("groupinvitedstr");
            		 List<String> groupinvitedparse=new ArrayList<String>();
            		 if(groupinvited!=null&&groupinvited.length()!=0)
            		 {
            			 groupinvitedparse=Utils.strParse(groupinvited);
            		 }
            		 
            		 groupinvitedparse.add(groupid); //���Ⱥ����
            		 avobject.put("groupinvitedstr", Utils.strCombine(groupinvitedparse));
            		 avobject.saveInBackground();
            	 }
            	 
            	 String groupsharedidstr=LoginUtil.userinfo.getString("groupsharedidstr");
            	 List<String> groupsharedids=new ArrayList<String>();
            	 if(groupsharedidstr!=null&&groupsharedidstr.length()!=0)
        		 {
            		 groupsharedids=Utils.strParse(groupsharedidstr);
        		 }
            	 
            	 groupsharedids.add(groupid);
            	 LoginUtil.userinfo.put("groupsharedidsstr", Utils.strCombine(groupsharedids));
            	 LoginUtil.userinfo.saveInBackground();
            	 
              }
        });

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
              @Override
              public void onClick(DialogInterface dialog, int which)
              {
              }
        });
        builder.show();   
	}
	
	
}
