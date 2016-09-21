package com.hkd.ithome.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.example.ithome.R;
import com.hkd.ithome.adapter.ItQuan_listAdapter;
import com.hkd.ithome.fragment.ITcircleFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class KejiChatActivity extends Activity implements IXListViewListener,OnClickListener{//implements 
	ArrayList<HashMap<String, Object>> listdata;
	HashMap<String, Object> map;
	ItQuan_listAdapter adapterList;
//	RelativeLayout headLayout;//listView头部布局
	@ViewInject(R.id.kejichangtan_top_img)
	ImageView kejichangtan_top_img;
	@ViewInject(R.id.kejichangtan_xListView)
	XListView myList;
//	@ViewInject(R.id.kejichangtan_listHead_rg)
	RadioGroup rg;
	
	Handler handler;
   @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.itquan_kejichangtan);
	ViewUtils.inject(this);
	
	kejichangtan_top_img.setOnClickListener(this);
//	
	//在setadapter之前添加头部
	RelativeLayout headerViewLayout=(RelativeLayout) LayoutInflater.from(this).inflate(R.layout.itquan_kejichangtan_listview_head, null);
	myList.addHeaderView(headerViewLayout);
	rg=(RadioGroup) headerViewLayout.findViewById(R.id.kejichangtan_listHead_rg);
	init();
	getListItem();
	adapterList=new ItQuan_listAdapter(this, listdata);
	myList.setAdapter(adapterList);
	
rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			switch (arg1) {
			case R.id.kejichangtan_listHead_rb_hotTie:
				Toast.makeText(KejiChatActivity.this, "热帖", Toast.LENGTH_SHORT).show();
				break;
			case R.id.kejichangtan_listHead_rb_newRespon:
				Toast.makeText(KejiChatActivity.this, "最新回复", Toast.LENGTH_SHORT).show();
				break;
			case R.id.kejichangtan_listHead_rb_newFaBiao:
				Toast.makeText(KejiChatActivity.this, "最新发表", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	});
	
}

   public void init(){
	   myList.setPullLoadEnable(false);
	   myList.setPullRefreshEnable(true);
	   myList.setXListViewListener(this);
   }
   
   
   public void getListItem() {

		listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			map = new HashMap<String, Object>();
			map.put("img", R.drawable.ic_launcher);
			map.put("type", "[总置顶]");
			map.put("title", "֧地铁上两个百合");
			map.put("author", "独悠");
			map.put("date", "一周前");
			map.put("author1", "每次都为改昵称烦恼");
			map.put("date1", "5天前");
			map.put("phone", "iOS圈");
			// map.put("img_scan",R.drawable.quan_hit);//���
			map.put("scan", "1266");
			// map.put("img_response",R.drawable.quan_comment);//�ظ�
			map.put("response", "126");
			listdata.add(map);

		}

	}
@Override
public void onRefresh() {
	// TODO Auto-generated method stub
	handler=new Handler();
	handler.postDelayed(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			adapterList.notifyDataSetChanged();
			load();
			
		}

		private void load() {
			// 获取系统当前时间
			SimpleDateFormat time=new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");
			myList.stopRefresh();
			myList.stopLoadMore();
			myList.setRefreshTime(time.format(new Date()));
			
		}
	}, 2000);
	
}
@Override
public void onLoadMore() {
	// TODO Auto-generated method stub
	
}
@Override
public void onClick(View arg0) {
	// TODO Auto-generated method stub
	switch (arg0.getId()) {
	case R.id.kejichangtan_top_img:
		System.out.println("------------点击返回");
	//	Intent intent=new Intent(KejiChatActivity.this, ITcircleFragment.class);
//		intent.putExtra("flag", 1);
//		setResult(10);
	//	startActivity(intent);
		finish();
		Toast.makeText(KejiChatActivity.this, "点击返回", 1000).show();
		break;

	default:
		break;
	}
	
}
	
   
}
