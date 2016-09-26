package com.hkd.ithome.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hkd.ithome.adapter.ItQuan_listAdapter;
import com.hkd.ithome.bean.ItQuanBeen;
import com.hkd.ithome.tools.ItQuanTools;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class KejiChatActivity extends Activity implements IXListViewListener,
		OnClickListener, OnItemClickListener, OnCheckedChangeListener {// implements
	List<ItQuanBeen> listdata;
	HashMap<String, Object> map;
	ItQuan_listAdapter adapterList;
	@ViewInject(R.id.kejichangtan_top_img)
	ImageView kejichangtan_top_img;
	@ViewInject(R.id.kejichangtan_xListView)
	XListView myList;
	@ViewInject(R.id.kejichangtan_imgEdit)
	// 点击发表言论
	ImageView imgEdit;
	RadioGroup rg;
	HttpUtils httpUtils;
	Gson gson;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_kejichangtan);
		ViewUtils.inject(this);
		init();//数据初始化
		getListViewDatas();//获得listItem数据
	}
	/*
	 * 数据初始化
	 */
	private void init() {
		myList.setPullLoadEnable(false);// true:可以上拉加载数据 相反false不可以
		myList.setPullRefreshEnable(true);// true:可以下拉刷新数据 相反false不可以
		myList.setXListViewListener(this);
		myList.setOnItemClickListener(this);
		myList.setXListViewListener(this);
		kejichangtan_top_img.setOnClickListener(this);
		imgEdit.setOnClickListener(this);//发表言论的点击事件
	}
      /*
       * 给listView添加头部
       */
	public void initHead() {
		RelativeLayout headerViewLayout = (RelativeLayout) LayoutInflater.from(
				this).inflate(R.layout.itquan_kejichangtan_listview_head, null);
		myList.addHeaderView(headerViewLayout);
		rg = (RadioGroup) headerViewLayout
				.findViewById(R.id.kejichangtan_listHead_rg);
		rg.setOnCheckedChangeListener(this);
	}

	/**
	 * 从网络解析json填充到数据源
	 * 
	 * @param index
	 *            页码
	 */
	private void getListViewDatas() {
		// System.out.println("-------进入-getListViewDatas()-从网络解析json填充到数据源");
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST, ItQuanTools.SELECT_information,
				new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String info = responseInfo.result;
						// System.out.println(info+"--------");
						gson = new Gson();
						if (listdata == null) {
							listdata = new ArrayList<ItQuanBeen>();
							adapterList = new ItQuan_listAdapter(
									KejiChatActivity.this, listdata);

							getJsonData(info);
							initHead();
							myList.setAdapter(adapterList);
						} else {
							getJsonData(info);
							adapterList.notifyDataSetChanged();
						}
					}

					private void getJsonData(String info) {
						// TODO Auto-generated method stub
						JsonParser jsonParser = new JsonParser();
						JsonElement jsonElement = jsonParser.parse(info);// 将json字符串转换成JsonElement
						JsonArray jsonArray;
						try {
							jsonArray = jsonElement.getAsJsonArray();// 获得JsonArray对象
							Iterator it = jsonArray.iterator();// 循环
							while (it.hasNext()) {
								jsonElement = (JsonElement) it.next();// 提取JsonElement
								String json = jsonElement.toString();// JsonElement转换成String
								ItQuanBeen itQuanInformation = gson.fromJson(
										json, ItQuanBeen.class);// String转化成JavaBean
								listdata.add(itQuanInformation);// 加入Listdata
							}

							// System.out.println("----------listdata:"+listdata.size());
						} catch (Exception e) {
							System.out.println("获得数据为空");
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("-----获取网络数据失败");

					}
				});

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// adapterList.notifyDataSetChanged();
				load();

			}

			private void load() {
				// 获取系统当前时间
				SimpleDateFormat time = new SimpleDateFormat(
						"yyyy--MM--dd HH:mm:ss");
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
			finish();
			break;
		case R.id.kejichangtan_imgEdit:
			Intent intent_imgEdit=new Intent(this, ItQuan_KeJiChat_ClickImgEdit.class);
			startActivity(intent_imgEdit);
			break;
		
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ItQuan_ListItemClickActivity.class);
		// intent.putExtra("title", listdata.get(arg2).get(title));
		intent.putExtra("title", "昨天买了一个包");
		intent.putExtra("scanner", 234);
		intent.putExtra("response", 23);
		startActivity(intent);

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.kejichangtan_listHead_rb_hotTie:
			Toast.makeText(KejiChatActivity.this, "热帖", Toast.LENGTH_SHORT)
					.show();
			getListViewDatas();
			break;
		case R.id.kejichangtan_listHead_rb_newRespon:
			Toast.makeText(KejiChatActivity.this, "最新回复", Toast.LENGTH_SHORT)
					.show();
			getListViewDatas();
			break;
		case R.id.kejichangtan_listHead_rb_newFaBiao:
			Toast.makeText(KejiChatActivity.this, "最新发表", Toast.LENGTH_SHORT)
					.show();
			getListViewDatas();
			break;

		default:
			break;
		}
	}

}
