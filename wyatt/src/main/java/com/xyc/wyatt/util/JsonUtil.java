package com.xyc.wyatt.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil<T> {

	public static JSONObject string2json(String text) {
		Object jsonObject = JSONObject.parse(text);
		JSONObject parseObject = JSONObject.parseObject((String) jsonObject);
		return parseObject;
	}

	public static int getResonseCode(String text) {
		JSONObject jo = string2json(text);
		int code = (Integer) jo.get("response");
		return code;
	}

	@SuppressWarnings("unchecked")
	public static  List  getList(String text, String name, Class clazz) {
		JSONObject jo = string2json(text);
		String productlist = jo.getString(name);
		return JSON.parseArray(productlist, clazz);
	}
}
