package com.hkd.ithome.adapter;

import java.util.List;

import com.example.ithome.R;
import com.example.ithome.R.color;
import com.hkd.ithome.bean.AddressInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class EditAddressListAdapter extends BaseAdapter   {

	List<AddressInfo> datas;
	Context context;

	public EditAddressListAdapter(List<AddressInfo> datas, Context context) {
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
					R.layout.lapin_edit_addresslist_listview_item, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(datas.get(position).getName());
		holder.phone.setText(datas.get(position).getPhone());
		if("1".endsWith(datas.get(position).getIsDefault())){
			holder.isdefault.setChecked(true);
			holder.isdefaultText.setText("默认地址");
			holder.isdefaultText.setTextColor(color.orange);
		}else{
			holder.isdefault.setChecked(false);
			holder.isdefaultText.setText("设为默认");
			holder.isdefaultText.setTextColor(color.black);
		}
		holder.address.setText(datas.get(position).getAddress());
		holder.edit.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				
				
			}
		});
		holder.delete.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				
				
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		@ViewInject(R.id.address_edit_listview_item_name)
		TextView name;
		@ViewInject(R.id.address_edit_listview_item_phone)
		TextView phone;
		@ViewInject(R.id.address_edit_listview_item_defaulttxt)
		TextView isdefaultText;
		@ViewInject(R.id.address_edit_listview_item_address)
		TextView address;
		@ViewInject (R.id.address_edit_listview_item_isdefault)
		CheckBox isdefault;
		@ViewInject(R.id.address_edit_listview_item_edit)
		TextView edit;
		@ViewInject(R.id.address_edit_listview_item_delete)
		TextView delete;
	}
	
}
