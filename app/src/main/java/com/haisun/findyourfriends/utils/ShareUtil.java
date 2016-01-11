package com.haisun.findyourfriends.utils;

import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShareUtil {

	public static List<String> singleShareName=new ArrayList<String>();//单聊时，成员的名字
	public static List<String> groupShareName=new ArrayList<String>();//群聊时，群的名字
	public static List<String> singleShareid=new ArrayList<String>();//单聊时，成员的id
	public static List<String> groupShareid=new ArrayList<String>();//群聊时，群的id

	public static AVObject shareperson=new AVObject("UserInfo");
	public static AVObject sharegroup=new AVObject("Group");
	public static ListView listview;
	public static Context context;//记录包含listview那一个界面的context
	public ShareUtil(){
		
	}
	
	public ShareUtil(Context context,ListView listview){
		this.listview=listview;
		this.context=context;
	}

	public static AVObject invitingPerson;
	
	public void setAVObject(AVObject invitingPerson){
		this.invitingPerson=invitingPerson;
	}
	

	public static void init()
	{ 
		try {
			LoginUtil.userinfo.refresh();
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String single=LoginUtil.userinfo.getString("singlesharedidsstr");
		if(single!=null&&single.length()!=0)
		{
			singleShareid=Utils.strParse(single);
		} 
		String group=LoginUtil.userinfo.getString("groupsharedidsstr");
		if(group!=null&&group.length()!=0)
		{
			groupShareid=Utils.strParse(group);
		}
		List<HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();
		AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		AVQuery<AVObject> query1 = new AVQuery<AVObject>("Group");
		if(singleShareid==null&&groupShareid ==null)
		{
			return ;
		}
		else {
			
			if(singleShareid!=null&&singleShareid.size()!=0)
			{
				singleShareName.clear();
				for(String shareid:singleShareid)
				{ 
					try {
						singleShareName.add(query.get(shareid).getString("username"));
					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(String singlesharename:singleShareName)
				{ 
					HashMap<String, Object> item=new HashMap<String, Object>();
					item.put("name",singlesharename);
					data.add(item);
				}	
			}
			if(groupShareid!=null&&groupShareid.size()!=0)
			{
				groupShareName.clear();
				for(String shareid:groupShareid)
				{ 
					try {
						query1.get(shareid).refresh();
						groupShareName.add(query1.get(shareid).getString("groupname"));
					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				for(String groupsharename:groupShareName)
				{ 
					HashMap<String, Object> item=new HashMap<String, Object>();
					item.put("name",groupsharename);
					data.add(item);
				}
			}
			SimpleAdapter adapter=new SimpleAdapter(context, data, R.layout.item,
			        new String[]{"name" },new int[]{R.id.name } );		
					listview.setAdapter(adapter);
		}	 
	}
	
	public static String id(String str)
	{
		for(int i=0;i<singleShareid.size();i++)
		{
			if(str.equals(singleShareName.get(i)))
			{
				return singleShareid.get(i);
			}
		}
		return null;
	}
	
	public static String groupid(String str)
	{
		for(int i=0;i<groupShareid.size();i++)
		{
			if(str.equals(groupShareName.get(i)))
			{
				return groupShareid.get(i);
			}
		}
		return null;
	}

	/**
	 * 将用户名为username的用户从群中删除，同时在用户列表中删除群的信息
	 * @param username
	 */

	public static void deleteMemberFromGroup(String username)
	{
		AVQuery<AVObject> query= new AVQuery<AVObject>("Group");
		try {
			sharegroup=query.get(groupid(username));
			//获取群的id，并从用户列表中删除所在群的id，表明脱离群关系
			String objectid=sharegroup.getObjectId();
			groupShareid.remove(objectid);
			if(groupShareid!=null&&groupShareid.size()!=0){
				LoginUtil.userinfo.put("groupsharedidsstr",Utils.strCombine(groupShareid));
			}
			else{
				LoginUtil.userinfo.put("groupsharedidsstr","");
			}
			LoginUtil.userinfo.save();
			
			//从群中删除用户的信息,如果群为空，则删除群
			String memberidsstr=sharegroup.getString("memberids");//�获取所有群成员的id
			List<String> memberids=Utils.strParse(memberidsstr);
			for(String id:memberids){
				if(id.equals(LoginUtil.userinfo.getObjectId())){
					memberids.remove(id);
					break;
				}
			}
			if(memberids!=null&&memberids.size()!=0){
				sharegroup.put("memberids",Utils.strCombine(memberids));
				sharegroup.save();
			}
			else {
				sharegroup.delete();
			} 
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 删除单个用户名为username成员
	 */
	public static void deleteMember(String username)
	{
		AVObject person=new AVObject(); 
		AVQuery<AVObject> query1 = new AVQuery<AVObject>("UserInfo");
		try {
			 person=query1.get(id(username));
			 String singlesharedstr=person.getString("singlesharedidsstr");
			 List<String> singleids=Utils.strParse(singlesharedstr);
			 singleids.remove(LoginUtil.userinfo.getObjectId());
 
			 //从被动好友一方删除登陆用户
			 if(singleids!=null&&singleids.size()!=0){
				 person.put("singlesharedidsstr", Utils.strCombine(singleids));
			 }
			 else {
				 person.put("singlesharedidsstr","");
			 }
			 person.save();
			 //从登陆用户列表中删除
			 singleShareid.remove(person.getObjectId());
			 if(singleShareid!=null&&singleShareid.size()!=0){
				 LoginUtil.userinfo.put("singlesharedidsstr", Utils.strCombine(singleShareid));
			 }
			 else {
				 LoginUtil.userinfo.put("singlesharedidsstr","");
			}
			 LoginUtil.userinfo.save();
			 
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isNameLeagal(String groupname)
	{
		List<AVObject> avObjects; 
		AVQuery<AVObject> singlequery= new AVQuery<AVObject>("UserInfo");
		AVQuery<AVObject> groupquery= new AVQuery<AVObject>("Group");
		singlequery.whereEqualTo("username", groupname);
		try {
			avObjects = singlequery.find();
			if(avObjects.size()!=0)
			{
				return false;
			}
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		groupquery.whereEqualTo("groupname", groupname);
		try {
			avObjects=groupquery.find();
			if(avObjects.size()!=0)
			{
				return false;
			}
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
}
