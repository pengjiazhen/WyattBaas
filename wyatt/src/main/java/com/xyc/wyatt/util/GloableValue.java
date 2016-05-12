package com.xyc.wyatt.util;

import android.content.Context;

import com.xyc.wyatt.domain.User;

public class GloableValue {

	public static int currentContent;
	
	public static User user = new User();
	
	public static boolean isSlideButton = false;
	public static boolean isListView = false;
	public static boolean requestOpenMenu = false;
	
	public static Context mContext;
	
	public static int changeUserInfo = 0;
	public static boolean  requestQueryRunRecord = false;
	
}
