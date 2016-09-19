package com.hkd.ithome.adapter;

import java.util.List;

import com.example.ithome.R;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotGoodsListViewAdapter extends BaseAdapter {
	
	private List<String> datas;
	private Context context;
	
	
	
	public HotGoodsListViewAdapter(List<String> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		//第一次加载加载页面
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.lapin_viewpager_listview_item,null);
			viewHolder.img=(ImageView) convertView.findViewById(R.id.lapin_listview_item_leftImg);
			viewHolder.title=(TextView) convertView.findViewById(R.id.lapin_listview_item_title);
			viewHolder.youhui=(TextView) convertView.findViewById(R.id.lapin_listview_item_youhui);
			viewHolder.time=(TextView) convertView.findViewById(R.id.lapin_listview_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.time.setText(datas.get(position));
		return convertView;
	}
	
	public final class ViewHolder{
		public ImageView img;
		public TextView title,youhui,time;
	}
	

}
