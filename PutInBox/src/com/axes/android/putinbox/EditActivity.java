package com.axes.android.putinbox;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.axes.android.putinbox.dao.Box;
import com.axes.android.putinbox.task.LoadImageTask;

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
	private Integer parentId;

	private LoadImageTask loadImageTask;
	/**
	 * 临时照片文件.
	 */
	private File tempPhotoFile;
	/**
	 * 正式照片文件.
	 */
	private File photoFile;
	/**
	 * 照片路径.
	 */
	private Uri photoPathUri;
	/**
	 * 选择照片的对话框.有两个选项:相机与相册.
	 */
	private Dialog photoChangeDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// =================================================
		// 画面初始化.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		nameTxt = (EditText) findViewById(R.id.name);
		descTxt = (EditText) findViewById(R.id.desc);
		photoView = (ImageView) findViewById(R.id.photoView);
		// --------------------------------------------------

		Intent i = getIntent();
		Integer id = i.getIntExtra("id", -1);

		if (id > 0) {
			// 编辑
			box = Box.loadById(App.openReadableDB(this), id);
			assert box == null;
			nameTxt.setText(box.getName());
			descTxt.setText(box.getDescription());

		} else {
			// 新增
			parentId = i.getIntExtra("parent", -1);
			setTitle(R.string.title_activity_new);
		}
		// 拍照事件
		photoView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// photo();
				photoChangeDialog.show();

			}
		});

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowCustomEnabled(true);

		photoChangeDialog = initPhotoDialog();

	}

	/**
	 * 初始化照片对话框.
	 * 
	 * @return
	 */
	private Dialog initPhotoDialog() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择照片");

		builder.setPositiveButton("相机", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface

			dialog, int which) {
				photo();

			}
		});
		builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface

			dialog, int which) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_IMAGE_SELECT);

			}
		});
		AlertDialog alert = builder.create();
		// alert.show();
		return alert;
	}

	/**
	 * 装载图片.
	 * 
	 * @param path
	 */
	private void loadImage(String path, String type) {
		if (loadImageTask != null) {
			loadImageTask.cancel(true);
			loadImageTask = null;
		}
		loadImageTask = new LoadImageTask(photoView);
		loadImageTask.execute(path, type);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
		if (box != null) {
			// 编辑
			
			if (box.getPhotoPath() != null) {
				// loadImage(box.getPhotoPath(), LoadImageTask.TYPE_FILE);
				if (photoPathUri == null) {
					photoPathUri = Uri.parse(box.getPhotoPath());
				}
				loadImage(photoPathUri.toString(), LoadImageTask.TYPE_URI);
				
			}
			
		} else {
			// 新增
			// box = new Box();
			
		}
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
		if (box == null) {
			box = new Box();
		}
		box.setName(nameTxt.getText().toString());
		box.setDescription(descTxt.getText().toString());

		// 保存相册内的照片为文件.
		if (photoFile == null && photoPathUri != null) {

		}

		box.setPhotoPath(photoPathUri == null ? null : photoPathUri.toString());
		SQLiteDatabase db = App.openWritableDB(this);
		if (parentId != null && parentId > 0) {
			// 有父容器
			Box parent = Box.loadById(db, parentId);
			box.setParent(parent);
		}
		try {
			if (box.getId() == null) {
				// 保存新增
				box.save(db);
			} else {
				// 保存更新.
				box.update(db);
				Intent i = new Intent();
				i.putExtra("success", true);
				setResult(RESULT_OK, i);
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
	 * 选择照片事件的请求码.
	 */
	static final int REQUEST_IMAGE_SELECT = 3;
	/**
	 * 保存照片的请求码.
	 */
	static final int REQUEST_TAKE_PHOTO = 2;

	// private void dispatchTakePictureIntent() {
	// Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	// // Ensure that there's a camera activity to handle the intent
	// if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	// // Create the File where the photo should go
	// File photoFile = null;
	// try {
	// photoFile = createImageFile();
	// } catch (IOException ex) {
	// // Error occurred while creating the File
	// // ...
	// }
	// // Continue only if the File was successfully created
	// if (photoFile != null) {
	// takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	// Uri.fromFile(photoFile));
	// tempPhotoFile = photoFile;
	// startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	// }
	// }
	// }

	/**
	 * 响应拍照事件.
	 * 
	 * @param view
	 */
	private void photo() {
		// dispatchTakePictureIntent();
		// dispatchTakePictureSmallIntent();
		tempPhotoFile = App.openCamera(this, REQUEST_IMAGE_CAPTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_IMAGE_CAPTURE:
			// 拍照返回
			if (resultCode == RESULT_OK) {
				if (tempPhotoFile != null) {
					// 替换照片文件
					photoFile = tempPhotoFile;
					photoPathUri = Uri.fromFile(photoFile);
					loadImage(photoPathUri.toString(), LoadImageTask.TYPE_URI);
				}
			}

			break;

		case REQUEST_IMAGE_SELECT:
			// 选择照片返回.
			if (resultCode == RESULT_OK) {
				try {
					// 获得图片的uri
					Uri originalUri = data.getData();
					photoFile = null;
					photoPathUri = originalUri;
					loadImage(photoPathUri.toString(), LoadImageTask.TYPE_URI);
				} catch (Exception e) {
					Log.e("myc", "选择照片失败.", e);
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 照片路径.
	 */
	// String mCurrentPhotoPath;

	// /**
	// * 保存照片.
	// *
	// * @return 照片的文件对象.
	// * @throws IOException
	// */
	// private File createImageFile() throws IOException {
	// // Create an image file name
	// String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
	// .format(new Date());
	// String imageFileName = "JPEG_" + timeStamp + "_";
	// // File storageDir = Environment
	// // .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	// File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	// // File storageDir = Environment.getExternalStorageDirectory();
	// File image = File.createTempFile(imageFileName, /* prefix */
	// ".jpg", /* suffix */
	// storageDir /* directory */
	// );
	//
	// // Save a file: path for use with ACTION_VIEW intents
	// mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	// return image;
	// }

}
