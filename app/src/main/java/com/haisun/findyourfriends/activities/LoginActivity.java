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

/**
 * ��½��d����
 * @author lenovo
 *
 */
public class LoginActivity extends Activity implements LocationSource,
AMapLocationListener{
	private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private TextView txt;
    private AVObject user;     //�û������ϵ���Ϣ
    private AVObject friend;    //����λ�õ����ѵ������ϵ���Ϣ
    private AVObject group; 
    private UserIno userinfo;   //��¼�û�����¼�û�����Ϣ��
    private EditText inputphonenumber;
    private String username;
    private String sharepersonname;
    private List<String> friendsname;
	 //���ڱ�������Ƿ�����
	private boolean firststart=false;  //��ʾ�Ƿ�Ϊ��һ�ο�ʼ��λ����ʱ��Ҫ������ӱ�ǲ���
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
		//��ʼ����ͼ����λ�Լ���λ��
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
	 * ��ȡ����ĺ��ѵ���Ϣ,����������ߣ������ñ��Ϊtrue
	 * @param v
	 */
	public void share(View v)
	{  
		Intent intent=new Intent(getApplicationContext(),ShareSituationActivity.class);
		startActivity(intent);
	}
	
	/**
	 * ���Լ�������״̬����Ϊ����
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
	 * �˳����Լ�������״̬����Ϊfalse����߳��˵�����
	 */
	public void exit(View v)
	{
	    user.put("on",false);
	    user.saveInBackground();
	    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
	    startActivity(intent);   
	}
	
	/**
	 * �Ե�ͼ��������
	 */
	private void SetupMap() {
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding));// ����С�����ͼ��
		myLocationStyle.strokeColor(Color.BLACK);// ����Բ�εı߿���ɫ
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));//����Բ�ε������ɫ
		// myLocationStyle.anchor(int,int)//����С�����ê��
		myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource((LocationSource) this);// ���ö�λ����
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// ����Ĭ�϶�λ��ť�Ƿ���ʾ
		aMap.setMyLocationEnabled(true);// ����Ϊtrue��ʾ��ʾ��λ�㲢�ɴ�����λ��false��ʾ���ض�λ�㲢���ɴ�����λ��Ĭ����false	
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
	
	/**
     * ����������д
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
 
    /**
     * ����������д
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }
     
    /**
	 * ֹͣ��λ
	 */
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}
    /**
     * ����������д
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
 
    /**
     * ����������д
     */
    @Override
    protected void onDestroy() {
    	super.onDestroy();
        mapView.onDestroy();
    }
	
    /**
	 * ��λ�ɹ���ص����������Ҳ��ϸ����Լ�������λ��
	 */
 
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
//			String str = ("��λ�ɹ�:(" + geoLng + "," + geoLat + ")"
//					+ "\n��    ��    :" + aLocation.getAccuracy() + "��"
//					+ "\n��λ��ʽ:" + aLocation.getProvider() + "\n��λʱ��:"
//					+ convertToTime(aLocation.getTime()) + "\n���б���:"
//					+ cityCode + "\nλ������:" + desc + "\nʡ:"
//					+ aLocation.getProvince() + "\n��:" + aLocation.getCity()
//					+ "\n��(��):" + aLocation.getDistrict() + "\n�������:" + aLocation
//					.getAdCode());
			AVGeoPoint locc=new AVGeoPoint(geoLat,geoLng);
			user.put("location", locc);
	        //user.saveInBackground();  
	        try {
				user.save();
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      //  Log.i("zj","zzzzz"+str);
	        if(Utils.friendon==1)
	        { 
//	        	try {
//				//	friend.refresh();
//				} catch (AVException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
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
	        	if(markers!=null){//Ⱥ���г�Ա�����
	        		for(AVObject member:members){
//	        			try {
//							member.refresh();//ˢ���Ա��ȡ������
//						} catch (AVException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
	        			
	        			if(firststarts[cnt]==true){ //����Ѿ����ϱ�ǩ��ɾ��������ӱ�ǩ,��Ϊ��Ҫ���ϸ���
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
		mListener.onLocationChanged(aLocation);// ��ʾϵͳС����	
	}
	
	/**
	 * ��ȡϵͳ��ǰʱ��
	 * @param time
	 * @return
	 */
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}
	
	/**
	 * ���λ
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2�汾��������������true��ʾ��϶�λ�а���gps��λ��false��ʾ�����綨λ��Ĭ����true Location
			 * API��λ����GPS�������϶�λ��ʽ����һ�������Ƕ�λprovider���ڶ�������ʱ�������2000���룬������������������λ���ף����ĸ������Ƕ�λ������
			 */
			mAMapLocationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000,10, this);
		}
	}	
}
