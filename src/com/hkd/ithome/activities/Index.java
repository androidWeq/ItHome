package com.hkd.ithome.activities;


import com.example.ithome.R;
import com.hkd.ithome.fragment.HotgoodsFragment;
import com.hkd.ithome.fragment.ITcircleFragment;
import com.hkd.ithome.fragment.MineFragment;
import com.hkd.ithome.fragment.NewsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Index extends FragmentActivity  {
	
	RadioGroup radioGroup;
    FrameLayout frameLayout;
	FragmentStatePagerAdapter statePagerAdapter;
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		initFragmentAdapter();
		initFragment();
		//SMSSDK.initSDK(this, "1771207c44520", "c9db0f1680bda2e6ad2ca25b5e43037a");
	}
	
	
	
    //初始化fragment的适配器
	private void initFragmentAdapter() {
		frameLayout=(FrameLayout) findViewById(R.id.fragHome);
		statePagerAdapter=new FragmentStatePagerAdapter(getSupportFragmentManager()) {
			public int getCount() {
				return 4;
			}
			public Fragment getItem(int arg0) {
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




	/**
	 * 初始化fragment
	 */
	private void initFragment() {
		
		//首次进入默认设置显示第一个fragment
		Fragment fragment=(Fragment) statePagerAdapter.instantiateItem(frameLayout,0);
		statePagerAdapter.setPrimaryItem(frameLayout,0,fragment);
		statePagerAdapter.finishUpdate(frameLayout);
		
		
		radioGroup=(RadioGroup) findViewById(R.id.rgHome);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//index 为将要显示的fragment的下标
				int index=0;
				switch (checkedId) {
				case R.id.rbNews:
					index=0;
					break;
				case R.id.rbHotgoods:
					index=1;
					break;
				case R.id.rbItcircle:
					index=2;
					break;
				case R.id.rbMine:
					index=3;
					break;	
				}
				//instantiateItem将会去fragmentMangaer中查找有没有index对应的碎片，如果有，直接从管理器中取出此碎片
				//如果没有，将会调用适配器中的getitem函数，由于我们传递了index索引，索引会返回给我们对应的碎片
				Fragment fragment=(Fragment) statePagerAdapter.instantiateItem(frameLayout,index);
				//告诉适配器，你需要显示哪个碎片
				statePagerAdapter.setPrimaryItem(frameLayout,0,fragment);
				//刷新  并且再fragment里面重新setMenuVisibility
				statePagerAdapter.finishUpdate(frameLayout);
			}
		});	
		
		
	}

}
