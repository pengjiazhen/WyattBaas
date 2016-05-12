package com.xyc.wyatt.net;

public class WTHttp {
	
	/*private static HttpClient client;

	public static HttpClient getHttpClient() {
		if (client == null) {
			synchronized (WTNetPost.class) {
				if (client == null) {
					client = new DefaultHttpClient();
				}
			}
		}
		return client;
	}
	
	
	public static InputStream string2inputSream(String src,String charset) throws Exception{
		InputStream is = new ByteArrayInputStream(src.getBytes(charset));
		return is;
	}
	
	public static String inputSream2String(InputStream is) throws Exception{
		StringBuffer sb = new StringBuffer();
		byte[] buff = new byte[1024];
		int len = -1;
		while (-1 != (len = is.read(buff))) {
			sb.append(new String(buff, 0, len));
		}
		return sb.toString();
	}
	
	public static void inputSream2File(InputStream is,String saveUri){
		File file = new File(saveUri);
		File pf = new File(file.getParent());
		if (!pf.exists()){
			pf.mkdirs();
		}
		OutputStream fos = null;
		BufferedOutputStream  bos = null;
		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			byte[] buff = new byte[1024];
			int len = -1;
			while (-1 != (len = is.read(buff))) {
				bos.write(buff, 0, len);
			}
			bos.flush();fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
				}finally{
					fos = null;
				}
			}
			if (bos!=null){
				try {
					bos.close();
				} catch (IOException e) {
				}finally{
					bos = null;
				}
			}
		}
//		
	}*/
}
