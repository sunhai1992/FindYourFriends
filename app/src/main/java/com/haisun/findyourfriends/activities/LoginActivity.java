package com.haisun.findyourfriends.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.haisun.findyourfriends.utils.Exit;
import com.haisun.findyourfriends.utils.LoginUtil;
import com.haisun.findyourfriends.R;
import com.haisun.findyourfriends.utils.ShareUtil;
import com.haisun.findyourfriends.models.UserIno;
import com.haisun.findyourfriends.utils.Utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity implements LocationSource,
AMapLocationListener{
	private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private TextView txt;
    private AVObject user;     //用户在网上的信息
    private AVObject friend;    //分享位置的朋友的在网上的信息
    private AVObject group; 
    private UserIno userinfo;   //记录用户（登录用户的信息）
    private EditText inputphonenumber;
    private String username;
    private String sharepersonname;
    private List<String> friendsname;
	 //用于标记朋友是否在线
	private boolean firststart=false;  //表示是否为第一次开始定位，此时需要进行添加标记操作
	private boolean[] firststarts;
	private List<AVObject> members=new ArrayList<AVObject>();
//	private final Timer timer = new Timer();
//	private TimerTask task;
	Marker marker; 
	Marker[] markers;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//初始化地图，定位自己的位置
		mapView=(MapView)findViewById(R.id.twodmap);
		mapView.onCreate(savedInstanceState); 
		aMap=mapView.getMap();
		SetupMap(); 
		intialization();
		username= LoginUtil.userinfo.getString("username");
		Exit.add(this);
		
		if(Utils.friendon==1)
		{ 
			 friend= ShareUtil.shareperson;
			 sharepersonname=friend.getString("username");
		}
		else if(Utils.friendon==2)
		{
			group=ShareUtil.sharegroup;
			String memberidsstr=group.getString("memberids");
			List<String> memberids=Utils.strParse(memberidsstr);
			friendsname=new ArrayList<String>();
			AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
			if(memberids.size()!=1)
			{
				markers=new Marker[memberids.size()-1];
				firststarts=new boolean[memberids.size()-1];
				for(int i=0;i<memberids.size();i++)
					try {
						if(LoginUtil.userinfo.getObjectId().equals(memberids.get(i)))//��ӷǱ��˵ĳ�Աobjectid
						{
							continue;
						}
						AVObject tmp=query.get(memberids.get(i));
						members.add(tmp);
						friendsname.add(tmp.getString("username"));
					} catch (AVException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			else {
				markers=null;
				firststarts=null;
			}
		}
	}

	/**
	 * 获取分享的好友的信息,如果好友在线，则设置标记为true
	 * @param v
	 */
	public void share(View v)
	{  
		Intent intent=new Intent(getApplicationContext(),ShareSituationActivity.class);
		startActivity(intent);
	}

	/**
	 * 将自己的在线状态设置为在线
	 */
	public void intialization()
	{ 
		 user=LoginUtil.userinfo; 
		 if(user==null)
		 {
			 AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
		    query.whereEqualTo("phonenumber",userinfo.getPhonenumber());
		    try {
		        List<AVObject> avObjects = query.find();
				user= avObjects.get(0);
				user.put("on", true);
//				user.saveInBackground();
				user.save();
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    txt=(TextView)findViewById(R.id.txt); 
 		 }
	}


	/**
	 * 退出后将自己的在线状态设置为false，这边出了点问题
	 */
	public void exit(View v)
	{
	    user.put("on",false);
	    user.saveInBackground();
	    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
	    startActivity(intent);   
	}

	/**
	 * 对地图进行设置
	 */
	private void SetupMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);//设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));//设置圆形的填充颜色
		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource((LocationSource) this);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);//设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
 

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }
     

	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
 

    @Override
    protected void onDestroy() {
    	super.onDestroy();
        mapView.onDestroy();
    }
	

 
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			try {
				user.refresh();
			} catch (AVException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Double geoLat = aLocation.getLatitude();
			Double geoLng = aLocation.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = aLocation.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}

			AVGeoPoint locc=new AVGeoPoint(geoLat,geoLng);
			user.put("location", locc);
	        //user.saveInBackground();  
	        try {
				user.save();
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        if(Utils.friendon==1)
	        { 

	        	if(firststart==true){
	        		marker.remove();
	        	} 
	        	else {
	        		firststart=true;
				}
	        	 
	        	MarkerOptions markerOption=new MarkerOptions();
	        	AVGeoPoint loc= friend.getAVGeoPoint("location");
	        	//Log.i("zj", "zj"+loc.getLatitude()+"   "+loc.getLongitude());
	        	markerOption.position(new LatLng(loc.getLatitude(),loc.getLongitude()));
	    		markerOption.icon(com.amap.api.maps2d.model.BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
	    	    markerOption.title(sharepersonname);
	    		marker=aMap.addMarker(markerOption);
	    		marker.showInfoWindow();	     
	        }
	        else if(Utils.friendon==2)
	        {
	        	int cnt=0;
	        	if(markers!=null){
	        		for(AVObject member:members){
	        			
	        			if(firststarts[cnt]==true){ //如果已经插上标签则删除重新添加标签,因为需要不断更新
			        		markers[cnt].remove();
			        	} 
			        	else {
			        		firststarts[cnt]=true;
						}
	        			MarkerOptions markerOption=new MarkerOptions();
			        	AVGeoPoint loc= member.getAVGeoPoint("location");
			        	markerOption.position(new LatLng(loc.getLatitude(),loc.getLongitude()));
			    		markerOption.icon(com.amap.api.maps2d.model.BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
			    	    markerOption.title(friendsname.get(cnt));
			    		markers[cnt]=aMap.addMarker(markerOption);
			    		markers[cnt].showInfoWindow();
			    	    cnt=cnt+1;
	        		}     		
	        	}
	        }
		} 
		mListener.onLocationChanged(aLocation);
	}

	/**
	 * 获取系统当前时间
	 * @param time
	 * @return
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);

			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000,10, this);
		}
	}	
}
