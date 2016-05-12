package com.xyc.wyatt;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mapapi.SDKInitializer;
import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.manager.CallBack;
import com.xyc.wyatt.manager.NetMangaer;
import com.xyc.wyatt.manager.PhoneInfoManager;
import com.xyc.wyatt.manager.ShareManager;
import com.xyc.wyatt.util.BitmapUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShareActivity extends BaseSherlockActivity implements
		OnClickListener {
	private EditText sh_content;
	private ImageView sh_map;
	private ImageView sh_picture;
	private TextView share;
	private Uri imageUri;
	private Bitmap cropBitmap;
    private File fileOFpicture;
    private File fileOfMap;
    private Uri uri;
    
    private String runData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		// SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_share);
		mContext = this;
		initView();
		init();
		initEvent();
  
	}

	@Override
	protected void initView() {
		initActionBar("分享", true);
		// TODO Auto-generated method stub
		sh_content = (EditText) findViewById(R.id.sh_content);
		sh_map = (ImageView) findViewById(R.id.sh_map);
		sh_picture = (ImageView) findViewById(R.id.sh_picture);
		share = (TextView) findViewById(R.id.tv_share);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		runData = intent.getStringExtra("runData");
		String runMap = intent.getStringExtra("runmap");
		sh_content.setText(runData);
		uri = Uri.parse(runMap);
	
		sh_map.setImageURI(uri);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		sh_picture.setOnClickListener(this);
		share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.sh_picture:
			showPictureDialog();
			break;
		case R.id.tv_share:
			showProgress();
			try {
				Bitmap map=BitmapUtil.getBitmapFromUri(imageUri, mContext);
				
				File mapFile=BitmapUtil.getFileFromUri(map);
				Bitmap bm = BitmapUtil.revitionImageSize(mapFile.getPath());
				
				fileOFpicture=BitmapUtil.compressImage(bm,"IMG"+String.valueOf((int)(Math.random()*1000+1)));
		        
				Bitmap picture=BitmapUtil.getBitmapFromUri(uri, mContext);
				
				File pictureFile=BitmapUtil.getFileFromUri(picture);
				Bitmap bm2 = BitmapUtil.revitionImageSize(pictureFile.getPath());
				
				fileOfMap=BitmapUtil.compressImage(bm2,"IMG"+String.valueOf((int)(Math.random()*1000+1))+"track");
				 
			  
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			File[] files=new File[]{fileOfMap,fileOFpicture};
			String runfeel=sh_content.getText().toString().trim();
			Dynamic dynamic=new Dynamic();
			dynamic.setUid(GloableValue.user.getId());
			dynamic.setRunFeel(runfeel);
			dynamic.setDtime(System.currentTimeMillis()+"");
			dynamic.setUserName(GloableValue.user.getUserName());
			dynamic.setAvatarPath(GloableValue.user.getAvatar());
			dynamic.setBrand(new PhoneInfoManager(this).getDeviceInfo(PhoneInfoManager.DeviceInfoKey.BRAND));
			saveDynamic(dynamic, files);
			break;
		}
	}

	protected void showPictureDialog() {
		Builder builder = new AlertDialog.Builder(mContext);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(mContext, R.layout.dialog_avatar_select, null);
		dialog.show();

		setDialogParams(dialog, view, 270, Gravity.CENTER);

		TextView tv_cam = (TextView) view.findViewById(R.id.tv_cam);
		TextView tv_xc = (TextView) view.findViewById(R.id.tv_xc);

		tv_xc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
				makeShortToast("相册");
				imageUri=WTUtil.startal(mContext);
				

			}
		});
		tv_cam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				dialog.dismiss();
			imageUri=WTUtil.startca(imageUri, (Activity) mContext);
				
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {

			case WTContant.REQUE_CODE_CAMERA:

			
					if (imageUri != null) {
						
						Bitmap bitmap = BitmapUtil.revitionImageSize(imageUri.getPath(), 200, 200);
						sh_picture.setImageBitmap(bitmap);
					} else {
						break;
					}

			
				break;
			case WTContant.REQUE_CODE_PHOTO:
				if (null != data) {// 为了取消选取不报空指针用的
					imageUri = data.getData();
					if (imageUri != null) {
						System.out.println(imageUri+"--");
//						cropBitmap = BitmapUtil.getBitmapFromUri(imageUri,
//								mContext);
						Bitmap bm=BitmapUtil.getBitmapFromUri(imageUri, mContext);
						
						Bitmap bitmap = BitmapUtil.revitionImageSize(bm, 200, 200);
						System.out.println(bitmap+"--");
						sh_picture.setImageBitmap(bitmap);
					} else {
						break;
					}

				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
   //保存动态
	public void saveDynamic(final Dynamic dynamic,final File[] files){
		
		 ShareManager.saveDynamic(dynamic, mContext,files,new SaveListener() {
			
			@Override
			public void onSuccess() {
				makeShortToast("分享成功");
				dismissProgress();
				Intent intent = new Intent();
				intent.setClass(mContext, WyattcirleActivity.class);
				startActivity(intent);
				finish();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				makeShortToast("网络出问题了,分享失败");
				 Log.i("bmob","批量上传出错："+arg0+"--"+arg1);
				dismissProgress();
			}
		});

	
}
}