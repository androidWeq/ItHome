package com.hkd.ithome.activities;

import java.security.MessageDigest;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.UserInfoModle;
import com.hkd.ithome.tools.MD5;
import com.hkd.ithome.tools.MeTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Me_Login extends Activity {

	@ViewInject(R.id.Password_My)
	EditText pad_my;
	@ViewInject(R.id.Login_Me)
	Button login_me;
	@ViewInject(R.id.User_My)
	EditText user_my;
	@ViewInject(R.id.No_Account)
	TextView no_account;
	String value_pad, value_user;
    HttpUtils http;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_login);
		ViewUtils.inject(this);
        http=new HttpUtils();
		user_my.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				loginEnable();

			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		pad_my.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				loginEnable();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	@OnClick({ R.id.No_Account,R.id.Login_Me })
	public void onclick(View v) {

		switch (v.getId()) {
		case R.id.No_Account:
			Intent intent=new Intent(Me_Login.this,Me_Register.class);
			startActivity(intent);
         break;
		case R.id.Login_Me:

        String url=MeTool.SELECTLogin;
    	
        RequestParams params = new RequestParams();
		UserInfoModle modle=new UserInfoModle();
		String md5pwd = MD5.toMD5(value_pad);
		modle.setUsername(value_user);
		modle.setPassword(md5pwd);
		String value=new Gson().toJson(modle);
		
	    params.addQueryStringParameter("params", value);
		http.send(HttpMethod.POST, url,params, new RequestCallBack<String>() {

			public void onSuccess(ResponseInfo<String> responseInfo) {
			
				
				if(responseInfo.result.equals("0")){
					Toast.makeText(Me_Login.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
					
				}else{
					Intent intent=new Intent(Me_Login.this,Me_login_success.class);
					AppApplication.getApp().setUsername(value_user);
//					intent.putExtra("username", value_user);
					startActivity(intent);
				    finish();
				}
				
	    	}

		@Override
			public void onFailure(HttpException error, String msg) {
				
			Toast.makeText(Me_Login.this, "登录失败!", Toast.LENGTH_SHORT)
			.show();	
			}
		 });	

         break;
         
		default:
			break;
		}
	}

	public void loginEnable() {
		value_pad = pad_my.getText().toString();
		value_user = user_my.getText().toString();

		if (value_user.length() != 0 && value_pad.length() != 0) {
			login_me.setEnabled(true);

		} else {
			login_me.setEnabled(false);

		}
	}

	
	
}
