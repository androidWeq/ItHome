package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.fragment.MineFragment;
import com.hkd.ithome.interfaces.OnUpdateText;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Me_login_success extends Activity{
	
	@ViewInject(R.id.username_success)
	TextView u_success;
	@ViewInject(R.id.Head_pic)
	ImageView head_pic;
	@ViewInject(R.id.change_pwd)
	Button change_pwd;
	@ViewInject(R.id.exit_account)
	Button exit_account;
	String username;
	OnUpdateText onUpdateText;
	private static Me_login_success mls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_login_success);
		ViewUtils.inject(this);
	    u_success.setText(AppApplication.getApp().getUsername());
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
//           Intent intent=new Intent(Me_login_success.this,MineFragment.class);
//           startActivity(intent);
//         intent.putExtra("username", username);
//         setResult(100, intent);
           finish();
           return true;
		}
		return super.onKeyDown(keyCode, event);//系统的返回键会直接返回主页面
	}
	
	@OnClick({R.id.exit_account,R.id.change_pwd})
	public void onclick(View v){
		switch (v.getId()) {
		case R.id.exit_account:
		AppApplication.getApp().setUsername(null);
		System.out.println("--suc"+AppApplication.getApp().getUsername());
//			Intent intent=new Intent(Me_login_success.this,Me_Login.class);
//			startActivity(intent);
			finish();
			break;
		case R.id.change_pwd:
			
			
			break;

		default:
			break;
		}
	}

	
	

}
