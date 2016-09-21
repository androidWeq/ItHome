package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.hkd.ithome.fragment.HotgoodsFragment;
import com.hkd.ithome.fragment.ITcircleFragment;
import com.hkd.ithome.fragment.MineFragment;
import com.hkd.ithome.fragment.NewsFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeActivity extends  FragmentActivity {
   
	@ViewInject(R.id.rgHome)
	RadioGroup rgHome;
	@ViewInject(R.id.rbNews)
	RadioButton rbNews;
	@ViewInject(R.id.rbHotgoods)
	RadioButton rbHotgoods;
	@ViewInject(R.id.rbItcircle)
	RadioButton rbItcircle;
	@ViewInject(R.id.rbMine)
	RadioButton rbMine;

	FragmentManager manager;
	FragmentTransaction tran;

	NewsFragment newsFragment;
	HotgoodsFragment hotgoodsFragment;
	ITcircleFragment iTcircleFragment;
	MineFragment mineFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		ViewUtils.inject(this);
      
		manager = getSupportFragmentManager();
		newsFragment = new NewsFragment();
		manager.beginTransaction().add(R.id.fragHome, newsFragment).commit();

		rgHome.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				FragmentTransaction ft = manager.beginTransaction();
				switch (arg1) {
				case R.id.rbNews:
					if (newsFragment == null) {
						newsFragment = new NewsFragment();
						ft.add(R.id.fragHome, newsFragment);
					} else {
						ft.show(newsFragment);
					}

					if (hotgoodsFragment != null && iTcircleFragment != null
							&& mineFragment != null) {
						ft.hide(hotgoodsFragment);
						ft.hide(iTcircleFragment);
						ft.hide(mineFragment);
					}
					ft.commit();

					break;

				case R.id.rbHotgoods:
					if (hotgoodsFragment == null) {
						hotgoodsFragment = new HotgoodsFragment();
						ft.add(R.id.fragHome, hotgoodsFragment);
					} else {
						ft.show(hotgoodsFragment);
					}
					
					if (newsFragment != null && iTcircleFragment != null
							&& mineFragment != null) {
						ft.hide(newsFragment);
						ft.hide(iTcircleFragment);
						ft.hide(mineFragment);
					}
					ft.commit();

					
					
					
					break;
				case R.id.rbItcircle:
					
					if (iTcircleFragment == null) {
						iTcircleFragment = new ITcircleFragment();
						ft.add(R.id.fragHome, iTcircleFragment);
					} else {
						ft.show(iTcircleFragment);
					}
					
					if (newsFragment != null && hotgoodsFragment != null
							&& mineFragment != null) {
						ft.hide(newsFragment);
						ft.hide(hotgoodsFragment);
						ft.hide(mineFragment);
					}
					ft.commit();
					
					
					break;
				case R.id.rbMine:
					
					if (mineFragment == null) {
						mineFragment = new MineFragment();
						ft.add(R.id.fragHome, mineFragment);
					} else {
						ft.show(mineFragment);
					}
					

					if (newsFragment != null && hotgoodsFragment != null
							&& iTcircleFragment != null) {
						ft.hide(newsFragment);
						ft.hide(hotgoodsFragment);
						ft.hide(iTcircleFragment);
					}
					ft.commit();
					
					
					break;

				default:
					break;
				}
			}
		});
		

	}
	
	FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(
			getSupportFragmentManager()) {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				return new NewsFragment();
			case 1:
				return new HotgoodsFragment();
			case 2:
				return new ITcircleFragment();
			case 3:
				return new MineFragment();
			default:
				return null;
			}
		}
	};
	
	
}
