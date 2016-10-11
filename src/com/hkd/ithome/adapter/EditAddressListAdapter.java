package com.hkd.ithome.adapter;

import java.util.List;

import com.example.ithome.R;
import com.example.ithome.R.color;
import com.google.gson.Gson;
import com.hkd.ithome.bean.AddressInfo;
import com.hkd.ithome.bean.GetJsonModel;
import com.hkd.ithome.tools.NoChange;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class EditAddressListAdapter extends BaseAdapter {

	List<AddressInfo> datas;
	Context context;
	int checkBoxIndex;
	HttpUtils httpUtils, http1;
	boolean changDeafault = false;//记录是否删除的默认地址,

	public EditAddressListAdapter(List<AddressInfo> datas, Context context) {
		super();
		httpUtils = new HttpUtils();
		http1 = new HttpUtils();
		this.datas = datas;
		this.context = context;
	}

	public int getCheckBoxIndex() {
		return checkBoxIndex;
	}

	public void setCheckBoxIndex(int checkBoxIndex) {
		this.checkBoxIndex = checkBoxIndex;
		this.notifyDataSetChanged();
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

	public View getView(final int position, View convertView, ViewGroup parent) {
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
		if ("1".endsWith(datas.get(position).getIsDefault())) {
			checkBoxIndex = position;// 记录checkbox改变之前的下标
			holder.isdefault.setChecked(true);
			holder.isdefaultText.setText("默认地址");
			holder.isdefaultText.setTextColor(color.orange);
		} else {
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
				// 如果当前删除的是默认地址
				if (datas.get(position).getIsDefault().equals("1")) {
					//System.out.println("-----进入默认地址删除界面");
					//删除的为默认地址,则默认设置第一条信息为默认地址
					changDeafault = true;
					// 服务器删除当前选定的地址
					deleteAddress(datas.get(position), position);
					// 数据源删除选定的地址
					datas.remove(position);
					

				} else {
					//System.out.println("----进入不是默认地址删除界面");
					changDeafault = false;
					// 删除的不是默认地址
					// 服务器删除当前选定的地址
					deleteAddress(datas.get(position), position);
					// 数据源删除选定的地址
					datas.remove(position);
					notifyDataSetChanged();
				}

			}

		});

		holder.isdefault
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (checkBoxIndex != position) {
							if (isChecked) {
								// 直接改变数据源刷新数据源 不用再次从网络获取
								// 如果当前的checkbox为true的话则这个就是改变厚的默认地址,修改样式
								datas.get(position).setIsDefault("1");
								// 设置原来的默认默认地址的数据源
								datas.get(checkBoxIndex).setIsDefault("0");
								int oldIndex = datas.get(checkBoxIndex).getId();
								int newIndex = datas.get(position).getId();
								System.out
										.println(oldIndex + "----" + newIndex);
								// 修改服务器的数据
								changAddressDefault(oldIndex, newIndex);
								// 记录当前为默认地址的checkbox的数据下标
								setCheckBoxIndex(position);
							}
						} else {
							// 当点击的是已经设置为默认地址的checkBox 则样式不改变
							buttonView.setChecked(true);
						}

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
		@ViewInject(R.id.address_edit_listview_item_isdefault)
		CheckBox isdefault;
		@ViewInject(R.id.address_edit_listview_item_edit)
		TextView edit;
		@ViewInject(R.id.address_edit_listview_item_delete)
		TextView delete;
	}

	/**
	 * 向数据库发送数据改变默认地址
	 * 
	 * @param oldIndex
	 *            改变前的默认地址的id
	 * @param newIndex
	 *            改变为默认地址的id
	 */
	public void changAddressDefault(int oldIndex, int newIndex) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("params", "{\"oldIndex\":\"" + oldIndex
				+ "\",\"newIndex\":\"" + newIndex + "\"}");
		httpUtils.send(HttpMethod.POST, NoChange.CHANGE_DEFAULT_ADDRESS,
				params, new RequestCallBack<String>() {

					public void onSuccess(ResponseInfo<String> responseInfo) {
						Gson gson = new Gson();
						GetJsonModel jsonModel = gson.fromJson(
								responseInfo.result, GetJsonModel.class);
						if ("success".equals(jsonModel.getMsg())) {
							Toast.makeText(context, "设置成功", 1).show();
						} else {
							Toast.makeText(context, "设置失败", 1).show();
						}

					}

					public void onFailure(HttpException error, String msg) {
						System.out.println("-----改变默认地址网络连接错误");

					}
				});
	}

	/**
	 * 删除选中服务器中的地址
	 * 
	 * @param info 地址信息对象
	 */
	public void deleteAddress(AddressInfo info, final int position) {
		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.addQueryStringParameter("params", gson.toJson(info));
		//System.out.println("----删除----"+ params.getQueryStringParams().toString());
		httpUtils.send(HttpMethod.POST, NoChange.DELETE_ADDRESSINFO, params,
				new RequestCallBack<String>() {

					public void onSuccess(ResponseInfo<String> responseInfo) {
						// datas.remove(position);
						System.out.println("------删除成功");
						//执行删除完毕后执行更新,否则服务端更新接口的params会将删除接口的params覆盖,出错
						Message msg = handler.obtainMessage();
						msg.what = 1;
						handler.sendMessage(msg);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("-----删除地址信息网络错误");

					}
				});
	}

	/**
	 * 更新服务器的默认地址信息
	 * 
	 * @param id
	 *            需要更改的地址信息id
	 */
	private void updateDefaultAddress(AddressInfo info) {
		RequestParams params = new RequestParams();
		Gson gson = new Gson();
		params.addQueryStringParameter("params", gson.toJson(info));
		//System.out.println("----更新----"+ params.getQueryStringParams().toString());
		http1.send(HttpMethod.POST, NoChange.UPDATE_ADDRESSINFO, params,
				new RequestCallBack<String>() {

					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println("------更新地址成功");

					}

					public void onFailure(HttpException error, String msg) {
						System.out.println("-----更新 地址信息网络错误----" + msg);

					}
				});

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (changDeafault) {
					// 如果还有剩余的地址信息
					if (datas.size() > 0) {
						// 默认设置listview第一条信息为默认地址
						datas.get(0).setIsDefault("1");
						// 修改服务器的默认地址
						//System.out.println("----默认更新");
						updateDefaultAddress(datas.get(0));
						setCheckBoxIndex(0);

					} else {
						// 没有地址了 显示提示添加地址的界面
						Toast.makeText(context,"没有默认地址请添加",1).show();
						notifyDataSetChanged();
					}
				}
				break;

			default:
				break;
			}
		};
	};

}
