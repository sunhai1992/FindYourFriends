package com.haisun.findyourfriends.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.haisun.findyourfriends.utils.Exit;
import com.haisun.findyourfriends.R;
import com.haisun.findyourfriends.utils.RegisterUtils;
import com.haisun.findyourfriends.models.UserIno;
import com.haisun.findyourfriends.utils.Utils;

public class RegisterActivity extends Activity {

	EditText inputusername;
	EditText inputphonenumber;
	EditText inputpassword;
	EditText inputpasswordensure;
	Button register;
	EditText authcode;
    String code;
     
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		inputusername=(EditText) findViewById(R.id.inputusername);	
	 	inputphonenumber=(EditText) findViewById(R.id.inputphonenumber);
	 	inputpassword=(EditText) findViewById(R.id.inputpassword);
	 	inputpasswordensure=(EditText) findViewById(R.id.inputpasswordensure);
	 	authcode=(EditText)findViewById(R.id.authcode);
	 	
	 	Exit.add(this);
	}

	public void returnback(View v)
	{
		Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
		RegisterActivity.this.startActivity(intent);
	}

	public void getAuthcode(View v)
	{
		String phonenumber = inputphonenumber.getText().toString();
		if(Utils.isIllegalPhonenumber(phonenumber))
	    {
			 RegisterUtils.sendCode(getApplicationContext(), phonenumber);
	    }
		else {
			Utils.toast(getApplicationContext(), "手机号格式不对");
		}
	}
	


	public void verifyAuthcode() {
		 final String code = authcode.getText().toString();
	     RegisterUtils.verifyCode(code,inputphonenumber.getText().toString());
	}
	 

	public void returnToMain(View v)
	{
		String username=inputusername.getText().toString();
		String password=inputpassword.getText().toString();
		String passwordensure=inputpasswordensure.getText().toString();
		String phonenumber=inputphonenumber.getText().toString();
		
		if(password.equals(passwordensure))
		{
			if(RegisterUtils.isRegistered(phonenumber)==true)
			{
				verifyAuthcode();
				if(RegisterUtils.veryfyflag==true)
				{
					UserIno userIno=new UserIno(username,phonenumber,password,0.0,0.0,false);
					RegisterUtils.register(userIno);
					Intent intent=new Intent(RegisterActivity.this,MainActivity.class);

					RegisterActivity.this.startActivity(intent);
				}
				else {
					Utils.toast(getApplicationContext(), "验证码输入出错");
				}
			}
			else {
				Utils.toast(getApplicationContext(), "此手机号已经被注册");
			}
		}
		else {
			Utils.toast(getApplicationContext(), "密码不一致");
		}	 
	}		
}

 
