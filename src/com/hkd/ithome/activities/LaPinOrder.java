package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.AddressInfo;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LaPinOrder extends Activity implements OnClickListener,TextWatcher{

	LinearLayout toChangeAddress,linearHidden;
	TextView name, phone, address, title, price, totalPrice, addNum, reduceNum,
			remark;
	EditText goodNum;
	ImageView goodImg;
	BitmapUtils bitmapUtils;
	HttpUtils httpUtils;
	GoodInfo goodInfo;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_detial_order);
		ViewUtils.inject(this);
		init();
		
	}
	

	
	protected void onStart() {
		
		super.onStart();
		getDefaultAddress();
	}

	/**
	 * 获得当前用户默认地址信息
	 */
	private void getDefaultAddress() {
		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		// 获取当前登录的用户的用户名
		String username = AppApplication.getApp().getUsername();
		params.addQueryStringParameter("params", "{\"username\":\"" + username
				+ "\"}");
		httpUtils.send(HttpMethod.POST, NoChange.SELECT_DEFAULT_ADDRESS,
				params, new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 网页返回的json 数据
						String info = responseInfo.result;
						System.out.println(info);
						Gson gson = new Gson();
						// 转换为AddressInfo对象
						AddressInfo addressInfo = gson.fromJson(info,
								AddressInfo.class);
						// System.out.println(addressInfo.getId()+"----"+addressInfo.getUsername()+"---"+addressInfo.getName());
						// 如果未查到默认地址那么geiId=0,进入到添加默认地址界面
						// 反之只要有默认地址那么geiId肯定不可能为0,则进入显示订单详情界面
						if (addressInfo.getId() == 0) {
							System.out.println("---没有默认地址,请添加");
							// 如果没有默认地址则进行popupWindow添加
							AlertDialog.Builder builder = new AlertDialog.Builder(
									LaPinOrder.this);
							builder.setTitle("提示");
							builder.setMessage("您还没有设置收货地址,请点击这里设置!");
							builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
									public void onClick(
												DialogInterface dialog,
												int which) {
											//点击取消直接返回到之前的界面
											finish();

										}
									});
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											//跳转到添加收货地址界面
											Intent intent = new Intent(
													LaPinOrder.this,
													LaPinAddressAdd.class);
											startActivity(intent);

										}
									});

							// 一定要记得创建和show
							builder.create().show();

						} else {
							//当前用户已经有默认地址
							linearHidden.setVisibility(View.VISIBLE);
							Intent intent=getIntent();
							goodInfo=(GoodInfo) intent.getSerializableExtra("goodInfo");
							title.setText(goodInfo.getTitle());
							price.setText("¥"+goodInfo.getPrice());
							bitmapUtils.display(goodImg,NoChange.SELECT_DEFAULT_ADDRESS+goodInfo.getImg());
							name.setText(addressInfo.getName());
							phone.setText(addressInfo.getPhone());
							address.setText(addressInfo.getAddress());
							goodNum.setText("1");
							
							
						}
					}

					public void onFailure(HttpException error, String msg) {

						System.out.println("获取默认地址失败");
					}
				});
	}

	/**
	 * 组件的绑定
	 */
	private void init() {
		name = (TextView) findViewById(R.id.lapin_order_name);
		address = (TextView) findViewById(R.id.lapin_order_address);
		phone = (TextView) findViewById(R.id.lapin_order_phone);
		title = (TextView) findViewById(R.id.lapin_order_tilte);
		price = (TextView) findViewById(R.id.lapin_order_price);
		totalPrice = (TextView) findViewById(R.id.lapin_order_totalPrice);
		addNum = (TextView) findViewById(R.id.lapin_order_add);
		reduceNum = (TextView) findViewById(R.id.lapin_order_reduce);
		remark = (TextView) findViewById(R.id.lapin_order_remark);
		goodNum=(EditText) findViewById(R.id.lapin_order_num);
		//点击弹出数字软键盘
		//goodNum.setInputType(EditorInfo.TYPE_CLASS_PHONE);
		goodImg = (ImageView) findViewById(R.id.lapin_order_img_good);
		toChangeAddress = (LinearLayout) findViewById(R.id.lapin_order_img_toaddress);
		linearHidden=(LinearLayout) findViewById(R.id.lapin_order_linear_hidden);
		bitmapUtils = new BitmapUtils(this);
		addNum.setOnClickListener(this);
		reduceNum.setOnClickListener(this);
		goodNum.addTextChangedListener(this);
	}


	
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.lapin_order_add:
			goodNum.setText((Integer.parseInt(goodNum.getText().toString())+1)+"");
			break;
        case R.id.lapin_order_reduce:
        	goodNum.setText((Integer.parseInt(goodNum.getText().toString())-1)+"");
			break;

		default:
			break;
		}
	}


	
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
		
	}


	/**
	 * textView的内容改变监控
	 */
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int num;
		//判断当删除所有数时,强制设置num=0
		if("".equals(goodNum.getText().toString()) || Integer.parseInt(goodNum.getText().toString())<0 ){
			num=0;
			goodNum.setText(0+"");
		}else{
			num=Integer.parseInt(goodNum.getText().toString());
		}
		
		totalPrice.setText("¥"+(num*goodInfo.getPrice()));
		
	}


	
	public void afterTextChanged(Editable s) {
		
		
	}


	

}
