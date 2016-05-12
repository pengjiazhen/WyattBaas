package com.xyc.wyatt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.Bmob;
import cn.domob.android.ads.RTSplashAd;
import cn.domob.android.ads.RTSplashAdListener;
import cn.domob.android.ads.SplashAd;
import cn.domob.android.ads.SplashAdListener;
import cn.domob.android.ads.SplashAd.SplashMode;
import cn.jpush.android.api.JPushInterface;

import com.xyc.wyatt.dao.RunRecordDao;
import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.manager.CallBack;
import com.xyc.wyatt.manager.RunRecordManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.JsonUtil;
import com.xyc.wyatt.util.StreamTools;
import com.xyc.wyatt.util.WTContant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 插屏广告界面
 * 
 * @author pengjiazhen
 *
 */
public class SplashActivity extends Activity {

	private Context mContext;
	protected static final String TAG = "SplashActivity";
	protected static final int SHOW_UPDATE_DIALOG = 5;
	protected static final int ENTER_HOME = 1;
	protected static final int URL_ERROR = 2;
	protected static final int NETWORK_ERROR = 3;
	protected static final int JSON_ERROR = 4;
	private TextView tv_splash_version;
	private String description;
	private TextView tv_update_info;
	private int userid;
	SplashAd splashAd;
	RTSplashAd rtSplashAd;
	public static final String PUBLISHER_ID = "56OJ2jGIuN3csBlAjP";
	public static final String SplashPPID = "16TLPkXoApn2WNUvS57U-gNk";
	// 缓存开屏广告:true 实时开屏广告:false
	// Cache splash ad:true Real-time splash ad:false
	private boolean isSplash = true;
	/**
	 * 新版本的下载地址
	 */
	private String apkurl;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.d(TAG, "[ExampleApplication] onCreate");
		super.onCreate(savedInstanceState);
		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		setContentView(R.layout.activity_splash);
		 // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, "90350b480fd173c8ef7a445825142eec");
		mContext = this;
		//initAd();
		initView();
		sp = getSharedPreferences("wt", MODE_PRIVATE);
		boolean auto_update = sp.getBoolean("auto_update", false);
		if (auto_update) {
			// 检查升级
			checkUpdate();
		} else {
			// 自动升级已经关闭
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// 进入主页面
					enterHome();
				}
			}, 2000);

		}
		// 用户自动登录
		automaticLogin();
		// 获取用户的runRecord信息
		userid = GloableValue.user.getId();
		//getRunRecord();

		GloableValue.mContext = getApplicationContext();

	}

	private void initAd() {
		if (isSplash) {
			// 缓存开屏广告
			// Cache splash ad
			splashAd = new SplashAd(this, PUBLISHER_ID, SplashPPID,
					SplashMode.SplashModeFullScreen);
			// setSplashTopMargin is available when you choose non-full-screen
			// splash mode.
			// splashAd.setSplashTopMargin(200);
			splashAd.setSplashAdListener(new SplashAdListener() {
				@Override
				public void onSplashPresent() {
					Log.i("DomobSDKDemo", "onSplashStart");
				}

				@Override
				public void onSplashDismiss() {
					Log.i("DomobSDKDemo", "onSplashClosed");
					// 开屏回调被关闭时，立即进行界面跳转，从开屏界面到主界面。
					// When splash ad is closed, jump to the next(main) Activity
					// immediately.
					// jump();
					// 如果应用没有单独的闪屏Activity，需要调用closeSplash方法去关闭开屏广告
					// If you do not carry a separate advertising activity, you
					// need to call closeRTSplash way to close the splash ad
					// splashAd.closeSplash();
				}

				@Override
				public void onSplashLoadFailed() {
					Log.i("DomobSDKDemo", "onSplashLoadFailed");
				}
			});

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (splashAd.isSplashAdReady()) {
						splashAd.splash(SplashActivity.this,
								SplashActivity.this
										.findViewById(R.id.splash_holder));
					} else {
						Toast.makeText(SplashActivity.this,
								"Splash ad is NOT ready.", Toast.LENGTH_SHORT)
								.show();
						// jump();
					}
				}
			}, 5);
		} else {
			// 实时开屏广告
			// Real-time splash ad
			rtSplashAd = new RTSplashAd(this, PUBLISHER_ID, SplashPPID,
					SplashMode.SplashModeFullScreen);
			// setRTSplashTopMargin is available when you choose non-full-screen
			// splash mode.
			// rtSplashAd.setRTSplashTopMargin(200);
			rtSplashAd.setRTSplashAdListener(new RTSplashAdListener() {
				@Override
				public void onRTSplashDismiss() {
					Log.i("DomobSDKDemo", "onRTSplashClosed");
					// 开屏回调被关闭时，立即进行界面跳转，从开屏界面到主界面。
					// When rtSplash ad is closed, jump to the next(main)
					// Activity immediately.
					// jump();
					// 如果应用没有单独的闪屏Activity，需要调用closeRTSplash方法去关闭开屏广告
					// If you do not carry a separate advertising activity, you
					// need to call closeRTSplash way to close the splash ad

					// rtSplashAd.closeRTSplash();
				}

				@Override
				public void onRTSplashLoadFailed() {
					Log.i("DomobSDKDemo", "onRTSplashLoadFailed");
				}

				@Override
				public void onRTSplashPresent() {
					Log.i("DomobSDKDemo", "onRTSplashStart");
				}

			});

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					rtSplashAd.splash(SplashActivity.this,
							SplashActivity.this
									.findViewById(R.id.splash_holder));
					// rtSplashAd.splash(SplashScreenActivity.this,
					// SplashScreenActivity.this.findViewById(R.id.splash_holder));
				}
			}, 1);
		}
	}

	@Override
	protected void onPause() {
		JPushInterface.onPause(mContext);
		super.onPause();
	}

	@Override
	protected void onResume() {
		JPushInterface.onResume(mContext);
		super.onResume();
	}

	/**
	 * 用户的自动登录
	 */
	private void automaticLogin() {
		// TODO 自动登录 在是splash中 检车用户的数据库中的状态
		UserDao dao = new UserDao(mContext);

		List<User> users = dao.findByCondition("where status=?",
				new String[] { String.valueOf(WTContant.LOGIN) });

		for (User user : users) {
			GloableValue.user.setObjectId(user.getObjectId());
			GloableValue.user.setId(user.getId());
			GloableValue.user.setUserName(user.getUserName());
			GloableValue.user.setStatus(user.getStatus());
			GloableValue.user.setWeight(user.getWeight());
			GloableValue.user.setHeight(user.getHeight());
			GloableValue.user.setAvatar(user.getAvatar());
			GloableValue.user.setAge(user.getAge());
			GloableValue.user.setSex(user.getSex());
		}

	}

	/*private void getRunRecord() {
		// TODO 获取用户的运动记录信息 并写到数据库中
		new Thread(new Runnable() {

			@Override
			public void run() {
				RunRecordManager.getRunRrcord(mContext, userid, new CallBack() {
					@Override
					public void before() {
					}

					@Override
					public void success(String result) {
						List<RunRecord> currentList = JsonUtil.getList(result,
								"recordList", RunRecord.class);
						for (RunRecord record : currentList) {
							RunRecordDao dao = new RunRecordDao(mContext);
							dao.saveOrUpdata(record, "id=?",
									new String[] { String.valueOf(record
											.getId()) }, "id=?",
									new String[] { String.valueOf(record
											.getId()) });
						}
					}

					@Override
					public void fail(String errorMessage) {
					}
				});

			}
		}).start();

	}*/

	private void initView() {
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:// 显示升级的对话框
				Log.i(TAG, "显示升级的对话框");
				showUpdateDialog();
				break;
			case ENTER_HOME:// 进入主页面
				enterHome();
				break;

			case URL_ERROR:// URL错误
				enterHome();
				Toast.makeText(getApplicationContext(), "URL错误", 0).show();

				break;

			case NETWORK_ERROR:// 网络异常
				enterHome();
				Toast.makeText(SplashActivity.this, "网络异常", 0).show();
				break;

			case JSON_ERROR:// JSON解析出错
				enterHome();
				Toast.makeText(SplashActivity.this, "JSON解析出错", 0).show();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 检查是否有新版本，如果有就升级
	 */
	private void checkUpdate() {
		new Thread() {
			public void run() {
				// URLhttp://192.168.1.254:8080/updateinfo.html
				Message mes = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {

					URL url = new URL(getString(R.string.serverurl));
					// 联网
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if (code == 200) {
						// 联网成功
						InputStream is = conn.getInputStream();
						// 把流转成String
						String result = StreamTools.readFromStream(is);
						Log.i(TAG, "联网成功了" + result);
						// json解析
						JSONObject obj = new JSONObject(result);
						// 得到服务器的版本信息
						String version = (String) obj.get("version");

						description = (String) obj.get("description");
						// description = new
						// String(description.getBytes("ISO8859-1"),"UTF-8");
						apkurl = (String) obj.get("apkurl");

						Log.e(TAG, "版本号" + version + "::描述" + description
								+ "::apk地址");

						// 校验是否有新版本
						if (getVersionName().equals(version)) {
							// 版本一致，没有新版本，进入主页面
							mes.what = ENTER_HOME;
						} else {
							// 有新版本，弹出一升级对话框
							mes.what = SHOW_UPDATE_DIALOG;
						}

					} else {
						mes.what = URL_ERROR;
					}

				} catch (MalformedURLException e) {
					mes.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					mes.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
					mes.what = JSON_ERROR;
				} finally {

					long endTime = System.currentTimeMillis();
					// 我们花了多少时间
					long dTime = endTime - startTime;
					// 2000
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					handler.sendMessage(mes);
				}

			};
		}.start();

	}

	protected void enterHome() {
		Intent intent = new Intent();
		intent.setClass(mContext, TrainingActivity.class);
		startActivity(intent);
		finish();

	}

	/**
	 * 得到应用程序的版本名称
	 */

	private String getVersionName() {
		// 用来管理手机的APK
		PackageManager pm = getPackageManager();

		try {
			// 得到知道APK的功能清单文件
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}

	}

	/**
	 * 弹出升级对话框
	 */
	protected void showUpdateDialog() {
		// this = Activity.this
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("提示升级");
		// builder.setCancelable(false);//强制升级
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// 进入主页面
				enterHome();
				dialog.dismiss();

			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("立刻升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 下载APK，并且替换安装
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// sdcard存在
					// afnal
					FinalHttp finalhttp = new FinalHttp();
					finalhttp.download(apkurl, Environment
							.getExternalStorageDirectory().getAbsolutePath()
							+ "/mobilesafe2.0.apk", new AjaxCallBack<File>() {

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							t.printStackTrace();
							Toast.makeText(getApplicationContext(), "下载失败", 1)
									.show();
							enterHome();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							tv_update_info.setVisibility(View.VISIBLE);
							// 当前下载百分比
							int progress = (int) (current * 100 / count);
							tv_update_info.setText("下载进度：" + progress + "%");
						}

						@Override
						public void onSuccess(File t) {
							super.onSuccess(t);
							installAPK(t);
						}

						/**
						 * 安装APK
						 * 
						 * @param t
						 */
						private void installAPK(File t) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t),
									"application/vnd.android.package-archive");

							startActivity(intent);

						}

					});
				} else {
					Toast.makeText(getApplicationContext(), "没有sdcard，请安装上在试",
							0).show();
					return;
				}

			}
		});
		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				enterHome();// 进入主页面
			}
		});
		builder.show();

	}

}
