package com.xyc.wyatt.view;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.xyc.wyatt.ImageBrowserActivity;
import com.xyc.wyatt.net.WTImageLoader;
import com.xyc.wyatt.net.WTImageLoaderConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class WTImageView extends ImageView {
	private Context context;
	private String uri;
	private boolean isLocalUri;
	private static final String LOCALURI = "file://";

	public WTImageView(Context context) {
		super(context);
		this.context = context;
		initListener();
	}

	public WTImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initListener();
	}

	public WTImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initListener();
	}

	public void initListener() {
		if (!this.isClickable()) {
			return;
		}
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (uri != null) {
					if (!uri.startsWith("http")) {
						uri = uri.replaceFirst(LOCALURI, "");
					}
					Intent intent = new Intent(context,
							ImageBrowserActivity.class);
					intent.putExtra("uri", uri);
					intent.putExtra("isLocalUri", isLocalUri);
					context.startActivity(intent);
				}
			}
		});
	}

	/**
	 * 
	 */
	public void showImage(String uri, boolean isLocalUri) {
		if (isLocalUri) {
			this.uri = LOCALURI + uri;
		} else {
			this.uri = uri;
		}
		WTImageLoader.showImageView(context, this.uri,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						Log.d("roundedImageView", failReason.toString());
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});

	}

}
