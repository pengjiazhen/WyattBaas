package com.xyc.wyatt.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件的帮助类
 * 
 * @author 彭家珍
 * 
 */
public class FileUtils {

	/**
	 * Returns specified application cache directory. Cache directory will be
	 * created on SD card by defined path if card is mounted. Else - Android
	 * defines cache directory on device's file system.
	 * 
	 * @param context
	 *            Application context
	 * @param cacheDir
	 *            Cache directory path (e.g.: "AppCacheDir",
	 *            "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getOwnCacheDirectory(Context context, String cacheDir) {
		if (cacheDir == null) {
			cacheDir = "wt";
		}
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(Environment.getExternalStorageDirectory(),
					cacheDir);
		}
		if (appCacheDir == null
				|| (!appCacheDir.exists() && !appCacheDir.mkdirs())) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

	public static String getOwnCacheDirectoryPath(Context context,
			String cacheDir) {
		return getOwnCacheDirectory(context, cacheDir).getAbsolutePath();
	}

	public static String SDPATH = WTContant.WTDIMAGEIRECTORY + File.separator;// 文件的保存路径
	// +File.separator+GlobalValues.getUser().getId()+File.separator

	/**
	 * 将bitmap的数据保存到一个文件夹中，
	 * 
	 * @param
	 * @param picName
	 *            保存时照片的名称
	 */
	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static File saveBitmap(Bitmap bm, String fileParentPath,
			String fileFullName) {
		try {
			createDir(fileParentPath);
			File f = new File(fileParentPath, fileFullName);
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			return f;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建sd卡上保存图片的目录
	 * 
	 * @param dirName
	 * @return
	 * @throws IOException
	 */
	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 */
	public static void delFile(String fileName) {
		File file = new File(SDPATH + fileName);
		if (file.isFile()) {
			file.delete();
		}
		file.exists();
	}

	/**
	 * 删除目录
	 */
	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	/**
	 * 判断路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	public static void createDir(String dirPath) {
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static boolean fileExist(String dirPath, String file) {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(dir, file);
			if (f.exists()) {
				return true;
			}
			return false;
		}
		return false;
	}

	public static boolean fileExist(String dirPath, String file, boolean create) {
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(dir, file);
			if (!f.exists()) {
				//WTLogger.e("fileUtils", "" + f.getAbsolutePath());
				if (create) {
					try {
						//WTLogger.e("fileUtils",
							//	f.isFile() + "创建" + f.getAbsolutePath());
						f.createNewFile();
					} catch (IOException e) {
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}

	public static Drawable getDrawableImageFromFile(String uri) {
		return new BitmapDrawable(getBitMapfromFile(uri));
	}

	public static Bitmap getBitMapfromFile(String uri) {
		return BitmapFactory.decodeFile(uri);
	}

	/**
	 * 把给定的路径的图片生产bitmap
	 * 
	 * @param path
	 * @return 生产的bitmap
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path) {
		if (path == null) {
			return null;
		}
		Bitmap bitmap = null;
		BufferedInputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;
			while (true) {
				if ((options.outWidth >> i <= 1000)
						&& (options.outHeight >> i <= 1000)) {
					in = new BufferedInputStream(new FileInputStream(new File(
							path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getImageFromMediaStore(Intent data, Context context) {
		if (!(context instanceof Activity)) {
			return null;
		}
		Uri originalUri = data.getData();
		ContentResolver resolver = context.getContentResolver();
		if (originalUri == null) {
			return null;
		}
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = ((Activity) context).managedQuery(originalUri, proj,
				null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String imagePath = cursor.getString(column_index);
		;
		return imagePath;
	}

}
