package com.xyc.wyatt.util;

import java.io.File;

public interface WTContant {

	/**
	 * 是否已经登陆 1为已经登陆 0为没有
	 */
	int LOGIN = 1;
	/**
	 *
	 */
	int OFF = 0;
	
	int CHANGE = 1;
	
	int REQUEST_AGE = 3;
	int REQUEST_HEIGHT = 1;
	int REQUEST_WEIGHT = 2;

	int MEDIA_TYPE_IMAGE = 100;
	int MEDIA_TYPE2_IMAGE = 200;

	int CROP_PHOTO_REQUEST_CODE = 200;
	int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 50;
	int REQUE_CODE_CROP = 10;
	int REQUE_CODE_CAMERA = 20;
	int REQUE_CODE_PHOTO = 30;
	int REQUE_CODE_LOGIN = 40;
	/**
	 * 图片存放目录
	 */
	String WTDIMAGEIRECTORY = FileUtils.getOwnCacheDirectoryPath(
			GloableValue.mContext, "wt" + File.separator + "####"
					+ File.separator + "image");
	String CHARSET = "utf-8";
	String[] IMAGE_SUFFIX = new String[]{".jpg",".png",".jpeg",".gif",".bmp"};
	// String SERVER_IP = "http://172.31.196.236:8080/wtweb";
	String SERVER_IP = "http://172.31.196.236:8080/wtweb";

	String USER_URL = "/userAction_";

	
	String RECORD_URL = "/runRecordAction_";

	String DYNAMIC_URL = "/dynamicAction_";

	String COMMENT_URL = "/commentAction_";
	/**
	 * 登陆
	 */
	String USER_LOGIN = SERVER_IP + USER_URL + "login.action";
	/**
	 * 注册
	 */
	String USER_REGIST = SERVER_IP + USER_URL + "regist.action";
	/**
	 * 检测用户名
	 */
	String USER_VALIDUSERNAME = SERVER_IP + USER_URL + "validUserName.action";
	/**
	 * 更新用户个人信息
	 */
	String USER_UPDATEINFO = SERVER_IP + USER_URL + "updateUserInfo.action";
	/**
	 * 更新用户头像
	 */
	String USER_UPDATEAVATAR = SERVER_IP + USER_URL + "updateAvatar.action";
	String USER_REGISTER = SERVER_IP + USER_URL + "regist.action";
	
	/**
	 * 获取用户
	 */
	String USER_GET=SERVER_IP+USER_URL+"searchById.action";

	String DYNAMIC_GET_DYNAMIC = SERVER_IP + DYNAMIC_URL
			+ "getDynamicByPage.action";
	/**
	 * getRunRecord
	 */
	String RECORD_GET_RECORD = SERVER_IP + RECORD_URL + "getRunRecord.action";
	/**
	 * saveRunRecord
	 */
	String RECORD_SAVE_RECORD = SERVER_IP + RECORD_URL + "saveRunRecord.action";
	String DYNAMIC_UPDATE_DYNAMIC = SERVER_IP + DYNAMIC_URL
			+ "updateDynamic.action";
	String DYNAMIC_SAVE_DYNAMIC=SERVER_IP+DYNAMIC_URL+"saveDynamic.action";

	String COMMENT_GET_COMMENT = SERVER_IP + COMMENT_URL + "getComment.action";

	String COMMENT_SAVE_COMMENT = SERVER_IP + COMMENT_URL
			+ "saveComment.action";
}
