package com.hkd.ithome.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ithome.R;
import com.hkd.ithome.adapter.ItQuan_keji_editext_photoListViewAdapter;
import com.hkd.ithome.app.AppApplication;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ItQuan_KeJiChat_ClickImgEdit extends Activity implements OnClickListener{
	@ViewInject(R.id.edit_title)
	EditText edit_title;//标题
	@ViewInject(R.id.tv_help)
	TextView tv_help;//帮助
	@ViewInject(R.id.tv_fabiao)//发表
	TextView tv_fabiao;
	@ViewInject(R.id.editext_content)
	EditText editext_content;//内容
	@ViewInject(R.id.img_camera)//照相机
	ImageView img_camera;
	@ViewInject(R.id.img_photo)//相册
	ImageView img_photo;
	@ViewInject(R.id.Img_backward_btn)//返回键
	ImageView Img_backward_btn;
	@ViewInject(R.id.relativeLayout_showHidden)//选择图片的显示与隐藏布局
	RelativeLayout relativeLayout_showHidden;
//	@ViewInject(R.id.contentSelectImg)//将选中的图片放到该ImageView里面
//	ImageView contentSelectImg;
//	@ViewInject(R.id.Img_del)//删除已选中的图片
//	ImageView Img_del;
	
	EditText phoneNum,yanZhengNum;
	TextView UserHaiWai;
	Button bt_getNum,bt_yanZheng;
	//photoListView
	ArrayList<HashMap<String, String>> listPhotodata;
	@ViewInject(R.id.noScrollgridview)//相片listview
	ListView myPhotoList;
	ItQuan_keji_editext_photoListViewAdapter photoAdapter;
	
	broadCastRe bCastRe;
	private static final int  PHOTO=101;
	private static final int CAMERA=100;
	Thread it;//开一个线程   耗时操作
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_kejichat_clickimgedit);
		ViewUtils.inject(this);
		listPhotodata=new ArrayList<HashMap<String,String>>();//相片集合
//		get();
		init();
		//广播的接收
		bCastRe=new broadCastRe();
		IntentFilter deleteFilter=new IntentFilter();
		deleteFilter.addAction("deleteItem");
		registerReceiver(bCastRe, deleteFilter);
		
		
		
		
	}
	private void init() {
		// TODO Auto-generated method stub
		Img_backward_btn.setOnClickListener(this);//返回按钮监听
		img_camera.setOnClickListener(this);//打开照相机监听
		img_photo.setOnClickListener(this);//打开照片监听
		tv_help.setOnClickListener(this);//点击帮助监听
		tv_fabiao.setOnClickListener(this);//点击发表监听
//		Img_del.setOnClickListener(this);//删除已选中的图片
		
	}
	/*
	 * 单击事件
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.Img_backward_btn:
			/*
			 * 点击返回:科技畅谈页面
			 * 1.弹出一个对话框  询问：是否要退出编辑
			 * 2.返回到科技畅谈页面
			 */
			AlertDialog.Builder dialog=new AlertDialog.Builder(ItQuan_KeJiChat_ClickImgEdit.this);
			dialog.setTitle("退出将丢失正在编辑的内容");
			dialog.setMessage("确定退出吗？");
//			dialog.setView(0,0,0,0);
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("确认",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					 finish();
				}
			});
			dialog.show();
			
			
			break;
		case R.id.img_camera://打开照相机
			Intent intent_camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent_camera, CAMERA);
			break;
		case R.id.img_photo://调用相册并取得照片的方法
			Intent intent_photo=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent_photo, PHOTO);
			break;
