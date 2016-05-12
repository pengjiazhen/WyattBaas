package com.xyc.wyatt.manager;

import com.xyc.wyatt.domain.Dynamic;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import android.content.Context;

public class DynamicManager {

	public static void getDynamicByobjectId(Context context,String objectId,GetListener<Dynamic> callback){
		BmobQuery<Dynamic> bq = new BmobQuery<Dynamic>();
		bq.getObject(context, objectId, callback);
	}
}
