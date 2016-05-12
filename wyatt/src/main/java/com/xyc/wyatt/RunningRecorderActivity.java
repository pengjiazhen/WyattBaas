package com.xyc.wyatt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.listener.FindListener;

import com.actionbarsherlock.view.Window;
import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.manager.RunRecordManager;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.view.RunCaChartView;
import com.xyc.wyatt.view.RunDistanceChartView;
import com.xyc.wyatt.view.RunSpeedChartView;

import android.os.Bundle;
import android.view.View;

public class RunningRecorderActivity extends BaseActivity {

	private View mRightView;
	private RunCaChartView cv_recorder_calorie;
	private RunSpeedChartView cv_recorder_speed;
	private RunDistanceChartView cv_recorder_distance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		mRightView = View.inflate(mContext, R.layout.activity_runing_record,
				null);
		addContentView(mRightView);
		GloableValue.currentContent = 2;
		initView();
		init();
		initEvent();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void initView() {
		cv_recorder_calorie = (RunCaChartView) mRightView
				.findViewById(R.id.cv_recorder_calorie);
		cv_recorder_speed = (RunSpeedChartView) mRightView
				.findViewById(R.id.cv_recorder_speed);
		cv_recorder_distance = (RunDistanceChartView) mRightView
				.findViewById(R.id.cv_recorder_distance);
	}

	protected void init() {
		initActionBar("图表汇总", true);
		

	}

	protected void initEvent() {
		cv_recorder_calorie.setOnClickListener(this);
		cv_recorder_speed.setOnClickListener(this);
		cv_recorder_distance.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.cv_recorder_calorie:

			break;
		case R.id.cv_recorder_speed:

			break;
		case R.id.cv_recorder_distance:

			break;

		default:
			break;
		}
		super.onClick(v);
	}

}
