package com.xyc.wyatt.view;

import com.xyc.wyatt.util.GloableValue;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView {

	private static final String TAG = "MyListView";

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	float startx = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
		//	Log.e(TAG, "myListView---down");
			GloableValue.isListView = true;
			startx = ev.getX();
			if (startx >=5.0) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			if (SlipView.isOpen) {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			//Log.e(TAG, "" + ev.getX());
			break;

		case MotionEvent.ACTION_UP:
			getParent().requestDisallowInterceptTouchEvent(false);
			//Log.e(TAG, "myListView---up");
			GloableValue.isListView = false;
			GloableValue.requestOpenMenu = false;
			// return false;
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
