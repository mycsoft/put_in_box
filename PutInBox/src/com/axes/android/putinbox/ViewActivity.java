package com.axes.android.putinbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.axes.android.putinbox.dao.Box;
import com.axes.android.putinbox.fragment.ListFrgt;
import com.axes.android.putinbox.task.LoadImageTask;

/**
 * 查看Activity;
 * 
 * @author MaYichao
 * 
 */
public class ViewActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);

		if (savedInstanceState == null) {
			BoxViewFragment f = new BoxViewFragment();
			f.setBoxId(getIntent().getIntExtra("id", -1));
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, f).commit();
		}
		ActionBar actionBar = getSupportActionBar();
		// actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class BoxViewFragment extends Fragment {

		private Box box;
		private int boxId = -1;

		private TextView nameTxt;
		private TextView descTxt;
		private ImageView imageView;
		
		private ListFrgt listFrgt;

		private LoadImageTask loadImageTask;

		public int getBoxId() {
			return boxId;
		}

		public void setBoxId(int boxId) {
			this.boxId = boxId;
		}

		public BoxViewFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view, container,
					false);
			nameTxt = (TextView) rootView.findViewById(R.id.name);
			descTxt = (TextView) rootView.findViewById(R.id.desc);
			imageView = (ImageView) rootView.findViewById(R.id.photoView);
			listFrgt = (ListFrgt)getFragmentManager().findFragmentById(R.id.fragment1);
//			listFrgt = (ListFrgt)rootView.findViewById(R.id.fragment1);
			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();
			// int id = getActivity().getIntent().getIntExtra("id", -1);
			// 启动后台任务,取得Box信息,并显示画面.
			setHasOptionsMenu(true);
			listFrgt.parentBoxId = boxId;
			// assert id < 0;
			if (box == null) {
				// 当信息没有加载时
				new LoadTask().execute(boxId);
			}
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_delete:
				clickDelete(item);
				return true;

			default:
				return super.onOptionsItemSelected(item);
			}
		}

		/**
		 * 装载图片.
		 * 
		 * @param path
		 */
		private void loadImage(String path) {
			if (loadImageTask != null) {
				loadImageTask.cancel(true);
				loadImageTask = null;
			}
			loadImageTask = new LoadImageTask(imageView);
			loadImageTask.execute(path);
		}

		/**
		 * 删除对象.
		 * 
		 * @param item
		 */
		public void clickDelete(MenuItem item) {
			new AlertDialog.Builder(getActivity()).setCancelable(true)
			.setMessage("确认要删除\"" + box.getName() + "\"吗?")
			.setPositiveButton(android.R.string.yes, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//确认删除
					new AsyncTask<Long, Void, String>() {
						
						@Override
						protected String doInBackground(Long... params) {
							box.delete(App.getApp(getActivity()).dao
									.getWritableDatabase());
							return null;
						}
						
						@Override
						protected void onPostExecute(String result) {
							if (result == null) {
								Toast.makeText(getActivity(), "删除成功!",
										Toast.LENGTH_SHORT).show();
								getActivity().finish();
							}
						}
						
					}.execute();
					
				}
			})
			.setNegativeButton(android.R.string.no, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//取消删除.
					dialog.cancel();
					
				}
			}).show();

		}

		/**
		 * 加载信息任务.
		 * 
		 * @author MaYichao
		 * 
		 */
		private class LoadTask extends AsyncTask<Integer, Integer, Box> {

			@Override
			protected Box doInBackground(Integer... params) {
				Integer id = params[0];
				assert id == null;
				// 根据id取得box对象.
				Box box = Box.loadById(App.openReadableDB(getActivity()), id);
				return box;
			}

			@Override
			protected void onPostExecute(Box result) {
				box = result;
				// 显示画面信息.
				nameTxt.setText(box.getName());
				descTxt.setText(box.getDescription());
				getActivity().setTitle(box.getName());

				if (box.getPhotoPath() != null) {
					// 显示图片
					// Bitmap img =
					// BitmapFactory.decodeFile(box.getPhotoPath());
					// imageView.setImageURI(Uri.fromFile(new
					// File(box.getPhotoPath())));
					loadImage(box.getPhotoPath());

				}
			}

		}
	}

}
