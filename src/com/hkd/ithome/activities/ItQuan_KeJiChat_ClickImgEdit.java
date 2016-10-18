package com.hkd.ithome.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.example.ithome.R;
import com.hkd.ithome.app.AppApplication;
import com.hkd.ithome.tools.ItQuanTools;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ItQuan_KeJiChat_ClickImgEdit extends Activity implements
		OnClickListener {
	@ViewInject(R.id.edit_title)
	EditText edit_title;// 标题
	@ViewInject(R.id.tv_help)
	TextView tv_help;// 帮助
	@ViewInject(R.id.tv_fabiao)
	// 发表
	TextView tv_fabiao;
	@ViewInject(R.id.editext_content)
	EditText editext_content;// 内容
	@ViewInject(R.id.img_camera)
	// 照相机
	ImageView img_camera;
	@ViewInject(R.id.img_photo)
	// 相册
	ImageView img_photo;
	@ViewInject(R.id.Img_backward_btn)
	// 返回键
	ImageView Img_backward_btn;
	@ViewInject(R.id.relativeLayout_showHidden)
	// 选择图片的显示与隐藏布局
	RelativeLayout relativeLayout_showHidden;
	// @ViewInject(R.id.contentSelectImg)//将选中的图片放到该ImageView里面
	// ImageView contentSelectImg;
	@ViewInject(R.id.Img_del)
	// 删除已选中的图片
	ImageView Img_del;
	@ViewInject(R.id.contentSelectImg)
	// 选择的图片
	ImageView contentSelectImg;
	String imgPath;
	AlertDialog dialog_yanZheng;
	View view_dialog;
	//
	int i = 30;
	String phoneNums;

	// View viewPhoto;
	EditText phoneNum, yanZhengNum;
	TextView UserHaiWai;
	Button bt_getNum, bt_yanZheng;
	// 手机号输入框
	private EditText inputPhoneEt;

	// 验证码输入框
	private EditText inputCodeEt;

	// 获取验证码按钮
	private Button requestCodeBtn;

	// 注册按钮
	private Button commitBtn;
	// photoListView
	// ArrayList<HashMap<String, Object>> listPhotodata;
	// ArrayList<ItQuanBeen> itquan_fabiaoData;
	@ViewInject(R.id.noScrollgridview)
	// 相片listview
	ListView myPhotoList;
	// ItQuan_keji_editext_photoListViewAdapter photoAdapter;
	BitmapUtils bitmapUtils;

	// broadCastRe bCastRe;
	private static final int PHOTO = 101;
	private static final int CAMERA = 100;
	Thread it;// 开一个线程 耗时操作

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_kejichat_clickimgedit);
		ViewUtils.inject(this);
		// 子部局 listPhotoItem
		// viewPhoto=LayoutInflater.from(ItQuan_KeJiChat_ClickImgEdit.this).inflate(R.layout.itquan_kejichangtan_clickedit_listitem_photo,
		// null);
		// contentSelectImg=(ImageView)
		// viewPhoto.findViewById(R.id.contentSelectImg);
		// listPhotodata=new ArrayList<HashMap<String,Object>>();//相片集合
		bitmapUtils = new BitmapUtils(this);
		// itquan_fabiaoData=new ArrayList<ItQuanBeen>();
		// get();
		init();
		// 广播的接收 删除photo
		// bCastRe=new broadCastRe();
		// IntentFilter deleteFilter=new IntentFilter();
		// deleteFilter.addAction("deleteItem");
		// registerReceiver(bCastRe, deleteFilter);

	}

	private void init() {
		// TODO Auto-generated method stub
		Img_backward_btn.setOnClickListener(this);// 返回按钮监听
		img_camera.setOnClickListener(this);// 打开照相机监听
		img_photo.setOnClickListener(this);// 打开照片监听
		tv_help.setOnClickListener(this);// 点击帮助监听
		tv_fabiao.setOnClickListener(this);// 点击发表监听
		Img_del.setOnClickListener(this);// 删除已选中的图片

	}

	/*
	 * 单击事件
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.Img_backward_btn:// 返回按钮
			/*
			 * 点击返回:科技畅谈页面 1.弹出一个对话框 询问：是否要退出编辑 2.返回到科技畅谈页面
			 */
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ItQuan_KeJiChat_ClickImgEdit.this);
			dialog.setTitle("退出将丢失正在编辑的内容");
			dialog.setMessage("确定退出吗？");
			// dialog.setView(0,0,0,0);
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
						}
					});
			dialog.show();

			break;
		case R.id.img_camera:// 打开照相机
			Intent intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent_camera, CAMERA);
			break;
		case R.id.img_photo:// 调用相册并取得照片的方法
			Intent intent_photo = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent_photo, PHOTO);
			break;
		case R.id.Img_del:// 隐藏布局
			relativeLayout_showHidden.setVisibility(View.GONE);
			break;
		case R.id.tv_fabiao:// 发表 判断是否已登录 1没登录 先登录2 已登录就发表
			/*
			 * 判断是否已登录 1没登录 先登录 2 已登录就发表 弹出一个提示框 短信验证 !!!!3.把将要发表的内容添加到listView中
			 * 并第一个显示（未做）
			 */
			System.out
					.println("---------发表:" + edit_title.getText().toString());
			// 前提是标题不为空
			if (!edit_title.getText().toString().equals("")) {
				System.out.println("if------1");
				// ，内容不为空
				if (!editext_content.getText().toString().equals("")) {
					System.out.println("if------2");
					// 用户名不能为空 即：先登录后发表
					if (AppApplication.getApp().getUsername() != null) {
						dialog_yanZheng = new AlertDialog.Builder(this)
								.create();
						view_dialog = LayoutInflater.from(
								ItQuan_KeJiChat_ClickImgEdit.this).inflate(
								R.layout.message_yanzheng, null);
						bt_getNum = (Button) view_dialog
								.findViewById(R.id.button_getYanZhengNum);// 获取验证码
						phoneNum = (EditText) view_dialog
								.findViewById(R.id.edit_phoneNum);// 输入手机号
						yanZhengNum = (EditText) view_dialog
								.findViewById(R.id.editText_yanZhengNum);// 输入验证码
						UserHaiWai = (TextView) view_dialog
								.findViewById(R.id.tv_userHaiWai);// 海外用户
						bt_yanZheng = (Button) view_dialog
								.findViewById(R.id.button_yanZhengNum);// 点击提交验证码

						// 启动短信验证sdk
						SMSSDK.initSDK(this, "18032e726c8fc",
								"a99c4e251d0f82a5989a2cf9053e2d70");
						EventHandler eventHandler = new EventHandler() {
							@Override
							public void afterEvent(int event, int result,
									Object data) {
								Message msg = new Message();
								msg.arg1 = event;
								msg.arg2 = result;
								msg.obj = data;
								handler.sendMessage(msg);
							}
						};
						// 注册回调监听接口
						SMSSDK.registerEventHandler(eventHandler);
						
						
						
						dialog_yanZheng.setView(view_dialog);
						dialog_yanZheng.show();
						WindowManager.LayoutParams params = dialog_yanZheng
								.getWindow().getAttributes();
						// 获得屏幕的宽高
						DisplayMetrics dm = new DisplayMetrics();
						// 获取屏幕信息
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						int screenWidth = dm.widthPixels;
						int screenHeigh = dm.heightPixels;
						params.width = screenWidth;
						params.height = (int) (screenHeigh * 0.7);
						dialog_yanZheng.getWindow().setAttributes(params);
						
						
						
						
						// 点击获取验证码
						bt_getNum.setOnClickListener(new OnClickListener() {
    
							@Override
							public void onClick(View arg0) {
								 phoneNums = phoneNum.getText().toString();
									System.out.println("---------手机号："+phoneNums);
								// 1. 通过规则判断手机号
								if (!judgePhoneNums(phoneNums)) {
									return;
								} // 2. 通过sdk发送短信验证
								SMSSDK.getVerificationCode("86", phoneNums);

								// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
								bt_getNum.setClickable(false);
								bt_getNum.setText("重新发送(" + i + ")");
								new Thread(new Runnable() {
									@Override
									public void run() {
										for (; i > 0; i--) {
											handler.sendEmptyMessage(-9);
											if (i <= 0) {
												break;
											}
											try {
												Thread.sleep(1000);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
										handler.sendEmptyMessage(-8);
									}
								}).start();
							}
						});
						// 点击提交验证码
						bt_yanZheng.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								 phoneNums = phoneNum.getText().toString();
								SMSSDK.submitVerificationCode("86", phoneNums,
										yanZhengNum.getText().toString());
								createProgressBar();

							}
						});

					} else {
						// 先登录
						Intent intent_login = new Intent(
								ItQuan_KeJiChat_ClickImgEdit.this,
								Me_Login.class);
						intent_login.putExtra("flag_login", 3);
						startActivity(intent_login);

					}
				} else {
					Toast.makeText(ItQuan_KeJiChat_ClickImgEdit.this, "内容不能为空",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(ItQuan_KeJiChat_ClickImgEdit.this, "标题不能为空",
						Toast.LENGTH_LONG).show();
			}

			break;

		default:
			break;
		}

	}

	/*
	 * 相机返回数据通过下面的回调方法取得，并处理(non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && null != data) {
			switch (requestCode) {
			case CAMERA:
				Bundle bundle = data.getExtras();
				// 获取相机返回的数据，并转换为图片格式
				Bitmap bitmap = (Bitmap) bundle.get("data");
				// System.out.println("-------进入");
				// System.out.println("--------bitmap"+bitmap);
				/*
				 * 1将拍下的照片放到容器里面 2.显示布局
				 */
				contentSelectImg.setImageBitmap(bitmap);
				relativeLayout_showHidden.setVisibility(View.VISIBLE);
				break;
			case PHOTO:
				Uri uri = data.getData();
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				cursor.moveToFirst();
				// String imgNo = cursor.getString(0); // 图片编号
				imgPath = cursor.getString(1); // 图片文件路径
				// String imgSize = cursor.getString(2); // 图片大小
				// String imgName = cursor.getString(3); // 图片文件名
				cursor.close();
				getLoadPhotoes(imgPath);// 将图片上传到img文件夹

				final Bitmap bitmap2 = BitmapFactory.decodeFile(imgPath);
				contentSelectImg.setImageBitmap(bitmap2);
				// PhotoesContain(bitmap2);
				System.out.println("-----------进入到case PHOTO:" + imgPath);
				// -----------进入到case
				// PHOTO:/storage/sdcard/KuwoMusic/welcome/20161011-ad.jpg

				// it=new Thread(new Runnable() {
				//
				// @Override
				// public void run() {
				// System.out.println("--------进入到run（）");
				// Message message=Message.obtain();
				// message.obj=imgPath;
				// handler.sendMessage(message);
				// }
				// });
				// it.start();
				relativeLayout_showHidden.setVisibility(View.VISIBLE);
				// getLoadPhotoes();
				//
				break;
			default:
				break;
			}
		}
	}

	/*
	 * 图片上传
	 */
	public void getLoadPhotoes(String path) {
		HttpUtils httpUtils = new HttpUtils();
		// 上传服务器地址和有关方法
		String url = ItQuanTools.SELECT_information;
		// String
		// url="http://192.168.1.124:8080/ITHome_DB/itquan_selectItQuanInfo";
		RequestParams params = new RequestParams();
		String head_Path = "path";
		// "MicroMsg/WeiXin/mmexport1473460957586.jpeg";
		// String headPath=path;
		File f = new File(head_Path);
		// System.out.println("--------------f:"+f.getName());
		params.addBodyParameter("ico", f, "image/*");
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 从文件夹中取出 photoPath

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						System.out.println("----------上传失败哦！");

					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				bt_getNum.setText("重新发送(" + i + ")");
			} else if (msg.what == -8) {
				bt_getNum.setText("获取验证码");
				bt_getNum.setClickable(true);
				i = 30;
			} else {
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				Log.e("event", "event=" + event);
				if (result == SMSSDK.RESULT_COMPLETE) {
					// 短信注册成功后，返回MainActivity,然后提示
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
						Toast.makeText(getApplicationContext(), "提交验证码成功",
								Toast.LENGTH_SHORT).show();
						// Intent intent = new
						// Intent(ItQuan_KeJiChat_ClickImgEdit.this,
						// MainActivity.class);
						// startActivity(intent);
						/*
						 * 把将要发表的评论上传 跳转到上一界面 // * 1.以json的方式上传到数据库 // *
						 * 2.在上一界面解析得到数据
						 */
						dialog_yanZheng.dismiss();
						System.out.println("-------Username:"
								+ AppApplication.getApp().getUsername());
						// Toast.makeText(ItQuan_KeJiChat_ClickImgEdit.this,
						// AppApplication.getApp().getUsername(), 100).show();
						Intent intent = new Intent(
								ItQuan_KeJiChat_ClickImgEdit.this,
								KejiChatActivity.class);
						intent.putExtra("title", edit_title.getText()
								.toString());// 发表标题
						intent.putExtra("content", editext_content.getText()
								.toString());// 发表内容
						intent.putExtra("author", AppApplication.getApp()
								.getUsername());// 作者名字
						SimpleDateFormat date = new SimpleDateFormat("yy-MM-dd");
						intent.putExtra("date", date.format(new Date()) + "");// 获得当前时间
						intent.putExtra("type_quan", "iOS圈");// 默认圈子类型
						System.out.println("Username-----------:"
								+ AppApplication.getApp().getUsername());
						// intent.putExtra("photo_path",imgPath);//得到取得的图片
						// System.out.println("listPhotodata:"+listPhotodata.size());
						// System.out.println("---title"+edit_title.getText()+"======"+editext_content.getText());
						// intent.putExtra("photoes", img_photo);
						setResult(121, intent);
						finish();
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Toast.makeText(getApplicationContext(), "验证码已经发送",
								Toast.LENGTH_SHORT).show();
					} else {
						((Throwable) data).printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * 判断手机号码是否合理
	 * 
	 * @param phoneNums
	 */
	private boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
			return true;
		}
		Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 判断一个字符串的位数
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	@SuppressLint("NewApi") public static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			System.out.println("-----判断一个字符串的位数-----手机号为空");
			return false;
		} else {
			return str.length() == length ? true : false;
		}
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobileNums) {
		System.out.println("-------------验证手机格式："+mobileNums);
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}

	/**
	 * progressbar
	 */
	private void createProgressBar() {
		FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		ProgressBar mProBar = new ProgressBar(this);
		mProBar.setLayoutParams(layoutParams);
		mProBar.setVisibility(View.VISIBLE);
		layout.addView(mProBar);
	}

	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}
	// 广播接受者
	// public class broadCastRe extends BroadcastReceiver{
	//
	// @Override
	// public void onReceive(Context arg0, Intent intent) {
	// // TODO Auto-generated method stub
	// if(intent.equals("deleteItem")){//删除指定的图片即：listItem
	// System.out.println("-------index:"+intent.getIntExtra("index", 0));
	// myPhotoList.removeViewAt(intent.getIntExtra("index", 0));
	// }
	// }
	//
	// }

}
