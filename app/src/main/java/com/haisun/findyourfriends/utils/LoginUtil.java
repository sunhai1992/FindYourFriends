package com.haisun.findyourfriends.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.models.UserIno;

import java.util.ArrayList;
import java.util.List;


public class LoginUtil {
	static String tableName = "UserInfo";
	public static AVObject userinfo = new AVObject();
	List<AVObject> avObjects;
	public static UserIno user = new UserIno();
	public static Context context;           //记录的是分享界面的context
	public static boolean flag = false;

	public LoginUtil() {
	}

	public LoginUtil(Context context) {
		this.context = context;
	}


	public boolean legal(Context context, String inputname, String inputpassword) {
		AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);

		query.whereEqualTo("username", inputname);
		query.whereEqualTo("password", inputpassword);

		try {
			avObjects = query.find();
			//Toast.makeText(getApplicationContext(), String.valueOf(avObjects.size()), 1).show();
			if (avObjects.size() == 0) {
				Utils.toast(context, "不存在此用户");
				return false;
			} else {
				userinfo = avObjects.get(0);
				if (userinfo == null) {
					Utils.toast(context, "caos");
				} else {
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
	 * 登陆后检查是否有人邀请自己,根据自己需要进行选择
	 *
	 * @param context
	 */
	private static List<String> singleinvitedids;
	private static List<String> groupinvitedids;
	private static String[] str;
	private static boolean[] defaultSelectedStatus;
	private static int singleinvitedlen = 0;
	private static int groupinvitedlen = 0;
	static int p = 0;

	public static void invitedSituation() {
		try {
			userinfo.refresh();
		} catch (AVException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String singleinvitedstr = userinfo.getString("singleinvitedstr");
		String groupinvitedstr = userinfo.getString("groupinvitedstr");
		AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		Log.i("zj", String.valueOf(p++));

		if (singleinvitedstr == null || singleinvitedstr.length() == 0) {
			singleinvitedlen = 0;
		} else {
			singleinvitedids = Utils.strParse(singleinvitedstr);
			singleinvitedlen = singleinvitedids.size();
		}
		if (groupinvitedstr == null || groupinvitedstr.length() == 0) {
			groupinvitedlen = 0;
		} else {
			groupinvitedids = Utils.strParse(groupinvitedstr);
			groupinvitedlen = groupinvitedids.size();
		}

		if (singleinvitedlen == 0 && groupinvitedlen == 0)
			return;
		str = new String[singleinvitedlen + groupinvitedlen];
		defaultSelectedStatus = new boolean[singleinvitedlen + groupinvitedlen];
		int cnt = 0;

		if (singleinvitedstr != null && singleinvitedstr.length() != 0) {  //single situation
			for (String singleid : singleinvitedids) //single situation
			{
				try {
					str[cnt] = query.get(singleid).getString("username") + "请求与你一起分享位置";
					cnt = cnt + 1;
					//Utils.toast(context, str[cnt-1]);
					Log.i("zj", String.valueOf(p++) + "llll");
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (groupinvitedstr != null && groupinvitedstr.length() != 0) {

			AVQuery<AVObject> querygroup = new AVQuery<AVObject>("Group");
			for (String groupid : groupinvitedids) {
				try {
					str[cnt] = querygroup.get(groupid).getString("groupowner") + "invited you to join the group";
					cnt = cnt + 1;
					//Utils.toast(context, str[cnt-1]);
					Log.i("zj", String.valueOf(p++) + "llll");
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("复选框");

		builder.setMultiChoiceItems(str, defaultSelectedStatus, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				defaultSelectedStatus[which] = isChecked;
			}
		});


		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				List<String> singlesharedids;
				List<String> groupsharedids;
				String singlesharedidsstr = userinfo.getString("singlesharedidsstr");
				String groupsharedidsstr = userinfo.getString("groupsharedidsstr");
				if (singlesharedidsstr == null || singlesharedidsstr.length() == 0) {
					singlesharedids = new ArrayList<String>();
				} else {
					singlesharedids = Utils.strParse(singlesharedidsstr);
				}

				if (groupsharedidsstr == null || groupsharedidsstr.length() == 0) {
					groupsharedids = new ArrayList<String>();
				} else {
					groupsharedids = Utils.strParse(groupsharedidsstr);
				}

				AVQuery<AVObject> queryy = new AVQuery<AVObject>("UserInfo");
				for (int i = 0; i < defaultSelectedStatus.length; i++) {
					//进行值的更新需要重绘listview
					if (defaultSelectedStatus[i] && i < singleinvitedlen) {
						if (flag == false) {
							flag = true;
						}
						singlesharedids.add(singleinvitedids.get(i));
						try {
							/**
							 * 给每一个邀请方增加记录
							 */

							AVObject userinfo1 = new AVObject();
							userinfo1 = queryy.get(singleinvitedids.get(i));
							String singeid = userinfo1.getString("singlesharedidsstr");//被邀请者已经有的好友列表
							List<String> singleids;
							if (singeid == null || singeid.length() == 0) {
								singleids = new ArrayList<String>();
							} else {
								singleids = Utils.strParse(singeid);
							}
							singleids.add(userinfo.getObjectId());//all invited people should add inviting person

							userinfo1.put("singlesharedidsstr", Utils.strCombine(singleids));
							userinfo1.save();
							userinfo1.refresh();
						} catch (AVException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (defaultSelectedStatus[i]) { //group
						groupsharedids.add(groupinvitedids.get(i - singleinvitedlen));
						AVQuery<AVObject> querygroup = new AVQuery<AVObject>("Group");
						try {
							AVObject grp = querygroup.get(groupinvitedids.get(i - singleinvitedlen));
							grp.refresh();
							String newid = userinfo.getObjectId();
							String memberidsstr = grp.getString("memberids");
							List<String> memberids;
							if (memberidsstr == null || memberidsstr.length() == 0) {
								memberids = new ArrayList<String>();
							} else {
								memberids = Utils.strParse(memberidsstr);
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
				//为自己添加新的聊天记录,同时清空被邀请一栏
				userinfo.put("singlesharedidsstr", Utils.strCombine(singlesharedids));
				userinfo.put("groupsharedidsstr", Utils.strCombine(groupsharedids));
				userinfo.remove("singleinvitedstr");
				userinfo.remove("groupinvitedstr");
				try {
					userinfo.save();
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}


}