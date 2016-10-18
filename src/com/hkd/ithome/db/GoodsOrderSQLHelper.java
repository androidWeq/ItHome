package com.hkd.ithome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GoodsOrderSQLHelper extends SQLiteOpenHelper{
	
	public static final String DB_NAME = "goodorder.db";// 数据库名称
	public static final int VERSION = 1;
	
	public static final String TABLE_ORDER = "orderinfo";//订单表
	public static final String TABLE_LINE = "lineinfo";//订单明细表

	public static final String ORDERID = "id";//
	public static final String USERNAME = "userName";
	public static final String ORDERTIME = "orderTime";
	public static final String ORDERNUM = "orderNum";
	
	
	public static final String LINENUM="lineNum";
	public static final String GOODSID="goodsId";
	public static final String QUANITY="quanity";
	public static final String UNITPRICE="unitPrice";
	private Context context;
    
	
	/**
	 * 创建数据库
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public GoodsOrderSQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context=context;
		
	}
	
	

	public Context getContext() {
		return context;
	}


	/**
	 * 创建表
	 */
	public void onCreate(SQLiteDatabase db) {
		String orderSql="create table "+TABLE_ORDER+"("
				+ORDERID+" Integer ,"
				+USERNAME+" varchar(20) ,"
				+ORDERTIME+" varchar(40),"
				+ORDERNUM+" varchar(40),  primary key ("+ORDERID+","+USERNAME+")"
	            +")";
		String lineSql="create table "+TABLE_LINE+"("
				+ORDERID+"  Integer,"
				+LINENUM+" Integer primary key autoincrement,"
				+GOODSID+" Integer,"
				+QUANITY+" Integer,"
				+USERNAME+" varchar(20),"
				+UNITPRICE+" numberic(8,2)"
	            +")";
		System.out.println(orderSql+"\n"+lineSql);
		db.execSQL(orderSql);
	    db.execSQL(lineSql);
		
	}

	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
		
	}

}
