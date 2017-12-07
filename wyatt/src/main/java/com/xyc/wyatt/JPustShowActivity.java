package com.xyc.wyatt;



import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

public class JPustShowActivity extends BaseSherlockActivity {

    private String title;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	setTheme(R.style.MySherockTheme);
        super.onCreate(savedInstanceState);
       
        initView();
        init();
        initEvent();
    }

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		 TextView tv = new TextView(this);
	        tv.setText("用户自定义打开的Activity");
	        Intent intent = getIntent();
	        if (null != intent) {
		        Bundle bundle = getIntent().getExtras();
		       title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
		        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
		        tv.setText("    " + content);
	        }
	        addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        if(title!=null && !title.equals("")){
	        	initActionBar(title, true);
	        }else{
	        	initActionBar("悦动", true);
	        }
	}

	@Override
	protected void initEvent() {
	}
	
	

}