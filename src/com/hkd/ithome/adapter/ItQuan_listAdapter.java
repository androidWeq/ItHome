package com.hkd.ithome.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ithome.R;
import com.google.gson.Gson;
import com.hkd.ithome.bean.ItQuanBeen;
import com.hkd.ithome.tools.ItQuanTools;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;

public class ItQuan_listAdapter extends BaseAdapter{
	Context context;
	List<ItQuanBeen> listdata;
	AnimationDrawable animationDrawable;
	BitmapUtils bitmapUtils;
	Gson gson;
	public ItQuan_listAdapter(Context context,List<ItQuanBeen> listdata ){
		this.context=context;
		this.listdata=listdata;
		 bitmapUtils=new BitmapUtils(context);
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
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
//		System.out.println("-------进入adapter()");
		// TODO Auto-generated method stub
		final Holder holder;
		if(arg1==null){
			holder=new Holder();
			arg1=LayoutInflater.from(context).inflate(R.layout.itquan_listitem, null);
			arg1.setTag(holder);
		}else{
			holder=(Holder) arg1.getTag();
		}
		
		holder.imgTu=(ImageView) arg1.findViewById(R.id.itquan_listitem_img);
		holder.tvTitle=(TextView) arg1.findViewById(R.id.itquan_listitem_tvTitle);
		holder.tvAuthor=(TextView) arg1.findViewById(R.id.itquan_listitem_tvAuthor);
		holder.tvdate=(TextView) arg1.findViewById(R.id.itquan_listitem_tvDate);
		holder.tvAuthor1=(TextView) arg1.findViewById(R.id.itquan_listitem_tvAuthor2);
		holder.tvdate1=(TextView) arg1.findViewById(R.id.itquan_listitem_tvDate2);
		holder.tvPhone=(TextView) arg1.findViewById(R.id.itquan_listitem_tvphone);
		holder.tvScan=(TextView) arg1.findViewById(R.id.itquan_listitem_tvScan);
		holder.tvResponse=(TextView) arg1.findViewById(R.id.itquan_listitem_tvResponse);
		holder.tv_type=(TextView) arg1.findViewById(R.id.itquan_listitem_tvType);
		
		bitmapUtils.display(holder.imgTu, ItQuanTools.IMG_PATH+"//"+listdata.get(arg0).getImgpath());
		holder.tv_type.setText(listdata.get(arg0).getType());
		holder.tvTitle.setText(listdata.get(arg0).getTitle());
		holder.tvAuthor.setText(listdata.get(arg0).getAuthor());
		holder.tvdate.setText(listdata.get(arg0).getDate());
		holder.tvAuthor1.setText(listdata.get(arg0).getAuthor1());
		holder.tvdate1.setText(listdata.get(arg0).getDate1());
		holder.tvPhone.setText(listdata.get(arg0).getFromQuan());
		holder.tvScan.setText(listdata.get(arg0).getScanner()+"");
		holder.tvResponse.setText(listdata.get(arg0).getResponse()+"");
		
		/*
		 * 浏览量  回复量
		 * 点击浏览量  则++
		 */
		holder.tvScan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {}
		});
		
		
		return arg1;
	}

	
	class Holder{
		
		ImageView imgTu,imgScan,imgResponse;
		TextView tvTitle,tvAuthor,tvdate,tvAuthor1,
		         tvdate1,tvPhone,tvScan,tvResponse,tv_type;
		
		
		
	}
}
