package com.hkd.ithome.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class LaPinViewPagerAdapter extends PagerAdapter {

	Context context;
	ArrayList<View> datasView;//viewPager 子页面的集合

	public LaPinViewPagerAdapter(Context context, ArrayList<View> datasView) {
		super();
		this.context = context;
		this.datasView = datasView;
	}

	public int getCount() {

		return datasView.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	// 移出子界面
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(datasView.get(position));
	}

	// 加载子界面
	public Object instantiateItem(ViewGroup container, int position) {
		View view = datasView.get(position);
		container.addView(view);
		return view;
	}

}
