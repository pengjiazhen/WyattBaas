package com.xyc.wyatt.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.listener.FindListener;

import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.manager.RunRecordManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class BaseChartView extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	protected static final String TAG = "BaseChartView";
	protected SurfaceHolder mSurfaceHolder;
	protected Canvas mCanvas;
	protected float density;
	protected int densityDpi;
	protected int heightPixels;
	protected int widthPixels;
	protected int drawType;// 0表示画背景 1表示画均线
	protected int num = 5;// 等分格的个数
	protected int left = 100;// 距离左边10
	protected int right;// 距离右边
	protected int BChartTop = 50;// 距离top 50
	protected int BChartbottom;
	protected int totalWidth;// 背景图总共的宽度
	protected int perWidth;// 等分线，每一格的宽度
	protected Paint LineGrayPaint;
	protected Paint textGrayPaint;
	protected Paint textRedPaint;
	private boolean isRunning = false;
	protected List<RunRecord> records;
	protected Paint backGroundPaint;
	protected Paint lineWhitePaint;
	protected Paint pointWhitePaint;

	public List<RunRecord> getRecords() {
		return records;
	}

	public void setRecords(List<RunRecord> records) {
		this.records = records;

	}

	public void init() {
		
		setZOrderOnTop(true);// 设置置顶（不然实现不了透明）
		mSurfaceHolder = getHolder();
		// 添加回调接口
		mSurfaceHolder.addCallback(this);
		// 设置背景透明
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);

		// 获取手机设备的屏幕信息
		DisplayMetrics displayMetrics = this.getContext().getResources()
				.getDisplayMetrics();
		density = displayMetrics.density;
		densityDpi = displayMetrics.densityDpi;
		heightPixels = displayMetrics.heightPixels;
		widthPixels = displayMetrics.widthPixels;

		// 确定背景图的宽高
		right = widthPixels - 20;
		BChartbottom = heightPixels / 4;
		totalWidth = right - left;
		perWidth = totalWidth / num;

		// 初始化画笔
		LineGrayPaint = new Paint();
		LineGrayPaint.setColor(Color.GRAY);
		LineGrayPaint.setAntiAlias(true);
		LineGrayPaint.setStrokeWidth(1);
		LineGrayPaint.setStyle(Style.STROKE);

		lineWhitePaint = new Paint();
		lineWhitePaint.setColor(Color.rgb(255, 255, 255));
		lineWhitePaint.setAntiAlias(true);
		lineWhitePaint.setStrokeWidth(1);
		lineWhitePaint.setStyle(Style.STROKE);

		textGrayPaint = new Paint();
		textGrayPaint.setColor(Color.GRAY);
		textGrayPaint.setAntiAlias(true);
		textGrayPaint.setTextSize(22);

		backGroundPaint = new Paint();
		backGroundPaint.setColor(Color.GRAY);
		backGroundPaint.setAntiAlias(true);
		backGroundPaint.setStrokeWidth(1);
		backGroundPaint.setStyle(Style.FILL);
		/* 设置渐变色 这个正方形的颜色是改变的 */
		Shader mShader = new LinearGradient(totalWidth, BChartbottom,
				totalWidth, 0, new int[] { Color.rgb(229, 28, 23),
						Color.rgb(232, 78, 40), Color.rgb(243, 108, 60),
						Color.rgb(249, 189, 187) }, null,
				Shader.TileMode.MIRROR); // 一个材质,打造出一个线性梯度沿著一条线。
		backGroundPaint.setShader(mShader);

		textRedPaint = new Paint();
		textRedPaint.setColor(Color.rgb(229, 28, 23));
		textRedPaint.setAntiAlias(true);
		textRedPaint.setTextSize(22);

		pointWhitePaint = new Paint();
		pointWhitePaint.setColor(Color.rgb(255, 255, 255));
		pointWhitePaint.setAntiAlias(true);
		pointWhitePaint.setStrokeWidth(1);

		Log.e(TAG, "density:" + density + "  densityDpi:" + densityDpi
				+ "  heightPixels:" + heightPixels + "  widthPixels:"
				+ widthPixels);
		
	}

	private void initLocalData() {
	//	List<RunRecord> records = new ArrayList<RunRecord>();
		for (int i = 1; i <= 30; i++) {
			RunRecord runRecord = new RunRecord();
			runRecord.setDistance(0);
			runRecord.setRunkaluli(0);
			runRecord.setRunSpeed(0);
			records.add(runRecord);
		}
	}

	protected abstract void drawBrokenChart();

	@Override
	public void run() {
		try {
			// 开始画图
			mCanvas = mSurfaceHolder.lockCanvas();
			drawChartBackground();
			drawBrokenChart();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mSurfaceHolder.unlockCanvasAndPost(mCanvas);
		}

	}

	protected synchronized void drawTool() {
		// 开启画图的线程
		isRunning = true;
		new Thread(this).start();
	}

	/**
	 * 画背景图
	 */
	public void drawChartBackground() {
		Log.e(TAG, "drawChartBackground");
		RectF rf = new RectF(left, BChartTop, right, BChartbottom);
		mCanvas.drawRoundRect(rf, 10, 10, backGroundPaint);
	}

	protected double[] getMaxMinDistance() {
		double max = 0;
		double min = Long.MAX_VALUE;
		double[] ll = new double[2];
		for (RunRecord rr : records) {
			if (rr.getDistance() > max) {
				max = rr.getDistance();
			}

			if (rr.getDistance() < min) {
				min = rr.getDistance();
			}
		}
		ll[0] = max;
		ll[1] = min;
		return ll;

	}

	protected double[] getMaxMinSpeed() {
		double max = 0;
		double min = Long.MAX_VALUE;
		double[] ll = new double[2];
		for (RunRecord rr : records) {
			if (rr.getRunSpeed() > max) {
				max = rr.getRunSpeed();
			}

			if (rr.getRunSpeed() < min) {
				min = rr.getRunSpeed();
			}
		}
		ll[0] = max;
		ll[1] = min;
		return ll;

	}

	protected double[] getMaxMinCal() {
		double max = 0;
		double min = Long.MAX_VALUE;
		double[] ll = new double[2];
		for (RunRecord rr : records) {
			if (rr.getRunkaluli() > max) {
				max = rr.getRunkaluli();
			}

			if (rr.getRunkaluli() < min) {
				min = rr.getRunkaluli();
			}
		}
		ll[0] = max;
		ll[1] = min;
		return ll;

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(TAG, "surfaceCreated");
		records = new ArrayList<RunRecord>();
		if (GloableValue.user.getStatus() != WTContant.OFF) {

			RunRecordManager.getRunRrcord(getContext(),
					GloableValue.user.getUserName(),
					new FindListener<RunRecord>() {
						@Override
						public void onSuccess(List<RunRecord> r) {
							records.addAll(r);
							drawTool();
						}

						@Override
						public void onError(int arg0, String arg1) {
							initLocalData();
							drawTool();
						}
					});

		} else {
			initLocalData();
			drawTool();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.e(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// 结束画图线程
		Log.e(TAG, "surfaceDestroyed");
		isRunning = false;
	}

	public BaseChartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public BaseChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BaseChartView(Context context) {
		super(context);
		init();
	}

}
