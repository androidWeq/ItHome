package com.hkd.ithome.activities;

import java.util.List;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hkd.ithome.adapter.EditAddressListAdapter;
import com.hkd.ithome.adapter.ShowAddressListAdapter;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.AddressInfo;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EditAddressList extends Activity implements OnClickListener{
	ImageView back;
	TextView addNewAddress;
	ListView listview;
	EditAddressListAdapter adapter;
	List<AddressInfo> datas;
	HttpUtils httpUtils;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_edit_address_listview);
		init();
		
	}
	private void init() {
		httpUtils=new HttpUtils();
		back=(ImageView) findViewById(R.id.lapin_edit_address_list_back);
		addNewAddress=(TextView) findViewById(R.id.lapin_edit_address_list_addNewAddress);
		listview=(ListView) findViewById(R.id.lapin_edit_address_list_listview);
		back.setOnClickListener(this);
		addNewAddress.setOnClickListener(this);
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		getAddressInfo();
	}
	
	/**
	 * 从服务端获取当前用户的所有地址信息
	 */
	public void getAddressInfo(){
		RequestParams params=new RequestParams();
		String username=AppApplication.getApp().getUsername();
		params.addQueryStringParameter("params", "{\"username\":\"" + username
				+ "\"}");
		httpUtils.send(HttpMethod.POST,NoChange.SELECT_ADDRESSLIST,params,new RequestCallBack<String>() {
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson =new Gson();
				datas=gson.fromJson(responseInfo.result,new TypeToken<List<AddressInfo>>(){}.getType());
				adapter=new EditAddressListAdapter(datas,EditAddressList.this);
				listview.setAdapter(adapter);
			}

			public void onFailure(HttpException error, String msg) {
				
				
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lapin_edit_address_list_back:
			finish();
			break;
		case R.id.lapin_edit_address_list_addNewAddress:
			Intent intent=new Intent(EditAddressList.this,LaPinAddressAdd.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		
	}
	
	

}
