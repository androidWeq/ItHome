package com.hkd.ithome.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.ithome.R;
import com.example.ithome.R.layout;
import com.google.gson.Gson;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.bean.UserInfoModle;
import com.hkd.ithome.fragment.MineFragment;
import com.hkd.ithome.interfaces.OnUpdateText;
import com.hkd.ithome.tools.MD5;
import com.hkd.ithome.tools.MeTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class Me_login_success extends Activity {

	@ViewInject(R.id.username_success)
	TextView u_success;
	@ViewInject(R.id.Head_pic)
	ImageView head_pic;
	@ViewInject(R.id.change_pwd)
	Button change_pwd;
	@ViewInject(R.id.exit_account)
	Button exit_account;

	LinearLayout mPopupLin, mLin_change;
	Button mTakePhoto, mPopupCancel, mPhotos, mCancelCg, mQdCg;
	PopupWindow mPopupWindow, mPopupWindowChangePwd;
	EditText mOldPwd, mNewPwd, mQdPwd;
	String value_old, value_new, value_qd;
	HttpUtils http;
	private Bitmap head;// 头像Bitmap
	private static String path = "/sdcard/DemoHead/";// sd路径

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_login_success);
		ViewUtils.inject(this);
		http = new HttpUtils();
		u_success.setText(AppApplication.getApp().getUsername());
		init();
		initChangePwd();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);// 系统的返回键会直接返回主页面
	}

	@OnClick({ R.id.exit_account, R.id.change_pwd, R.id.Head_pic, })
	public void onclick(View v) {
		switch (v.getId()) {
		case R.id.exit_account:
			AppApplication.getApp().setUsername(null);
			finish();
			break;
		case R.id.change_pwd:
			showpopupCP();
			break;
		case R.id.Head_pic:
			showpopup();
			break;
		default:
			break;
		}
	}

	public void showpopup() {
		mPopupWindow.showAtLocation(head_pic, Gravity.BOTTOM, 0, 0);
	}

	public void showpopupCP() {
		mPopupWindowChangePwd.showAtLocation(head_pic, Gravity.BOTTOM, 0, 0);
	}

	public void init() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.me_head_portrait, null);
		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		mPopupLin = (LinearLayout) view.findViewById(R.id.popup_lin);
		mTakePhoto = (Button) view.findViewById(R.id.btn_takephoto);
		mPhotos = (Button) view.findViewById(R.id.btn_photos);
		mPopupCancel = (Button) view.findViewById(R.id.popup_cancel);
		mPopupLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		mPopupCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		mTakePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 最好用try/catch包裹一下，防止因为用户未给应用程序开启相机权限，而使程序崩溃
				try {
					Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 开启相机应用程序获取并返回图片（capture：俘获）
					intent2.putExtra(
							MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(new File(Environment
									.getExternalStorageDirectory(), "head.jpg")));// 指明存储图片或视频的地址URI
					startActivityForResult(intent2, 2);// 采用ForResult打开
				} catch (Exception e) {
					Toast.makeText(Me_login_success.this, "相机无法启动，请先开启相机权限",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		mPhotos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(Intent.ACTION_PICK, null);// 返回被选中项的URI
				intent1.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");// 得到所有图片的URI
				// System.out.println("MediaStore.Images.Media.EXTERNAL_CONTENT_URI  ------------>   "
				// + MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//
				// content://media/external/images/media
				startActivityForResult(intent1, 1);
			}
		});
		// 点空白销毁,三个缺一不可
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果
		mPopupWindow.setAnimationStyle(R.style.popupAnimation);
		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从Sd中找头像，转换成Bitmap
		if (bt != null) {
			// 如果本地有头像图片的话
			head_pic.setImageBitmap(bt);
		} else {
			// 如果本地没有头像图片则从服务器取头像，然后保存在SD卡中，本Demo的网络请求头像部分忽略

		}
	}

	public void initChangePwd() {
		View view = LayoutInflater.from(this).inflate(R.layout.me_change_pwd,
				null);
		mPopupWindowChangePwd = new PopupWindow(view,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLin_change = (LinearLayout) view.findViewById(R.id.popup_lin_change);
		mOldPwd = (EditText) view.findViewById(R.id.old_pwd);
		mNewPwd = (EditText) view.findViewById(R.id.new_pwd);
		mQdPwd = (EditText) view.findViewById(R.id.qd_pwd);
		mCancelCg = (Button) view.findViewById(R.id.cancel_change);
		mQdCg = (Button) view.findViewById(R.id.qd_change);
		// 点空白销毁,三个缺一不可
		mPopupWindowChangePwd.setFocusable(true);
		mPopupWindowChangePwd.setOutsideTouchable(true);
		mPopupWindowChangePwd.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果
		mPopupWindowChangePwd.setAnimationStyle(R.style.popupAnimation);
		mLin_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			mPopupWindowChangePwd.dismiss();	
			}
		});
		mCancelCg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindowChangePwd.dismiss();	
			}
		});
		mOldPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				ChangeEnable();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mNewPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				ChangeEnable();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mQdPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				ChangeEnable();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mQdCg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!value_new.equals(value_qd)) {
				Toast.makeText(Me_login_success.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				String url = MeTool.SELECTLogin;
				RequestParams params = new RequestParams();
				UserInfoModle modle = new UserInfoModle();
				String md5pwd = MD5.toMD5(value_old);
				modle.setUsername(u_success.getText().toString());
				modle.setPassword(md5pwd);
				String value = new Gson().toJson(modle);
				params.addQueryStringParameter("params", value);
				http.send(HttpMethod.POST, url, params,new RequestCallBack<String>() {
				@Override
				public void onSuccess(
				ResponseInfo<String> responseInfo) {
				if (responseInfo.result.equals("0")) {
					 Toast.makeText(Me_login_success.this,"原密码错误!", Toast.LENGTH_SHORT).show();   
                 
				 } else {
					 String url=MeTool.UPDATE;
					 RequestParams params = new RequestParams();
					 UserInfoModle modle = new UserInfoModle();
					 String md5pwd = MD5.toMD5(value_new);
					 int id=Integer.parseInt(responseInfo.result); 
					 modle.setId(id);
					 modle.setUsername(u_success.getText().toString());
					 modle.setPassword(md5pwd);
					 String value = new Gson().toJson(modle);
					 params.addQueryStringParameter("params", value);
					 http.send(HttpMethod.POST, url, params,new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Toast.makeText(Me_login_success.this, "修改成功", Toast.LENGTH_SHORT).show();
						     mPopupWindowChangePwd.dismiss();
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(Me_login_success.this, "修改失败", Toast.LENGTH_SHORT).show();	
						}
					}); 	 
					
				}
				
                }

				@Override
				public void onFailure(HttpException error,String msg) {
					Toast.makeText(Me_login_success.this, "修改异常!", Toast.LENGTH_SHORT).show();	
							}
						});
			}
		});

	}

	public void ChangeEnable() {
		value_old = mOldPwd.getText().toString();
		value_new = mNewPwd.getText().toString();
		value_qd = mQdPwd.getText().toString();
		if (value_old.length() != 0 && value_new.length() != 0
				&& value_qd.length() != 0) {
			mQdCg.setEnabled(true);
		} else {
			mQdCg.setEnabled(false);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 从相册里面取相片的返回结果
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}

			break;
		// 相机拍照后的返回结果
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/head.jpg");
				cropPhoto(Uri.fromFile(temp));// 裁剪图片
			}

			break;
		// 调用系统裁剪图片后
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if (head != null) {
					/**
					 * 上传服务器代码
					 */
					setPicToView(head);// 保存在SD卡中
					head_pic.setImageBitmap(head);// 用ImageView显示出来
				}
			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		// 找到指定URI对应的资源图片
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		// 进入系统裁剪图片的界面
		startActivityForResult(intent, 3);
	}

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd卡是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建以此File对象为名（path）的文件夹
		String fileName = path + "head.jpg";// 图片名字
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件（compress：压缩）

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
