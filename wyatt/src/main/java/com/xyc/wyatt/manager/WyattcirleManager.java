package com.xyc.wyatt.manager;

import android.content.Context;

import com.xyc.wyatt.domain.Dynamic;
import com.xyc.wyatt.util.WTContant;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobQuery.CachePolicy;
import cn.bmob.v3.listener.FindListener;




public class WyattcirleManager {
    public static void getDuimamic(Context context,CachePolicy cachePolicy,int pageNum,FindListener<Dynamic> callback){
    	int pageSize=20;
    	BmobQuery<Dynamic> bq = new BmobQuery<Dynamic>();
    	bq.setLimit(pageSize);
    	bq.order("-createdAt");
    	bq.setSkip((pageNum-1)*pageSize);
    	boolean isCache = bq.hasCachedResult(context, Dynamic.class);
    	if (isCache) {// --此为举个例子，并不一定按这种方式来设置缓存策略
			bq.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK); // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
		} else {
			bq.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE); // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
		}
    	if(cachePolicy!=null){
    		bq.setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
    	}
    	bq.findObjects(context, callback);
    }
  
    public static void updateDuimamic(int id,String points,CallBack callBack){
    	
    	Map<String, String> dynamicMap=new HashMap<String, String>();
    	dynamicMap.put("points", points);
    	
    	//WTNetPost.postData(WTContant.DYNAMIC_UPDATE_DYNAMIC+"?id="+id, dynamicMap, callBack);
    	System.out.println(WTContant.DYNAMIC_UPDATE_DYNAMIC+"?id="+id);

    }
}
