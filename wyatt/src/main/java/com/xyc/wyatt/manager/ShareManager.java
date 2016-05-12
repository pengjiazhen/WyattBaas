package com.xyc.wyatt.manager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.net.WTNetPost;
import com.xyc.wyatt.util.GloableValue;
import com.xyc.wyatt.util.WTContant;

public class ShareManager {
	public static void saveDynamic(final Dynamic dynamic,
			final Context context, final File[] files,
			final SaveListener callBack) {
		String[] filesPath = new String[] { files[0].getPath(),
				files[1].getPath() };
		//Log.i("bmob", "地址--" + files[1].getPath() + "--" + files[0].getPath());
		BmobProFile.getInstance(context).uploadBatch(filesPath,
				new com.bmob.btp.callback.UploadBatchListener() {

					@Override
					public void onError(int arg0, String arg1) {

						Log.i("bmob", "批量上传出错：" + arg0 + "--" + arg1);
						if (callBack != null) {
							callBack.onFailure(arg0, arg1);
						}
					}

					@Override
					public void onSuccess(boolean isFinish, String[] fileNames,
							String[] urls, BmobFile[] files) {
						//Log.i("bmob", "urls--" + urls[0] + "--" + urls[1]);
						/**
						 * @param fileName
						 *            ：文件名
						 * @param fileUrl
						 *            ：文件url地址
						 * @param accessKey
						 *            ：web应用密钥中的AccessKey
						 * @param effectTime
						 *            ：有效时长(秒)
						 * @param secretKey
						 *            ：密钥
						 * @return 可访问的URL地址
						 */
						if(isFinish){
							String url1 = BmobProFile.getInstance(context).signURL(
									fileNames[0], urls[0],
									"8d7cee679ba6af2fc615662976ae86e3",
									1 * 365 * 24 * 60 * 60, null);
							String url2 = BmobProFile.getInstance(context).signURL(
									fileNames[1], urls[1],
									"8d7cee679ba6af2fc615662976ae86e3",
									1 * 365 * 24 * 60 * 60, null);
							
							dynamic.setRunImage(url2);
							dynamic.setPicture(url1);
							
							dynamic.save(context, callBack);
						}

					}

					@Override
					public void onProgress(int arg0, int arg1, int arg2,
							int arg3) {
						// TODO Auto-generated method stub

					}
				});

	}
	

}
