package com.xyc.wyatt.manager;

import java.util.HashMap;
import java.util.Map;







import android.content.Context;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.xyc.wyatt.domain.Comments;
import com.xyc.wyatt.net.WTNetGet;
import com.xyc.wyatt.net.WTNetPost;
import com.xyc.wyatt.util.WTContant;

public class CommentManager {
   public static void getComments(int did,CallBack callBack){
	  
   }
   public static void getComments(Context context,String objectId,FindListener<Comments> callBack){
	   BmobQuery<Comments> bq = new BmobQuery<Comments>();
	   bq.addWhereEqualTo("dobjectId", objectId);
	   bq.order("createdAt");
	   bq.findObjects(context, callBack);
   }
   public static void saveComments(Comments comments,Context context,SaveListener callBack){
	  comments.save(context, callBack);
   }
}
