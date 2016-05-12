package com.xyc.wyatt;

import cn.bmob.v3.listener.SaveListener;

import com.xyc.wyatt.domain.Opinion;
import com.xyc.wyatt.view.KeyboardListenEdittext;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class OpinionActivity extends BaseSherlockActivity{

	private Context mContext;
	private EditText et_opinion;
	private KeyboardListenEdittext cet_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.MySherockTheme);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opinion);
		mContext=this;
		initView();
		init();
		initEvent();
	}
	@Override
	protected void initView() {
		cet_content=(KeyboardListenEdittext) findViewById(R.id.cet_content);
		et_opinion = (EditText) findViewById(R.id.et_opinion);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		setActionBar("意见反馈", true,false,null);
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}
	public void onSubmit(View view){
		String content = cet_content.getText().toString();
		String phone=et_opinion.getText().toString();
		if("".equals(content) || "".equals(phone)){
			makeShortToast("请输入内容或电话");
			return;
		}
		showProgress();
		Opinion opinion = new Opinion();
		opinion.setContent(content);
		opinion.setPhone(phone);
		opinion.save(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				makeShortToast("提交成功，我们会尽快给您答疑");
				dismissProgress();
				finish();
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				makeShortToast("网络忙，请稍后重试");
				
			}
		});
	}

}
