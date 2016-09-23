package com.hkd.ithome.adapter;

import java.util.List;

import com.example.ithome.R;
import com.hkd.ithome.bean.GoodInfo;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HotGoodsListViewAdapter extends BaseAdapter {
	
	private List<GoodInfo> datas;
	private Context context;
	BitmapUtils bitmapUtils;

	public HotGoodsListViewAdapter(List<GoodInfo> datas, Context context) {
		super();
		bitmapUtils=new BitmapUtils(context);
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
		bitmapUtils.display(viewHolder.img,NoChange.WEB_SERVERS_ADDRESS+datas.get(position).getImg());
		viewHolder.title.setText(datas.get(position).getTitle());
		viewHolder.youhui.setText("下单立减"+datas.get(position).getYouhui()+"元");
		viewHolder.time.setText(datas.get(position).getTime());
		return convertView;
	}
	
	public final class ViewHolder{
		public ImageView img;
		public TextView title,youhui,time;
	}
	

}
