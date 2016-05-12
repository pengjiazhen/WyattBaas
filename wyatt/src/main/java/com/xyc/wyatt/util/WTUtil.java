package com.xyc.wyatt.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.baidu.mapapi.model.LatLng;
import com.xyc.wyatt.R;
import com.xyc.wyatt.domain.WTimePattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 
 * 
 * @author pengjiazhen
 *
 */

public class WTUtil {
 private static String tag="zhong";
	/**
	 * 获取时间的格式
	 * 
	 * @param skTimePattern
	 *            时间格式
	 * @see com.xy.sk.bean.SKTimePattern
	 * @return 转换后相对应的时间格式 /
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSKTimePattern(WTimePattern wTimePattern,
			long timeMillis) {

		return timeMillis != 0 ? new SimpleDateFormat(wTimePattern.pattern())
				.format(new Date(timeMillis)) : new SimpleDateFormat(
				wTimePattern.pattern()).format(new Date());
	}

	/**
	 * 时间转换类,用于显示界面时间时转化用，保存时间不可用此方法 eg：当前时间为2014-07-06 17:59:00
	 * 如果是当天的消息，直接显示时间格式为 09:55 如果是昨天的消息，显示时间为 昨天 09:55 如果是同年的消息 ，显示 07-06 17:59
	 * 否则 2014-07-06 17:59
	 * 
	 * @param time
	 *            格式为 以2014-07-06 形式开头
	 * @see com.xy.sk.util.SKUtil <br/>
	 *      中的getSKTimePattern(SKTimePattern skTimePattern,long timeMillis){
	 * @return
	 */
	public static String getSKTimePattern(String time, Context context) {
		long timeMillis=Long.valueOf(time);
		if (time == null) {
			// return getSKTimePattern(SKTimePattern.MDHM);
		}
		if (time.length() < 10 && !time.startsWith("20")) {
			return null;
		}
		time = getSKTimePattern(WTimePattern.YMD2,Long.valueOf(time));
		String year = time.substring(0, 4);
		String month = time.substring(4, 6);
		String day = time.substring(6, 8);
		String yesterday = getExtraTime(-1);// 昨天的这个时间点
		String today = getExtraTime(0);// 今天的这个时间点
		String t = year + month + day;
		if (t.equals(today)) {
			// 是今天
			return getSKTimePattern(WTimePattern.HM, timeMillis);
		} else if (t.equals(yesterday)) {
			// 是昨天
			return context.getResources().getString(R.string.wt_time_yesterday)
					+ getSKTimePattern(WTimePattern.HM, timeMillis);
		} else if (year.equals(today.substring(0, 4))) {
			// 同年
			return getSKTimePattern(WTimePattern.MDHM, timeMillis);
		}
		return time;
	}

	private static String getExtraTime(int amount) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, amount);
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = new DecimalFormat("00")
				.format(c.get(Calendar.MONTH) + 1);
		;
		String day = new DecimalFormat("00").format(c
				.get(Calendar.DAY_OF_MONTH));
		;
		return year + month + day;
	}

	/**
	 * 时间转换 00:00:00
	 * 
	 * @param t
	 * @return
	 */
	public static String longMillisToTime(long t) {
		long ss = (t / 1000) % 60;
		long mm = (t / (60 * 1000)) % 60;
		long hh = (t / (60 * 60 * 1000)) % 60;
		String s = ss < 10 ? "0" + ss : ss + "";
		String m = mm < 10 ? "0" + mm : mm + "";
		String h = hh < 10 ? "0" + hh : hh + "";
		return h + ":" + m + ":" + s;
	}

	/**
	 * 
	 * 
	 * @param d
	 * @return
	 */
	public static String getDistance(double d) {
		if (d > 0) {
			d = d * 1000;

			int distance = (int) d;
			if (distance < 1000) {
				return distance + "M";
			} else {
				return BigDecimal.valueOf(distance).divide(
						BigDecimal.valueOf(1000))
						+ "KM";
			}
		}
		return 0 + "M";

	}

	/**
	 * 
	 * 
	 * @param ll
	 * @return
	 */
	public static double getSlope(LatLng ll) {
		return ll.latitude / ll.longitude;
	}

	public static double getTwoPointDistance(LatLng start, LatLng end) {
		double s1 = getSlope(start);
		double s2 = getSlope(end);

		double r1 = Math.atan(s1);
		double r2 = Math.atan(s2);

		double r = Math.abs(r2 - r1);
		return (r * Math.PI / 180) * Math.PI * 2 * 6371;
	}

	private static final double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double getTwoPointDistance(double lat1, double lng1,
			double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		// s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public static final String TAG = "MessageForgetUri";
	
	public static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = null;
		try {
			// This location works best if you want the created images to be
			// shared
			// between applications and persist after your app has been
			// uninstalled.

			mediaStorageDir = new File(
					FileUtils.SDPATH.replace("####", "smile"), "avatar");

			Log.d(TAG, "Successfully created mediaStorageDir: "
					+ mediaStorageDir);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Error in Creating mediaStorageDir: " + mediaStorageDir);
		}

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				// 在SD卡上创建文件夹需要权限：
				// <uses-permission
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				Log.d(TAG,
						"failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
				return null;
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type ==WTContant.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		}else if(type==WTContant.MEDIA_TYPE2_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp +"track"+ ".jpg");
		}
		
		else {
			return null;
		}

		return mediaFile;
	}
	
	
	
	public void cropPhoto(Activity act, Bitmap photo) {
		Intent iintent = new Intent("com.android.camera.action.CROP");
		// iintent.setClassName("com.android.camera",
		// "com.android.camera.CropImage");
		// iintent.setDataAndType(uri, "image/*");
		// iintent.putExtra("crop", "true");
		iintent.putExtra("data", photo);
		iintent.setType("image/*");
		iintent.putExtra("outputX", 500);
		iintent.putExtra("outputY", 500);
		iintent.putExtra("aspectX", 1);
		iintent.putExtra("aspectY", 1);
		iintent.putExtra("scale", true);
		iintent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		act.startActivityForResult(iintent, WTContant.CROP_PHOTO_REQUEST_CODE);

	}
	public static Uri startca(Uri imageUri,Activity act) {
		// 利用系统自带的相机应用:拍照
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageUri = WTUtil.getOutputMediaFileUri(WTContant.MEDIA_TYPE_IMAGE);
          Log.i(tag, imageUri+"");
		// 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
		// set the image file name
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		
		act.startActivityForResult(intent, WTContant.REQUE_CODE_CAMERA);
		return imageUri;
	}

	public static  Uri startal(Context mContext) {
		return getImageFromPhoto(mContext, WTContant.REQUE_CODE_PHOTO);
	}

	public static Uri getImageFromPhoto(Context context, int REQUE_CODE_PHOTO) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		Uri imageUri = WTUtil.getOutputMediaFileUri(WTContant.MEDIA_TYPE_IMAGE);
		intent.setDataAndType(imageUri,
				"image/*");
		((Activity) context).startActivityForResult(intent, REQUE_CODE_PHOTO);
	return imageUri;
	}
	public static void startPhotoZoom(Context context, Uri uri,
			int REQUE_CODE_CROP) {
		int dp = 500;

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);// 输出是X方向的比例
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高，切忌不要再改动下列数字，会卡死
		intent.putExtra("outputX", dp);// 输出X方向的像素
		intent.putExtra("outputY", dp);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);// 设置为不返回数据 设置true手机会hold不住

		((Activity) context).startActivityForResult(intent, REQUE_CODE_CROP);
	}
	
}
