package com.hkd.ithome.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import com.example.ithome.R;
import com.hkd.ithome.activities.ItQuan_ListItemClickActivity;
import com.hkd.ithome.activities.KejiChatActivity;
import com.hkd.ithome.adapter.ItQuan_Adapter;
import com.hkd.ithome.adapter.ItQuan_listAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ITcircleFragment extends Fragment implements IXListViewListener,OnItemClickListener {
	ArrayList<HashMap<String, Object>> gridViewdata, listdata;
	HashMap<String, Object> map, mapList;
	GridView gridView;
	// ListView myList;
	@ViewInject(R.id.itquan_listView)
	XListView myList;
	ImageView img_animation;
	AnimationDrawable animation;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_itcircle, null);
		ViewUtils.inject(this, v);
		getListItem();
		init();
		
		myList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), ItQuan_ListItemClickActivity.class);
				intent.putExtra("path", "http://....");
				startActivity(intent);
			}
		});

		return v;
	}
	
	
	public void init(){
		myList.setXListViewListener(this);
		//true:可以上拉加载数据    相反false不可以
		//
		myList.setPullLoadEnable(false);
		
		adapterList = new ItQuan_listAdapter(getActivity(), listdata);

		LinearLayout headerViewLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.itquan_gridview, null);
		// listview添加头部必须在myList.setAdapter()之前
		myList.addHeaderView(headerViewLayout);
		// �� gridView
		gridView = (GridView) headerViewLayout.findViewById(R.id.itquan_gridView);
		getGridItem();
		gridView.setOnItemClickListener(this);
		myList.setAdapter(adapterList);
		
		headerViewLayout.setFocusable(true);
		
		
		//gridView监听事件
		
	}
	
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(this.getView()!=null)
			this.getView().setVisibility(menuVisible ? View.VISIBLE :View.GONE);
	}

	// ��ȡҪ����������Դ
	// public void getDatasForJson(){
	// isLoad=true;
	// ++page;
	// Thread it=new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// String
	// strUrl="http://182.92.102.79/ls-server/api/goods?city=2419&page="+page+"&size=10";
	// String data=HttpGet.getHttpGet().getURL(strUrl);
	// Message message=Message.obtain();
	// message.what=0;
	// message.obj=data;
	// handler.sendMessage(message);
	//
	// }
	// });
	// it.start();
	//
	// }

	/*
	 * Handler handler=new Handler(){ public void
	 * handleMessage(android.os.Message msg) { switch (msg.what) { case 0:
	 * getdataByJson(msg.obj+""); myAdapter=new
	 * MyAdapter_AllShangJia(getActivity(), jsListData);
	 * mylist.setAdapter(myAdapter); isLoad=false; mylist.stopLoadMore();
	 * //����ֹͣ������ animation.stop(); img_animation.setVisibility(View.GONE);
	 * break; case 1: System.out.println("---------handler 1");
	 * jsListData.clear(); getdataByJson(msg.obj+""); if(myAdapter==null){
	 * myAdapter=new MyAdapter_AllShangJia(getActivity(), jsListData); }
	 * mylist.setAdapter(myAdapter);
	 * 
	 * // System.out.println("------------jsdata:"+jsListData);
	 * myAdapter.notifyDataSetChanged(); isLoad=false; mylist.stopRefresh();
	 * break;
	 * 
	 * default: break; } }; }; //json���� public void getdataByJson(String
	 * data){ try { JSONObject job=new JSONObject(data); JSONArray
	 * jsArray=job.getJSONArray("datas"); int length=jsArray.length();
	 * Model_JsonIndexListView model_jsListView; for(int i=0;i<length;i++){
	 * JSONObject jobo=(JSONObject) jsArray.get(i); model_jsListView=new
	 * Model_JsonIndexListView();
	 * model_jsListView.setImgUrl(jobo.getString("imgUrl"));//ͼƬ��ַ
	 * model_jsListView.setTitle(jobo.getString("sortTitle"));//����
	 * model_jsListView.setPrice(jobo.getString("price"));//��ϵ //
	 * jsListView.setValue(jobo.getString("value"));//��ַ
	 * model_jsListView.setBought(jobo.getString("bought")); JSONObject
	 * job_shop=jobo.getJSONObject("shop");
	 * model_jsListView.setName(job_shop.getString("name"));
	 * model_jsListView.setValue(job_shop.getString("address"));//��ַ
	 * jsListData.add(model_jsListView); }
	 * 
	 * } catch (JSONException e) { // TODO Auto-generated catch block
	 * System.out.println("------����Դ����"); e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * Xlistview �������¼�
	 * 
	 * @see com.hkd.xlistview.XListView.IXListViewListener#onRefresh()
	 * 
	 * @Override public void onRefresh() { // ˢ�� if(!isLoad){ isLoad=true;
	 * System.out.println("---------onRefresh() "); Thread it=new Thread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub try {
	 * Thread.sleep(2000); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } String
	 * strUrl="http://182.92.102.79/ls-server/api/goods?city=2419&page=1&size=10"
	 * ; String data=HttpGet.getHttpGet().getURL(strUrl); Message
	 * message=Message.obtain(); message.what=1; message.obj=data;
	 * handler.sendMessage(message);
	 * 
	 * } }); it.start(); }
	 * 
	 * 
	 * }
	 */
	
	public void getListItem() {

		listdata = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 10; i++) {
			mapList = new HashMap<String, Object>();
			mapList.put("img", R.drawable.quan_zhanwu);
			mapList.put("type", "[求助]");
			mapList.put("title", "֧地铁上两个百合");
			mapList.put("author", "独悠");
			mapList.put("date", "一周前");
			mapList.put("author1", "每次都为改昵称烦恼");
			mapList.put("date1", "5天前");
			mapList.put("phone", "wind10手机圈");
			// mapList.put("img_scan",R.drawable.quan_hit);//���
			mapList.put("scan", "1266");
			// mapList.put("img_response",R.drawable.quan_comment);//�ظ�
			mapList.put("response", "126");
			listdata.add(mapList);

		}

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
				if (listdata == null) {
					listdata = new ArrayList<HashMap<String, Object>>();

				} else {

				}
				// listdata.add(object)
				// quanBuDatas.add(quanBuDatas.size() + "下拉刷新,头部");
//				Toast.makeText(getActivity(), "进入", Toast.LENGTH_LONG).show();
				adapter.notifyDataSetChanged();
				onLoad();
			}

		}, 2000);

	}

	private void onLoad() {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		//System.out.println());// new Date()为获取当前系统时间
		myList.stopRefresh();
		myList.stopLoadMore();
		myList.setRefreshTime(df.format(new Date()));
		//
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}
     //点击gridViewItem
	@Override               
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
		case 0://科技畅谈
			System.out.println("-------------进入case 0");
			Intent intent=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent);
			break;
		case 1://科技畅谈
			Intent intent1=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent1);
			break;
		case 2://科技畅谈
			Intent intent2=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent2);
			break;
		case 3://科技畅谈
			Intent intent3=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent3);
			break;
		case 4://科技畅谈
			Intent intent4=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent4);
			break;
		case 5://科技畅谈
			Intent intent5=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent5);
			break;
		case 6://科技畅谈
			Intent intent6=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent6);
			break;
		case 7://科技畅谈
			Intent intent7=new Intent(getActivity(), KejiChatActivity.class);
			startActivity(intent7);
			break;

		default:
			break;
		}
		
	}

	

}
