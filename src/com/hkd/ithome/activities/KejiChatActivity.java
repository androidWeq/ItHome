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
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
	
	@ViewInject(R.id.kejichangtan_frame)
	FrameLayout kejichangtan_frame;
	@ViewInject(R.id.lapin_loadingContent)
	RelativeLayout lapinLoadingContent;// 加载动画页面
	@ViewInject(R.id.lapin_loadingContent_rotatingImg)
	ImageView lapinLoadingImg;// 加载旋转动画图片
	RotateAnimation rotateAnimation;
	LinearInterpolator lin;
	
	private static final int GETFABIAO=122;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_kejichangtan);
		ViewUtils.inject(this);
		initRotateAnimation();//开时旋转动画
		init();//数据初始化
		getListViewDatas();//获得listItem数据
	}
	/**
	 * 加载动画的初始化
	 */
	public void initRotateAnimation() {
		 rotateAnimation = (RotateAnimation) AnimationUtils
				.loadAnimation(KejiChatActivity.this, R.anim.rotating);
		 lin = new LinearInterpolator();// 设置为匀速转动
		rotateAnimation.setInterpolator(lin);
		lapinLoadingImg.startAnimation(rotateAnimation);

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
							System.out.println("头部："+myList.getHeaderViewsCount());
							if(myList.getHeaderViewsCount()==1){
								System.out.println("if头部：");
								/*
								 * 判断mylist是否有头部
								 * 若没有  添加头部 否则不然
								 */
								initHead();
							}
							
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
						lapinLoadingImg.clearAnimation();
						lapinLoadingContent.setVisibility(View.GONE);
						kejichangtan_frame.setVisibility(View.VISIBLE);

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
//			Intent intent_imgEdit=new Intent(this, ItQuan_KeJiChat_ClickImgEdit.class);
			Intent intent_imgEdit=new Intent(this,ItQuan_KeJiChat_ClickImgEdit.class);
//			System.out.println("-------------要进入Photoes_MainActivity");
//			startActivity(intent_imgEdit);
			startActivityForResult(intent_imgEdit, GETFABIAO);//GETFABIAO=122
			
			break;
		
		default:
			break;
		}

	}
	//打开一个webView
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, WebviewActivity.class);
		intent.putExtra("link",ItQuanTools.WEBVIEW_ADDRESS);
		startActivity(intent);

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.kejichangtan_listHead_rb_hotTie:
//			Toast.makeText(KejiChatActivity.this, "热帖", Toast.LENGTH_SHORT)
//					.show();
//			lapinLoadingContent.setVisibility(View.VISIBLE);
//			kejichangtan_frame.setVisibility(View.GONE);
//			lapinLoadingImg.startAnimation(rotateAnimation);
			getListViewDatas();
			break;
		case R.id.kejichangtan_listHead_rb_newRespon:
//			Toast.makeText(KejiChatActivity.this, "最新回复", Toast.LENGTH_SHORT)
//					.show();
//			lapinLoadingContent.setVisibility(View.VISIBLE);
//			kejichangtan_frame.setVisibility(View.GONE);
//			lapinLoadingImg.startAnimation(rotateAnimation);
			getListViewDatas();
			break;
		case R.id.kejichangtan_listHead_rb_newFaBiao:
//			Toast.makeText(KejiChatActivity.this, "最新发表", Toast.LENGTH_SHORT)
//					.show();
//			lapinLoadingContent.setVisibility(View.VISIBLE);
//			kejichangtan_frame.setVisibility(View.GONE);
//			lapinLoadingImg.startAnimation(rotateAnimation);
			getListViewDatas();
			break;

		default:
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	Toast.makeText(KejiChatActivity.this, requestCode+"----"+resultCode, 100).show();
	if(resultCode==121){
		System.out.println("------进入121");
		/*
		 * 得到将要发表的数据   1.转换为json数据   再由HttpUtil解析数据给listView添加新数据
		 */
		String title=data.getStringExtra("title");
//		System.out.println("-------:"+title);
		String content=data.getStringExtra("content");
		String author=data.getStringExtra("author");
		String fromQuan=data.getStringExtra("type_quan");
		String date=data.getStringExtra("date");
		ItQuanBeen itQuan_fabiao = new ItQuanBeen();
		itQuan_fabiao.setTitle(content);//发帖内容
		itQuan_fabiao.setAuthor(author);//发帖人
		itQuan_fabiao.setDate(date);//发帖日期
		itQuan_fabiao.setFromQuan(fromQuan);//圈子来源
//		itQuan_fabiao.setFabiao_content(content);
		// 加入Listdata
		listdata.add(0,itQuan_fabiao);
		adapterList.notifyDataSetChanged();
		myList.invalidate();
	}else{
		System.out.println("进入122");
	}
//	Toast.makeText(KejiChatActivity.this, "&&&&&&&&&&&&&"+listdata.get(0).getTitle(), 100000).show();
	
	
	
	
	
	}
	
//	String url="http://192.168.1.124:8080/ITHome_DB/itquan_selectItQuanInfo";

}
