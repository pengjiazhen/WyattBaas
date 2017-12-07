package com.xyc.wyatt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xyc.wyatt.manager.UserManager;
import com.xyc.wyatt.util.BitmapUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;
import com.xyc.wyatt.view.RoundedImageView;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends BaseSherlockActivity implements
		OnClickListener {

	public static final String TAG = "UserInfoActivity";
	private RelativeLayout rl_avatar;
	private RelativeLayout rl_nick_name;
	private RelativeLayout rl_sex;
	private RelativeLayout rl_age;
	private RelativeLayout rl_height;
	private RelativeLayout rl_weight;
	private RoundedImageView riv_avatar;
	private TextView tv_nick_name;
	private TextView tv_sex;
	private TextView tv_age;
	private TextView tv_height;
	private TextView tv_weight;
	private TextView tv_avatar;

	private Uri imageUri;
	private Uri uri;
	private Bitmap cropBitmap;
	private Uri copyBitmapUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		mContext = this;
		initView();
		init();
		initEvent();
	}

	@Override
	protected void initView() {
		rl_avatar = (RelativeLayout) findViewById(R.id.rl_avatar);
		rl_nick_name = (RelativeLayout) findViewById(R.id.rl_nick_name);
		rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
		rl_age = (RelativeLayout) findViewById(R.id.rl_age);
		rl_height = (RelativeLayout) findViewById(R.id.rl_height);
		rl_weight = (RelativeLayout) findViewById(R.id.rl_weight);

		riv_avatar = (RoundedImageView) findViewById(R.id.riv_avatar);
		tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
		tv_weight = (TextView) findViewById(R.id.tv_weight);
		tv_sex = (TextView) findViewById(R.id.tv_sex);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_height = (TextView) findViewById(R.id.tv_height);
		tv_avatar = (TextView) findViewById(R.id.tv_avatar);

	}

	@Override
	protected void init() {
		initActionBar("个人资料", true);

	}

	@Override
	protected void initEvent() {
		// rl_avatar.setOnClickListener(this);
		rl_nick_name.setOnClickListener(this);
		rl_sex.setOnClickListener(this);
		rl_age.setOnClickListener(this);
		rl_height.setOnClickListener(this);
		rl_weight.setOnClickListener(this);
		tv_avatar.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// 设置用户信息
		String avatarPath = GloableValue.user.getAvatar();
		if (avatarPath != null && !"".equals(avatarPath)) {
			if (avatarPath.startsWith("http")) {
				riv_avatar.showImage(avatarPath, false);
			} else {
				riv_avatar.showImage(avatarPath, true);
			}
		} else {
			// String path = WTContant.SERVER_IP + "/test2.jpg";
			// riv_avatar.showImage(path, false);
			riv_avatar.setImageResource(R.drawable.avatar_default1);
		}
		// 昵称
		tv_nick_name.setText(GloableValue.user.getUserName());

		// 体重
		tv_weight.setText(String.valueOf(GloableValue.user.getWeight()) + "kg");
		// 身高
		tv_height.setText(String.valueOf(GloableValue.user.getHeight()) + "cm");
		// GloableValue.user.setAvatar(user.getAvatar()); 头像

		// 年龄
		tv_age.setText(String.valueOf(GloableValue.user.getAge()));
		// 性别
		tv_sex.setText(GloableValue.user.getSex());
		super.onResume();
	}

	@Override
	protected void onDestroy() {

		if (GloableValue.changeUserInfo == 1) {
			// TODO 标记为1 信息改变了 与服务器同步信息
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_avatar:
			showAvatarDialog();
			break;
		case R.id.rl_nick_name:
			// intent = new Intent();
			// intent.setClass(mContext, NickNameEditActivity.class);
			// startActivity(intent);
			break;
		case R.id.rl_sex:
			showAlertDialog();
			break;
		case R.id.rl_age:
			intent = new Intent();
			intent.setClass(mContext, UserInfoEditActivity.class);
			intent.putExtra("value", tv_age.getText());
			intent.putExtra("type", 0);
			startActivityForResult(intent, WTContant.REQUEST_AGE);
			break;
		case R.id.rl_height:
			intent = new Intent();
			intent.setClass(mContext, UserInfoEditActivity.class);
			intent.putExtra("value", tv_height.getText());
			intent.putExtra("type", 1);
			startActivityForResult(intent, WTContant.REQUEST_HEIGHT);
			break;
		case R.id.rl_weight:
			intent = new Intent();
			intent.setClass(mContext, UserInfoEditActivity.class);
			intent.putExtra("value", tv_weight.getText());
			intent.putExtra("type", 2);
			startActivityForResult(intent, WTContant.REQUEST_WEIGHT);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String value = "";
		if (data != null) {
			value = data.getStringExtra("value");
		}
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// case WTContant.REQUEST_AGE:
			// tv_age.setText(value);
			// break;
			// case WTContant.REQUEST_HEIGHT:
			// tv_height.setText(value);
			// break;
			// case WTContant.REQUEST_WEIGHT:
			// tv_weight.setText(value);
			// break;
			case WTContant.REQUE_CODE_CAMERA:
				try {
					Bitmap bitmap = BitmapUtil.getBitmapFromUri(imageUri,
							mContext);
					copyBitmapUri = BitmapUtil.saveBitmap(bitmap);
					WTUtil.startPhotoZoom(mContext, copyBitmapUri,
							WTContant.REQUE_CODE_CROP);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				break;
			case WTContant.REQUE_CODE_PHOTO:
				if (null != data) {// 为了取消选取不报空指针用的
					imageUri = data.getData();

					// 获取窗口信息
					// WindowManager wm = (WindowManager) mContext
					// .getSystemService(Context.WINDOW_SERVICE);
					// DisplayMetrics metrics = new DisplayMetrics();
					// wm.getDefaultDisplay().getMetrics(metrics);
					// 压缩图片
					// Bitmap revitionBitmap = BitmapUtil.revitionImageSize(
					// imageUri.getPath(), metrics.widthPixels,
					// metrics.heightPixels);
					try {
						Bitmap bitmap = BitmapUtil.getBitmapFromUri(imageUri,
								mContext);
						copyBitmapUri = BitmapUtil.saveBitmap(bitmap);
						WTUtil.startPhotoZoom(mContext, copyBitmapUri,
								WTContant.REQUE_CODE_CROP);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
				break;
			case WTContant.REQUE_CODE_CROP:
				if (copyBitmapUri == null) {
					break;
				}
				cropBitmap = BitmapUtil.getBitmapFromUri(copyBitmapUri,
						mContext);
				if (cropBitmap != null) {
					riv_avatar.showImage(copyBitmapUri.getPath(), true);
					GloableValue.user.setAvatar(copyBitmapUri.getPath());
					save2net();
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void save2net() {
		Bitmap bitmap = BitmapUtil.getBitmapFromUri(copyBitmapUri, mContext);
		File file = BitmapUtil.getFileFromUri(bitmap);
		Bitmap bm2=null;
		try {
			bm2 = BitmapUtil.revitionImageSize(file.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File avatarImage=BitmapUtil.compressImage(bm2);
		UserManager.updateUserAvatar(GloableValue.user.getUserName(), 0,
				new File[] { avatarImage }, mContext, new UpdateListener() {
					@Override
					public void onSuccess() {
						makeShortToast("修改成功");
						UserManager.updateLocalUser(mContext);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						makeShortToast(arg1);
					}
				});

	}

	// 弹出性别选择框
	protected void showAlertDialog() {
		Builder builder = new AlertDialog.Builder(mContext);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(mContext, R.layout.dialog_select_sex, null);
		dialog.show();

		setDialogParams(dialog, view, 1300, Gravity.CENTER);

		TextView man = (TextView) view.findViewById(R.id.tv_man);
		TextView woman = (TextView) view.findViewById(R.id.tv_woman);

		man.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				makeShortToast("男");
				dialog.dismiss();
				tv_sex.setText("男");
				String newSex = "男";
				// 设置全局用户变量
				if (!newSex.equals(GloableValue.user.getSex())) {
					// 用户名改变
					GloableValue.user.setSex(newSex);
					//String sex = "sex";
					updateUserInfo();
				} else {
				}

			}
		});
		woman.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				makeShortToast("女");
				dialog.dismiss();
				tv_sex.setText("女");
				String newSex = "女";
				// 设置全局用户变量
				if (!newSex.equals(GloableValue.user.getSex())) {
					// 用户名改变
					GloableValue.user.setSex(newSex);
					String sex = "sex";
					// updateOnNet(sex, newSex);
					updateUserInfo();
				} else {
				}
			}

		});
	}

	private void updateUserInfo() {
		UserManager.updateUser(mContext, new UpdateListener() {
			@Override
			public void onSuccess() {
				makeShortToast("修改成功");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				makeShortToast(arg1);
			}
		});
	}

	// 弹出头像选择选择框
	protected void showAvatarDialog() {
		Builder builder = new AlertDialog.Builder(mContext);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(mContext, R.layout.dialog_avatar_select, null);
		dialog.show();

		setDialogParams(dialog, view, 1300, Gravity.CENTER);

		TextView tv_cam = (TextView) view.findViewById(R.id.tv_cam);

		TextView tv_xc = (TextView) view.findViewById(R.id.tv_xc);
		// 相册
		tv_xc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				WTUtil.startal(mContext);

			}
		});
		// 相机
		tv_cam.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				imageUri = WTUtil.startca(imageUri, (Activity) mContext);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// 此处还可以做其他的bar响应，如果需要其他的，可以复写这个方法
		switch (id) {
		// case android.R.id.home:
		// Intent intent = new Intent();
		// intent.setClass(mContext, TrainingActivity.class);
		// startActivity(intent);
		// finish();
		// break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			// Intent intent = new Intent();
			// intent.setClass(mContext, TrainingActivity.class);
			// startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
