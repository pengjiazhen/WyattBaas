package com.xyc.wyatt.net;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.xyc.wyatt.R;

/**
 * 图片加载器，可加载网络图片和缓存、本地图片，并对这些图片进行相应的缓�? 显示图片�?使用的uri�?
 * 
 * String imageUri = "http://site.com/image.png"; // from Web String imageUri
 * ="file:///mnt/sdcard/image.png"; // from SD card String imageUri
 * ="content://media/external/audio/albumart/13"; // from content provider
 * String imageUri = "assets://image.png"; // from assets String imageUri =
 * "drawable://" + R.drawable.image; // from drawables (only images, non-9patch)
 * 
 */
public class WTImageLoaderConfig {
	private static ImageLoaderConfiguration imageLoaderConfiguration;
	// private static SKImageLoaderConfig imageLoader;
	private static File cacheDir;
	private static DisplayImageOptions options;

	private WTImageLoaderConfig() {

	}

	/**
	 * 初始化配�?
	 * 
	 * @param context
	 *            上下�?
	 *
	 * @return 图片加载�?
	 */
	public static void initImageLoaderConfiguration(Context context) {
		// if(imageLoader == null){
		// imageLoader = new SKImageLoaderConfig();
		cacheDir = StorageUtils.getCacheDirectory(context);
		imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context)
				// .taskExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
				// .taskExecutorForCachedImages(AsyncTask.THREAD_POOL_EXECUTOR)
				.threadPoolSize(5)
				// default // Sets thread pool size for image display tasks.
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				// default 先进先出
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)) // �?近最久未使用
				.memoryCacheSize(4 * 1024 * 1024)// 内存的缓�? 4M
				.discCache(new UnlimitedDiscCache(cacheDir)) // default
																// UnlimitedDiscCache
				.discCacheSize(100 * 1024 * 1024) // 硬盘缓存的大�? 100M
				// .discCacheFileCount(100) //By default: disc cache is
				// unlimited.
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
				.imageDownloader(new BaseImageDownloader(context)) // default
				.imageDecoder(new BaseImageDecoder()) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				// .enableLogging()
				.build();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.picdefault)
				// 在ImageView加载过程中显示图�?
				.showImageForEmptyUri(R.drawable.picdefault)
				// image连接地址为空�?
				.showImageOnFail(R.drawable.picdefault)
				// image加载失败
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(2)) // 加载图片的task，以圆角显示图片
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // 图片缩放类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		// }
		// return imageLoader;
	}

	/**
	 * 得到�?个图片加载配置管理器imageLoaderConfiguration，在imageLoaderConfiguration中可�?
	 * 对图片加载的方式以及存储等进行相应的配置
	 * 
	 * @return 图片加载配置管理�?
	 */
	public static ImageLoaderConfiguration getImageLoaderConfiguration(
			Context context) {
		imageLoaderConfiguration = null;
		initImageLoaderConfiguration(context);
		return imageLoaderConfiguration;
	}

	/**
	 * 得到图片显示选项DisplayImageOptions，DisplayImageOptions规定了图片在哪里显示�? 以何种方式显�?
	 *
	 * @return 图片显示选项
	 */
	public static DisplayImageOptions getDisplayOptions() {
		return options;
	}

}
