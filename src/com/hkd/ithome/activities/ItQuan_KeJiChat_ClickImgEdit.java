package com.hkd.ithome.activities;

import com.example.ithome.R;
import com.hkd.ithome.app.AppApplication;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItQuan_KeJiChat_ClickImgEdit extends Activity implements OnClickListener{
	@ViewInject(R.id.edit_title)
	EditText edit_title;//标题
	@ViewInject(R.id.tv_help)
	TextView tv_help;//帮助
	@ViewInject(R.id.tv_fabiao)//发表
	TextView tv_fabiao;
	@ViewInject(R.id.editext_content)
	EditText editext_content;
	@ViewInject(R.id.img_camera)//照相机
	ImageView img_camera;
	@ViewInject(R.id.img_photo)//相册
	ImageView img_photo;
	@ViewInject(R.id.Img_backward_btn)//返回键
	ImageView Img_backward_btn;
	@ViewInject(R.id.relativeLayout_showHidden)//选择图片的显示与隐藏布局
	RelativeLayout relativeLayout_showHidden;
	@ViewInject(R.id.contentSelectImg)//将选中的图片放到该ImageView里面
	ImageView contentSelectImg;
	@ViewInject(R.id.Img_del)//删除已选中的图片
	ImageView Img_del;
	EditText phoneNum,yanZhengNum;
	TextView UserHaiWai;
	Button bt_getNum,bt_yanZheng;
	
	private static final int  PHOTO=101;
	private static final int CAMERA=100;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itquan_kejichat_clickimgedit);
		ViewUtils.inject(this);
		init();
		
	}
	private void init() {
		// TODO Auto-generated method stub
		Img_backward_btn.setOnClickListener(this);//返回按钮监听
		img_camera.setOnClickListener(this);//打开照相机监听
		img_photo.setOnClickListener(this);//打开照片监听
		tv_help.setOnClickListener(this);//点击帮助监听
		tv_fabiao.setOnClickListener(this);//点击发表监听
		Img_del.setOnClickListener(this);//删除已选中的图片
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
					// TODO Auto-generated method stub
					Intent intent=new Intent(ItQuan_KeJiChat_ClickImgEdit.this, KejiChatActivity.class);
					 startActivity(intent);
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
		case R.id.Img_del://隐藏布局
		    relativeLayout_showHidden.setVisibility(View.GONE);
		    break;
		case R.id.tv_fabiao://发表 判断是否已登录   1没登录 先登录2 已登录就发表
			/*
			 * 判断是否已登录  
			 *  1没登录 先登录
			 *  2 已登录就发表     弹出一个提示框 短信验证
			 *  !!!!3.把将要发表的内容添加到listView中 并第一个显示（未做）
			 */
			if(AppApplication.getApp().getUsername()!=null){            
                AlertDialog dialog_yanZheng = new AlertDialog.Builder
                		(this).create();
                View view_dialog=LayoutInflater.from(ItQuan_KeJiChat_ClickImgEdit.this).inflate(R.layout.message_yanzheng, null);
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
				Intent intent=new Intent(ItQuan_KeJiChat_ClickImgEdit.this,Me_Login.class);
				startActivity(intent);
			}
//			
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
				contentSelectImg.setImageBitmap(bitmap);
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
	                Bitmap bitmap2 = BitmapFactory.decodeFile(imgPath);
	                /*
					 * 1将拍下的照片放到容器里面
					 * 2.显示布局
					 */
	                contentSelectImg.setImageBitmap(bitmap2);
					relativeLayout_showHidden.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
		}
	}
	
	
	
	
	
	
	

}
