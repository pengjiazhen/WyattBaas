package com.xyc.wyatt.net;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

/**
 * 图片加载核心? TODO
 * 
 * @author
 * @date 2014�?8�?7�? 上午11:29:18
 */
public class WTImageLoader {
	private static ImageLoader imageLoader;

	public static ImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			synchronized (WTImageLoader.class) {
				if (imageLoader == null) {
					imageLoader = ImageLoader.getInstance();
					imageLoader.init(WTImageLoaderConfig
							.getImageLoaderConfiguration(context));
				}
			}
		}
		return imageLoader;
	}

	private static String getMomeryCacheKeyForImageUri(String uri,
			Context context) {// SKConstant.user_avatar_downLoad+userAccount
		List<String> cacheKeys = MemoryCacheUtil.findCacheKeysForImageUri(uri,
				WTImageLoader.getImageLoader(context).getMemoryCache());
		return cacheKeys != null && !cacheKeys.isEmpty() ? cacheKeys.get(0)
				: null;
	}

	/**
	 * 从内存缓存中移除图片
	 * 
	 * @param uri
	 * @param context
	 */
	public static void removeImageFromMomeryCache(String uri, Context context) {
		String key = getMomeryCacheKeyForImageUri(uri, context);
		if (key == null) {
			return;
		}
		WTImageLoader.getImageLoader(context).getMemoryCache().remove(key);
	}

	private static File getFileForImageUri(String uri, Context context) {// SKConstant.user_avatar_downLoad+userAccount
		File file = DiscCacheUtil.findInCache(uri, WTImageLoader
				.getImageLoader(context).getDiscCache());
		return file != null ? file : null;
	}

	/**
	 * 从硬盘上移除文件
	 * 
	 * @param uri
	 * @param context
	 */
	public static void removeFileFromDisCache(String uri, Context context) {
		File file = getFileForImageUri(uri, context);
		if (file != null) {
			file.delete();
		}
	}

	/**
	 * 把图片从内存缓存和硬盘缓存中移除
	 * 
	 * @param uri
	 * @param context
	 */
	public static void removeImageFromMomeryAndDis(String uri, Context context) {
		removeImageFromMomeryCache(uri, context);
		removeFileFromDisCache(uri, context);
	}

	public static void showImageView(Context context,String uri,
			ImageLoadingListener imageLoadingListener) {
		getImageLoader(context).loadImage(uri,WTImageLoaderConfig.getDisplayOptions(), imageLoadingListener);
	}
}
