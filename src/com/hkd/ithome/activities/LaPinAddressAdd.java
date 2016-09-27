package com.hkd.ithome.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.ithome.R;
import com.google.gson.Gson;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.AddressInfo;
import com.hkd.ithome.bean.GetJsonModel;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

public class LaPinAddressAdd extends Activity implements OnClickListener,OnWheelChangedListener {
	EditText name, phone, postcode, street, xiangxi;
	TextView city, confirm;
    PopupWindow popupWindow;
    HttpUtils httpUtils;
    
    /**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONObject mJsonObj;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;

	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区s
	 */
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentAreaName ="";
	
	
	
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lapin_add_address);

		init();
		initJsonData();
		initDatas();
		initPopupWindow();
		
	}

	private void init() {
		name = (EditText) findViewById(R.id.addressinfo_name);
		phone = (EditText) findViewById(R.id.addressinfo_phone);
		postcode = (EditText) findViewById(R.id.addressinfo_postcode);
		xiangxi = (EditText) findViewById(R.id.addressinfo_xiangxi);

		confirm = (TextView) findViewById(R.id.addressinfo_confirm);
		city = (TextView) findViewById(R.id.addressinfo_city);
		street = (EditText) findViewById(R.id.addressinfo_street);

		city.setOnClickListener(this);
		confirm.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addressinfo_city:
			// 在参照的View控件下方显示
		     popupWindow.showAsDropDown(LaPinAddressAdd.this.findViewById(R.id.addressinfo_xiangxi));
				updateCities();
				updateAreas();
			break;
		case R.id.addressinfo_confirm:
			System.out.println("-----点击了确认添加地址");
			httpUtils=new HttpUtils();
			RequestParams params=new RequestParams();
			AddressInfo addressInfo=new AddressInfo();
			addressInfo.setName(name.getText().toString());
			String address=mCurrentProviceName+mCurrentCityName+mCurrentAreaName+street.getText().toString()+xiangxi.getText().toString();
			addressInfo.setAddress(address);
			addressInfo.setIsDefault("0");
			addressInfo.setPhone(phone.getText().toString());
			addressInfo.setPostcode(postcode.getText().toString());
			addressInfo.setUsername(AppApplication.getApp().getUsername());
			final Gson gson=new Gson();
			System.out.println(gson.toJson(addressInfo));
			params.addQueryStringParameter("params",gson.toJson(addressInfo));
			httpUtils.send(HttpMethod.POST,NoChange.ADD_ADDRESSINFO,params,new RequestCallBack<String>() {
				public void onSuccess(ResponseInfo<String> responseInfo) {
					System.out.println(responseInfo.result);
					GetJsonModel jsonModel=gson.fromJson(responseInfo.result,GetJsonModel.class);
					if(jsonModel.getMsg().equals("success")){
						Toast.makeText(LaPinAddressAdd.this,"保存成功",1).show();
					}else{
						Toast.makeText(LaPinAddressAdd.this,"保存失败",1).show();
					}
					finish();
				}

				
				public void onFailure(HttpException error, String msg) {
							System.out.println("---获取信息失败");	
				}
			});
			
			break;

		default:
			break;
		}		
	}
	
	public void initPopupWindow(){
		// 利用layoutInflater获得View
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = layoutInflater.inflate(R.layout.citys, null);
				mProvince = (WheelView) view.findViewById(R.id.id_province);
				mCity = (WheelView) view.findViewById(R.id.id_city);
				mArea = (WheelView) view.findViewById(R.id.id_area);
				

				// 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

				popupWindow = new PopupWindow(view,
						WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.WRAP_CONTENT);
				// 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
				popupWindow.setFocusable(true);

				// 必须要给调用这个方法，否则点击popWindow以外部分，popWindow不会消失
			     popupWindow.setBackgroundDrawable(new BitmapDrawable());
			     
			     
			    mProvince.setViewAdapter(new ArrayWheelAdapter<String>(LaPinAddressAdd.this, mProvinceDatas));
					// 添加change事件
				mProvince.addChangingListener(this);
					// 添加change事件
				mCity.addChangingListener(this);
					// 添加change事件
				mArea.addChangingListener(this);

				mProvince.setVisibleItems(5);
				mCity.setVisibleItems(5);
				mArea.setVisibleItems(5);
			    popupWindow.setOnDismissListener(new OnDismissListener() {
					public void onDismiss() {						
						city.setText(mCurrentProviceName+mCurrentCityName+mCurrentAreaName);
					}
				});
			     
	}
	
	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData()
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1)
			{
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas()
	{
		try
		{
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("p");// 省名字

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try
				{
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1)
				{
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++)
				{
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// 市名字
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try
					{
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e)
					{
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++)
					{
						String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		mJsonObj = null;
	}

	/**
	 * change事件的处理
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		if (wheel == mProvince)
		{
			updateCities();
		} else if (wheel == mCity)
		{
			updateAreas();
		} else if (wheel == mArea)
		{
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
		}
	}
	
	
	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities()
	{
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null)
		{
			cities = new String[] { "" };
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(0);
		updateAreas();
	}
	
	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas()
	{
		int pCurrent = mCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);

		if (areas == null)
		{
			areas = new String[] { "" };
		}
		mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mArea.setCurrentItem(0);
	}
	
}
