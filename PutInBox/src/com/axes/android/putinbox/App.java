package com.axes.android.putinbox;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.axes.android.putinbox.dao.Dao;

public class App extends Application {

	public Dao dao;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new Dao(this, "putInBox", null, 1);
	}

	/**
	 * 打开新增顶级容器画面.
	 * 
	 * @see #startAddView(Long);
	 */
	public void startAddView() {
		startAddView(null);

	}

	/**
	 * 启动新增画面.
	 * 
	 * @param parentId
	 *            父级id. 空为顶级.
	 */
	public void startAddView(Integer parentId) {
		Intent i = new Intent(this, EditActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (parentId != null) {
			i.putExtra("parent", parentId);
		}
		startActivity(i);

	}

	// @Deprecated
	// public void saveItem(Map map) {
	// dao.saveItem(map);
	//
	// }

	public static App getApp(Activity act) {
		return (App) act.getApplication();
	}

	/**
	 * 打开一个可写数据库.
	 * 
	 * @param act
	 * @return
	 */
	public static SQLiteDatabase openWritableDB(Activity act) {
		App app = getApp(act);
		return app.dao.getWritableDatabase();
	}

	/**
	 * 打开一个只读数据库.
	 * 
	 * @param act
	 * @return
	 */
	public static SQLiteDatabase openReadableDB(Activity act) {
		App app = getApp(act);
		return app.dao.getReadableDatabase();
	}
	
	/**
	 * 打开相机,进行拍照.
	 */
	public static File openCamera(Activity context,int requestCode){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile(context);
			} catch (IOException ex) {
				// Error occurred while creating the File
				// ...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
//				tempPhotoFile = photoFile;
				context.startActivityForResult(takePictureIntent, requestCode);
				return photoFile;
			}
		}
		return null;
	}
	
	/**
	 * 保存照片.
	 * 
	 * @return 照片的文件对象.
	 * @throws IOException
	 */
	private static File createImageFile(Context context) throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		// File storageDir = Environment
		// .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		// File storageDir = Environment.getExternalStorageDirectory();
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
//		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

}
