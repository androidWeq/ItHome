package com.hkd.ithome.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ithome.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ItQuan_keji_editext_photoListViewAdapter extends BaseAdapter{
	Context context;
	ArrayList<HashMap<String, String>> listPhoto;
	
	
  public ItQuan_keji_editext_photoListViewAdapter(Context context,ArrayList<HashMap<String, String>> listPhoto){
	  this.context=context;
	  this.listPhoto=listPhoto;
  }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPhoto.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listPhoto.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View viewPhoto, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Holder holder;
		
		if(viewPhoto==null){
			holder=new Holder();
			viewPhoto=LayoutInflater.from(context).inflate(R.layout.itquan_kejichangtan_clickedit_listitem_photo, null);
			ViewUtils.inject(holder, viewPhoto);
			viewPhoto.setTag(holder);
		}else{
			holder=(Holder) viewPhoto.getTag();
		}
		HashMap<String, String> map=listPhoto.get(arg0);
		holder.contentSelectImg.setImageResource(Integer.parseInt(map.get("map")));
		holder.Img_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setAction("deleteItem");
				intent.putExtra("index", arg0);//获得点击的item下标
				context.sendBroadcast(intent);
			}
		});
		return viewPhoto;
	}
	
	public class Holder{
		@ViewInject(R.id.contentSelectImg)//选择的图片
		ImageView contentSelectImg;
		@ViewInject(R.id.Img_del)//删除
		ImageView Img_del;
		
		
	}

//	@Override
//	public void onClick(View arg) {
//		// 发送广播 删除该item
////		Intent intent=new Intent();
////		intent.setAction("deleteItem");
////		intent.putExtra("index", arg0);
////		context.sendBroadcast(intent);
//		 
//           
//         //发送广播  
//          
//		
//		
//	    
//		
//	}

}
