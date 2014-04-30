package com.axes.android.putinbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.axes.android.putinbox.dao.Box;

/**
 * 编辑画面.
 * 
 * @author MaYichao
 * 
 */
public class EditActivity extends ActionBarActivity {
	private EditText nameTxt;
	private EditText descTxt;
	private ImageView photoView;
	private ActionBar actionBar;
	private Box box;
	/**
	 * 临时照片文件.
	 */
	private File tempPhotoFile;
	/**
	 * 正式照片文件.
	 */
	private File photoFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		nameTxt = (EditText) findViewById(R.id.name);
		descTxt = (EditText) findViewById(R.id.desc);
		photoView = (ImageView) findViewById(R.id.photoView);

		// 拍照事件
		photoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photo();

			}
		});

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowCustomEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			save();
			return false;

		case android.R.id.home:
			finish();
			return false;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	public void save(View v) {
		save();
	}

	/**
	 * 保存事件.包括新增与修改.
	 */
	private void save() {
		// Map map = new HashMap();
		// map.put("name", nameTxt.getText().toString());
		// Box box = new Box();
		if(box == null){
			box = new Box();
		}
		box.setName(nameTxt.getText().toString());
		box.setDescription(descTxt.getText().toString());
		box.setPhotoPath(photoFile == null ? null : photoFile.getAbsolutePath());
		SQLiteDatabase db = App.openWritableDB(this);
		try {
			if (box.getId() == null) {
				// 保存新增
				box.save(db);
			} else {
				// 保存更新.
				// box.update(db);
			}
		} finally {
			db.close();
		}
		// ((App)getApplication()).saveItem(map);
		finish();
	}

	// ==================== 拍照 ===============================
	/**
	 * 拍照事件的请求码.
	 */
	static final int REQUEST_IMAGE_CAPTURE = 1;
	/**
	 * 保存照片的请求码.
	 */
	static final int REQUEST_TAKE_PHOTO = 2;

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				// ...
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				tempPhotoFile = photoFile;
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	/**
	 * 响应拍照事件.
	 * 
	 * @param view
	 */
	private void photo() {
		dispatchTakePictureIntent();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//			Bundle extras = data.getExtras();
			//替换照片文件
			photoFile = tempPhotoFile;
			
//			Bitmap imageBitmap = (Bitmap) extras.get("data");
//			try {
//				Bitmap imageBitmap;
////				imageBitmap = BitmapFactory.decodeStream(new FileInputStream(photoFile));
//				imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//				// 设置略缩图.
//				photoView.setImageBitmap(imageBitmap);
//				try {
//					photoFile = createImageFile();
//					imageBitmap.compress(CompressFormat.JPEG, 90, new FileOutputStream(photoFile));
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					Log.e("photo", "save photo failed", e);
//				}
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			imageBitmap.
			
			photoView.setImageURI(Uri.fromFile(photoFile));
		}
	}

	/**
	 * 照片路径.
	 */
	String mCurrentPhotoPath;

	/**
	 * 保存照片.
	 * 
	 * @return 照片的文件对象.
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//		File storageDir = Environment.getExternalStorageDirectory();
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void dispatchTakePictureSmallIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Ensure that there's a camera activity to handle the intent
//			if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//				// Create the File where the photo should go
//				File photoFile = null;
//				try {
//					photoFile = createImageFile();
//				} catch (IOException ex) {
//					// Error occurred while creating the File
//					// ...
//				}
//				// Continue only if the File was successfully created
//				if (photoFile != null) {
//					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//							Uri.fromFile(photoFile));
					startActivityForResult(takePictureIntent,
							REQUEST_TAKE_PHOTO);
//				}
//			}
		}

	}

}
