package com.xyc.wyatt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.Comments;
import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.view.RoundedImageView;
import com.xyc.wyatt.view.SettingItemView;
import com.xyc.wyatt.view.SlideButton.OnSlideButtonClickListener;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingActivity extends BaseActivity {
	private View mRightView;
	private RoundedImageView riv_headimage;
	private TextView tv_user_name;
	private TextView tv_login;
	private ImageView iv_right_user_info;
	private ImageView iv_right_suggestion;
	private ImageView iv_right_mark;
	private ImageView iv_right_about;
	private RelativeLayout rl_suggestion;
	private RelativeLayout rl_give_mark;
	private RelativeLayout rl_about;
	private RelativeLayout rl_logout;
	private SettingItemView siv_version_update;
	private SettingItemView siv_sound_tip;
	private SettingItemView siv_music;
	private RelativeLayout rl_usermessage_layout;

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
		//riv_headimage.setOnClickListener(this);
		//tv_user_name.setOnClickListener(this);
		//tv_login.setOnClickListener(this);
		rl_usermessage_layout.setOnClickListener(this);
		iv_right_user_info.setOnClickListener(this);
		iv_right_suggestion.setOnClickListener(this);
		iv_right_mark.setOnClickListener(this);
		iv_right_about.setOnClickListener(this);
		rl_suggestion.setOnClickListener(this);
		rl_give_mark.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		rl_logout.setOnClickListener(this);
		siv_version_update.setOnClickListener(this);
		siv_sound_tip.setOnClickListener(this);
		siv_music.setOnClickListener(this);
		siv_version_update.setOnSlideButtonClickListener(new OnSlideButtonClickListener() {
			@Override
			public void click(boolean slide_status) {
				siv_version_update.changeDesc(slide_status);
				Editor edit = sp.edit();
				edit.putBoolean("auto_update", slide_status);
				edit.commit();
			}
		});
		siv_sound_tip.setOnSlideButtonClickListener(new OnSlideButtonClickListener() {
			@Override
			public void click(boolean slide_status) {
				siv_sound_tip.changeDesc(slide_status);
				Editor edit = sp.edit();
				edit.putBoolean("sound_tip", slide_status);
				edit.commit();
			}
		});
		siv_music.setOnSlideButtonClickListener(new OnSlideButtonClickListener() {
			@Override
			public void click(boolean slide_status) {
				siv_music.changeDesc(slide_status);
				Editor edit = sp.edit();
				edit.putBoolean("music", slide_status);
				edit.commit();
			}
		});
	}

	
	protected void init() {
		sp = getSharedPreferences("wt",MODE_PRIVATE);
		
		boolean sound_tip = sp.getBoolean("sound_tip", false);
		siv_sound_tip.setOpen(sound_tip);
		
		boolean music = sp.getBoolean("music", false);
		siv_music.setOpen(music);
		
		boolean auto_update = sp.getBoolean("auto_update", false);
		siv_version_update.setOpen(auto_update);
		
		
	}

	protected void initView() {
		initActionBar("设置 ", true);
		riv_headimage = (RoundedImageView) mRightView
				.findViewById(R.id.riv_headimage);
		rl_usermessage_layout = (RelativeLayout) mRightView.findViewById(R.id.rl_usermessage_layout);
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
		rl_logout = (RelativeLayout) mRightView.findViewById(R.id.rl_logout);
		siv_version_update = (SettingItemView) mRightView
				.findViewById(R.id.siv_version_update);
		siv_sound_tip = (SettingItemView) mRightView
				.findViewById(R.id.siv_sound_tip);
		siv_music = (SettingItemView) mRightView
				.findViewById(R.id.siv_music);

	}
	
	@Override
	protected void onResume() {
		//TODO 检测用户的登陆状态 用来改变侧滑框中的 头像的状态
				if(GloableValue.user.getStatus()==WTContant.LOGIN){
					//处于登陆状态的时候
					//头像设置
					// 设置用户信息
					String avatarPath = GloableValue.user.getAvatar();
					if (avatarPath != null && !"".equals(avatarPath)) {
						if (avatarPath.startsWith("http")) {
							riv_headimage.showImage(avatarPath, false);
						} else {
							riv_headimage.showImage(avatarPath, true);
						}
					} 
					//用户名设置为登陆
					tv_user_name.setText(GloableValue.user.getUserName());
					tv_login.setText(null);
					
				}else{
					//在没有登陆的状态
					//头像设置
					riv_headimage.setImageResource(R.drawable.avatar_default1);	
					//用户名设置为登陆
					tv_user_name.setText(R.string.username_default);
					rl_logout.setVisibility(View.VISIBLE);
					
				}
		super.onResume();
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		int id = v.getId();
		switch (id) {
		case R.id.rl_usermessage_layout:
			if(GloableValue.user.getStatus()==WTContant.LOGIN){
				intent = new Intent();
				intent.setClass(mContext, UserInfoActivity.class);
				startActivity(intent);
				//finish();
			}else{
				intent = new Intent();
				intent.setClass(mContext, LoginActivity.class);
				startActivity(intent);
				finish();
			}
			break;
//		case R.id.riv_headimage:
//			break;
//		case R.id.tv_user_name:
//			break;
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
			intent = new Intent(mContext, AboutActivity.class);
			startActivity(intent);
//			makeShortToast("关于悦动");
			break;
		case R.id.rl_suggestion:
			intent = new Intent(mContext, OpinionActivity.class);
			startActivity(intent);
			//makeShortToast("意见反馈");
			break;
		case R.id.rl_give_mark:
			makeShortToast("评分");
			break;
		case R.id.rl_about:
			intent = new Intent(mContext, AboutActivity.class);
			startActivity(intent);
			makeShortToast("关于悦动");
			break;
		case R.id.rl_logout:
			if (GloableValue.user.getStatus() == WTContant.LOGIN) {
				//退出登录
				loginOut();
			}
			makeShortToast("退出登录");
			break;
		default:
			break;
		}
		super.onClick(v);
	}

	private void loginOut() {
		
		//退出时  更新全局用户的登陆状态
		GloableValue.user.setStatus(WTContant.OFF);
		//头像设置
		riv_headimage.setImageResource(R.drawable.avatar_default1);
		//用户名设置为登陆
		tv_user_name.setText(R.string.username_default);
		tv_login.setText("点击登录");
		//隐藏退出登录按钮
		rl_logout.setVisibility(View.INVISIBLE);
		// 并更新本地数据库
		UserDao dao = new UserDao(mContext);
		
		dao.saveOrUpdata(GloableValue.user,"status=?", new String[]{WTContant.LOGIN+""},"status=?" , new String[]{String.valueOf(WTContant.LOGIN)});
		
		List<User> findAll = dao.findAll();
		for(User u:findAll){
			Log.e("Setting", u.getStatus()+"");
		}
	}

}
