package com.hkd.ithome.app;
import com.hkd.ithome.db.SQLHelper;

import android.app.Application;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;
	private SQLHelper sqlHelper;
	String username;
	boolean addressIsDefault=true;//判断用户是不是没有收货地址而要重新添加,true代表没有,false代表有

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
	}

	/** 获取Application */
	public static AppApplication getApp() {
//		if(mAppApplication==null){
//			mAppApplication=new AppApplication();
//		}
		return mAppApplication;
	}
	
	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mAppApplication);
		return sqlHelper;
	}
	
	/** 摧毁应用进程时候调用 */
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
	}
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		
	}

	public void clearAppCache() {
	}

	public boolean isAddressIsDefault() {
		return addressIsDefault;
	}

	public void setAddressIsDefault(boolean addressIsDefault) {
		this.addressIsDefault = addressIsDefault;
	}
	
	
}
