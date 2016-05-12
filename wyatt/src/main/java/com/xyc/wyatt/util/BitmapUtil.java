package com.xyc.wyatt.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.xyc.wyatt.domain.WTimePattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class BitmapUtil {
	public static int max = 0;
	public static boolean act_bool = true;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>(); // 保存图片的bitmap集合

	public static List<String> drr = new ArrayList<String>();// 保存图片路径的集合

	/**
	 * 把给定的路径的图片生产bitmap
	 * 
	 * @param path
	 * @return 生产的bitmap
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		// 1280*780
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static Bitmap revitionImageSize(Bitmap bm, int reqWidth,
			int reqHeight) {
		BufferedInputStream in = null;
		File bmFile = getFileFromUri(bm);
		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		try {
			in = new BufferedInputStream(new FileInputStream(bmFile));
			// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
			BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		try {
			in = new BufferedInputStream(new FileInputStream(bmFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeStream(in, null, options);
	}

	/**
	 * 获取压缩后的图片
	 * 
	 * @param res
	 * 
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static Bitmap revitionImageSize(String path, int reqWidth,
			int reqHeight) {
		BufferedInputStream in = null;
		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		try {

			in = new BufferedInputStream(new FileInputStream(new File(path)));
			// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
			BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeStream(in, null, options);
	}

	/**
	 * 获取压缩后的图片
	 * 
	 * @param res
	 * 
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static Bitmap revitionImageSize(Resources res, int id, int reqWidth,
			int reqHeight) {
		BufferedInputStream in = null;
		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
		BitmapFactory.decodeResource(res, id, options);
		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeResource(res, id, options);
	}

	/**
	 * 计算压缩比例值
	 * 
	 * @param options
	 *            解析图片的配置信息
	 * @param reqWidth
	 *            所需图片压缩尺寸最小宽度
	 * @param reqHeight
	 *            所需图片压缩尺寸最小高度
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 保存图片原宽高值
		final int height = options.outHeight;
		final int width = options.outWidth;
		// 初始化压缩比例为1
		int inSampleSize = 1;

		// 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// 压缩比例值每次循环两倍增加,
			// 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * 让图片翻转90度
	 * 
	 * @param bm
	 * @return
	 */
	public static Bitmap flipBitmap(Bitmap bm) {
		Matrix matrix = new Matrix();
		matrix.postRotate(90); /* 翻转90度 */
		int width = bm.getWidth();
		int height = bm.getHeight();
		bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return bm;
	}

	public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
		try {
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(
					mContext.getContentResolver(), uri);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static File getFileFromUri(Bitmap bm) {
		File f = WTUtil.getOutputMediaFile(WTContant.MEDIA_TYPE_IMAGE);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}

	/** 保存方法 */
	public static Uri saveBitmap(Bitmap bm) {
		File f = WTUtil.getOutputMediaFile(WTContant.MEDIA_TYPE_IMAGE);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Uri.fromFile(f);

	}

	public static File compressImage(Bitmap bm, String text) {
		File file2 = new File(FileUtils.SDPATH.replace("####", "file"));
		if (!file2.exists()) {
			file2.mkdirs();
		}
		File file = new File(file2.getPath(), WTUtil.getSKTimePattern(WTimePattern.YMD, System.currentTimeMillis())+"_"+text + WTContant.IMAGE_SUFFIX[2]);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.e("bitmaputil", file.getName());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int options = 80;
		bm.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
		while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
			byteArrayOutputStream.reset();
			bm.compress(Bitmap.CompressFormat.JPEG, options,
					byteArrayOutputStream);
			options -= 5;
		}
		return bs2file(byteArrayOutputStream, file);
	}

	public static File compressImage(Bitmap bm) {

		File file = new File(WTContant.WTDIMAGEIRECTORY + "file"
				+ WTContant.IMAGE_SUFFIX[2]);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int options = 80;
		bm.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);
		while (byteArrayOutputStream.toByteArray().length / 1024 > 100) {
			byteArrayOutputStream.reset();
			bm.compress(Bitmap.CompressFormat.JPEG, options,
					byteArrayOutputStream);
			options -= 5;
		}
		file = bs2file(byteArrayOutputStream);

		return file;
	}

	private static File bs2file(ByteArrayOutputStream byteArrayOutputStream) {
		if (byteArrayOutputStream != null) {
			BufferedOutputStream stream = null;
			try {
				File file = new File(WTContant.WTDIMAGEIRECTORY + "file"
						+ WTContant.IMAGE_SUFFIX[2]);
				FileOutputStream fstream = new FileOutputStream(file);
				stream = new BufferedOutputStream(fstream);
				stream.write(byteArrayOutputStream.toByteArray());
				return file;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

	private static File bs2file(ByteArrayOutputStream byteArrayOutputStream,
			File file) {
		if (byteArrayOutputStream != null) {
			BufferedOutputStream stream = null;
			try {

				FileOutputStream fstream = new FileOutputStream(file);
				stream = new BufferedOutputStream(fstream);
				stream.write(byteArrayOutputStream.toByteArray());
				return file;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}
}
