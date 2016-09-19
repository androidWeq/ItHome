package com.hkd.ithome.activities;

import java.util.ArrayList;
import java.util.HashMap;

import me.maxwin.view.XListView;

import com.example.ithome.R;
import com.hkd.ithome.adapter.ItQuan_listAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class KejiChatActivity extends Activity{
//	ArrayList<HashMap<String, Object>> listdata;
//	HashMap<String, Object> map;
//	@ViewInject(R.id.kejichangtan_xListView)
//	XListView myList;
//	ItQuan_listAdapter adapterList;
//	LinearLayout headLayout;//listView头部布局
   @Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.itquan_kejichangtan);
//	ViewUtils.inject(this);
//	
//	//在setadapter之前添加头部
//	LinearLayout headerViewLayout=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.xlistview_header_weq, null);
//	myList.addHeaderView(headerViewLayout);
////	headLayout=(LinearLayout) headerViewLayout.findViewById(R.id.listView_headLayout);
//	
//	
//	
//	
//	adapterList=new ItQuan_listAdapter(this, listdata);
//	myList.setAdapter(adapterList);
	
}
   
   
   
   
//   public void getListItem() {
//
//		listdata = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < 10; i++) {
//			map = new HashMap<String, Object>();
//			map.put("img", R.drawable.ic_launcher);
//			map.put("type", "[总置顶]");
//			map.put("title", "֧地铁上两个百合");
//			map.put("author", "独悠");
//			map.put("date", "一周前");
//			map.put("author1", "每次都为改昵称烦恼");
//			map.put("date1", "5天前");
//			map.put("phone", "wind10手机圈");
//			// map.put("img_scan",R.drawable.quan_hit);//���
//			map.put("scan", "1266");
//			// map.put("img_response",R.drawable.quan_comment);//�ظ�
//			map.put("response", "126");
//			listdata.add(map);
//
//		}
//
//	}
	
   
}
