package com.hkd.ithome.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ithome.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItQuan_listAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String, Object>> listdata;
	HashMap<String, Object> mapList;
	public ItQuan_listAdapter(Context context,ArrayList<HashMap<String, Object>> listdata){
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
		Holder holder;
		if(arg1==null){
			holder=new Holder();
			arg1=LayoutInflater.from(context).inflate(R.layout.itquan_listitem, null);
			arg1.setTag(holder);
		}else{
			holder=(Holder) arg1.getTag();
		}
		
		holder.imgTu=(ImageView) arg1.findViewById(R.id.itquan_listitem_img);
//		holder.imgScan=(ImageView) arg1.findViewById(R.id.itquan_listitem_img);
//		holder.imgResponse=(ImageView) arg1.findViewById(R.id.itquan_listitem_img);
		holder.tvTitle=(TextView) arg1.findViewById(R.id.itquan_listitem_tvTitle);
		holder.tvAuthor=(TextView) arg1.findViewById(R.id.itquan_listitem_tvAuthor);
		holder.tvdate=(TextView) arg1.findViewById(R.id.itquan_listitem_tvDate);
		holder.tvAuthor1=(TextView) arg1.findViewById(R.id.itquan_listitem_tvAuthor2);
		holder.tvdate1=(TextView) arg1.findViewById(R.id.itquan_listitem_tvDate2);
		holder.tvPhone=(TextView) arg1.findViewById(R.id.itquan_listitem_tvphone);
		holder.tvScan=(TextView) arg1.findViewById(R.id.itquan_listitem_tvScan);
		holder.tvResponse=(TextView) arg1.findViewById(R.id.itquan_listitem_tvResponse);
		
		mapList=listdata.get(arg0);
		holder.imgTu.setBackgroundResource((Integer) mapList.get("img"));
		holder.tvTitle.setText(mapList.get("title").toString());
		holder.tvAuthor.setText(mapList.get("author").toString());
		holder.tvdate.setText(mapList.get("date").toString());
		holder.tvAuthor1.setText(mapList.get("author1").toString());
		holder.tvdate1.setText(mapList.get("date1").toString());
		holder.tvPhone.setText(mapList.get("phone").toString());
		holder.tvScan.setText(mapList.get("scan").toString());
		holder.tvResponse.setText(mapList.get("response").toString());
		
		
		return arg1;
	}

	
	class Holder{
		
		ImageView imgTu,imgScan,imgResponse;
		TextView tvTitle,tvAuthor,tvdate,tvAuthor1,tvdate1,tvPhone,tvScan,tvResponse;
		
		
		
	}
}
