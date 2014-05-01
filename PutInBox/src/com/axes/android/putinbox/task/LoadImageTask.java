package com.axes.android.putinbox.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 装载图片任务.
 * 执行:参数(路径)
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
		String path = params[0];
		// 装载图片.
		Bitmap imageBitmap;
		// imageBitmap = BitmapFactory.decodeStream(new
		// FileInputStream(photoFile));
		Options options = new Options();
		options.inSampleSize = 2;
		imageBitmap = BitmapFactory.decodeFile(path, options);
		return imageBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		imageView.setImageBitmap(result);
	}

}