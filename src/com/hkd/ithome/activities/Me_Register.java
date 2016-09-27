package com.hkd.ithome.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.hkd.ithome.bean.UserInfoModle;
import com.hkd.ithome.tools.MD5;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class Me_Register extends Activity {

	@ViewInject(R.id.User_Register)
	EditText user_register;
	@ViewInject(R.id.Password_Register)
	EditText password_register;
	@ViewInject(R.id.Password_Repet)
	EditText password_repet;
	@ViewInject(R.id.Register_Me)
	Button register_me;
	String value_user, value_pwd, value_repet;
	HttpUtils http;
	boolean isSame = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_login_register);
		ViewUtils.inject(this);
		http = new HttpUtils();
		user_register.addTextChangedListener(new TextWatcher() {

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
		password_register.addTextChangedListener(new TextWatcher() {

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
		password_repet.addTextChangedListener(new TextWatcher() {

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

	@OnClick({ R.id.Register_Me })
	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.Register_Me:
			if (!value_pwd.equals(value_repet)) {
				password_repet.setError("两次输入的密码不一致!");
				return;
			}
			// selectSameUser();
			addUserInfo();
			break;

		default:
			break;
		}
	}

	// public void selectSameUser(){
	// System.out.println("----select");
	// String urls =
	// "http://192.168.1.119:8080/ITHome/userInfo_selectUserInfo?";
	// RequestParams params = new RequestParams();
	// UserInfoModle modle = new UserInfoModle();
	// modle.setUsername(value_user);
	// String value = new Gson().toJson(modle);
	// params.addQueryStringParameter("params", value);
	// http.send(HttpMethod.POST, urls, params, new RequestCallBack<String>() {
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> responseInfo) {
	// System.out.println("---------on");
	// if(responseInfo.result.length()>0){
	// System.out.println("---true");
	// user_register.setError("该用户名已存在!");
	// isSame=true;
	// }else{
	// System.out.println("---false");
	// isSame=false;
	// }
	// if(isSame==false){
	// addUserInfo();
	// }
	// }
	// @Override
	// public void onFailure(HttpException error, String msg) {
	// }
	// });
	//
	// }

	public void loginEnable() {
		value_user = user_register.getText().toString();
		value_pwd = password_register.getText().toString();
		value_repet = password_repet.getText().toString();

		if (value_user.length() != 0 && value_pwd.length() != 0
				&& value_repet.length() != 0) {
			register_me.setEnabled(true);

		} else {
			register_me.setEnabled(false);

		}
	}

	public void addUserInfo() {
		String md5pwd = MD5.toMD5(value_repet);
		String url = "http://192.168.1.112:8080/ITHome_DB/userInfo_addUserInfo?";
		RequestParams params = new RequestParams();
		UserInfoModle modle = new UserInfoModle();
		modle.setUsername(value_user);
		modle.setPassword(md5pwd);
		String value = new Gson().toJson(modle);
		
		// params.addQueryStringParameter("params", "{\"name\":\"+va+"\"}");
		params.addQueryStringParameter("params", value);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (responseInfo.result.equals("0")) {
					Toast.makeText(Me_Register.this, "注册成功!",
							Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(Me_Register.this, "用户名已存在!",
							Toast.LENGTH_SHORT).show();
				}

				// new Gson().fromJson(responseInfo.result, **.class);
				// **代表你要吧json数据转换成什么类型, modle,HasMap,List类型等等
			}

			@Override
			public void onFailure(HttpException error, String msg) {

				Toast.makeText(Me_Register.this, "注册失败!", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

}
