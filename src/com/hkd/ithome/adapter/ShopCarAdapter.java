package com.hkd.ithome.adapter;


import java.util.HashMap;
import java.util.List;

import com.example.ithome.R;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.GoodInfo;

import com.hkd.ithome.interfaces.OnUpdateText;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopCarAdapter extends BaseAdapter   {

	Context context;
	List<GoodInfo> datas;
	BitmapUtils bitmapUtils;
	ShopCarAdapter adapter;
	OnUpdateText onUpdateText;
	double allprice=0;
	int isTrue=0;
	private  HashMap<Integer, Boolean> isSelected;//记录item的check是否选择
	
	
	
	/*public ShopCarAdapter getAdapter(Context context,List<GoodInfo> datas){
		if(adapter==null){
			adapter=new ShopCarAdapter(context,datas);
		}
		
		return adapter;
		
	}*/
	
	
	 
	public double getAllprice() {
		return allprice;
	}


	


	public void setAllprice(double allprice) {
		this.allprice = allprice;
	}


	public int getIsTrue() {
		return isTrue;
	}


	public void setIsTrue(int isTrue) {
		this.isTrue = isTrue;
	}


	public void setOnUpdateText(OnUpdateText onUpdateText) {
		this.onUpdateText = onUpdateText;
	}
   
	
	
	public void initCheckBox(boolean isChecked) {
		if(datas.size()==0){
			onUpdateText.updateText("0.0");
		}
		isSelected=new HashMap<Integer, Boolean>();
        for (int i = 0; i<datas.size();i++) {
        	isSelected.put(i,isChecked);
        }
    }
	


	public ShopCarAdapter(Context context,List<GoodInfo> datas) {
		super();
		this.context = context;
		this.datas = datas;
		isTrue=0;
		bitmapUtils=new BitmapUtils(context);
		isSelected = new HashMap<Integer, Boolean>();
		// //默认CheckBox为未勾选状态
		initCheckBox(false);
	}
	
	


	public  HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	
	public GoodInfo getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}
	
	public void updatas(){
		int length=isSelected.size()-1;
		for(int i=length;i>=0;i--){
			if(isSelected.get(i)){
				datas.remove(i);
			}
		}
		initCheckBox(false);
		updateAdapter();
	}

	
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	/**
	 * 更新数据源,刷新适配器
	 */
	public void updateAdapter(){
		this.notifyDataSetChanged();
	}
	/**
	 * 更新服务器的购物车列表的单个商品数量
	 * @param goodId 商品Id
	 * @param goodNum  商品数量
	 */
	public void updateMySqlShopCar(int goodId,int goodNum){
		updateAdapter();
		HttpUtils httpUtils=new HttpUtils();
		String username=AppApplication.getApp().getUsername();
		RequestParams params=new RequestParams();
		params.addQueryStringParameter("params","{\"goodId\":\"" + goodId
				+ "\",\"username\":\"" + username + "\",\"goodNum\":\"" + goodNum + "\"}");
		httpUtils.send(HttpMethod.POST,NoChange.UPDATE_SHOPCAR_GOODNUM,params,new RequestCallBack<String>() {

			
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("_____更新购物车数量成功");
				
			}

			
			public void onFailure(HttpException error, String msg) {
				
				System.out.println("____更新商品数量网络连接错误");
			}
		});
		
	}

	
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.lapin_shopcar_listview_item,null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);	
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		/**
		 * 是否选中当前商品提交订单
		 * 
		 */
		System.out.println(datas.size()+"________"+isSelected.size());
		
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				double price=getItem(position).getPrice();
				double num=getItem(position).getGoodNum();
				if(isChecked){
					//System.out.println("_______adapter checkBox+++");
					allprice+=price*num;
					isTrue++;
					//System.out.println(isTrue+"______istrue++");
				}else{
					allprice-=price*num;
					isTrue--;
					//System.out.println(isTrue+"______istrue--");
				}
				//记录当前item变化的状态
				isSelected.put(position,isChecked);
				
				if(isTrue==datas.size()){
					//设置全选为真
					onUpdateText.updateBoolean(true);
				}else{
					//设置全选为假
					onUpdateText.updateBoolean(false);
				}
				if(isTrue==0){
					onUpdateText.updateText("0.0");
					allprice=0;
				}else{
					onUpdateText.updateText(allprice+"");
				}
				
				
			}
		});
		
		/**
		 * 数量减
		 */
		holder.jian.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				//功能:当选中当前item的checkbox时,减少该商品数量底部的总价也会对应减少
				if(isSelected.get(position)){
					allprice-=getItem(position).getPrice();
					onUpdateText.updateText(allprice+"");
				}
				
				int num=getItem(position).getGoodNum();
				if(num-1>0){
					getItem(position).setGoodNum(num-1);
					num=num-1;
					updateMySqlShopCar(getItem(position).getId(),num);
				}else{
					getItem(position).setGoodNum(1);
					num=1;
				}
				
				
			}
		});
		/**
		 * 数量加
		 */
		holder.jia.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//功能:当选中当前item的checkbox时,增加该商品数量底部的总价也会对应增加
				if(isSelected.get(position)){
					allprice+=getItem(position).getPrice();
					onUpdateText.updateText(allprice+"");
				}
				int num=getItem(position).getGoodNum();
				getItem(position).setGoodNum(num+1);
				updateMySqlShopCar(getItem(position).getId(),num+1);
			}
		});
		
		
		bitmapUtils.display(holder.img,NoChange.WEB_SERVERS_ADDRESS+datas.get(position).getImg());
		
		double price=getItem(position).getPrice();
		int num=getItem(position).getGoodNum();
		holder.num.setText(num+"");
		holder.price.setText(price+"");
		holder.title.setText(getItem(position).getTitle());
		holder.checkNum.setText(price*num+"");		
		//为了点击全选按钮让item的checkbox全为true
		
		holder.checkBox.setChecked(isSelected.get(position));
		
		
		return convertView;
	}
	
	
	
	
	
	public static  class ViewHolder{
		@ViewInject(R.id.shopcar_listviewitem_payone)
		CheckBox checkBox;
		@ViewInject(R.id.shopcar_listviewitem_img)
		ImageView img;
		@ViewInject(R.id.shopcar_listviewitem_title)
		TextView title;
		@ViewInject(R.id.shopcar_listviewitem_jia)
		TextView jia;
		@ViewInject(R.id.shopcar_listviewitem_jian)
		TextView jian;
		@ViewInject(R.id.shopcar_listviewitem_num)
		TextView num;
		@ViewInject(R.id.shopcar_listviewitem_unitprice)
		TextView price;
		@ViewInject(R.id.shopcar_listviewitem_checknum)
		TextView checkNum;
		
	}
	
	
	

}
