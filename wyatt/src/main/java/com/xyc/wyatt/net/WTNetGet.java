package com.xyc.wyatt.net;

public class WTNetGet {
	/*
	public static void getData(final String uri, final CallBack callBack) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				callBack.before();
				//WTLogger.e("SKThreadPool", "SKThreadPool:"+Thread.currentThread().getName()+"正在执行");
				HttpGet get = new HttpGet(uri);
				// get.setHeaders(headers);
				try {
					HttpResponse response = WTHttp.getHttpClient().execute(get);
					if (response.getStatusLine().getStatusCode() == 200) {
						callBack.success(EntityUtils.toString(
								response.getEntity(),WTContant.CHARSET));
					}
				} catch (Exception e) {
					callBack.fail(e.getMessage());
				}
			}
		};
		WThreadPool.getThreadPool().execute(task);
	}
	
	public static void getFile(final String uri,final CallBack callBack,final String saveUri) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				//WTLogger.e("SKThreadPool", "SKThreadPool:"+Thread.currentThread().getName()+"正在执行");
				HttpGet get = new HttpGet(uri);
				HttpParams httpParams = new BasicHttpParams();//
				HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
				HttpConnectionParams.setSoTimeout(httpParams, 8000);
				get.setParams(httpParams);
				try {
					callBack.before();
					HttpResponse response = WTHttp.getHttpClient().execute(get);
					if (response.getStatusLine().getStatusCode() == 200) {
						InputStream is = response.getEntity().getContent();
						WTHttp.inputSream2File(is, saveUri);
						callBack.success(saveUri);
					}
				} catch (Exception e) {
					callBack.fail(e.getLocalizedMessage());
				}
			}
		};
		WThreadPool.getThreadPool().execute(task);
	}*/
}
