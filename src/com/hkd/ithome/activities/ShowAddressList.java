package com.hkd.ithome.activities;

import java.util.List;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowAddressList extends Activity implements OnItemClickListener,OnClickListener  {
	ListView listView;//listview
	ImageView back;  //返回图片
	TextView toEditAddress;//去往地址管理界面
	HttpUtils httpUtils;
    ShowAddressListAdapter adapter;
    List<AddressInfo> datas;
    private final int ADDRESSLIST2ORDER=2;
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_show_address_list);
		init();
	}
	private void init() {
		httpUtils=new HttpUtils();
		listView=(ListView) findViewById(R.id.lapin_address_list_listview);
		back=(ImageView) findViewById(R.id.lapin_address_list_back);
		toEditAddress=(TextView) findViewById(R.id.lapin_address_list_toEdit);
		listView.setOnItemClickListener(this);
		toEditAddress.setOnClickListener(this); 
		back.setOnClickListener(this);
	}
	
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
				adapter=new ShowAddressListAdapter(datas,ShowAddressList.this);
				listView.setAdapter(adapter);
			}

			public void onFailure(HttpException error, String msg) {
				
				
			}
		});
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AddressInfo info=datas.get(position);
		Intent intent=new Intent();
		Bundle bundle =new Bundle();
		bundle.putSerializable("addressInfo",info);
		intent.putExtras(bundle);
		setResult(ADDRESSLIST2ORDER,intent);
		AppApplication.getApp().setAddressIsDefault(false);
		finish();
		
	}
	@Override
	public void onClick(View v) {
		
		
		switch (v.getId()) {
		case R.id.lapin_address_list_toEdit:
			//跳转到编辑地址列表界面 
			Intent intent=new Intent(ShowAddressList.this,EditAddressList.class);
			startActivity(intent);
			break;
		case R.id.lapin_address_list_back:
			finish();
			break;

		default:
			break;
		}
		
	}

}
