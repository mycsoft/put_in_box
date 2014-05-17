package com.axes.android.putinbox.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * 装载图片任务. 执行:参数(路径)
 * 
 * @author MaYichao
 * 
 */
public class LoadImageTask extends AsyncTask<String, Integer, Bitmap> {
	ImageView imageView;

	public LoadImageTask(ImageView imageView) {
		assert imageView == null;
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {

		return loadImage(params[0]);
		// return null;

	}

	/**
	 * 加载图片
	 */
	protected Bitmap loadImage(String path) {

		// 图片显示区域不可为空.
		if (imageView == null || imageView.getWidth() <= 0
				|| imageView.getHeight() <= 0) {
			// 图片无法显示.
			return null;
		}

		// String path = params[0];
		if (path == null) {
			return null;
		}
		// 装载图片.
		Bitmap imageBitmap;

		Options options = new Options();
		// 预加载
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		// =======================================================
		// 计算图片需要的缩放比例.
		if (options.outWidth <= 0 || options.outHeight <= 0) {
			// 图片无法显示.
			return null;
		}

		options.inSampleSize = Math.max(
				options.outWidth / imageView.getWidth(), options.outHeight
						/ imageView.getHeight());
		Log.d("myc", "in sample size=" + options.inSampleSize);
		// options.inSampleSize = 2;
		// ========================================================

		// 正式加载
		options.inJustDecodeBounds = false;
		imageBitmap = BitmapFactory.decodeFile(path, options);
		return imageBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null)
			imageView.setImageBitmap(result);
	}

}