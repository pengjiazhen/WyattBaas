package com.xyc.wyatt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.manager.RunRecordManager;
import com.xyc.wyatt.util.BitmapUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

@SuppressLint("SimpleDateFormat")
public class RuningActivity extends BaseSherlockActivity implements
		OnClickListener {

	public static final String LTAG = "RuningActivity";

	private Context mContext;
	private TextView tv_run_time;
	private TextView tv_run_distance;
	private TextView tv_run_spend;
	private TextView tv_run_calorie;

	private TextView tv_run_start;
	private TextView tv_run_countinue;
	private TextView tv_run_stop;
	private TextView tv_run_save;

	private LinearLayout ll_bottom;
	private LinearLayout ll_top;
	private LinearLayout ll_top_tip;
	private TextView tv_top_tip;

	// 定位相关
	public static final int REQUESTLOCATING = 0;
	private MapView mMapView = null;
	private SDKReceiver mReceiver;

	private BaiduMap mBaiduMap;
	private boolean isFirstLoc = true;// 是否是第一次定位
	private MyLocationListenner mListener;
	private LocationClient locationClient;
	private boolean needRequestLocation = false;
	private List<LatLng> points;
	private List<LatLng> twoPoints;
	private LatLng start;
	private LatLng end;
	private int position;
	private boolean drawing = true;// 是否正在画图，退出时设置成false，中断死循环

	private RunRecord recordImage = new RunRecord();
	private RunRecord record2;
	private double runDiastance;
	private String runTime;
	private double runSpeed;
	private double runCalorie;// kaluli
	private long timeCount;//
	private RunRecord runRecord;
	private boolean isComplete = false;

	private String day;
	private File file;
	private boolean saving = true;
	private boolean share = false;
	private Uri uri;
	private String[] badTips = new String[] { "平时要多注意补充营养哦" };
	private String[] goodTips = new String[] { "不错哦，继续保持！！！" };
	private double distanceRecom;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case REQUESTLOCATING: // 处理重复定位 定时定位 一秒定位一次
				Log.e(LTAG, "请求定位");
				if (needRequestLocation) {
					// 发送定位请求
					locationClient.requestLocation();
					Message m = new Message();
					m.what = REQUESTLOCATING;
					handler.sendMessageDelayed(m, 1000);
					// 更新界面数据

					// 更新时间
					timeCount = timeCount + 1000;
					runTime = WTUtil.longMillisToTime(timeCount);
					tv_run_time.setText(runTime);

					// 更新速度
					tv_run_spend.setText((int) runSpeed + "m/s");
					// 更新路程
					tv_run_distance.setText(WTUtil.getDistance(runDiastance));
					// 更新卡路里
					tv_run_calorie.setText((int) runCalorie + "卡");
				}

				break;

			default:
				break;
			}
			super.dispatchMessage(msg);
		}
	};

	private String objectId;
	private String nextObjectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_runing);
		mContext = this;
		Intent intent = getIntent();
		if (intent != null) {
			day = intent.getStringExtra("day");
			objectId = intent.getStringExtra("objectId");
			nextObjectId = intent.getStringExtra("nextObjectId");
			distanceRecom = intent.getDoubleExtra("distanceRecom", 0);
		}
		initView();
		init();
		initEvent();
	}

	@Override
	protected void initView() {

		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		ll_top = (LinearLayout) findViewById(R.id.ll_top);
		ll_top_tip = (LinearLayout) findViewById(R.id.ll_top_tip);

		tv_run_time = (TextView) findViewById(R.id.tv_run_time);
		tv_run_distance = (TextView) findViewById(R.id.tv_run_distance);
		tv_run_spend = (TextView) findViewById(R.id.tv_run_spend);
		tv_run_calorie = (TextView) findViewById(R.id.tv_run_calorie);

		tv_run_start = (TextView) findViewById(R.id.tv_run_start);
		tv_run_countinue = (TextView) findViewById(R.id.tv_run_countinue);
		tv_run_stop = (TextView) findViewById(R.id.tv_run_stop);
		tv_run_save = (TextView) findViewById(R.id.tv_run_save);
		tv_top_tip = (TextView) findViewById(R.id.tv_top_tip);
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.wt_bmapView);

	}

	@Override
	protected void init() {
		initActionBar("第" + day + "天", true);
		points = new LinkedList<LatLng>();
		twoPoints = new LinkedList<LatLng>();
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);

		// 初始化地图
		mMapView.showZoomControls(false);

		mBaiduMap = mMapView.getMap();

		// 设置缩放级别
		mBaiduMap.setMapStatus(MapStatusUpdateFactory
				.newMapStatus(new MapStatus.Builder().zoom(17).build()));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 开启交通图层
		mBaiduMap.setTrafficEnabled(true);
		// 开启热力图层
		mBaiduMap.setBaiduHeatMapEnabled(false);
		// 初始化定位
		mListener = new MyLocationListenner();
		locationClient = new LocationClient(mContext);
		// 注册定位监听
		locationClient.registerLocationListener(mListener);

		// 定位配置
		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setOpenGps(true);// 打开gps
		locationClientOption.setCoorType("bd09ll"); // 设置坐标类型
		locationClientOption.setScanSpan(500);

		locationClient.setLocOption(locationClientOption);
		locationClient.start();

	}

	@Override
	protected void initEvent() {
		tv_run_start.setOnClickListener(this);
		tv_run_countinue.setOnClickListener(this);
		tv_run_stop.setOnClickListener(this);
		tv_run_save.setOnClickListener(this);
	}

	@Override
	protected int getOptionsMenu() {
		return R.menu.menu_run;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_share:

			if (TextUtils.isEmpty(runTime)) {
				Dialog dialog = new AlertDialog.Builder(this)

				.setTitle("消息")

				.setMessage("亲，你还没开始运动哦....")

				.setPositiveButton("确定", null)

				.show();
			} else {
				if (isComplete && share) {
					// showShare();
					shareApplication("[悦动消息:]我今天跑了"
							+ WTUtil.getDistance(runDiastance) + ",速度是"
							+ (int) runSpeed + "m/s,消耗了卡路里" + (int) runCalorie
							+ "卡,快来和我一起运动吧！(消息来源:悦动)");
				}
			}
			break;
		case android.R.id.home:
			Intent intent = new Intent();
			intent.setClass(mContext, TrainingActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void sendRequestLocation() {
		Message msg = new Message();
		msg.what = REQUESTLOCATING;
		handler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_run_start:
			needRequestLocation = true;

			sendRequestLocation();

			tv_run_start.setVisibility(View.GONE);

			ll_bottom.setVisibility(View.VISIBLE);

			tv_run_countinue.setText(R.string.run_pause);
			break;
		case R.id.tv_run_countinue:
			String text = tv_run_countinue.getText().toString();
			if ("继续".equals(text)) {
				needRequestLocation = true;

				sendRequestLocation();

				tv_run_countinue.setText(R.string.run_pause);
			} else {
				tv_run_countinue.setText(R.string.run_countinue);
				needRequestLocation = false;
			}
			break;
		case R.id.tv_run_stop:
			needRequestLocation = false;
			isComplete = true;
			ll_bottom.setVisibility(View.GONE);
			tv_run_save.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_run_save:
			// 这里将数据填充到实体中,发送后台保存，在保存前要验证是否登陆
			if (GloableValue.user.getStatus() == WTContant.LOGIN) {
				// 已经登陆了 ，执行保存数据的操作
				if (saving) {
					saving = false;
					// showProgress();
					ll_top_tip.setVisibility(View.VISIBLE);
					ll_top.setVisibility(View.GONE);
					new Thread(new Runnable() {
						@Override
						public void run() {
							printScreen();
						}
					}).start();

				} else if (share) {
					Intent intent = new Intent();
					intent.setClass(mContext, ShareActivity.class);
					intent.putExtra("runData",
							"我今天跑了" + WTUtil.getDistance(runDiastance) + ",速度是"
									+ (int) runSpeed + "m/s,消耗了卡路里"
									+ (int) runCalorie + "卡");
					intent.putExtra("runmap", uri.toString());
					startActivity(intent);
					finish();
				} else {
					CharSequence text2 = tv_run_save.getText();
					if (text2.equals("保存运动数据")) {
						makeShortToast("数据保存中");
					} else {
						makeShortToast("数据保存失败，无法分享!");
					}
				}
			} else {
				// 跳转到登陆界面
				Intent intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				intent.putExtra("where", "runningActivity");
				startActivityForResult(intent, WTContant.REQUE_CODE_LOGIN);
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 保存数据
	 */
	public void saveRecord2Net() {
		recordImage.setObjectId(objectId);
		recordImage.setUserName(GloableValue.user.getUserName());
		recordImage.setDistance((int) (runDiastance * 1000));// 距离
		recordImage.setRunTime((timeCount / 1000));// 运动时间
		recordImage.setRunSpeed(((long) (runDiastance * 1000))
				/ (timeCount / 1000));// 运动速度
		recordImage.setRunkaluli((int) runCalorie);// 消耗卡路里
		recordImage.setIsCompleted("y");
		recordImage.setSave1(day);
		if (distanceRecom > (int) ((runDiastance) * 1000)) {
			recordImage.setTip(badTips[0]);
		} else {
			recordImage.setTip(goodTips[0]);
		}
		RunRecordManager.saveRunRrcord(mContext, recordImage,
				new File[] { file }, new UpdateListener() {
					@Override
					public void onSuccess() {
						ll_top_tip.setVisibility(View.GONE);
						ll_top.setVisibility(View.VISIBLE);
						tv_run_save.setText("分享数据到悦动圈");
						share = true;
						Toast.makeText(mContext, "数据保存成功", 0).show();
						GloableValue.requestQueryRunRecord = true;
						// 执行修改下一天的运动计划，
						RunRecord record = new RunRecord();
						record.setObjectId(nextObjectId);
						record.setDistance((int) ((runDiastance) * 1000 + 100));
						// record.setTip();

						record.update(mContext);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						ll_top_tip.setVisibility(View.GONE);
						ll_top.setVisibility(View.VISIBLE);
						saving = true;
						Toast.makeText(mContext, "出问题了，数据保存失败", 0).show();
					}
				});

	}

	/**
	 * 截图
	 */
	public void printScreen() {
		mBaiduMap.snapshot(new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap bitmap) {
				file = BitmapUtil.compressImage(bitmap, "track");
				uri = Uri.fromFile(file);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tv_top_tip.setText("数据保存中...");
						// 保存运动数据
						saveRecord2Net();
					}
				});
			}
		});
	}

	/**
	 * 分享到其他平台
	 * */
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
		oks.setText("我今天跑了" + WTUtil.getDistance(runDiastance) + ",速度是"
				+ (int) runSpeed + "m/s,消耗了卡路里" + (int) runCalorie + "卡");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(file.getAbsolutePath());// 确保SDcard下面存在此张图片
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
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			Log.e(LTAG, location.getLatitude() + "::" + location.getLongitude());

			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;

			if (isFirstLoc) {
				isFirstLoc = false;
				MyLocationData locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(100).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();

				mBaiduMap.setMyLocationData(locData);
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				lastPoint = ll;
				points.add(ll);

				startDrawLineThread(location.getRadius());
			} else {

				if (!points.isEmpty()) {
					// LatLng ll = new LatLng(
					// points.get(points.size() - 1).latitude + 0.00005
					// * new Random().nextInt(10),
					// points.get(points.size() - 1).longitude + 0.00005
					// * new Random().nextInt(10));
					//
//					LatLng ll = new LatLng(location.getLatitude() + 0.00005
//							* new Random().nextInt(10), location.getLongitude()
//							+ 0.00005 * new Random().nextInt(10));
					
					LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
					points.add(ll);

				}

			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private double lastx;
	private double lasty;
	private LatLng lastPoint;

	/**
	 * 开启画轨迹的线程 用一个集合来轮询这个points集合
	 */
	private void startDrawLineThread(final float radiaus) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.e(LTAG, "线程开启");
				int pointSize = 0;
				while (true) {
					if (!drawing)
						break;
					if (points.size() > pointSize) {
						// Log.e(LTAG, points.size() + "");
						twoPoints.clear();
						if (points.size() > 2) {
							start = lastPoint;
							if (points.size() - 1 == position) {
								end = start;
							} else {
								end = points.get(position + 1);
							}

							twoPoints.add(start);
							twoPoints.add(end);
							// runOnUiThread(new Runnable() {
							// @Override
							// public void run() {

							// 设置地图的中心点
							MapStatusUpdate u = MapStatusUpdateFactory
									.newLatLng(twoPoints.get(0));

							// 设置蓝色点的位置
							MyLocationData locData = new MyLocationData.Builder()
									.accuracy(radiaus)
									// 此处设置开发者获取到的方向信息，顺时针0-360
									.direction(100)
									.latitude(twoPoints.get(1).latitude)
									.longitude(twoPoints.get(1).longitude)
									.build();
							mBaiduMap.setMyLocationData(locData);

							// 在地图上画轨迹
							OverlayOptions lineOptions = new PolylineOptions()
									.color(Color.RED).width(2)
									.points(twoPoints);
							mBaiduMap.addOverlay(lineOptions);
							mBaiduMap.animateMapStatus(u);
							Log.e(LTAG, twoPoints.get(0).latitude + "::"
									+ twoPoints.get(0).longitude);
							Log.e(LTAG, twoPoints.get(1).latitude + "::"
									+ twoPoints.get(1).longitude);
							// 计算速度
							calculateSpeed();
							// 计算路程
							calculateDistance();
							// 计算卡路里
							calculateCa();
							// }
							// });
							position++;
							pointSize = points.size();
							lastPoint = end;
						}
					}
				}
			}

		}).start();
	}

	private void calculateSpeed() {
		runSpeed = WTUtil.getTwoPointDistance(start.latitude, start.longitude,
				end.latitude, end.longitude) * 1000;
		;
	}

	private void calculateDistance() {
		runDiastance = runDiastance
				+ WTUtil.getTwoPointDistance(start.latitude, start.longitude,
						end.latitude, end.longitude);
	}

	private void calculateCa() {
		/*
		 * 跑步的热消耗通用计算公式： 跑步热量（kcal）＝体重（kg）×运动时间（小时）×指数K 指数K＝30÷速度（分钟/400米）
		 */
		double runSpeedAvg = (runDiastance * 1000)
				/ ((double) timeCount / 3600);
		double K = 30 / ((400 / runSpeedAvg) / 60);
		runCalorie = 60 * ((double) timeCount / 3600) * K;
	}

	@Override
	protected void onDestroy() {
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		// 取消监听 SDK 广播
		needRequestLocation = false;
		unregisterReceiver(mReceiver);

		// 退出时销毁定位
		locationClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mBaiduMap.clear();
		mMapView.onDestroy();
		mMapView = null;
		drawing = false;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 * 
	 * @author pengjiazhen
	 *
	 */
	private class SDKReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s);
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				Toast.makeText(context,
						"key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", 0)
						.show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(context, "网络出错", 0).show();

			}

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == event.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(mContext, TrainingActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
