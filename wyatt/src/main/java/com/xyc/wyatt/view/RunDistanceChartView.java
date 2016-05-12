package com.xyc.wyatt.view;

import java.util.Random;

import com.xyc.wyatt.domain.RunRecord;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 绘制折线图
 * 
 * @author pengjiazhen
 *
 */
public class RunDistanceChartView extends BaseChartView {

	@Override
	protected void drawBrokenChart() {
		// 绘制表头上的文字
		float maginLeft = left - textRedPaint.measureText("运动路程") - 5;
		mCanvas.drawText("运动路程", maginLeft + left, BChartTop - 8, textRedPaint);

		PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		lineWhitePaint.setPathEffect(effects);

		Path path1 = new Path();
		int y = BChartTop + 15;
		int textHeight = (int) (textRedPaint.descent() - textRedPaint.ascent());
		
		//写底部的数字
		mCanvas.drawText("1",
				left, BChartbottom
						+ textHeight / 2+15, textRedPaint);
		mCanvas.drawText("15",
				left+totalWidth/2-textRedPaint.measureText("15")/2, BChartbottom
						+ textHeight / 2+15, textRedPaint);
		mCanvas.drawText("30",
				left+totalWidth-textRedPaint.measureText("30"), BChartbottom
						+ textHeight / 2+15, textRedPaint);


		// 写最大值
		double max = getMaxMinDistance()[0];
		double min = getMaxMinDistance()[1];
		mCanvas.drawText((int)max + "", left - textRedPaint.measureText((int)max + "")
				- 5, y + textHeight / 2, textRedPaint);
	
		// 画上面的虚线
		path1.moveTo(left, y);
		path1.lineTo(right, y);

		double gap = max - min;
		if (gap > 0) {

			// 分成四等分
			// 画中间的三根虚线
			int n = 3;
			double sper = (max - min) / n;// 每一等分代表的价格
			for (int i = 1; i < n; i++) {
				y = i * ((BChartbottom - BChartTop) / n) + BChartTop;
				path1.moveTo(left, y);
				path1.lineTo(right, y);
				int text = (int) (max - i * sper);
				mCanvas.drawText(text + "",
						left - textRedPaint.measureText(text + "") - 5, y
								+ textHeight / 2, textRedPaint);
			}
			mCanvas.drawPath(path1, lineWhitePaint);
		}
		// 画折线
		
		Path path = new Path();
		if (records.size() > 0) {
			double heightScale = (double)(BChartbottom - BChartTop-15)/(max - min);
			int n = records.size();
			
			int xper = totalWidth / n;// 每天的宽度
			int cLeft = left;
			int cTop = 0;
			
			path.moveTo(left, (int) (BChartTop+15 + ((float)max - records.get(0).getDistance()) * heightScale));
			

			for (int i=1;i<records.size();i++) {
				RunRecord rr = records.get(i);
				cLeft =left + i*xper;
				cTop =  (int) (BChartTop+15 + ((float)max - rr.getDistance()) * heightScale);
				path.lineTo(cLeft+xper, cTop);
				
			}
		}

		Paint LinePaint = new Paint();
		LinePaint.setColor(Color.WHITE);
		LinePaint.setStyle(Style.STROKE);
		LinePaint.setAntiAlias(true);
		LinePaint.setStrokeWidth(2);
		mCanvas.drawPath(path, LinePaint);

	}
	private float per16 = 0.166666666f;
	private float per26 = 0.333333333f;
	private float per56 = 0.833333333f;
	public RunDistanceChartView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public RunDistanceChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RunDistanceChartView(Context context) {
		super(context);
	}

}
