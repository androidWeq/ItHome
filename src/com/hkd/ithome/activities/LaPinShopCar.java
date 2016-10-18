package com.hkd.ithome.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkd.ithome.adapter.ShopCarAdapter;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.bean.ShopCarInfo;
import com.hkd.ithome.interfaces.OnUpdateText;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class LaPinShopCar  extends Activity implements OnUpdateText,OnClickListener{
	ListView listView;
	ShopCarAdapter adapter;
	HttpUtils httpUtils;
	List<GoodInfo> datas;
	TextView totalPrice,topay;
	CheckBox payall;
	ArrayList<ShopCarInfo> shopcarDatas;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_shopcar);
		init();
		setShopCar(AppApplication.getApp().getUsername());
	}


	private void init() {
		listView=(ListView) findViewById(R.id.shopcar_listview);
		totalPrice=(TextView) findViewById(R.id.shopcar_allprice);
		payall=(CheckBox) findViewById(R.id.shopcar_payall);
		topay=(TextView) findViewById(R.id.shopcar_zhifu);
		topay.setOnClickListener(this);
		payall.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					adapter.initCheckBox(true);
					adapter.notifyDataSetChanged();
				}
				
			}
		});
	}
	
	public void setShopCar(String username){
		httpUtils=new HttpUtils();
		RequestParams params=new RequestParams();
		params.addQueryStringParameter("params","{\"username\":\"" + username
				+ "\"}");
		httpUtils.send(HttpMethod.POST,NoChange.SELECT_SHOP_CAR,params,new RequestCallBack<String>() {

			
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson =new Gson();
				datas=gson.fromJson(responseInfo.result,new TypeToken<List<GoodInfo>>(){}.getType());
				//System.out.println(datas.size()+"__________");
				adapter=ShopCarAdapter.getAdapter(LaPinShopCar.this,datas);
				adapter.setOnUpdateText(LaPinShopCar.this);
				listView.setAdapter(adapter);
			}

			
			public void onFailure(HttpException error, String msg) {
				
				System.out.println("___查询购物车网络链接失败");
			}
		});
	}


	
	public void updateText(String allPrice) {
		//根据adapter的item勾选的来确定总商品价格
		totalPrice.setText(allPrice);
		
	}


	
	public void updateBoolean(boolean flag) {
		//依据adapter 的item的checkbox是否全选来设定全选按钮的状态
		payall.setChecked(flag);
	}


	
	public void onClick(View v) {
		shopcarDatas=new ArrayList<ShopCarInfo>();
		ShopCarInfo info;
		HashMap<Integer,Boolean> hs=adapter.getIsSelected();
		for(int i=0;i<hs.size();i++){
			if(hs.get(i)){
				info=new ShopCarInfo();
				info.setOrderId(datas.get(i).getOrderId());
				info.setIsPayed(0);
				info.setUsername(AppApplication.getApp().getUsername());
				shopcarDatas.add(info);
			}
		}
		System.out.println(shopcarDatas.size()+"_______");
		
		
		
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter.setAllprice(0);
		adapter.setIsTrue(0);
	}

}
