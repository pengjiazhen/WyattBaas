package com.xyc.wyatt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.xyc.wyatt.domain.WTimePattern;
import com.xyc.wyatt.util.DensityUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;
import com.xyc.wyatt.view.RoundedImageView;
import com.xyc.wyatt.view.SlipView;
import com.xyc.wyatt.view.SlipView.OnMenuCloseListener;
import com.xyc.wyatt.view.SlipView.OnMenuOpenListener;
import com.xyc.wyatt.view.SlipView.OnMenuSlipingListener;
import com.xyc.wyatt.view.XListView;
import com.xyc.wyatt.view.XListView.IXListViewListener;

/**
 * 所有一级二级Activity的基类
 * 
 * @author pengjiazhen
 *
 */
public class BaseActivity extends SherlockActivity implements OnClickListener {
	private final String TAG = "BaseActivity";

	protected Context mContext;
	private int submMenuResId;
	private OnMenuItemClickListener itemClickListener;
	protected SharedPreferences sp;

	/**
	 * 侧滑菜单自定义控件
	 */
	protected SlipView sv;

	private ImageView iv_training_day;
	private ImageView iv_traininghistory;
	private ImageView iv_wyattcirle;
	private ImageView iv_setting;

	private TextView tv_training_day;
	private TextView tv_traininghistory;
	private TextView tv_wyattcirle;
	private TextView tv_setting;

	private RelativeLayout rl_avatar;// 头像
	private RelativeLayout rl_training_day;// 训练日
	private RelativeLayout rl_traininghistory;// 我的运动记录
	private RelativeLayout rl_wyattcirle;// 悦动圈
	private RelativeLayout rl_setting;// 设置
	private TextView tv_username;// 用户名
	private RoundedImageView riv_avatar;// 用户头像

