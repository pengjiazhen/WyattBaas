package com.xyc.wyatt.manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.xyc.wyatt.domain.RunRecord;
import com.xyc.wyatt.domain.User;
import com.xyc.wyatt.domain.WTimePattern;
import com.xyc.wyatt.net.WTNetGet;
import com.xyc.wyatt.net.WTNetPost;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;
import com.xyc.wyatt.util.WTUtil;

public class RunRecordManager {
	/**
	 * 获取运动记录上
	 * 
	 * @param context
	 * @param userid
	 * @param callback
	 */
	public static void getRunRrcord(Context context, String userName,
			FindListener<RunRecord> callback) {
		BmobQuery<RunRecord> bq = new BmobQuery<RunRecord>();
		//bq.addWhereContains("userName", userName);
		bq.addWhereEqualTo("userName", userName);
		bq.order("createdAt");
		boolean isCache = bq.hasCachedResult(context, RunRecord.class);
		if (isCache) {// --此为举个例子，并不一定按这种方式来设置缓存策略
			bq.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK); // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
			Log.d("runRecordManager", "有缓存");
			if(GloableValue.requestQueryRunRecord){
				bq.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE); // 如果有缓存,并且缓存数据有更新
				GloableValue.requestQueryRunRecord=false;
			}
		} else {
			bq.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE); // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
			Log.d("runRecordManager", "没有缓存");
		}
		bq.findObjects(context, callback);

		// WTNetGet.getData(WTContant.RECORD_GET_RECORD+"?uid="+userid,
		// callback);
		//
		// System.out.println(WTContant.RECORD_GET_RECORD+"?uid="+userid);

	}

	/**
	 * 获取运动记录上
	 * 
	 * @param context
	 * @param userid
	 * @param callback
	 */
	public static void getRunRrcordByObjectId(Context context, String objectId,
			FindListener<RunRecord> callback) {
		BmobQuery<RunRecord> bq = new BmobQuery<RunRecord>();
		bq.addWhereEqualTo("objectId", objectId);
		bq.findObjects(context, callback);
	}

	/**
	 * 保存运动记录
	 * 
	 * @param context
	 * @param userid
	 * @param callback
	 */
	public static void saveRunRrcord(final Context context,
			final RunRecord runrecord, File[] files,
			final UpdateListener callback) {
		BTPFileResponse response = BmobProFile.getInstance(context).upload(
				files[0].getPath(), new UploadListener() {
					@Override
					public void onError(int arg0, String arg1) {
						Toast.makeText(context, "截图保存失败", Toast.LENGTH_SHORT).show();
						Log.e("runRecordManager", arg1);
						//保存，不管图片是否保存成功
					}

					@Override
					public void onProgress(int arg0) {
					}

					@Override
					public void onSuccess(String fileName, String url,
							BmobFile file) {
						String url2 = file.getUrl();
						runrecord.setDate(System.currentTimeMillis() + "");
						runrecord.setRunImage(url2);
						runrecord.update(context, callback);
					}

				});

	}

	/**
	 * 保存运动记录
	 * 
	 * @param context
	 * @param userid
	 * @param callback
	 */
	public static void updateRunRrcord(final Context context,
			final RunRecord runrecord,
			final UpdateListener callback) {
		runrecord.update(context, callback);
	}
}
