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
	
	/**
	 * ������ϵ����Ϣ
	 * @param v
	 */
	public void searchContact(View v)
	{
		ShareUtil util=new ShareUtil();
	 	query.whereEqualTo("phonenumber", phonenumber.getText().toString());
	 	try {
		    avObjects = query.find();
			//Toast.makeText(getApplicationContext(), String.valueOf(avObjects.size()), 1).show();
			if(avObjects.size()==0)
			{
				Utils.toast(getApplicationContext(), "�����ڴ��û�");
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
	
	/**
	 * ���ֻ�ͨѶ¼�л����ϵ�˵���Ϣ
	 * @param v
	 */
	String groupstr;
	public void selectedFromContacts(View v)
	{
	    groupstr=groupname.getText().toString();
		if(groupstr==null||groupstr.equals(""))
		{
			Utils.toast(getApplicationContext(), "������Ⱥ��");
			return;
		}
		
		if(ShareUtil.isNameLeagal(groupstr)==false)
		{
			Utils.toast(getApplicationContext(), "��Ⱥ���Ѿ�����");
			return;
		}
		
		Utils.getPhoneContacts(this);
		ArrayList<String> mContactsName=Utils.mContactsName;
		//�����Ի��� 
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ѡ��");
		final boolean [] defaultSelectedStatus=new boolean[mContactsName.size()];
	    String [] str=(String[])mContactsName.toArray(new String[mContactsName.size()]); 
		
		//���帴ѡ��ѡ��   
		builder.setMultiChoiceItems(str, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener()
        {
             @Override
              public void onClick(DialogInterface dialog, int which, boolean isChecked)
              { 
            	 //�����ظ�ѡ��ȡ��������Ӧȥ�ı�item��Ӧ��boolֵ�����ȷ��ʱ���������bool[],�õ�ѡ�������  
                   defaultSelectedStatus[which]=isChecked;
	               }
        });
		//���öԻ���[�϶�]��ť         
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
        {
			List<AVObject> allavObjects=Utils.allavObjects;
			List<AVObject> addavObjects=new ArrayList<AVObject>();
			List<String> groupids=new ArrayList<String>();
             @Override
             public void onClick(DialogInterface dialog, int which)
             {
            	 AVObject group=new AVObject("Group");
            	 groupids.add(LoginUtil.userinfo.getObjectId());//��Ⱥ��id�����Ⱥ��
            	  
            	 for(int i=0;i<defaultSelectedStatus.length;i++)
            	 {
            		 if(defaultSelectedStatus[i])
            		 {
            			 groupids.add(allavObjects.get(i).getObjectId());  //Ⱥ�������˵�id
            			 addavObjects.add(allavObjects.get(i));
            		 } 
            	 }
            	 group.put("groupname", groupstr);
            	 group.put("groupowner", LoginUtil.userinfo.getObjectId());//���Ⱥ��������
            	 //group.put("memberids", Utils.strCombine(groupids)); //��ӳ�Ա��
            	 group.put("memberids", LoginUtil.userinfo.getObjectId()); //��ʱ��ֻtianjia qunzhu,houxuchengyuanjinlai zai tianjia
//            	 group.saveInBackground();��߲����������ԭ����������Ҫ���objectid
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
            		 String groupinvited=avobject.getString("groupinvitedstr");//��ȡ�������еı�����Ⱥ�����
            		 List<String> groupinvitedparse=new ArrayList<String>();
            		 if(groupinvited!=null&&groupinvited.length()!=0)
            		 {
            			 groupinvitedparse=Utils.strParse(groupinvited);
            		 }
            		 
            		 groupinvitedparse.add(groupid); //���Ⱥ����
            		 avobject.put("groupinvitedstr", Utils.strCombine(groupinvitedparse));//����ÿ����Ա����������
            		 avobject.saveInBackground();
            	 }
            	 
            	 String groupsharedidstr=LoginUtil.userinfo.getString("groupsharedidstr");//��Ⱥ�������Ⱥ����
            	 List<String> groupsharedids=new ArrayList<String>();
            	 if(groupsharedidstr!=null&&groupsharedidstr.length()!=0)
        		 {
            		 groupsharedids=Utils.strParse(groupsharedidstr);
        		 }
            	 
            	 groupsharedids.add(groupid);
            	 LoginUtil.userinfo.put("groupsharedidsstr", Utils.strCombine(groupsharedids));//����Ⱥ���б���������Ⱥ�����Ⱥ���·�
            	 LoginUtil.userinfo.saveInBackground();
            	 
              }
        });
		//���öԻ���[��]��ť   
		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
        {
              @Override
              public void onClick(DialogInterface dialog, int which)
              {
              }
        });
        builder.show();   
	}
	
	
}
