package com.xyc.wyatt.view;

import com.nineoldandroids.view.ViewHelper;
import com.xyc.wyatt.R;
import com.xyc.wyatt.TrainingActivity;
import com.xyc.wyatt.util.GloableValue;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SlipView extends HorizontalScrollView {

	private final String TAG="SlipView";
	/**
	 * 右边的边距
	 */
	private int mPaddingRight;
	private LinearLayout mWrapper;
	private FrameLayout mContentWrapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private ViewGroup mContentOverflay;
	private int mScreenWidth;
	private boolean once = false;
	private int mMenuWidth;
	private int mContentWidth;
	public static boolean isOpen;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		if(!GloableValue.isSlideButton){
			return super.onInterceptTouchEvent(ev);
		}
		
		return false;
	}
	
	/**
	 * 有用到自定义属性时调用
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取自定义属性的值
		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.slipView, defStyle, 0);
		for (int i = 0; i < ta.length(); i++) {
			int index = ta.getIndex(i);
			switch (index) {
			case R.styleable.slipView_padding_Right:
				mPaddingRight = (int) ta.getDimensionPixelSize(index,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		ta.recycle();
		// 得到屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		/**
		 * 一个结构描述屏幕显示的一般信息，如它的大小，密度，和字体缩放。
		 */
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);

		mScreenWidth = metrics.widthPixels;

	}

	/**
	 * 未使用自定义属性是调用
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlipView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 当用new关键字实例化得时候调用
	 * 
	 * @param context
	 */
	public SlipView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置子View的宽和高 设置自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mPaddingRight;
			mContent.getLayoutParams().width = mScreenWidth;
			mContentOverflay.getLayoutParams().width = mScreenWidth;
			once = true;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 通过设置偏移量将menu掩藏
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			scrollTo(mMenuWidth, 0);
		}
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		
		case MotionEvent.ACTION_UP:
			// 左边掩藏的宽度
			int scrollX = getScrollX();
			if (scrollX <= mMenuWidth / 2) {
				if (mOnMenuOpenListener != null) {
					mOnMenuOpenListener.onOpen(this);
				}
				this.smoothScrollTo(0, 0);
				isOpen = true;
			} else {
				if (mOnMenuCloseListener != null) {
					mOnMenuCloseListener.onClose(this);
				}
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			}

			return true;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 打开菜单
	 */
	public void openMenu() {
		if (isOpen)
			return;
		if (mOnMenuOpenListener != null) {
			mOnMenuOpenListener.onOpen(this);
		}
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	public void closeMenu() {
		if (!isOpen)
			return;
		if (mOnMenuCloseListener != null) {
			mOnMenuCloseListener.onClose(this);
		}
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;

	}

	/**
	 * 切换菜单
	 */
	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	/**
	 * 在view滚动的时候调用
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		/**
		 * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
		 * 
		 * 区别2：菜单的偏移量需要修改
		 * 
		 * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0 0.6+
		 * 0.4 * (1- scale) ;
		 * 
		 */
		float scale = l * 1.0f / mMenuWidth;
		float rightScale = 0.7f + 0.3f * scale;
		float leftScale = 1.0f - scale * 0.3f;
		float leftAlpha = 0.6f + 0.4f * (1 - scale);

		// 调用属性动画，设置TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.8f);
		// ViewHelper.setScaleX(mMenu, leftScale);
		// ViewHelper.setScaleY(mMenu, leftScale);
		// ViewHelper.setAlpha(mMenu, leftAlpha);
		// 设置content的缩放的中心点
		// ViewHelper.setPivotX(mContent, 0);
		// ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		// ViewHelper.setScaleX(mContent, rightScale);
		// ViewHelper.setScaleY(mContent, rightScale);
		
		if(mOnMenuSlipingListener != null){
			mOnMenuSlipingListener.onSliping(this,scale);
		}

	}

	public void addContentView(View view) {
		initView();
		mContent.addView(view);
	}

	
	public void initView() {
		mWrapper = (LinearLayout) getChildAt(0);
		mMenu = (ViewGroup) mWrapper.getChildAt(0);
		mContentWrapper = (FrameLayout) mWrapper.getChildAt(1);
		mContent = (ViewGroup) mContentWrapper.getChildAt(0);
		mContentOverflay = (ViewGroup) mContentWrapper.getChildAt(1);
		initMenuItemEvent();
	}

	public void initMenuItemEvent() {
	
	}

	public void setContentBackground(int resId) {
		mContent.setBackgroundResource(resId);
	}

	private OnMenuOpenListener mOnMenuOpenListener;

	/**
	 * 菜单打开监听器
	 * @author pengjiazhen
	 *
	 */
	public interface OnMenuOpenListener {
		public void onOpen(SlipView v);
	}

	private OnMenuCloseListener mOnMenuCloseListener;

	/**
	 * 菜单关闭监听器
	 * @author pengjiazhen
	 *
	 */
	public interface OnMenuCloseListener {
		public void onClose(SlipView v);
	}
	
	private OnMenuSlipingListener mOnMenuSlipingListener;

	/**
	 * 菜单打开过程中监听器
	 * @author pengjiazhen
	 *
	 */
	public interface OnMenuSlipingListener {
		public void onSliping(SlipView v,float scale);
	}

	public void setmOnMenuSlipingListener(
			OnMenuSlipingListener mOnMenuSlipingListener) {
		this.mOnMenuSlipingListener = mOnMenuSlipingListener;
	}

	public void setmOnMenuOpenListener(OnMenuOpenListener mOnMenuOpenListener) {
		this.mOnMenuOpenListener = mOnMenuOpenListener;
	}

	public void setmOnMenuCloseListener(OnMenuCloseListener mOnMenuCloseListener) {
		this.mOnMenuCloseListener = mOnMenuCloseListener;
	}

	public boolean isOpen() {
		return isOpen;
	}

//	public void setOpen(boolean isOpen) {
//		this.isOpen = isOpen;
//	}

}
