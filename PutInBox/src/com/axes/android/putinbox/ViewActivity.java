package com.axes.android.putinbox;

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
import android.widget.TextView;

import com.axes.android.putinbox.dao.Box;

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
//		actionBar.setHomeButtonEnabled(true);
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
		if(id == android.R.id.home){
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
			nameTxt = (TextView)rootView.findViewById(R.id.name);
			descTxt = (TextView)rootView.findViewById(R.id.desc);
			return rootView;
		}

		@Override
		public void onStart() {
			super.onStart();
			// int id = getActivity().getIntent().getIntExtra("id", -1);
			// 启动后台任务,取得Box信息,并显示画面.

			// assert id < 0;
			if (box == null) {
				//当信息没有加载时
				new LoadTask().execute(boxId);
			}
		}

		/**
		 * 加载信息任务.
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
			}

		}
	}

}
