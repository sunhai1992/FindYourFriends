package com.haisun.findyourfriends.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;
import android.widget.Toast;

public class Utils {
 
	public static int friendon=0; 
	  public static void toast(Context context,String content) {
		    Toast.makeText(context , content, Toast.LENGTH_SHORT).show();
	  }
	  
	  /**
	   * �ж��ֻ�����ĺϷ���
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
	 * ��Ϊaddallunique������δʵ�֣������кܶ������������������������ʵ�����ƹ��ܵ�
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
	      
	    /**��ȡ��Phon���ֶ�**/  
	    private static final String[] PHONES_PROJECTION = new String[] {  
	            Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };  	     
	    /**��ϵ����ʾ����**/  
	    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
	    /**�绰����**/  
	    private static final int PHONES_NUMBER_INDEX = 1;  	      
	    /**ͷ��ID**/  
	    private static final int PHONES_PHOTO_ID_INDEX = 2;       
	    /**��ϵ�˵�ID**/  
	    private static final int PHONES_CONTACT_ID_INDEX = 3;        	  
	   
  //	    /**��ϵ��ͷ��**/  
//	    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();  
	   
	    
	    /**��ϵ�������ϵ�������Ϣ  */
	    public static List<AVObject> allavObjects=new ArrayList<AVObject>(); 
	    /**��ϵ������**/  
	    public static ArrayList<String> mContactsName = new ArrayList<String>();     
	    /**��ϵ���ֻ�����**/  
	    public static ArrayList<String> mContactsNumber = new ArrayList<String>();   
	    
	  /**�õ��ֻ�ͨѶ¼��ϵ����Ϣ**/  
	  public static void getPhoneContacts(Context mContext) {  
		  AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		  List<AVObject> avObjects; 
		  AVObject tmp=new AVObject("UserInfo");
	      ContentResolver resolver = mContext.getContentResolver();  
 
	      // ��ȡ�ֻ���ϵ��  
	      Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);  
	    
	      allavObjects.clear();
	      mContactsName.clear();
	      mContactsNumber.clear();
	      if (phoneCursor != null) {  
	          while (phoneCursor.moveToNext()) {  
	        	  
	              //�õ��ֻ�����  
	              String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
	              //���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��  
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
	             
	                
//	              //�õ���ϵ������  
//	              String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);  
	             
//	              //�õ���ϵ��ID  
//	              Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);  
//	    
//	              //�õ���ϵ��ͷ��ID  
//	              Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);  
//	                
//	              //�õ���ϵ��ͷ��Bitamp  
//	              Bitmap contactPhoto = null;  
//	    
//	              //photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�  
//	              if(photoid > 0 ) {  
//	                  Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
//	                  InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
//	                  contactPhoto = BitmapFactory.decodeStream(input);  
//	              }else {  
//	                  contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.contact_photo);  
//	              }  
	                
	              
	              mContactsNumber.add(phoneNumber);  
	            //  mContactsPhonto.add(contactPhoto);  
	          }  
	          phoneCursor.close();  
	      }  
	  }  
	  
}
