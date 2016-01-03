package com.haisun.findyourfriends.utils;

import java.util.List;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.models.UserIno;

/**
 * ע���û�ʹ�õĺ�������
 * @author lenovo
 *
 */
public class RegisterUtils {
	/**
	 * �ж��ֻ������Ƿ�ע��
	 * @param phonenumber
	 * @return
	 * @throws AVException 
	 */
	static String tableName = "UserInfo";
	static AVObject user = new AVObject(tableName);
	public static boolean isRegistered(String phonenumber)  
	{
		try {
		 	AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
		 	query.whereEqualTo("phonenumber", phonenumber);
		 	List<AVObject> avObjects = query.find();
		 	if(avObjects.size()!=0)
		 	{
		 		//Toast.makeText(getApplicationContext(), "���ֻ����Ѿ���ע��", 1).show();
		 		return false;
		 	}
		} catch (AVException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	 	return true;
	}
	
	/**
	 * �������ݿ��������һ���û���Ϣ,��ʾע��ɹ�
	 * @param userInfo
	 */
	public static void register(UserIno userInfo){
		AVGeoPoint point = new AVGeoPoint(0, 0);
		user.put("phonenumber", userInfo.getPhonenumber());
		user.put("username", userInfo.getUsername());
		user.put("password", userInfo.getPassword());
		user.put("on", userInfo.isOn());
		user.saveInBackground();
	}
	
	 /**
	  * �����ֻ���֤��
	  * @param phone  �ֻ�����
	  */
	 public static void sendCode(Context context,String phone)
	 {
		 try {
			AVOSCloud.requestSMSCode(phone,"ZJChat","register",10);
			Utils.toast(context, "���ͳɹ�����ע�����");
		} catch (AVException e) {
			// TODO Auto-generated catch block
			//Utils.toast(getApplicationContext(), phone+"xxxx");
			e.printStackTrace();
		} 
	 }
	  
	 /**
		 * ��֤�ֻ���֤��
		 * @param code
		 */
		public static boolean veryfyflag=false;
		public static void verifyCode(String code,String phonenumber) {
			 AVOSCloud.verifySMSCodeInBackground(code,phonenumber, new AVMobilePhoneVerifyCallback() {
			      @Override
			      public void done(AVException e) {
			    	  if(e!=null){
			      //�˴���������û���Ҫ��ɵĲ���
			    		  veryfyflag=false;
			    		  
			    	  }
			    	  else { 
			    		  veryfyflag=true;
					}
			      }
			    });
		 }
}
