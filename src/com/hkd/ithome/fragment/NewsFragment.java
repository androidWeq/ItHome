package com.hkd.ithome.fragment;import com.example.ithome.R;import com.lidroid.xutils.ViewUtils;import android.os.Bundle;import android.support.v4.app.Fragment;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;public class NewsFragment extends Fragment {	@Override	public View onCreateView(LayoutInflater inflater, ViewGroup container,			Bundle savedInstanceState) {		View v = inflater.inflate(R.layout.fragment_news, null);		ViewUtils.inject(this, v);		return v;	}}