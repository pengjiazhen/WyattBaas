package com.xyc.wyatt.manager;

import android.content.Context;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.xyc.wyatt.dao.UserDao;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.util.GloableValue;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserManager {

	/**
	 * 用户登陆
	 * 
	 * @param user
	 * @param callBack
	 */
	public static void userLogin(User user, Context context,
			final FindListener<User> callBack) {

		BmobQuery<User> bq = new BmobQuery<User>();
		bq.addWhereEqualTo("userName", user.getUserName());
		bq.addWhereEqualTo("passWord", user.getPassWord());
		bq.findObjects(context, callBack);
		// Map<String, String>login_user = new HashMap<String, String>();
		//
		// login_user.put("userName", user.getUserName());
		// login_user.put("passWord",user.getPassWord());
		//
		//
		// WTNetPost.postData(WTContant.USER_LOGIN, login_user, callBack);
		//

	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @param callBack
	 */
	public static void userRegister(User user, final Context context,
			SaveListener callBack) {

		user.save(context, callBack);

		// Map<String, String>register_user = new HashMap<String, String>();
		// register_user.put("userName", user.getUserName());
		// register_user.put("passWord",user.getPassWord());
		//
		// WTNetPost.postData(WTContant.USER_REGIST, register_user, callBack);

	}

	/**
	 * 检测用户名
	 * 
	 * @param user
	 * @param callBack
	 */
	public static void validUserName2one(User user, final Context context,
			FindListener<User> callBack) {

		BmobQuery<User> bq = new BmobQuery<User>();
		bq.addWhereContains("userName", user.getUserName());
		bq.findObjects(context, callBack);

	}

	/*
	public static void updateUser(String newdate, int id, String updateType,
			Context context, UpdateListener callBack) {

		GloableValue.user.update(context, callBack);
		// Map<String, String>update_user = new HashMap<String, String>();
		//
		// update_user.put("id", id+"");
		// update_user.put("updateType", updateType);
		// update_user.put(updateType, newdate);
		// WTNetPost.postData(WTContant.USER_UPDATEINFO, update_user, callBack);

	}*/


	public static void updateUser(Context context, UpdateListener callBack) {
		GloableValue.user.update(context, callBack);
		updateLocalUser(context);
	}

	public static void updateLocalUser(Context context) {
		UserDao dao = new UserDao(context);
		dao.saveOrUpdata(GloableValue.user, "userName=?",
				new String[] { GloableValue.user.getUserName() }, "userName=?",
				new String[] { GloableValue.user.getUserName() });
	}


	/*public static void getUser(int id, Context context, CallBack callBack) {
		WTNetGet.getData(WTContant.USER_GET + "?id=" + id, callBack);
		System.out.println(WTContant.USER_GET + "?id=" + id);
	}*/


	public static void getUserByUserName(String userName, Context context,
			FindListener<User> callBack) {
		BmobQuery<User> bq = new BmobQuery<User>();
		bq.addWhereContains("userName", userName);
		bq.findObjects(context, callBack);

		// WTNetGet.getData(WTContant.USER_GET+"?id="+id, callBack);
		// System.out.println(WTContant.USER_GET+"?id="+id);
	}


	public static void updateUserAvatar(String userName, int id, File[] files,
			final Context context, final UpdateListener callBack) {
		BTPFileResponse response = BmobProFile.getInstance(context).upload(
				files[0].getPath(), new UploadListener() {
					@Override
					public void onError(int arg0, String arg1) {
					}

					@Override
					public void onProgress(int arg0) {
					}

					@Override
					public void onSuccess(String fileName, String url,
							BmobFile file) {
						String url2 = file.getUrl();
						GloableValue.user.setAvatar(url2);
						GloableValue.user.update(context, callBack);
						
					}

				});

		// Map<String, String>updateUserAvatar = new HashMap<String, String>();
		//
		// updateUserAvatar.put("id", id+"");
		// updateUserAvatar.put("userName", userName);
		//
		// WTNetPost.SKPostDataCore(WTContant.USER_UPDATEAVATAR,
		// updateUserAvatar,files, callBack);

	}

}
