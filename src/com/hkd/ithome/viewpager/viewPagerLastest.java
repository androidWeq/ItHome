package com.hkd.ithome.viewpager;import java.text.DateFormat;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import java.util.HashMap;import java.util.Iterator;import me.maxwin.view.XListView;import me.maxwin.view.XListView.IXListViewListener;import com.example.ithome.R;import com.google.gson.Gson;import com.google.gson.JsonArray;import com.google.gson.JsonElement;import com.google.gson.JsonParser;import com.hkd.ithome.activities.WebviewActivity;import com.hkd.ithome.adapter.NewsAdapter;import com.hkd.ithome.bean.NewsModel;import com.lidroid.xutils.HttpUtils;import com.lidroid.xutils.ViewUtils;import com.lidroid.xutils.exception.HttpException;import com.lidroid.xutils.http.RequestParams;import com.lidroid.xutils.http.ResponseInfo;import com.lidroid.xutils.http.callback.RequestCallBack;import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;import com.lidroid.xutils.view.annotation.ViewInject;import android.content.Intent;import android.os.Bundle;import android.os.Handler;import android.support.v4.app.Fragment;import android.support.v4.view.PagerAdapter;import android.support.v4.view.ViewPager;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.webkit.WebView;import android.widget.AdapterView;import android.widget.AdapterView.OnItemClickListener;import android.widget.Toast;public class viewPagerLastest extends Fragment implements IXListViewListener {	@ViewInject(R.id.xListView)	XListView xListView;	//头部幻灯片	View headerNews, vpNewsOne, vpNewsTwo;	ArrayList<View> headerLists;	ViewPager vpHeader;	public static final int LOADMORE_OVER = 0;// 上拉加载下一页完成	public static final int REFISH_OVER = 1;// 下拉刷新完成	private boolean threadIsRun = false;	NewsAdapter newsAdapter;	ArrayList<NewsModel> newsDatas;	//获取数据	HttpUtils http;	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		// TODO Auto-generated method stub		View v = inflater.inflate(R.layout.viewpager_lastest, null);		ViewUtils.inject(this, v);		xListView.setXListViewListener(this);				headerNews=getActivity().getLayoutInflater().inflate(R.layout.header_news,null);		vpHeader=(ViewPager) headerNews.findViewById(R.id.vpHeader);		vpNewsOne=getActivity().getLayoutInflater().inflate(R.layout.viewpager_header_news_one, null);		vpNewsTwo=getActivity().getLayoutInflater().inflate(R.layout.viewpager_header_news_two, null);		headerLists=new ArrayList<View>();		headerLists.add(vpNewsOne);		headerLists.add(vpNewsTwo);		vpHeader.setAdapter(HeaderNewsAdapter); 		xListView.addHeaderView(headerNews);				http=new HttpUtils();		String url="http://192.168.253.6:8080/ItHomeSSH/newsInfo_selectNews?";        NewsModel modle=new  NewsModel();        modle.setPageIndex(1);        modle.setTypeId("1");				RequestParams params=new RequestParams();		String value=new Gson().toJson(modle);		params.addQueryStringParameter("params",value);		http.send(HttpMethod.POST, url,params, new RequestCallBack<String>() {			@Override			public void onSuccess(ResponseInfo<String> responseInfo) {				//成功				//System.out.println("---------onSuccess"+responseInfo.result);				if(responseInfo.result!=null){				Gson gson = new Gson();				newsDatas=new ArrayList<NewsModel>();				JsonParser jsonParser = new JsonParser();				// 将json字符串转换成JsonElement				JsonElement jsonElement = jsonParser.parse(responseInfo.result);				JsonArray jsonArray;				jsonArray = jsonElement.getAsJsonArray();				Iterator it = jsonArray.iterator();// Iterator处理				while (it.hasNext()) { // 循环					jsonElement = (JsonElement) it.next();// 提取JsonElement					String json = jsonElement.toString();// JsonElement转换成String					NewsModel newsModel=gson.fromJson(json, NewsModel.class);// String转化成JavaBean					newsDatas.add(newsModel);				}				newsAdapter = new NewsAdapter(getActivity(), newsDatas);				xListView.setAdapter(newsAdapter);				}else{					newsDatas=new ArrayList<NewsModel>();					NewsModel newsModel;					for(int i=0;i<10;i++){						newsModel=new NewsModel();						newsModel.setTitle("为了拒绝苹果130亿欧元税款，爱尔兰宁可得罪欧盟");						newsModel.setTime("16:34");						newsModel.setPinglun("23");						newsDatas.add(newsModel);					}					newsAdapter = new NewsAdapter(getActivity(), newsDatas);					xListView.setAdapter(newsAdapter);				}			}			@Override			public void onFailure(HttpException error, String msg) {				// TODO Auto-generated method stub				System.out.println("---------onFail");				Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();				newsDatas=new ArrayList<NewsModel>();				NewsModel newsModel;				for(int i=0;i<10;i++){					newsModel=new NewsModel();					newsModel.setTitle("为了拒绝苹果130亿欧元税款，爱尔兰宁可得罪欧盟");					newsModel.setTime("16:34");					newsModel.setPinglun("23");					newsDatas.add(newsModel);				}				newsAdapter = new NewsAdapter(getActivity(), newsDatas);				xListView.setAdapter(newsAdapter);			}					});								xListView.setOnItemClickListener(new OnItemClickListener() {			@Override			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,					long arg3) {				// TODO Auto-generated method stub				Intent intentToWebview=new Intent(getActivity(),WebviewActivity.class);				intentToWebview.putExtra("link", newsDatas.get(arg2-2).getLink());				if(newsDatas.get(arg2-1).getLink()!=null){					startActivity(intentToWebview);				}			}		});		return v;	}	@Override	public void onRefresh() {		System.out.println("-----onRefresh");		// 下拉执行		if (!threadIsRun) {			System.out.println("-----onLoadMore");			threadIsRun = true;			/*			 * 1.获取下一页数据 2.刷新适配器			 */			Thread t = new Thread(new Runnable() {				@Override				public void run() {					try {						Thread.sleep(2000);					} catch (InterruptedException e) {						// TODO Auto-generated catch block						e.printStackTrace();					}					// mDatas.clear();					// addDatas();					handler.sendEmptyMessage(REFISH_OVER);				}			});			t.start();		}	}	@Override	public void onLoadMore() {		// 下拉执行		if (!threadIsRun) {			System.out.println("-----onLoadMore");			threadIsRun = true;			/*			 * 1.获取下一页数据 2.刷新适配器			 */			Thread t = new Thread(new Runnable() {				@Override				public void run() {					try {						Thread.sleep(2000);					} catch (InterruptedException e) {						// TODO Auto-generated catch block						e.printStackTrace();					}					// mDatas.clear();					// addDatas();					handler.sendEmptyMessage(REFISH_OVER);				}			});			t.start();		}	}	Handler handler = new Handler() {		public void handleMessage(android.os.Message msg) {			switch (msg.what) {			case REFISH_OVER:			case LOADMORE_OVER:				threadIsRun = false;				// favoriteAdapter.notifyDataSetChanged();				xListView.stopLoadMore();				xListView.stopRefresh();				System.out.println("stop==========");				Date date = new Date();				DateFormat format = new SimpleDateFormat("HH:mm:ss");				String time = format.format(date);				xListView.setRefreshTime(time);				break;			default:				break;			}		};	};			//顶部幻灯片适配器	PagerAdapter HeaderNewsAdapter = new PagerAdapter() {		@Override		public boolean isViewFromObject(View arg0, Object arg1) {			// TODO Auto-generated method stub			return arg0 == arg1;		}		@Override		public void destroyItem(ViewGroup container, int position, Object object) {			// TODO Auto-generated method stub			container.removeView(headerLists.get(position));		}		@Override		public Object instantiateItem(ViewGroup container, int position) {			// TODO Auto-generated method stub			View v = headerLists.get(position);			container.addView(v);			return v;		}		@Override		public int getCount() {			// TODO Auto-generated method stub			return headerLists.size();		}	};}