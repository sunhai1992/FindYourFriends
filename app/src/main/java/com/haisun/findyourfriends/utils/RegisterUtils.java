package com.haisun.findyourfriends.utils;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.models.UserIno;

import java.util.List;


public class RegisterUtils {
	/**
	 * 判断手机号码是否被注册
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
	 * 网上数据库表中新增一条用户信息,表示注册成功
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
	 * 发送手机验证码
	 * @param phone  手机号码
	 */
	 public static void sendCode(Context context,String phone)
	 {
		 try {
			AVOSCloud.requestSMSCode(phone,"ZJChat","register",10);
			Utils.toast(context, "发送成功，请注意查收");
		} catch (AVException e) {
			// TODO Auto-generated catch block
			//Utils.toast(getApplicationContext(), phone+"xxxx");
			e.printStackTrace();
		} 
	 }

	/**
	 * 验证手机验证码
	 * @param code
	 */
		public static boolean veryfyflag=false;
		public static void verifyCode(String code,String phonenumber) {
			 AVOSCloud.verifySMSCodeInBackground(code,phonenumber, new AVMobilePhoneVerifyCallback() {
			      @Override
			      public void done(AVException e) {
			    	  if(e!=null){
			    		  veryfyflag=false;
			    		  
			    	  }
			    	  else { 
			    		  veryfyflag=true;
					}
			      }
			    });
		 }
}
