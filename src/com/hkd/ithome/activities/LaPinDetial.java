package com.hkd.ithome.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.ithome.R;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.db.DBUtil;
import com.hkd.ithome.db.GoodsOrderDBUtil;
import com.hkd.ithome.db.GoodsOrderSQLHelper;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LaPinDetial extends Activity implements OnClickListener {
	TextView title, time, youhui, describe, toBuy, toShopCar;
	ImageView img;
	BitmapUtils bitmapUtils;
	GoodInfo info;
	int goodsId; // 商品id
	double untiPrice;// 商品单价
	HttpUtils httpUtils;

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
		info = (GoodInfo) intent.getSerializableExtra("goodInfo");
		goodsId = info.getId();
		untiPrice = info.getPrice();
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
		toShopCar = (TextView) findViewById(R.id.lapin_detial_shopcar);
		toBuy.setOnClickListener(this);
		toShopCar.setOnClickListener(this);
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.lapin_detial_buy:
			Intent intent;
			if (AppApplication.getApp().getUsername() == null) {
				// 未登录跳转到登录界面
				intent = new Intent(LaPinDetial.this, Me_Login.class);
			} else {
				// 已经登录直接跳转到详情页面
				intent = new Intent(LaPinDetial.this, LaPinOrder.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("goodInfo", info);
				intent.putExtras(bundle);
			}
            startActivity(intent);
			break;
		case R.id.lapin_detial_shopcar:
			String username = null;
			username = AppApplication.getApp().getUsername();
			if (username == null) {
				// 未登录跳转到登录界面
				Intent intent2 = new Intent(LaPinDetial.this, Me_Login.class);
				startActivity(intent2);
			} else {
				httpUtils=new HttpUtils();
				RequestParams params=new RequestParams();
				params.addQueryStringParameter("params","{\"goodId\":\"" + goodsId
				+ "\",\"username\":\"" + username + "\"}");
				httpUtils.send(HttpMethod.POST,NoChange.ADD_SHOP_CAR,params,new RequestCallBack<String>() {

					
					public void onSuccess(ResponseInfo<String> responseInfo) {
						
						if ("添加购物车成功".equals(responseInfo.result)) {
							Toast.makeText(LaPinDetial.this,"添加购物车成功",1).show();
						}else{
							System.out.println("________ 添加购物车失败");
						}
					}

					
					public void onFailure(HttpException error, String msg) {
						
						System.out.println("______添加购物车网络失败");
					}
				});
			}

			break;

		default:
			break;
		}

	}

}
