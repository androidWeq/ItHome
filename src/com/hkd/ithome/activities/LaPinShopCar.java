package com.hkd.ithome.activities;

import com.example.ithome.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class LaPinShopCar  extends Activity{
	ListView listView;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_shopcar);
		init();
	}


	private void init() {
		listView=(ListView) findViewById(R.id.shopcar_listview);
		
	}

}
