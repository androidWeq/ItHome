package com.hkd.ithome.activities;

import com.example.ithome.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ItQuan_WebViewActivity extends Activity {
	WebView mwebView;
	String link;
	int index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.news_webview);
		super.onCreate(savedInstanceState);
		mwebView = (WebView) findViewById(R.id.webView);
		Intent intent_getlink = this.getIntent();
		link = intent_getlink.getStringExtra("link");
		index=intent_getlink.getIntExtra("index", 0);
		mwebView.loadUrl(link);
		// 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		mwebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
		// 启用支持javascript脚本
		WebSettings settings = mwebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setAppCacheEnabled(true);// 开启缓存
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存

	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Intent intent=new Intent(this, KejiChatActivity.class);
		intent.putExtra("index", index);
		setResult(112, intent);
		finish();
		
	}
}
