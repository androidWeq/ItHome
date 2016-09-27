package com.hkd.ithome.activities;

import com.example.ithome.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Me_Gold_Shop extends Activity {

	WebView mWebView;
    Button back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_gold_shop);
		back=(Button) findViewById(R.id.gold_back);
		mWebView=(WebView) findViewById(R.id.gold_web);
		String url = "https://my.ruanmei.com/shop/shopindex.aspx";
		mWebView.loadUrl(url);
		// 设置从当前浏览器打开新链接
		mWebView.setWebViewClient(new WebViewClient());
		// 设置JavaScript支持
		WebSettings set = mWebView.getSettings();
		set.setJavaScriptEnabled(true);

		set.setAppCacheEnabled(true);// 开启缓冲
		set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				finish();
			}
		});
	}

}
