package com.haisun.findyourfriends.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.models.UserIno;

/**
 * �����û���¼����û�����Ϣ
 * @author lenovo
 *
 */
public class LoginUtil {
	static String tableName = "UserInfo";
	public static AVObject userinfo=new AVObject();
	List<AVObject> avObjects; 
	public static UserIno user=new UserIno();
	public static Context context;//��¼���Ƿ�������context
	public static boolean flag=false;
	public LoginUtil(){}
	
	public LoginUtil(Context context)
	{
		this.context=context;
	}
	
	/**
	 * �ж��û���¼�ĺϷ��ԣ�����Ϸ����¼���û���Ϣ
	 * @param context
	 * @param inputname
	 * @param inputpassword
	 * @return
	 */
	public boolean legal(Context context,String inputname,String inputpassword){
		AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
	 	
	 	query.whereEqualTo("username", inputname);
	    query.whereEqualTo("password", inputpassword);

	 	try {
		    avObjects = query.find();
			//Toast.makeText(getApplicationContext(), String.valueOf(avObjects.size()), 1).show();
			if(avObjects.size()==0)
			{
				Utils.toast(context,"�����ڴ��û�");
				return false;
			}
			else {  
			 	 userinfo=avObjects.get(0);
			 	 if(userinfo==null){
			 		Utils.toast(context, "caos");
			 	 }else{ 
				 	 user.setPassword(userinfo.getString("password"));
				 	 user.setUsername(userinfo.getString("username"));
				 	 user.setLatitude(userinfo.getDouble("latitude"));
				 	 user.setLongtitude(userinfo.getDouble("longtitude"));
				 	 user.setPhonenumber(userinfo.getString("phonenumber"));
				 	 user.setOn(true);
			 	 } 
			}
		} catch (AVException e) { 
			e.printStackTrace();
			return false;
		}
	 	return true;
	}
	/**
	 * ��½�����Ƿ����������Լ�,�����Լ���Ҫ����ѡ��
	 * @param context
	 */
	private static List<String> singleinvitedids;
	private static List<String> groupinvitedids;
	private static String [] str;
	private static boolean[] defaultSelectedStatus;
	private static int singleinvitedlen=0;
    private static int groupinvitedlen=0;
	static int p=0;
	public static void invitedSituation()
	{ 
		 try {
				userinfo.refresh();
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		String singleinvitedstr=userinfo.getString("singleinvitedstr");//�ѱ������
		String groupinvitedstr=userinfo.getString("groupinvitedstr");
	    AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
	    Log.i("zj",String.valueOf(p++));
	 
	    if(singleinvitedstr==null||singleinvitedstr.length()==0){
	    	singleinvitedlen=0;
	    }
	    else{
	    	singleinvitedids=Utils.strParse(singleinvitedstr);   
	    	singleinvitedlen=singleinvitedids.size();
	    }
	    if(groupinvitedstr==null||groupinvitedstr.length()==0){
	    	groupinvitedlen=0;
	    }
	    else {
	    	groupinvitedids=Utils.strParse(groupinvitedstr); 
	    	groupinvitedlen=groupinvitedids.size();
		}
	    	
	    if(singleinvitedlen==0&&groupinvitedlen==0)
	    	return;
	    str=new String[singleinvitedlen+ groupinvitedlen];
	    defaultSelectedStatus=new boolean[singleinvitedlen+ groupinvitedlen];
		int cnt=0;
	   
		if(singleinvitedstr!=null&&singleinvitedstr.length()!=0){  //single situation
			for(String singleid:singleinvitedids) //single situation
			{
				try { 
					str[cnt]=query.get(singleid).getString("username")+"��������һ�����λ��";
					cnt=cnt+1;
					//Utils.toast(context, str[cnt-1]);
					Log.i("zj",String.valueOf(p++)+"llll");
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
	    if(groupinvitedstr!=null&&groupinvitedstr.length()!=0){  //group situation  
	    	
	    	AVQuery<AVObject> querygroup = new AVQuery<AVObject>("Group");
			for(String groupid:groupinvitedids)
			{
				try { 
					str[cnt]=querygroup.get(groupid).getString("groupowner")+"invited you to join the group";
					cnt=cnt+1;
					//Utils.toast(context, str[cnt-1]);
					Log.i("zj",String.valueOf(p++)+"llll");
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	    }
 			//�����Ի��� 
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("��ѡ��");
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
	             @Override
	             public void onClick(DialogInterface dialog, int which)
	             {
	            	 List<String> singlesharedids;
	            	 List<String> groupsharedids;
	            	 String singlesharedidsstr=userinfo.getString("singlesharedidsstr");
	            	 String groupsharedidsstr=userinfo.getString("groupsharedidsstr");
	            	 if(singlesharedidsstr==null||singlesharedidsstr.length()==0)
	            	 {
	            		 singlesharedids=new ArrayList<String>();
	            	 }
	            	 else {
						singlesharedids=Utils.strParse(singlesharedidsstr);
					}
	            	 
	            	 if(groupsharedidsstr==null||groupsharedidsstr.length()==0)
	            	 {
	            		 groupsharedids=new ArrayList<String>();
	            	 }
	            	 else {
	            		 groupsharedids=Utils.strParse(groupsharedidsstr);
					}
	            	 
	            	 AVQuery<AVObject> queryy = new AVQuery<AVObject>("UserInfo");
	            	 for(int i=0;i<defaultSelectedStatus.length;i++)
	            	 {
	            		 //����ֵ�ĸ�����Ҫ�ػ�listview
	            		 if(defaultSelectedStatus[i]&&i<singleinvitedlen)//single
	            		 {
	            			 if(flag==false)
	            			 {
	            				 flag=true;
	            			 }
	            			 singlesharedids.add(singleinvitedids.get(i));
	            			 try {
	            				 /**
	            				  * ��ÿһ�����뷽���Ӽ�¼
	            				  */
	            				 AVObject userinfo1=new AVObject();
	            				 userinfo1=queryy.get(singleinvitedids.get(i));
	            				 String singeid=userinfo1.getString("singlesharedidsstr");//���������Ѿ��еĺ����б�
	            				 List<String> singleids;
	            				 if(singeid==null||singeid.length()==0) {
	            					 singleids=new ArrayList<String>();
	            				 }
	            				 else {
									 singleids = Utils.strParse(singeid);
								 }
								 singleids.add(userinfo.getObjectId());//all invited people should add inviting person
 
								// userinfo1.addAllUnique("singlesharedids", singleids);
								 userinfo1.put("singlesharedidsstr", Utils.strCombine(singleids));
//								 userinfo1.saveInBackground();
								 userinfo1.save();
								 userinfo1.refresh();
	            			 } catch (AVException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
 	            		 }
	            		 
	            		 else if(defaultSelectedStatus[i]){ //group
	            			 groupsharedids.add(groupinvitedids.get(i-singleinvitedlen));
	            		     AVQuery<AVObject> querygroup = new AVQuery<AVObject>("Group"); //each group add the id of the new member
	            		     try {
								AVObject grp=querygroup.get(groupinvitedids.get(i-singleinvitedlen));//get the id of the group
								grp.refresh();
								String newid=userinfo.getObjectId();
								String memberidsstr=grp.getString("memberids");
								List<String> memberids;
								if(memberidsstr==null||memberidsstr.length()==0){
									memberids=new ArrayList<String>();
								}
								else {
									memberids=Utils.strParse(memberidsstr);
								}
								memberids.add(newid);
								grp.put("memberids", Utils.strCombine(memberids));
								grp.save(); 
							} catch (AVException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
	            	 }
	            	 //Ϊ�Լ�����µ������¼,ͬʱ��ձ�����һ��,inviting person should add all invited people
	            	  userinfo.put("singlesharedidsstr", Utils.strCombine(singlesharedids));
	            	  userinfo.put("groupsharedidsstr", Utils.strCombine(groupsharedids));
	            	  userinfo.remove("singleinvitedstr");
	            	  userinfo.remove("groupinvitedstr");
//	            	  userinfo.saveInBackground();
	            	  try {
						userinfo.save();
					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	
//	static int p=0;
//	public static void invitedSituation()
//	{ 
//		 try {
//				userinfo.refresh();
//			} catch (AVException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		String singlee=userinfo.getString("singleinvitedstr");//�ѱ������
//		String group=userinfo.getString("groupinvitedstr");
//	    AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
//	    Log.i("zj",String.valueOf(p++));
//		if(singlee!=null&&singlee.length()!=0){  //single situation
//			final List<String> singleinvited=Utils.strParse(singlee);
//			final String [] str=new String[singleinvited.size()];
//			final boolean[] defaultSelectedStatus=new boolean[singleinvited.size()];
//			int cnt=0;
//			for(String single:singleinvited)
//			{
//				
//				try { 
//					str[cnt++]=query.get(single).getString("username")+"��������һ�����λ��";
//					Utils.toast(context, str[cnt-1]);
//					Log.i("zj",String.valueOf(p++)+"llll");
//				} catch (AVException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//	  
// 			//�����Ի��� 
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//			builder.setTitle("��ѡ��");
//			//���帴ѡ��ѡ��   
//			builder.setMultiChoiceItems(str, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener()
//	        {
//	             @Override
//	              public void onClick(DialogInterface dialog, int which, boolean isChecked)
//	              { 
//	            	 //�����ظ�ѡ��ȡ��������Ӧȥ�ı�item��Ӧ��boolֵ�����ȷ��ʱ���������bool[],�õ�ѡ�������  
//	                   defaultSelectedStatus[which]=isChecked;
// 	               }
//	        });
//			//���öԻ���[�϶�]��ť         
//			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener()
//	        {
//	             @Override
//	             public void onClick(DialogInterface dialog, int which)
//	             {
//	            	 List<String> singlesharedids;
//	            	 String singlesharedidsstr=userinfo.getString("singlesharedidsstr");
//	            	 if(singlesharedidsstr==null||singlesharedidsstr.length()==0)
//	            	 {
//	            		 singlesharedids=new ArrayList<String>();
//	            	 }
//	            	 else {
//						singlesharedids=Utils.strParse(singlesharedidsstr);
//					}
//	            	 AVQuery<AVObject> queryy = new AVQuery<AVObject>("UserInfo");
//	            	 for(int i=0;i<defaultSelectedStatus.length;i++)
//	            	 {
//	            		 //����ֵ�ĸ�����Ҫ�ػ�listview
//	            		 if(defaultSelectedStatus[i])
//	            		 {
//	            			 if(flag==false)
//	            			 {
//	            				 flag=true;
//	            			 }
//	            			 singlesharedids.add(singleinvited.get(i));
//	            			 try {
//	            				 /**
//	            				  * ��ÿһ�����뷽���Ӽ�¼
//	            				  */
//	            				 AVObject userinfo1=new AVObject();
//	            				 userinfo1=queryy.get(singleinvited.get(i));
//	            				 String singeid=userinfo1.getString("singlesharedidsstr");//���������Ѿ��еĺ����б�
//								 List<String> singleids= Utils.strParse(singeid);
//								 if(singleids==null)
//								 {
//									 singleids=new ArrayList<String>();
//								 }
//								 singleids.add(userinfo.getObjectId());
// 
//								// userinfo1.addAllUnique("singlesharedids", singleids);
//								 userinfo1.put("singlesharedidsstr", Utils.strCombine(singleids));
//								 userinfo1.saveInBackground();
//	            			 } catch (AVException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
// 	            		 }
//	            	 }
//	            	 //Ϊ�Լ�����µ������¼,ͬʱ��ձ�����һ��
//	            	  userinfo.put("singlesharedidsstr", Utils.strCombine(singlesharedids));
//	            	  userinfo.remove("singleinvitedstr");
//	            	  userinfo.saveInBackground();
//	              }
//            });
//			//���öԻ���[��]��ť   
//			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
//	        {
//	              @Override
//	              public void onClick(DialogInterface dialog, int which)
//	              {
//	              }
//	        });
//	        builder.show();    
//		}
// 
//	}
	
	
	
}
