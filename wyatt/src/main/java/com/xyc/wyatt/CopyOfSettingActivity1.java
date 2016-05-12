package com.xyc.wyatt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xyc.wyatt.domain.Comments;
import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.view.RoundedImageView;
import com.xyc.wyatt.view.SettingItemView;
import com.xyc.wyatt.view.SlideButton;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CopyOfSettingActivity1 extends BaseActivity {
	private View mRightView;
	private RoundedImageView riv_headimage;
	private TextView tv_user_name;
	private TextView tv_login;
	private TextView tv_title_sound_tip;
	private TextView tv_title_update;
	private TextView tv_desc_sound_tip;
	private TextView tv_desc_update;
	private SlideButton sb_status_sound_tip;
	private SlideButton sb_status_update;
	private ImageView iv_right_user_info;
	private ImageView iv_right_suggestion;
	private ImageView iv_right_mark;
	private ImageView iv_right_about;
	private RelativeLayout rl_suggestion;
	private RelativeLayout rl_give_mark;
	private RelativeLayout rl_about;
	private boolean sound_tip;
	private boolean auto_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRightView = View.inflate(this, R.layout.activity_setting, null);
		addContentView(mRightView);
		GloableValue.currentContent = 4;
		initView();
		init();
		initEvent();
	}

	protected void initEvent() {
		riv_headimage.setOnClickListener(this);
		tv_user_name.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		iv_right_user_info.setOnClickListener(this);
		iv_right_suggestion.setOnClickListener(this);
		iv_right_mark.setOnClickListener(this);
		iv_right_about.setOnClickListener(this);
		rl_suggestion.setOnClickListener(this);
		rl_give_mark.setOnClickListener(this);
		rl_about.setOnClickListener(this);
	}

	protected void init() {
		sp = getSharedPreferences("wt", MODE_PRIVATE);
		sound_tip = sp.getBoolean("sound_tip", false);
		auto_update = sp.getBoolean("auto_update", false);
	}

	protected void initView() {
		initActionBar("设置 ", true);
		riv_headimage = (RoundedImageView) mRightView
				.findViewById(R.id.riv_headimage);
		tv_user_name = (TextView) mRightView.findViewById(R.id.tv_user_name);
		tv_login = (TextView) mRightView.findViewById(R.id.tv_login);
		iv_right_user_info = (ImageView) mRightView
				.findViewById(R.id.iv_right_user_info);
		iv_right_suggestion = (ImageView) mRightView
				.findViewById(R.id.iv_right_suggestion);
		iv_right_mark = (ImageView) mRightView.findViewById(R.id.iv_right_mark);
		iv_right_about = (ImageView) mRightView
				.findViewById(R.id.iv_right_about);
		rl_suggestion = (RelativeLayout) mRightView
				.findViewById(R.id.rl_suggestion);
		rl_give_mark = (RelativeLayout) mRightView
				.findViewById(R.id.rl_give_mark);
		rl_about = (RelativeLayout) mRightView.findViewById(R.id.rl_about);

//		
//		tv_title_sound_tip = (TextView) mRightView.findViewById(R.id.tv_title_sound_tip);
//		tv_title_update = (TextView) mRightView.findViewById(R.id.tv_title_update);
//		tv_desc_sound_tip = (TextView) mRightView.findViewById(R.id.tv_desc_sound_tip);
//		tv_desc_update = (TextView) mRightView.findViewById(R.id.tv_desc_update);
//		sb_status_sound_tip = (SlideButton) mRightView.findViewById(R.id.sb_status_sound_tip);
//		sb_status_update = (SlideButton) mRightView.findViewById(R.id.sb_status_update);
	
	
	
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.riv_headimage:
			makeShortToast("头像");
			break;
		case R.id.tv_user_name:
			break;
		case R.id.tv_login:
			makeShortToast("登陆");
			break;
		case R.id.iv_right_user_info:
			makeShortToast("个人信息");
			break;
		case R.id.iv_right_suggestion:
			makeShortToast("意见反馈");
			break;
		case R.id.iv_right_mark:
			makeShortToast("评分");
			break;
		case R.id.iv_right_about:
			makeShortToast("关于悦动");
			break;
		case R.id.rl_suggestion:
			makeShortToast("意见反馈");
			break;
		case R.id.rl_give_mark:
			makeShortToast("评分");
			break;
		case R.id.rl_about:
			makeShortToast("关于悦动");
			break;
		// case R.id.siv_version_update:
		// Editor edit = sp.edit();
		// edit.putBoolean("auto_update", siv_version_update.isOpen());
		// edit.commit();
		// break;
		// case R.id.siv_sound_tip:
		// Editor edit1 = sp.edit();
		// edit1.putBoolean("sound_tip", siv_sound_tip.isOpen());
		// edit1.commit();
		// break;
		default:
			break;
		}
		super.onClick(v);
	}

}
