package com.hkd.ithome.activities;

import java.util.List;

import com.example.ithome.R;
import com.hkd.ithome.adapter.EditAddressListAdapter;
import com.hkd.ithome.bean.AddressInfo;
import com.lidroid.xutils.HttpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EditAddressList extends Activity {
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
	}

}
