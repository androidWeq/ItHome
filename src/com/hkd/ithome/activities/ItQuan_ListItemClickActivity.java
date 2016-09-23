package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ItQuan_ListItemClickActivity extends Activity{
	@ViewInject(R.id.itquan_listitem_click_Visibile_tvTop)
	TextView tv_top;
	@ViewInject(R.id.itquan_listitem_click_Visibile_tvScan)
	TextView tv_top_Scan;
	@ViewInject(R.id.itquan_listitem_click_Visibile_tvResponse)
	TextView tv_top_Response;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_listitem_click);
		ViewUtils.inject(this);
		Intent text=getIntent();
		tv_top.setText(text.getStringExtra("title"));
		tv_top_Scan.setText(text.getIntExtra("scanner", 0)+"");
		tv_top_Response.setText(text.getIntExtra("response", 0)+"");
	}

}