//		case R.id.Img_del://隐藏布局
//		    relativeLayout_showHidden.setVisibility(View.GONE);
//		    break;
		case R.id.tv_fabiao://发表 判断是否已登录   1没登录 先登录2 已登录就发表
			/*
			 * 判断是否已登录  
			 *  1没登录 先登录
			 *  2 已登录就发表     弹出一个提示框 短信验证
			 *  !!!!3.把将要发表的内容添加到listView中 并第一个显示（未做）
			 */
			System.out.println("---------发表");
			  //前提是标题不为空
			if(edit_title.getText()!=null && edit_title.getText().toString()!=""){
				//，内容不为空
				if(editext_content.getText()!=null && edit_title.getText().toString()!=""){
					//用户名不能为空   即：先登录后发表
			if(AppApplication.getApp().getUsername()!=null){            
                AlertDialog dialog_yanZheng = new AlertDialog.Builder
                		(this).create();
                View view_dialog=LayoutInflater.from(ItQuan_KeJiChat_ClickImgEdit.this).inflate(R.layout.message_yanzheng, null);
                bt_getNum=(Button) view_dialog.findViewById(R.id.button_getYanZhengNum);//点击获取验证码
                 phoneNum=(EditText) view_dialog.findViewById(R.id.edit_phoneNum);//输入手机号
                 yanZhengNum=(EditText) view_dialog.findViewById(R.id.editText_yanZhengNum);//输入验证码
            	 UserHaiWai=(TextView) view_dialog.findViewById(R.id.tv_userHaiWai);//海外用户
            	bt_yanZheng=(Button) view_dialog.findViewById(R.id.button_yanZhengNum);//点击提交验证码
                /*
                 * 验证码的获取
                 * 权限注册：
                 * <uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
                 * <uses-permission android:name="android.permission.RECEIVE_SMS"></uses-permission> 
                 * <uses-permission android:name="android.permission.READ_SMS"></uses-permission>
                 * 1.输入手机号后，点击获取验证码，将手机号在数据库中查询  是否已存在
                 * 2.若存在  提示 手机号已注册 清空手机输入框  并重新输入
                 * 3.若不存在  通过服务器将数据库中的   随机验证码 发送给该手机
                 */
                
                dialog_yanZheng.setView(view_dialog);
                dialog_yanZheng.show();
                WindowManager.LayoutParams params = 
                		dialog_yanZheng.getWindow().getAttributes();
                //获得屏幕的宽高
                DisplayMetrics dm = new DisplayMetrics();
                 //获取屏幕信息
                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                        int screenWidth = dm.widthPixels;
                        int screenHeigh = dm.heightPixels;
                		params.width = screenWidth;
                		params.height = (int) (screenHeigh*0.7);
                		dialog_yanZheng.getWindow().setAttributes(params);
               
			
			
			}else{
				//把将要发表的评论  带到上一界面
				System.out.println("-------name:"+AppApplication.getApp().getUsername());
//				Toast.makeText(ItQuan_KeJiChat_ClickImgEdit.this, AppApplication.getApp().getUsername(), 100).show();
				Intent intent=new Intent(ItQuan_KeJiChat_ClickImgEdit.this,KejiChatActivity.class);
				intent.putExtra("title", edit_title.getText());
				intent.putExtra("content",editext_content.getText());
				System.out.println("---title"+edit_title.getText()+"======"+editext_content.getText());
//				intent.putExtra("photoes", img_photo);
				setResult(121, intent);
				finish();
			}
			}else{
				Toast.makeText(ItQuan_KeJiChat_ClickImgEdit.this, "内容不能为空", Toast.LENGTH_LONG).show();
			}
			}else{
				Toast.makeText(ItQuan_KeJiChat_ClickImgEdit.this, "标题不能为空", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
		
	}
	/*
	 * 相机返回数据通过下面的回调方法取得，并处理(non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK && null !=data){
			switch (requestCode) {
			case CAMERA:
				Bundle bundle=data.getExtras();
				//获取相机返回的数据，并转换为图片格式
				Bitmap bitmap=(Bitmap) bundle.get("data");
//				System.out.println("-------进入");
//				System.out.println("--------bitmap"+bitmap);
				/*
				 * 1将拍下的照片放到容器里面
				 * 2.显示布局
				 */
//				contentSelectImg.setImageBitmap(bitmap);
				relativeLayout_showHidden.setVisibility(View.VISIBLE);
				break;
			case PHOTO:
				 Uri uri = data.getData();
	                Cursor cursor = this.getContentResolver().query(uri, null,
	                        null, null, null);
	                cursor.moveToFirst();
//	                  String imgNo = cursor.getString(0); // 图片编号 
	                  String imgPath = cursor.getString(1); // 图片文件路径 
//	                  String imgSize = cursor.getString(2); // 图片大小 
//	                  String imgName = cursor.getString(3); // 图片文件名
	                  cursor.close();
	                  final Bitmap bitmap2=BitmapFactory.decodeFile(imgPath);
//	                  contentSelectImg.setImageBitmap(bitmap2);
//	                  PhotoesContain(bitmap2);
	                  it=new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message message=Message.obtain();
							message.obj=bitmap2;
							handler.sendMessage(message);
						}
					});
					  relativeLayout_showHidden.setVisibility(View.VISIBLE);
//					  getLoadPhotoes();
//				
				break;
			default:
				break;
			}
		}
	}
	
	//图片上传
	public void getLoadPhotoes(){
		HttpUtils httpUtils=new HttpUtils();
		String url="http://192.168.1.124:8080/ITHome_DB/itquan_selectItQuanInfo";
		RequestParams params=new RequestParams();
		String headPath="/tencent/MicroMsg/WeiXin/mmexport1473460957586.jpeg";
		File f=new File(headPath);
		System.out.println("--------------f:"+f.getName());
		params.addBodyParameter("ico",f,"image/*");
		httpUtils.send(HttpMethod.POST, url, params,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	//图片容器  listview
	public void PhotoesContain(Bitmap bit){
		//添加之后刷新
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("img", bit+"");
			listPhotodata.add(map);
			if(photoAdapter!=null){
				photoAdapter.notifyDataSetChanged();
			}else{
			photoAdapter=new ItQuan_keji_editext_photoListViewAdapter(ItQuan_KeJiChat_ClickImgEdit.this, listPhotodata);
	        myPhotoList.setAdapter(photoAdapter);
			}
	
	}
	/*
	 * handler
	 */
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			PhotoesContain((Bitmap) msg.obj);
			
		}
	};
	//广播接受者
	public class broadCastRe extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.equals("deleteItem")){//删除指定的图片即：listItem
				System.out.println("-------index:"+intent.getIntExtra("index", 0));
				myPhotoList.removeViewAt(intent.getIntExtra("index", 0));
			}
		}
		
	}
	

}
