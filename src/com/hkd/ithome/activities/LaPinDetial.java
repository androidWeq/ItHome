package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.BitmapUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LaPinDetial extends Activity implements OnClickListener {
	TextView title, time, youhui, describe, toBuy;
	ImageView img;
	BitmapUtils bitmapUtils;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_goods_detial);
		init();
		getGoodInfo();

	}

	/**
	 * 获得传递的信息,给组件绑定信息
	 */
	private void getGoodInfo() {
		Intent intent = getIntent();
		GoodInfo info = (GoodInfo) intent.getSerializableExtra("goodInfo");
		title.setText(info.getTitle());
		time.setText(info.getTime());
		youhui.setText("下单立减" + info.getYouhui() + "元");
		describe.setText(info.getDescribe());
		bitmapUtils.display(img, NoChange.WEB_SERVERS_ADDRESS + info.getImg());

	}

	/**
	 * 绑定组件
	 */
	private void init() {
		bitmapUtils = new BitmapUtils(this);
		title = (TextView) findViewById(R.id.lapin_detial_title);
		time = (TextView) findViewById(R.id.lapin_detial_time);
		youhui = (TextView) findViewById(R.id.lapin_detial_youhui);
		describe = (TextView) findViewById(R.id.lapin_detial_describe);
		toBuy = (TextView) findViewById(R.id.lapin_detial_buy);
		img = (ImageView) findViewById(R.id.lapin_detaia_img);
        toBuy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(LaPinDetial.this, LaPinOrder.class);
		startActivity(intent);

	}

}
