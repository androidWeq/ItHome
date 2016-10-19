package com.hkd.ithome.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.hkd.ithome.activities.ItQuan_WebViewActivity;
import com.hkd.ithome.activities.KejiChatActivity;
import com.hkd.ithome.activities.SouSuoActivity;
import com.hkd.ithome.activities.WebviewActivity;
import com.hkd.ithome.adapter.ItQuan_Adapter;
import com.hkd.ithome.adapter.ItQuan_listAdapter;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.bean.ItQuanBeen;
import com.hkd.ithome.tools.ItQuanTools;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ITcircleFragment extends Fragment implements IXListViewListener,OnItemClickListener ,OnClickListener{
	ArrayList<HashMap<String, Object>> gridViewdata;
	List<ItQuanBeen> listdata;
	HashMap<String, Object> map, mapList;
	GridView gridView;
	HttpUtils httpUtils;//网络获取数据
	@ViewInject(R.id.itquan_listView)
	XListView myList;
	@ViewInject(R.id.Frg_Image_sousuo)
	ImageView Image_sousuo;//点击图片搜索
	Gson gson;
	private Handler handler;
	Date date;
	ItQuan_Adapter adapter;
	ItQuan_listAdapter adapterList;
	
	int[] img = { R.drawable.quan_zatan, R.drawable.quan_jike,
			R.drawable.quan_android, R.drawable.quan_win10,
			R.drawable.quan_wp10, R.drawable.quan_ios, R.drawable.quan_ruanmei,
			R.drawable.quan_zhanwu };
	String[] tvTop = { "科技畅谈", "极客圈", "安卓圈", "Win10圈", "Win10手机圈",
			"iOS圈", "软媒产品", "站务处理" };
	String[] tvBelow = { "+312", "+34", "+203", "+96", "+561", "+150", "+40",
			"+20" };
	
//	//旋转动画显示与隐藏
//	AnimationDrawable animationDrawable;
//	@ViewInject(R.id.Img_rota)
//	ImageView Img_rota;
	@ViewInject(R.id.Frame)
	FrameLayout Frame;
	View v;
	RelativeLayout lapinLoadingContent;// 加载动画页面
	ImageView lapinLoadingImg;// 加载旋转动画图片
	int Scan=0;
//	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 v = inflater.inflate(R.layout.fragment_itcircle, null);
		ViewUtils.inject(this, v);
		initRotateAnimation();//动画
		init();  
//		getListViewDatas();
		Image_sousuo.setOnClickListener(this);
		myList.setOnItemClickListener(this);
		return v;
	}
	/**
	 * 加载动画的初始化
	 */
	public void initRotateAnimation() {
		lapinLoadingContent = (RelativeLayout) v
				.findViewById(R.id.lapin_loadingContent);
		lapinLoadingImg = (ImageView) v
				.findViewById(R.id.lapin_loadingContent_rotatingImg);
		RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils
				.loadAnimation(getActivity(), R.anim.rotating);
		LinearInterpolator lin = new LinearInterpolator();// 设置为匀速转动
		rotateAnimation.setInterpolator(lin);
		lapinLoadingImg.startAnimation(rotateAnimation);

	}
	
	
	public void init(){
		System.out.println("-------进入init()");
		myList.setXListViewListener(this);
		//true:可以上拉加载数据    相反false不可以
		//
		myList.setPullLoadEnable(false);
         
		LinearLayout headerViewLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.itquan_gridview, null);
		// listview添加头部必须在myList.setAdapter()之前
		myList.addHeaderView(headerViewLayout);
		// �� gridView
		gridView = (GridView) headerViewLayout.findViewById(R.id.itquan_gridView);
		getGridItem();
		gridView.setOnItemClickListener(this);
		
	}
	//碎片的显示与隐藏
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(this.getView()!=null)
			this.getView().setVisibility(menuVisible ? View.VISIBLE :View.GONE);
	}
	
	private void getGridItem() {
		// TODO Auto-generated method stub

		/*
		 * gridView
		 */

		gridViewdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 8; i++) {
			map = new HashMap<String, Object>();
			map.put("img", img[i]);
			map.put("tv1", tvTop[i]);
			map.put("tv2", tvBelow[i]);
			gridViewdata.add(map);
			// System.out.println("----------------listdata.size():"+listdata.size());
		}
		adapter = new ItQuan_Adapter(getActivity(), gridViewdata);
		gridView.setAdapter(adapter);

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		handler=new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// myList.add(listdata.size() + "下拉刷新,头部");
				   if(listdata!=null){
					   listdata.clear();
				   }
					getListViewDatas();
				
