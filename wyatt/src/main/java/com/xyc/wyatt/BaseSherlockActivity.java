package com.xyc.wyatt;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.xyc.wyatt.util.DensityUtil;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.view.SlipView;
import com.xyc.wyatt.view.SlipView.OnMenuCloseListener;
import com.xyc.wyatt.view.SlipView.OnMenuOpenListener;
import com.xyc.wyatt.view.SlipView.OnMenuSlipingListener;

/**
 * 所有三级以上Activity的基类
 * 
 * @author pengjiazhen
 *
 */
public abstract class BaseSherlockActivity extends SherlockActivity {
	private final String TAG = "BaseActivity";

	protected Context mContext;
	private int submMenuResId;
	private OnMenuItemClickListener itemClickListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;

	}

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void init();

	/**
	 * 注册点击事件
	 */
	protected abstract void initEvent();

	/***************** 标题栏处理 *******************/

	protected void initActionBar(CharSequence title, boolean homeBackEnable,
			int submMenuResId, final Context context,
			OnMenuItemClickListener itemClickListener) {
		setActionBar(title, homeBackEnable, false, null);
		setResource(submMenuResId, context, itemClickListener);
	}

	protected void initActionBar(int titleResId, boolean homeBackEnable,
			int submMenuResId, final Context context,
			OnMenuItemClickListener itemClickListener) {
		setActionBar(getResources().getString(titleResId), homeBackEnable,
				false, null);
		setResource(submMenuResId, context, itemClickListener);
	}

	protected void initActionBar(CharSequence title, int submMenuResId,
			final Context context, OnMenuItemClickListener itemClickListener) {
		setActionBar(title, true, false, null);
		setResource(submMenuResId, context, itemClickListener);
	}

	public void initActionBar(int titleResId, boolean homeBackEnable) {
		setActionBar(getResources().getString(titleResId), homeBackEnable,
				false, null);
	}

	public void initActionBar(CharSequence title, boolean homeBackEnable) {
		setActionBar(title, homeBackEnable, false, null);
	}

	public void initActionBar(CharSequence title, boolean homeBackEnable,
			boolean showCustomEnable, View customView) {
		setActionBar(title, homeBackEnable, showCustomEnable, customView);
	}

	public void setResource(int submMenuResId, final Context context,
			OnMenuItemClickListener itemClickListener) {
		this.mContext = context;
		this.submMenuResId = submMenuResId;
		this.itemClickListener = itemClickListener;
	}

	public void setActionBar(CharSequence title, boolean homeBackEnable,
			boolean showCustomEnable, View customView) {
		if (customView == null) {
			getSupportActionBar().setCustomView(R.layout.main_action_bar);
		} else {
			getSupportActionBar().setCustomView(customView);
		}
		getSupportActionBar().setDisplayShowCustomEnabled(showCustomEnable);
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
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void makeLongToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
	}

	protected void makeShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
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
	/**
	 * 分享数据到微信好友
	 */
	protected void shareApplication(String text) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(intent);
	}
}
