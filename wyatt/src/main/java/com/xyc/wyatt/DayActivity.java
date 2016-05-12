package com.xyc.wyatt;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.actionbarsherlock.view.MenuItem;
import com.baidu.mapapi.SDKInitializer;
import com.xyc.wyatt.dao.RunRecordDao;
import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.domain.WTimePattern;
import com.xyc.wyatt.manager.RunRecordManager;
import com.xyc.wyatt.net.WTImageLoader;
import com.xyc.wyatt.util.BitmapUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTUtil;
import com.xyc.wyatt.view.RoundedImageView;
import com.xyc.wyatt.view.WTImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DayActivity extends BaseSherlockActivity {

	private Context mContext;
	private RunRecord runRecord;
	private TextView tv_day_record_date; // 日期
	private TextView tv_day_record_juli; // 距离
	private TextView tv_day_record_kaluli; // 卡路里
	private WTImageView riv_day_record_map; // 运动路径
	private String objectId;
	protected static final int CHANGE_UI = 1;
	protected static final int ERROR = 2;

	// // 定义一个主线程的消息处理器
	// private Handler handler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == CHANGE_UI) {
	// Bitmap bitmap = (Bitmap) msg.obj;
	// iv_day_record_map.setImageBitmap(bitmap);
	// }else if(msg.what == ERROR){
	// Toast.makeText(mContext, "图片获取失败", 0).show();
	// }
	// }
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_record);
		mContext = this;
		initView();
		init();
		initEvent();
		Intent intent = getIntent();
		if (intent != null) {
			showProgress();
			objectId = intent.getStringExtra("objectId");
			RunRecordManager.getRunRrcordByObjectId(mContext, objectId,
					new FindListener<RunRecord>() {
						@Override
						public void onSuccess(List<RunRecord> arg0) {
							if (!arg0.isEmpty()) {
								runRecord = arg0.get(0);
								fillData();

							}
							dismissProgress();
						}

						@Override
						public void onError(int arg0, String arg1) {
							dismissProgress();
						}
					});
		}

	}

	// 添加分享
	@Override
	protected int getOptionsMenu() {

		return R.menu.menu_run;
	}

	// 分享点击
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		switch (id) {
		case R.id.action_share:
			// makeShortToast("分享");
			// showShare();
			if (runRecord == null) {
				makeShortToast("正在加载数据...");
			} else {
				String text = "我今天跑了" + runRecord.getDistance() + ",速度是"
						+ runRecord.getRunSpeed() + "m/s,消耗了卡路里"
						+ runRecord.getRunkaluli() + "卡,快来和我一起运动吧！(消息来源:悦动)";
				shareApplication(text);
//				Drawable drawable = riv_day_record_map.getDrawable();
//				if (drawable != null) {
					//BitmapDrawable bd = (BitmapDrawable) drawable;

					//File file = BitmapUtil.getFileFromUri(bd.getBitmap());

					//Uri uri = Uri.parse(file.getAbsolutePath());
					
//				} else {
//					shareApplication(text);
//				}
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void initView() {
		tv_day_record_date = (TextView) findViewById(R.id.tv_day_record_date);
		tv_day_record_juli = (TextView) findViewById(R.id.tv_day_record_juli);
		tv_day_record_kaluli = (TextView) findViewById(R.id.tv_day_record_kaluli);
		riv_day_record_map = (WTImageView) findViewById(R.id.riv_day_record_map);
	}

	@Override
	protected void init() {
		initActionBar("运动记录", true);

	}

	private void fillData() {
		if (runRecord.getIsCompleted().endsWith("y")
				&& !runRecord.getIsCompleted().isEmpty()) {
			tv_day_record_date.setText(WTUtil.getSKTimePattern(
					WTimePattern.YMDHM, Long.parseLong(runRecord.getDate())));
		} else {
			tv_day_record_date.setText("");
		}
		tv_day_record_juli.setText(String.valueOf(runRecord.getDistance())
				+ "m");
		tv_day_record_kaluli.setText(String.valueOf(runRecord.getRunkaluli())
				+ "卡路里");
		riv_day_record_map.showImage(runRecord.getRunImage(), false);
		// getAvatar();

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("aaaa");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath();//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);

	}

	

	/**
	 * 分享数据到微信好友
	 */
//	private void shareApplicationImage(Uri uri,String text) {
//		Intent intent = new Intent();
//		intent.setAction("android.intent.action.SEND");
//		intent.addCategory(Intent.CATEGORY_DEFAULT);
//		intent.putExtra(Intent.EXTRA_STREAM, uri);
//		//intent.putExtra(Intent.EXTRA_TEXT, text);
//		// 文件类型
//		intent.setType("image/*");
//		startActivity(intent);
//	}

	private void getAvatar() {
		// 设置用户信息
		String Save1 = runRecord.getSave1();
		int uid = runRecord.getUid();
		RunRecordDao dao = new RunRecordDao(mContext);
		// skSql = "select * from "+getTableName()+" "+skSql;
		RunRecord record = dao.findOneByCondition("where uid=? and save1=?",
				new String[] { uid + "", Save1 });
		String avatarPath = record.getRunImage();
		if (avatarPath != null && !"".equals(avatarPath)) {
			if (avatarPath.startsWith("http")) {
				riv_day_record_map.showImage(avatarPath, false);
			} else {
				riv_day_record_map.showImage(avatarPath, true);
			}
		} else {
			// String path = WTContant.SERVER_IP + "/test2.jpg";
			// riv_avatar.showImage(path, false);
			riv_day_record_map.setImageResource(R.drawable.avatar_default1);
		}

	}
	/*
	 * private void getAvatar() { new Thread() { public void run() { //
	 * 鏈接服務器get請求 獲取圖片 try { //URL url = new URL(runRecord.getRunImage());//
	 * 對路徑進行包裝 URL url = new URL(
	 * "http://img0.bdstatic.com/img/image/4a75a05f8041bf84df4a4933667824811426747915.jpg"
	 * ); // 根據url 發送一個http請求 HttpURLConnection conn = (HttpURLConnection) url
	 * .openConnection(); // 設置請求的方法 conn.setRequestMethod("GET"); // 设置连接超时时间
	 * conn.setConnectTimeout(5000); // 设置读取超时时间 // conn.setReadTimeout(5000);
	 * // 设置请求参数 conn.setRequestProperty( "User-Agent",
	 * "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET4.OC; .NET4.OE; .NET4 CLR 2.0.50727; .NET4 CLR 3.0.4506.2152; .NET4 CLR 3.5.30729; Shuame)"
	 * ); // 得到服务器返回的的响应码 int code = conn.getResponseCode(); if (code == 200) {
	 * // 获取服务器端返回的数据 InputStream流中包含的就是图片的信息 InputStream is =
	 * conn.getInputStream(); // 将得到的数据流 显示到局面上 Bitmap bitmap =
	 * BitmapFactory.decodeStream(is); // iv.setImageBitmap(bitmap); //
	 * 告诉主线程一个消息 帮我修改UI界面 内容：bitmap Message msg = new Message(); msg.what =
	 * CHANGE_UI; msg.obj = bitmap; handler.sendMessage(msg);
	 * 
	 * } else { Message msg = new Message(); msg.what = ERROR;
	 * handler.sendMessage(msg); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); Message msg = new Message();
	 * msg.what = ERROR; handler.sendMessage(msg); } } }.start();
	 * 
	 * }
	 */
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if (keyCode == event.KEYCODE_BACK) {
	// Intent intent = new Intent();
	// intent.setClass(mContext, TrainingActivity.class);
	// startActivity(intent);
	// finish();
	// }
	// return super.onKeyDown(keyCode, event);
	// }
}
