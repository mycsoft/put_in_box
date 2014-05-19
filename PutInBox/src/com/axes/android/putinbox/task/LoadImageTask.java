package com.axes.android.putinbox.task;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.axes.android.putinbox.R;

/**
 * 装载图片任务. 执行:参数(路径)
 * 
 * @author MaYichao
 * 
 */
public class LoadImageTask extends AsyncTask<String, Integer, Bitmap> {
	ImageView imageView;
	/** 来源类型-文件 */
	public static final String TYPE_FILE = "file";
	/** 来源类型-文件 */
	public static final String TYPE_URI = "URI";

	public LoadImageTask(ImageView imageView) {
		assert imageView == null;
		this.imageView = imageView;
	}

	@Override
	protected Bitmap doInBackground(String... params) {

		// 图片显示区域不可为空.
//		if (imageView == null || imageView.getWidth() <= 0
//				|| imageView.getHeight() <= 0) {
//			// 图片无法显示.
//			return null;
//		}
		// 解析类型
		String type = TYPE_URI;
		if (params.length > 1) {
			type = params[1];
		}

		if (!type.equals(TYPE_FILE)) {
			return loadImageByUri(params[0]);
		} else {
			return loadImageByFile(params[0]);
		}

		// return null;

	}

	@Override
	protected void onPreExecute() {
		imageView.setImageResource(R.drawable.default_photo);
	}

	/**
	 * 加载图片uri
	 */
	protected Bitmap loadImageByUri(final String uriString) {

		// if (uriString == null) {
		// return null;
		// }
		// try {
		// InputStream inputStream = imageView.getContext()
		// .getContentResolver().openInputStream(Uri.parse(uriString));
		// return loadImageByStream(inputStream);
		//
		// } catch (Exception e) {
		// Log.w("myc", "can't load image uri:" + uriString, e);
		// return null;
		// }

		return new LoadImageFactory() {

			protected boolean canLoad() {
				if (super.canLoad()) {
					return uriString != null;
				} else {
					return false;
				}
			};

			@Override
			protected Bitmap decodeImage(Options options) {
				try {
					InputStream inputStream = imageView.getContext()
							.getContentResolver()
							.openInputStream(Uri.parse(uriString));
					return BitmapFactory.decodeStream(inputStream, null,
							options);

				} catch (Exception e) {
					Log.w("myc", "加载图片失败",e);
					return null;
				}
			}
		}.loadImage();
	}

	/**
	 * 加载图片文件
	 */
	protected Bitmap loadImageByFile(final String path) {

		// if (path == null) {
		// return null;
		// }
		// try {
		// InputStream inputStream = new FileInputStream(path);
		// return loadImageByStream(inputStream);
		//
		// } catch (Exception e) {
		// Log.w("myc", "can't load image file:" + path, e);
		// return null;
		// }

		return new LoadImageFactory() {

			protected boolean canLoad() {
				if (super.canLoad()) {
					return path != null;
				} else {
					return false;
				}
			};

			@Override
			protected Bitmap decodeImage(Options options) {

				return BitmapFactory.decodeFile(path, options);

			}
		}.loadImage();
	}

	// /**
	// * 加载图片文件
	// *
	// * @throws IOException
	// */
	// private Bitmap loadImageByStream(InputStream input) throws IOException {
	// // 图片显示区域不可为空.
	// if (imageView == null || imageView.getWidth() <= 0
	// || imageView.getHeight() <= 0) {
	// // 图片无法显示.
	// return null;
	// }
	//
	// // String path = params[0];
	// if (input == null) {
	// return null;
	// }
	// try {
	// // 装载图片.
	// Bitmap imageBitmap;
	//
	// Options options = new Options();
	// // 预加载
	// options.inJustDecodeBounds = true;
	// // BitmapFactory.decodeFile(path, options);
	// BitmapFactory.decodeStream(input, null, options);
	// // =======================================================
	// // 计算图片需要的缩放比例.
	// if (options.outWidth <= 0 || options.outHeight <= 0) {
	// // 图片无法显示.
	// return null;
	// }
	//
	// options.inSampleSize = Math.max(
	// options.outWidth / imageView.getWidth(), options.outHeight
	// / imageView.getHeight());
	// Log.d("myc", "in sample size=" + options.inSampleSize);
	// // options.inSampleSize = 2;
	// // ========================================================
	//
	// // 正式加载
	// options.inJustDecodeBounds = false;
	// imageBitmap = BitmapFactory.decodeStream(input, null, options);
	// return imageBitmap;
	// } finally {
	// input.close();
	// }
	// }

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result != null)
			imageView.setImageBitmap(result);
	}

	/**
	 * 图片加载工厂
	 * 
	 * @author MaYichao
	 * 
	 */
	private abstract class LoadImageFactory {

		/**
		 * 加载图片文件
		 * 
		 * @throws IOException
		 */
		public Bitmap loadImage() {

			// String path = params[0];
			if (!canLoad()) {
				return null;
			}
			// if (input == null) {
			// }
			try {
				// 装载图片.
				Bitmap imageBitmap;

				Options options = new Options();
				// 预加载
				options.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(path, options);
				decodeImage(options);
				// BitmapFactory.decodeStream(input, null, options);
				// =======================================================
				// 计算图片需要的缩放比例.
				if (options.outWidth <= 0 || options.outHeight <= 0) {
					// 图片无法显示.
					return null;
				}

				options.inSampleSize = Math.max(
						options.outWidth / imageView.getWidth(),
						options.outHeight / imageView.getHeight());
				Log.d("myc", "in sample size=" + options.inSampleSize);
				// options.inSampleSize = 2;
				// ========================================================

				// 正式加载
				options.inJustDecodeBounds = false;
				// imageBitmap = BitmapFactory.decodeStream(input, null,
				// options);
				imageBitmap = decodeImage(options);
				return imageBitmap;
			} finally {
				postLoading();
			}
		}

		/**
		 * 解析图片.
		 * 
		 * @param options
		 */

		protected abstract Bitmap decodeImage(Options options);

		/**
		 * 加载处理之后.
		 */
		protected void postLoading() {
		};

		/**
		 * 检查是否可以加载.
		 * 
		 * @return
		 */
		protected boolean canLoad() {
			// 图片显示区域不可为空.
			if (imageView == null || imageView.getWidth() <= 0
					|| imageView.getHeight() <= 0) {
				// 图片无法显示.
				return false;
			}

			return true;
		};

	}

}