package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Me_Theme extends Activity {

	@ViewInject(R.id.theme_back)
	Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_my);
		ViewUtils.inject(this);
	}

	@OnClick({ R.id.theme_back })
	public void onclick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.theme_back:
			finish();
			break;

		default:
			break;
		}
	}
}
