package com.xyc.wyatt.net;

/**
 * 网络数据的提交
 */
public class WTNetPost {
	
	/*
	*//**
	 * 提交表单数据?
	 * 
	 * @param uri
	 * @param callBack
	 * @param params
	 *//*
	public static void postData(String uri, Map<String, String> params,
			CallBack callBack) {
		SKPostDataCore(uri, params, null, callBack);
	}

	*//**
	 * 单个文件上传
	 * @param uri
	 * @param file
	 * @param callBack
	 *//*
	public static void postFile(String uri,File file,CallBack callBack){
		SKPostDataCore(uri, null, new File[]{file}, callBack);
	}
	
	*//**
	 * 单个文件上传带参?
	 * @param uri
	 * @param params
	 * @param file
	 * @param callBack
	 *//*
	public static void postFile(String uri, Map<String, String> params,File file,CallBack callBack){
		SKPostDataCore(uri, params, new File[]{file}, callBack);
	}
	
	*//**
	 * 多个文件上传
	 * @param uri
	 * @param files
	 * @param callBack
	 *//*
	public static void postFiles(String uri,File[] files,CallBack callBack){
		SKPostDataCore(uri, null, files, callBack);
	}
	
	*//**
	 * 多个文件上传，并且带参数
	 * @param uri
	 * @param params
	 * @param files
	 * @param callBack
	 *//*
	public static void postFiles(String uri, Map<String, String> params,File[] files, CallBack callBack){
		SKPostDataCore(uri, params, files, callBack);
	}
	
	
	
	*//**
	 * 
	 * @param uri
	 * @param params <br/>key值和服务器端接收时用的字段一?
	 * @param files  <br/>  
	 * @param callBack
	 *//*
	public static void SKPostDataCore(final String uri,final Map<String, String> params,final File[] files,final CallBack callBack) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (callBack!=null){
						callBack.before();
					}
					
					MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
					if (files != null) {
						FileBody fileBody = null;
						// 加入文件参数
						for (File file : files) {
							//Bitmap bm = BitmapUtil.revitionImageSize(file.getPath());
							//file = BitmapUtil.compressImage(bm);
							
							fileBody = new FileBody(file);
							multipartEntityBuilder.addPart("files", fileBody);
						}
					}
					// 加入文本参数
					if (params != null) {
						Set<String> keySet = params.keySet();
						Iterator<String> it = keySet.iterator();
						StringBody stringBody = null;
						while (it.hasNext()) {
							String key = it.next();
							stringBody = new StringBody(params.get(key),Charset.defaultCharset());
							multipartEntityBuilder.addPart(key, stringBody);
						}
					}
					HttpEntity httpEntity = multipartEntityBuilder.build();;
				    HttpPost httpPost = new HttpPost(uri);
				    httpPost.setEntity(httpEntity);
				    HttpResponse response = WTHttp.getHttpClient().execute(httpPost);
				   
				    if (response.getStatusLine().getStatusCode() == 200){
				    	HttpEntity he = response.getEntity();
				    	if (callBack!=null){
				    	callBack.success(he==null?null:WTHttp.inputSream2String(he.getContent()));
				    	}
				    	}
				    response = null;
				    httpPost = null;
				    httpEntity = null;
				} catch (Exception e) {
					if (callBack!=null){
					callBack.fail(e.getLocalizedMessage());
					}
				}finally{
					
				}
			}
		});
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	
	public static void sendPost(String uri, Map<String, String> params,CallBack callBack) {
		HttpPost post = new HttpPost(uri);

		// 处理超时
		HttpParams httpParams = new BasicHttpParams();//
		httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 8000);
		HttpConnectionParams.setSoTimeout(httpParams, 8000);
		post.setParams(httpParams);

		// 设置参数
		if (params != null && params.size() > 0) {
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				BasicNameValuePair pair = new BasicNameValuePair(item.getKey(),
						item.getValue());
				parameters.add(pair);
			}
			try {
				if (callBack!=null){
					callBack.before();
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
						parameters, WTContant.CHARSET);
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				if (callBack!=null){
					callBack.fail(e.getLocalizedMessage());
				}
			}
		}

		try {
			HttpResponse response = WTHttp.getHttpClient().execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				if (callBack!=null){
					callBack.success( EntityUtils.toString(response.getEntity(),
							WTContant.CHARSET));
				}
			}

		} catch (Exception e) {
			if (callBack!=null){
				callBack.fail(e.getLocalizedMessage());
			}
		}

	}
	*/
	
	
}
