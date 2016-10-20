package com.hkd.ithome.activities;

import com.dtr.zxing.activity.CaptureActivity;
import com.example.ithome.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SouSuoActivity extends Activity implements OnCheckedChangeListener,OnClickListener {
	@ViewInject(R.id.img_sao)
	ImageView img_sao;// 二维码扫描
	@ViewInject(R.id.img_soso)
	ImageView Image_sousuo;// 搜索
	RadioGroup rg;
	@ViewInject(R.id.search_history)
	TextView search_history;// 搜索历史
	@ViewInject(R.id.search_clear)
	TextView delete_searchHistory;// 清空记录
	@ViewInject(R.id.sosuorb_WenZhang)
	RadioButton sosuorb_WenZhang;
	@ViewInject(R.id.sosuorb_laPin)
	RadioButton sosuorb_laPin;
	@ViewInject(R.id.sosuorb_ItQuan)
	RadioButton sosuorb_ItQuan;

	private final static int ITQUAN = 3;
	private final static int ZIXUN = 1;
	private final static int LAPIN = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sosuo_img);
		ViewUtils.inject(this);
		init();
		Intent intent = getIntent();
		switch (intent.getIntExtra("name", 0)) {
		case ZIXUN:// 由资讯页面过来搜索
           
			break;
		case LAPIN:// 由辣品页面过来搜索
			break;
		case ITQUAN:// 由ItQuan页面过来搜索
			sosuorb_ItQuan.setChecked(true);
			break;

		default:
			break;
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		rg = (RadioGroup) findViewById(R.id.sosuo_rg);
		rg.setOnCheckedChangeListener(this);
		img_sao.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.sosuorb_WenZhang:
			

			break;
		case R.id.sosuorb_laPin:

			break;
		case R.id.sosuorb_ItQuan:

			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.img_sao:
			Intent intent = new Intent(SouSuoActivity.this, CaptureActivity.class);
			startActivityForResult(intent, 0);
			break;

		default:
			break;
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 12) {
//			content = data.getExtras().getString("result");
			String content=data.getStringExtra("context").toString();
//			tv_show.setText(content);
			
//			System.out.println("------------��תWeb");
			Intent intent2 = new Intent(SouSuoActivity.this,
					WebviewActivity.class);
			intent2.putExtra("link",content);
			startActivity(intent2);
		}
	}

}
