package com.hkd.ithome.adapter;

import java.util.List;

import com.example.ithome.R;
import com.hkd.ithome.bean.AddressInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShowAddressListAdapter extends BaseAdapter {

	List<AddressInfo> datas;
	Context context;

	public ShowAddressListAdapter(List<AddressInfo> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}

	public int getCount() {

		return datas.size();
	}

	public Object getItem(int position) {

		return datas.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lapin_show_addresslist_listview_item, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(datas.get(position).getName());
		holder.phone.setText(datas.get(position).getPhone());
		if("1".endsWith(datas.get(position).getIsDefault())){
			holder.isdefault.setVisibility(View.VISIBLE);
		}else{
			holder.isdefault.setVisibility(View.GONE);
		}
		holder.address.setText(datas.get(position).getAddress());
		
		
		return convertView;
	}

	class ViewHolder {
		@ViewInject(R.id.address_listview_item_name)
		TextView name;
		@ViewInject(R.id.address_listview_item_phone)
		TextView phone;
		@ViewInject(R.id.address_listview_item_isdefault)
		TextView isdefault;
		@ViewInject(R.id.address_listview_item_address)
		TextView address;
	}

}
