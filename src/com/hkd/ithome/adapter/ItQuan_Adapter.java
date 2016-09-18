package com.hkd.ithome.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ithome.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItQuan_Adapter extends BaseAdapter{

	Context context;
	ArrayList<HashMap<String, Object>> listdata;
	public ItQuan_Adapter(Context context,ArrayList<HashMap<String, Object>> listdata){
		this.context=context;
		this.listdata=listdata;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listdata.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

//		System.out.println("-----------------------jinAda");
		Holder holder;
		if(arg1==null){
			holder=new Holder();
			arg1=LayoutInflater.from(context).inflate(R.layout.itquan_griditem, null);
			arg1.setTag(holder);
		}else{
			holder=(Holder) arg1.getTag();
		}
		
		holder.tv_top=(TextView) arg1.findViewById(R.id.itquan_griditem_tvAbove);
		holder.tv_below=(TextView) arg1.findViewById(R.id.itquan_griditem_tvBelow);
		holder.img=(ImageView) arg1.findViewById(R.id.itquan_griditem_img);
		HashMap<String, Object> map=listdata.get(arg0);
		holder.img.setImageResource((Integer) map.get("img"));
		holder.tv_top.setText(map.get("tv1").toString());
		holder.tv_below.setText(map.get("tv2").toString());
		 
		return arg1;
	}
	
	class Holder{
		TextView tv_top;
		TextView tv_below;
		ImageView img;
		
	}

}
