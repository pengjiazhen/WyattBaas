package com.xyc.wyatt.manager;
/**
 * 鍥炶皟鎺ュ彛
 * @author 鍚翠紶榫?
 * @date 2014骞?8鏈?8鏃? 涓嬪崍1:08:33
 */
public interface CallBack {
	
	/**
	 * 涓氬姟鎿嶄綔鎴愬姛鍚庯紝杩斿洖涓?涓猺esult锛岃result鐢辨湇鍔″櫒绔繑鍥?
	 * @param result
	 */
	void success(String result);
	
	/**
	 * 鎿嶄綔澶辫触鐨勫師鍥?
	 * @param errorMessage
	 */
	void fail(String errorMessage);
	
	void before();
}
