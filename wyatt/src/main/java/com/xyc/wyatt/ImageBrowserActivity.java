package com.xyc.wyatt;

import com.xyc.wyatt.view.WTImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class ImageBrowserActivity extends Activity{
	public String imageUri;
	public WTImageView browserImageView;
	public boolean isLocalUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_browser);
		browserImageView = (WTImageView) this.findViewById(R.id.iv_browser);
		Intent intent = getIntent();
		if (intent!=null){
			imageUri = intent.getStringExtra("uri");
			isLocalUri = intent.getBooleanExtra("isLocalUri", false);
			if(imageUri.startsWith("http")){
				browserImageView.showImage(imageUri,false);
			}else{
				browserImageView.showImage(imageUri,true);
			}
			
			
		}
		browserImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
