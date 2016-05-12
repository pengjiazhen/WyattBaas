package com.xyc.wyatt.util;

import com.baidu.mapapi.model.LatLng;

public class MapUtil {
	
	/**
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static double getPiontsDistance(LatLng start, LatLng end) {
		double sum = (start.latitude - end.latitude)
				* (start.latitude - end.latitude)
				+ (start.longitude - end.longitude)
				* (start.longitude - end.longitude);
		return Math.sqrt(sum);
	}
	
	

	
}
