package com.hkd.ithome.tools;

public class NoChange {
	
	/**
	 * 服务器地址
	 */

	public final static String WEB_SERVERS_ADDRESS="http://192.168.1.125:8080/ITHome";





	
	/**
	 * 查询辣品信息的接口
	 */
	public final static String SELECT_GOODS=WEB_SERVERS_ADDRESS+"/lapin_seletGoodsInfo?";
	
	/**
	 * 查询当前用户默认地址信息的接口
	 */
	public final static String SELECT_DEFAULT_ADDRESS=WEB_SERVERS_ADDRESS+"/lapin_getdefaultAddressInfo?";
    
	/**
	 * 添加当前用户的收货地址
	 */
	public final static String ADD_ADDRESSINFO=WEB_SERVERS_ADDRESS+"/lapin_addAddressInfo?";
	
	/**
	 * 查询当前用户所有的收货地址
	 */
	public final static String SELECT_ADDRESSLIST=WEB_SERVERS_ADDRESS+"/lapin_getAddressList?";
}