//				 listdata.add(object)
				Toast.makeText(getActivity(), "进入", Toast.LENGTH_LONG).show();
				adapter.notifyDataSetChanged();
				onLoad();
			}

		}, 2000);

	}

	private void onLoad() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//设置日期格式
		//System.out.println());// new Date()为获取当前系统时间
		myList.stopRefresh();
		myList.stopLoadMore();
//		if(df.format(new Date())>){
//			
//		}
		myList.setRefreshTime(df.format(new Date()));
		//
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
	/*
	 * onStart() 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("-----进入onStart()");
		if(listdata!=null){
			listdata.clear();
		}
		getListViewDatas();
		//浏览量+1
		
		super.onStart();
		
	}
	
	
     //点击gridViewItem
	@Override               
	public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.itquan_listView://XlistView点击事件 
			Intent intent=new Intent(getActivity(),ItQuan_WebViewActivity.class);
			intent.putExtra("link",ItQuanTools.WEBVIEW_ADDRESS);//头像
			intent.putExtra("index", arg2);
			startActivityForResult(intent, 11);
			
//			
//		
			
			break;
		case R.id.itquan_gridView://GridView点击事件
			Intent inten=new Intent(getActivity(),KejiChatActivity.class);
			startActivity(inten);
			break;
			
		default:
			break;
		}
	
		}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==112){
		int	index=data.getIntExtra("index", 0);
		getUpdateScanner(index);
		// 发送广播在adapter里面改变数据   从数据库获得 浏览量 进行+1
		Intent intent=new Intent();
		intent.setAction("UpdateScanner");
		intent.putExtra("Scan", Scan);
		getActivity().sendBroadcast(intent);
		
		}
	}
		
	
	public void getUpdateScanner(final int arg2) {
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST,ItQuanTools.SELECT_information,
				new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String info = responseInfo.result;
						 try {
							 System.out.println("-----try");
							JSONArray jarray=new JSONArray(info);
							JSONObject job=jarray.getJSONObject(arg2);
							//记录浏览量Scan
							Scan=((Integer) job.get("scanner"))+1;
							System.out.println("------scanner:"+Scan);
//							holder.tvScan.setText(Scan);
						} catch (JSONException e) {
							// TODO Auto-generated catch block

							 System.out.println("-----catch");
							e.printStackTrace();
						}
								
						
					}

					@Override
					public void onFailure(HttpException error,
							String msg) {
						// TODO Auto-generated method stub
						
					}
		
		});
		
	}
	/**
	 * 从网络解析json填充到数据源
	 * @param index  页码
	 */
	private void getListViewDatas() {
		System.out.println("-------进入-getListViewDatas()-从网络解析json填充到数据源");
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.POST,ItQuanTools.SELECT_information,
				new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String info = responseInfo.result;
//						 System.out.println(info+"--------");
						 gson = new Gson();
						if(listdata==null){
							listdata=new ArrayList<ItQuanBeen>();
							adapterList = new ItQuan_listAdapter(getActivity(),listdata);
							getJsonData(info);
//							init();
							myList.setAdapter(adapterList);
						}else{
							getJsonData(info);
							adapterList.notifyDataSetChanged();
						}
						// 取消动画
						lapinLoadingImg.clearAnimation();
						lapinLoadingContent.setVisibility(View.INVISIBLE);
						Frame.setVisibility(View.VISIBLE);
					}
					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("-----获取网络数据失败");

					}
				});

		}
	//获得HttpUtil json数据
	private void getJsonData(String info) {
		// TODO Auto-generated method stub
		JsonParser jsonParser=new JsonParser();
		JsonElement jsonElement=jsonParser.parse(info);// 将json字符串转换成JsonElement
		JsonArray jsonArray;
		try {
		jsonArray=jsonElement.getAsJsonArray();//获得JsonArray对象
		Iterator it =jsonArray.iterator();//循环
		while(it.hasNext()){
			jsonElement=(JsonElement) it.next();// 提取JsonElement
			String json=jsonElement.toString();// JsonElement转换成String
			ItQuanBeen  itQuanInformation=gson.fromJson(json, ItQuanBeen.class);// String转化成JavaBean
			listdata.add(itQuanInformation);// 加入Listdata
		}
		
//		System.out.println("----------listdata:"+listdata.size());
		} catch (Exception e) {
			System.out.println("获得数据为空");
			e.printStackTrace();
		}
		
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.Frg_Image_sousuo://点击跳转 到搜页面 进行搜索
			Intent intent=new Intent(getActivity(), SouSuoActivity.class);
			intent.putExtra("name",3);
			startActivity(intent);
			
			break;

		default:
			break;
		}
		
	}
	

}
