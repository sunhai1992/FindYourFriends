package com.haisun.findyourfriends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVOSCloud;
import com.haisun.findyourfriends.utils.Exit;
import com.haisun.findyourfriends.utils.LoginUtil;
import com.haisun.findyourfriends.R;
import com.haisun.findyourfriends.models.UserIno;

public class MainActivity extends ActionBarActivity {

	private UserIno user=new UserIno();
	EditText inputusername;
	EditText inputpassword;
	LoginUtil logutil=new LoginUtil();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AVOSCloud.initialize(this, "uplup1334zfe2awcldee4affug24eypki3ro0t8a3rdo5xrh", "5noonhaqp1e0f17kvpgj9uyt7d70vj3yxq0muksfvqo1gkhz");
	    inputusername=(EditText) findViewById(R.id.et_username);
	    inputpassword=(EditText) findViewById(R.id.et_password);	    
	    
	    
	    Exit.add(this);
	}

	/**
	 * ��ת����½֮��Ľ���
	 * @param v
	 */
	public void login(View v){
		    String username=inputusername.getText().toString();
		    String password=inputpassword.getText().toString();
		    if(logutil.legal(getApplicationContext(),username, password)==true)
		    {
		    	 user=logutil.user;
		    	 Intent intent=new Intent(MainActivity.this,LoginActivity.class);
			     Bundle data = new Bundle();
			     data.putSerializable("user", user); 
		         intent.putExtras(data);	       
		         MainActivity.this.startActivity(intent);   
		    }
	}
	
//	List<AVObject> avObjects;
//	public boolean legal(String inputname,String inputpassword){
//		String tableName = "UserInfo";
//		AVObject userinfo = new AVObject(tableName);
//	 	AVQuery<AVObject> query = new AVQuery<AVObject>(tableName);
//	 	
//	 	query.whereEqualTo("username", inputname);
//	    query.whereEqualTo("password", inputpassword);
//
//	 	try {
//		    avObjects = query.find();
//			//Toast.makeText(getApplicationContext(), String.valueOf(avObjects.size()), 1).show();
//			if(avObjects.size()==0)
//			{
//				Toast.makeText(getApplicationContext(), "�����ڴ��û�", 1).show();
//				return false;
//			}
//			else { 
//			 	 userinfo=avObjects.get(0);
//			 	 if(userinfo==null){
//			 		Toast.makeText(getApplicationContext(), "caos", 1).show();
//			 	 }else{
//				 	 user.setPassword(userinfo.getString("password").toString());
//				 	 user.setUsername(userinfo.getString("username"));
//				 	 user.setLatitude(userinfo.getDouble("latitude"));
//				 	 user.setLongtitude(userinfo.getDouble("longtitude"));
//				 	 user.setPhonenumber(userinfo.getString("phonenumber"));
//				 	 user.setOn(true);
//			 	 } 
//			}
//		} catch (AVException e) { 
//			e.printStackTrace();
//			return false;
//		}
//	 	return true;
//	}
	
	/**
	 * δע�������ע��������ע��
	 * @param v
	 */
	public void register(View v){
		Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
	    Bundle data = new Bundle();
	    data.putString("name","haha"); 
        intent.putExtras(data);	       
        MainActivity.this.startActivity(intent);   
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
