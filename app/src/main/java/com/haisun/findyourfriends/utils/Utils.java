package com.haisun.findyourfriends.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
 
	public static int friendon=0; 
	  public static void toast(Context context,String content) {
		    Toast.makeText(context , content, Toast.LENGTH_SHORT).show();
	  }

	/**
	 * 判断手机号码的合法性
	 * @param phonenumber
	 * @return
	 */
	  public static boolean isIllegalPhonenumber(String phonenumber)
	  {
		  char[] c = phonenumber.toCharArray();
		  int i;
		  if(phonenumber.length()!=11||c[0]!='1')
		  {
			 return false;
		  } 
		  for(i=1;i<=10;i++)
		  {
			  if(c[i]>='0'&&c[i]<='9')
			  {;}
			  else{
				  return false;
			  }
		  }
		  return true;
	  }
	/**
	 * 因为addallunique函数暂未实现，总是有很多错误，所以这两个函数是用来实现类似功能的
	 * @param str
	 * @return
	 */
	  public static List<String> strParse(String str)
	  {
		  List<String> parsedstr=new ArrayList<String>();
		  if(str==null||str.length()==0)
		  {
			  return null;
		  }
		  else {
			String[] s=str.split(",");
			parsedstr=Arrays.asList(s); 
			return new ArrayList(parsedstr);
		}
	  }
	  
	  public static String strCombine(List<String> parse)
	  {
		  String combinestr="";
		  boolean flag=false;
		  for(String str:parse)
		  {
			  if(flag==false)
			  {
				  combinestr=combinestr+str;
				  flag=true;
			  }
			  else {
				  combinestr=combinestr+","+str;
			}
		  }
		   return combinestr;
	  }
	      

	    private static final String[] PHONES_PROJECTION = new String[] {  
	            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };
	    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
	    private static final int PHONES_NUMBER_INDEX = 1;
	    private static final int PHONES_PHOTO_ID_INDEX = 2;
	    private static final int PHONES_CONTACT_ID_INDEX = 3;        	  

	    public static List<AVObject> allavObjects=new ArrayList<AVObject>();
	    public static ArrayList<String> mContactsName = new ArrayList<String>();
	    public static ArrayList<String> mContactsNumber = new ArrayList<String>();
	    public static void getPhoneContacts(Context mContext) {
		  AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		  List<AVObject> avObjects; 
		  AVObject tmp=new AVObject("UserInfo");
	      ContentResolver resolver = mContext.getContentResolver();  
 
	      // 获取手机联系人
	      Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);  
	    
	      allavObjects.clear();
	      mContactsName.clear();
	      mContactsNumber.clear();
	      if (phoneCursor != null) {  
	          while (phoneCursor.moveToNext()) {  
	        	  
	              //得到手机号码
	              String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
	              //当手机号码为空的或者为空字段 跳过当前循环
	              if (TextUtils.isEmpty(phoneNumber))  
	                  continue;  
	              query.whereEqualTo("phonenumber", phoneNumber);
	              try {
					avObjects=query.find();
					if(avObjects!=null&&avObjects.size()!=0)
					{
						allavObjects.add(avObjects.get(0));
						 String contactName=avObjects.get(0).getString("username");
						 mContactsName.add(contactName); 
					}
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  mContactsNumber.add(phoneNumber);
	          }  
	          phoneCursor.close();  
	      }  
	  }  
	  
}
