package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Me_Login extends Activity {

	@ViewInject(R.id.Password_My)
	EditText pad_my;
	@ViewInject(R.id.Login_Me)
	Button login_me;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_login);
		ViewUtils.inject(this);

		pad_my.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (pad_my.getText().toString().length()==0) {
					login_me.setEnabled(false);
				} else {
					login_me.setEnabled(true);
				}
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

	@OnClick({ })
	public void onclick(View v) {

		switch (v.getId()) {


		default:
			break;
		}
	}
}
