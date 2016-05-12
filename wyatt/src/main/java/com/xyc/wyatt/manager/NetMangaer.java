package com.xyc.wyatt.manager;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetMangaer {
	  /**
		 * 检查是否有可用的网络
		 * @param context
		 * @return   
		 */
		public static boolean checkNetworkAvailable(Context context){
			ConnectivityManager manager =  (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (manager != null) {
				NetworkInfo[] allNetworkInfo = manager.getAllNetworkInfo();
				for (int i = 0; i < allNetworkInfo.length; i++) {
					if (allNetworkInfo != null) {
						if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED){
							return true;
						}
					}
				}
			}
			return false;
		}
		
		
}
