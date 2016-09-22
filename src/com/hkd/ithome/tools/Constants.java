package com.hkd.ithome.tools;

import java.util.ArrayList;

import com.hkd.ithome.bean.NewsClassify;


public class Constants {
	public static ArrayList<NewsClassify> getData() {
		ArrayList<NewsClassify> newsClassify = new ArrayList<NewsClassify>();
		NewsClassify classify = new NewsClassify();
		classify.setId(0);
		classify.setTitle("推荐");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(1);
		classify.setTitle("热点");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(2);
		classify.setTitle("数码");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(3);
		classify.setTitle("杭州");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(4);
		classify.setTitle("社会");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(5);
		classify.setTitle("娱乐");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(6);
		classify.setTitle("科技");
		newsClassify.add(classify);
		classify = new NewsClassify();
		classify.setId(7);
		classify.setTitle("汽车");
		newsClassify.add(classify);
		return newsClassify;
	}
}
