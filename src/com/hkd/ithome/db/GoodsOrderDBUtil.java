package com.hkd.ithome.db;

import java.util.List;

import com.hkd.ithome.bean.GoodInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GoodsOrderDBUtil {
	private static GoodsOrderDBUtil mInstance;
	private  Context mContext;
	private GoodsOrderSQLHelper sqlHelper;
	private SQLiteDatabase liteDatabase;

	private GoodsOrderDBUtil(Context context) {
		mContext = context;
		sqlHelper = new GoodsOrderSQLHelper(context);
		liteDatabase = sqlHelper.getWritableDatabase();
	}
    
	public static GoodsOrderDBUtil getInstance(Context context){
		if(mInstance==null){
			mInstance=new GoodsOrderDBUtil(context);
		}
		return mInstance;
		
	}
	/**
	 * order订单的添加
	 * @param values
	 */
	public void insertOrder(ContentValues values){
		liteDatabase.insert(GoodsOrderSQLHelper.TABLE_ORDER,null,values);
	}
	
	/**
	 * 查询order表中当前的id
	 * @return
	 */
	public int selectOrderId(String username){
		int orderId=-1;
		Cursor cursor=liteDatabase.query(GoodsOrderSQLHelper.TABLE_ORDER,null,GoodsOrderSQLHelper.USERNAME+" =?",new String[]{username},null,null,null);
		while(cursor.moveToNext()){
			orderId=cursor.getColumnIndex(GoodsOrderSQLHelper.ORDERID);
		}
		
		return orderId;
	}
	
	public void insertLineItem(ContentValues values){
		liteDatabase.insert(GoodsOrderSQLHelper.TABLE_LINE,null,values);
	}
	
	public List<GoodInfo> getShopCar(String username){
		Cursor cursor=liteDatabase.query(GoodsOrderSQLHelper.TABLE_ORDER,null,GoodsOrderSQLHelper.USERNAME+" =?",new String[]{username},null,null,null);
		
		return null;
	}
}
