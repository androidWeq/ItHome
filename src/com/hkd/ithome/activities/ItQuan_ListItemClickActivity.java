package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ItQuan_ListItemClickActivity extends Activity{
	@ViewInject(R.id.webView)
	WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_listitem_click);
		ViewUtils.inject(this);
//		Intent intent=getIntent();
//		webView.loadUrl(intent.getStringExtra("web_path"));
		
		
	}

}