	private LinearLayout ll;
	private LinearLayout ll_content_overflay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_base);
		mContext = this;
		sv = (SlipView) findViewById(R.id.sv);
		initBaseView();
		initBase();
		initBaseEvent();

	}

	protected void initBaseView() {
		ll = (LinearLayout) sv.findViewById(R.id.content);
		ll_content_overflay = (LinearLayout) sv
				.findViewById(R.id.ll_content_overflay);
		rl_avatar = (RelativeLayout) sv.findViewById(R.id.rl_avatar);
		rl_training_day = (RelativeLayout) sv
				.findViewById(R.id.rl_training_day);
		rl_traininghistory = (RelativeLayout) sv
				.findViewById(R.id.rl_traininghistory);
		rl_wyattcirle = (RelativeLayout) sv.findViewById(R.id.rl_wyattcirle);
		rl_setting = (RelativeLayout) sv.findViewById(R.id.rl_setting);

		iv_setting = (ImageView) sv.findViewById(R.id.iv_setting);
		iv_wyattcirle = (ImageView) sv.findViewById(R.id.iv_wyattcirle);
		iv_traininghistory = (ImageView) sv
				.findViewById(R.id.iv_traininghistory);
		iv_training_day = (ImageView) sv.findViewById(R.id.iv_training_day);

		tv_setting = (TextView) sv.findViewById(R.id.tv_setting);
		tv_wyattcirle = (TextView) sv.findViewById(R.id.tv_wyattcirle);
		tv_traininghistory = (TextView) sv
				.findViewById(R.id.tv_traininghistory);
		tv_training_day = (TextView) sv.findViewById(R.id.tv_training_day);
		tv_username = (TextView) sv.findViewById(R.id.tv_username);
		riv_avatar = (RoundedImageView) sv.findViewById(R.id.riv_avatar);
	}

	protected void initBase() {

	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	protected void initBaseEvent() {
		rl_avatar.setOnClickListener(this);
		rl_training_day.setOnClickListener(this);
		rl_traininghistory.setOnClickListener(this);
		rl_wyattcirle.setOnClickListener(this);
		rl_setting.setOnClickListener(this);

		sv.setmOnMenuOpenListener(new OnMenuOpenListener() {
			@Override
			public void onOpen(SlipView v) {
				//Log.e(TAG, "菜单打开了");
				// TODO 检测用户的登陆状态 用来改变侧滑框中的 头像的状态
				if (GloableValue.user.getStatus() == WTContant.LOGIN) {
					// 处于登陆状态的时候
					// 头像设置
					String avatarPath = GloableValue.user.getAvatar();
					if (avatarPath != null && !"".equals(avatarPath)) {
						if (avatarPath.startsWith("http")) {
							riv_avatar.showImage(avatarPath, false);
						} else {
							riv_avatar.showImage(avatarPath, true);
						}
					}
//					else {
//						String path = WTContant.SERVER_IP + "/test1.jpg";
//						riv_avatar.showImage(path, false);
//					}
					// 用户名设置为登陆
					tv_username.setText(GloableValue.user.getUserName());

				} else {
					// 在没有登陆的状态
					// 头像设置
					riv_avatar.setImageResource(R.drawable.avatar_default1);

					// 用户名设置为登陆
					tv_username.setText(R.string.username_default);

				}

				switch (GloableValue.currentContent) {
				case 1:
					iv_training_day
							.setImageResource(R.drawable.wt_training_day_red);
					tv_training_day.setTextColor(getResources().getColor(
							R.color.wt_menu_text_red));
					break;
				case 2:
					iv_traininghistory
							.setImageResource(R.drawable.wt_run_history_red);
					tv_traininghistory.setTextColor(getResources().getColor(
							R.color.wt_menu_text_red));
					break;
				case 3:

					iv_wyattcirle
							.setImageResource(R.drawable.wt_wyatt_circle_red);
					tv_wyattcirle.setTextColor(getResources().getColor(
							R.color.wt_menu_text_red));
					break;
				case 4:
					iv_setting.setImageResource(R.drawable.wt_setting_red);
					tv_setting.setTextColor(getResources().getColor(
							R.color.wt_menu_text_red));
					break;

				default:
					break;
				}
			}
		});
		sv.setmOnMenuCloseListener(new OnMenuCloseListener() {

			@Override
			public void onClose(SlipView v) {
				// ll.setBackgroundColor(Color.WHITE);
			}
		});

		sv.setmOnMenuSlipingListener(new OnMenuSlipingListener() {

			@Override
			public void onSliping(SlipView v, float scale) {
				int alpha = (int) (ll.getAlpha() + (1 - scale) * 33);
				ll_content_overflay.setBackgroundColor(Color.argb(alpha, 00,
						00, 00));
			}
		});
	}

	/*********** 处理右边的视图 ***********/
	protected void addContentView(View view) {
		sv.addContentView(view);
	}

	/***************** 标题栏处理 *******************/

	protected void initActionBar(CharSequence title, boolean homeBackEnable,
			int submMenuResId, final Context context,
			OnMenuItemClickListener itemClickListener) {
		setActionBar(title, homeBackEnable);
		setResource(submMenuResId, context, itemClickListener);
	}

	protected void initActionBar(int titleResId, boolean homeBackEnable,
			int submMenuResId, final Context context,
			OnMenuItemClickListener itemClickListener) {
		setActionBar(getResources().getString(titleResId), homeBackEnable);
		setResource(submMenuResId, context, itemClickListener);
	}

	protected void initActionBar(CharSequence title, int submMenuResId,
			final Context context, OnMenuItemClickListener itemClickListener) {
		setActionBar(title, true);
		setResource(submMenuResId, context, itemClickListener);
	}

	public void initActionBar(int titleResId, boolean homeBackEnable) {
		setActionBar(getResources().getString(titleResId), homeBackEnable);
	}

	public void initActionBar(CharSequence title, boolean homeBackEnable) {
		setActionBar(title, homeBackEnable);
	}

	public void setResource(int submMenuResId, final Context context,
			OnMenuItemClickListener itemClickListener) {
		this.mContext = context;
		this.submMenuResId = submMenuResId;
		this.itemClickListener = itemClickListener;
	}

	public void setActionBar(CharSequence title, boolean homeBackEnable) {
		getSupportActionBar().setCustomView(R.layout.main_action_bar);
		getSupportActionBar().setDisplayShowCustomEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		// getSupportActionBar().setIcon(
		// getResources().getDrawable(R.drawable.run_man_48));
		// getSupportActionBar().setLogo(
		// getResources().getDrawable(R.drawable.head_image_1));
		getSupportActionBar().setTitle(title);
		getSupportActionBar().setDisplayHomeAsUpEnabled(homeBackEnable);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.bg_title_bar));

	}

	/**
	 * 改变actionbar的标题
	 * 
	 * @param title
	 */
	public void changeActionBarTitle(String title) {
		getSupportActionBar().setTitle(title);
	}

	/**
	 * 给出一个菜单项,可以重写 - 如果不需要任何的菜单在actionbar右边显示，可以重写此方法，此处可以返回0。 -
	 * 同样如果需要添加其他的菜单在actionbar中，可以重写此方法，并且返回这个菜单的resid。
	 * 这个时候需要重写onOptionsItemSelected方法监听这个菜单的点击事件
	 * ，不要忘记监听返回按钮，其id为android.R.id.home
	 * 
	 * @return
	 */
	protected int getOptionsMenu() {
		return 0;
	};

	/**
	 * actionbar菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getOptionsMenu() == 0) {
			return false;
		}

		// 子类需要复写
		getSupportMenuInflater().inflate(getOptionsMenu(), menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 响应actionbar overFlowView 菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		// 此处还可以做其他的bar响应，如果需要其他的，可以复写这个方法
		switch (id) {
		case android.R.id.home:
			sv.toggle();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 如果侧滑菜单已经打开了，就直接关闭
			boolean open = sv.isOpen();
			if (open) {
				sv.toggle();
				return false;
			} else {
				// 弹出对话框，询问是否要退出应用
				showAlertDialog();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// Log.e("BaseActivity", "点击了屏幕");
		//sv.toggle();
		int id = v.getId();
		switch (id) {
		case R.id.rl_avatar:
			if (GloableValue.currentContent != 0) {
				Log.e("slipview", "头像");
				if (GloableValue.user.getStatus() == WTContant.LOGIN) {
					Intent intent = new Intent();
					intent.setClass(mContext, UserInfoActivity.class);
					startActivity(intent);
					sv.toggle();
				} else {
					Intent intent = new Intent();
					intent.setClass(mContext, LoginActivity.class);
					startActivity(intent);
					sv.toggle();
					finish();
					
				}

			}

			break;
		case R.id.rl_training_day:
			if (GloableValue.currentContent != 1) {
				Intent intent = new Intent();
				intent.setClass(mContext, TrainingActivity.class);
				startActivity(intent);

				resetMenuImage();
				resetMenuTextColor();
				iv_training_day
						.setImageResource(R.drawable.wt_training_day_red);
				tv_training_day.setTextColor(getResources().getColor(
						R.color.wt_menu_text_red));
				finish();
			}
			break;
		case R.id.rl_traininghistory:
			if (GloableValue.currentContent != 2) {
				Intent intent = new Intent();
				intent.setClass(mContext, RunningRecorderActivity.class);
				startActivity(intent);

				resetMenuImage();
				resetMenuTextColor();
				iv_traininghistory
						.setImageResource(R.drawable.wt_run_history_red);
				tv_traininghistory.setTextColor(getResources().getColor(
						R.color.wt_menu_text_red));
				finish();
			}
			break;
		case R.id.rl_wyattcirle:
			if (GloableValue.currentContent != 3) {
				Log.e("slipview", "悦动圈");

				Intent intent = new Intent();
				intent.setClass(mContext, WyattcirleActivity.class);
				startActivity(intent);

				resetMenuImage();
				resetMenuTextColor();
				iv_wyattcirle.setImageResource(R.drawable.wt_wyatt_circle_red);
				tv_wyattcirle.setTextColor(getResources().getColor(
						R.color.wt_menu_text_red));

				finish();
			}
			break;
		case R.id.rl_setting:
			if (GloableValue.currentContent != 4) {
				Log.e("slipview", "设置");

				Intent intent = new Intent();
				intent.setClass(mContext, SettingActivity.class);
				startActivity(intent);

				resetMenuImage();
				resetMenuTextColor();
				iv_setting.setImageResource(R.drawable.wt_setting_red);
				tv_setting.setTextColor(getResources().getColor(
						R.color.wt_menu_text_red));
				finish();
			}
		default:
			break;
		}
		sv.isOpen=false;
	}

	protected void makeLongToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}

	protected void makeShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	protected void resetMenuImage() {
		iv_setting.setImageResource(R.drawable.wt_setting_gray);
		iv_training_day.setImageResource(R.drawable.wt_training_day_gray);
		iv_traininghistory.setImageResource(R.drawable.wt_run_history_gray);
		iv_wyattcirle.setImageResource(R.drawable.wt_wyatt_circle_gray);
	}

	protected void resetMenuTextColor() {
		tv_setting.setTextColor(getResources().getColor(
				R.color.wt_menu_text_gray));
		tv_training_day.setTextColor(getResources().getColor(
				R.color.wt_menu_text_gray));
		tv_traininghistory.setTextColor(getResources().getColor(
				R.color.wt_menu_text_gray));
		tv_wyattcirle.setTextColor(getResources().getColor(
				R.color.wt_menu_text_gray));
	}

	// 退出确认弹出框
	protected void showAlertDialog() {
		Builder builder = new AlertDialog.Builder(mContext);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(mContext, R.layout.dialog_exit_app, null);
		dialog.show();

		setDialogParams(dialog, view, 800, Gravity.CENTER);

		TextView cancle = (TextView) view.findViewById(R.id.tv_cancle);
		TextView comfirm = (TextView) view.findViewById(R.id.tv_comfirm);

		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		comfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				finish();
			}
		});
	}

	/**
	 * 设置弹出框的参数
	 * 
	 * @param dialog
	 * @param view
	 * @param height
	 * @param gravity
	 */
	protected void setDialogParams(AlertDialog dialog, View view, int height,
			int gravity) {
		// 得到屏幕宽度
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		int dialogWidth = (int) (metrics.widthPixels * 0.75);// dialog的宽度是屏幕宽度的0.75

		Window dialogWindow = dialog.getWindow();
		android.view.WindowManager.LayoutParams params = dialogWindow
				.getAttributes();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = dialogWidth;
		dialogWindow.setGravity(gravity);
		dialog.addContentView(view, params);
	}

	/**
	 * 
	 */
	private boolean isRequesting;
	private String lastTime;

	/**
	 * 提供子类去写里面的方法来实现在上啦刷新和下拉刷新的事件
	 * 
	 * @author Administrator
	 * 
	 */
	interface SetRefreshThings {
		/**
		 * 在下拉刷新中要做的事
		 */
		void pullDownToRefresh();

		/**
		 * 在上拉刷新中要做的事
		 */
		void pullUpToRefresh();
	}

	/**
	 * 设置listview的上拉刷新和下拉刷新
	 * 
	 * @param listView
	 *            要设置的listview对象
	 * @param setRefreshThings
	 *            在上拉刷新和下拉刷新中要做的事
	 */
	@Deprecated
	public void setRefresh(XListView listView, SetRefreshThings setRefreshThings) {

		final XListView finalListView = listView;
		final SetRefreshThings finalsetRefreshThing = setRefreshThings;
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);

		lastTime = String.valueOf(System.currentTimeMillis());
		finalListView.setRefreshTime(WTUtil
				.getSKTimePattern(lastTime, mContext));
		listView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {

				if (finalsetRefreshThing != null && isRequesting == false) {
					isRequesting = true;
					finalsetRefreshThing.pullDownToRefresh();
					isRequesting = false;
				}
				finalListView.setRefreshTime(WTUtil.getSKTimePattern(lastTime,
						mContext));
				lastTime = String.valueOf(System.currentTimeMillis());
			}

			@Override
			public void onLoadMore() {
				if (finalsetRefreshThing != null && isRequesting == false) {
					isRequesting = true;
					finalsetRefreshThing.pullUpToRefresh();
					isRequesting = false;
				}
			}
		});
	}

	public void setRefresh(XListView listView,
			SetRefreshThings setRefreshThings, boolean PullRefreshEnable,
			boolean PullLoadEnable) {

		final XListView finalListView = listView;
		final SetRefreshThings finalsetRefreshThing = setRefreshThings;
		listView.setPullRefreshEnable(PullRefreshEnable);
		listView.setPullLoadEnable(PullLoadEnable);

		lastTime = String.valueOf(System.currentTimeMillis());
		finalListView.setRefreshTime(WTUtil
				.getSKTimePattern(lastTime, mContext));

		listView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {

				if (finalsetRefreshThing != null && isRequesting == false) {
					isRequesting = true;
					finalsetRefreshThing.pullDownToRefresh();
					isRequesting = false;
				}
				finalListView.setRefreshTime(WTUtil.getSKTimePattern(lastTime,
						mContext));
				lastTime = String.valueOf(System.currentTimeMillis());
			}

			@Override
			public void onLoadMore() {
				if (finalsetRefreshThing != null && isRequesting == false) {
					isRequesting = true;
					finalsetRefreshThing.pullUpToRefresh();
					isRequesting = false;
				}
			}
		});
	}

	/**
	 * 显示actionbar的progress
	 */
	protected void showProgress() {
		setSupportProgressBarIndeterminateVisibility(true);
	}

	/**
	 * 让actionbar的progress消失
	 */
	protected void dismissProgress() {
		setSupportProgressBarIndeterminateVisibility(false);
	}

}
