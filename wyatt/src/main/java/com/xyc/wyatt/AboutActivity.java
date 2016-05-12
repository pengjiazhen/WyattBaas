package com.xyc.wyatt;

import cn.bmob.v3.listener.SaveListener;

import com.xyc.wyatt.domain.Opinion;
import com.xyc.wyatt.view.KeyboardListenEdittext;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class AboutActivity extends BaseSherlockActivity{

	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mContext=this;
		initView();
		init();
		initEvent();
	}
	@Override
	protected void initView() {
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		setActionBar("关于悦动", true,false,null);
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}
	

}
