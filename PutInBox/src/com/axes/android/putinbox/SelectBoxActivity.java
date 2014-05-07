package com.axes.android.putinbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.axes.android.putinbox.dao.Box;

/**
 * 容器选择Activity.
 * 
 * @author mayc
 * 
 */
public class SelectBoxActivity extends ActionBarActivity {

	/**
	 * 画面片段.
	 */
	private PlaceholderFragment fragment = new PlaceholderFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ==========================================
		// 取得待选择的容器对象.
		int id = getIntent().getIntExtra("id", -1);

		assert id < 0; // id不可为空.
		Box box = Box.loadById(App.openReadableDB(this), id);

		assert box == null; // 查询不到box.

		// ---------------------------------------

		setContentView(R.layout.activity_select_box);

		if (savedInstanceState == null) {
			fragment.box = box;
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_box, menu);
		return true;
	}

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle action bar item clicks here. The action bar will
	// // automatically handle clicks on the Home/Up button, so long
	// // as you specify a parent activity in AndroidManifest.xml.
	// // int id = item.getItemId();
	// // if (id == R.id.action_ok) {
	// // //保存新的位置
	// // new AsyncTask<Params, Progress, Result>() {
	// // };
	// // return true;
	// // }
	// return super.onOptionsItemSelected(item);
	// }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * 待选择的容器的box.
		 */
		private Box box;
		/**
		 * 容器选择器.
		 */
		private Spinner parentSpinner;
		/**
		 * 选中项目id-根
		 */
		private final static int HOME = 0;
		/**
		 * 选中项目id-其它
		 */
		private final static int OTHER = -100;

		public PlaceholderFragment() {

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_select_box,
					container, false);
			setHasOptionsMenu(true);
			Box parent = box.getParent();

			parentSpinner = (Spinner) rootView
					.findViewById(R.id.parent_spinner);
			// 组装容器选择器
			initSpinner(parent);
			return rootView;
		}

		/**
		 * 组装容器.
		 * 
		 * @param parent
		 */
		private void initSpinner(Box parent) {
			List<Map<String, ?>> parentSpinnerList = new ArrayList<Map<String, ?>>();

			// 根
			HashMap<String, Object> root = new HashMap<String, Object>();
			root.put("name", "单独放");
			root.put("id", HOME);
			parentSpinnerList.add(root);
			// 当前
			if (parent == null) {
				// 当前 == 根

			} else {
				HashMap<String, Object> p = new HashMap<String, Object>();
				p.put("name", parent.getName());
				p.put("id", parent.getId());
				parentSpinnerList.add(p);

			}
			// 其它
			HashMap<String, Object> other = new HashMap<String, Object>();
			other.put("name", "其它位置");
			other.put("id", OTHER);
			parentSpinnerList.add(other);

			parentSpinner.setAdapter(new SimpleAdapter(getActivity(),
					parentSpinnerList,
					android.R.layout.simple_spinner_dropdown_item,
					new String[] { "name" }, new int[] { android.R.id.text1 }));
			// 确定选中项.
			parentSpinner.setSelection(parent == null ? 0 : 1);

			parentSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							Map<String, Object> map = (Map<String, Object>) parentSpinner
									.getAdapter().getItem(position);
							int selectId = (Integer) map.get("id");
							if (selectId == OTHER) {
								// 调出细节选择画面.
								startActivityForResult(new Intent(
										getActivity(),
										SelectBoxListActivity.class), 1);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub

						}

					});

		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_ok) {
				Integer parentId = (int) parentSpinner.getSelectedItemId();
				if (parentId <= 0) {
					parentId = null;
				}
				// 保存新的位置
				new AsyncTask<Integer, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Integer... params) {
						int boxId = params[0];
						Integer parentId = params[1];
						SQLiteDatabase db = App.openReadableDB(getActivity());
						Box box = Box.loadById(db, boxId);
						Integer pId = box.getParent() != null ? box.getParent()
								.getId() : null;
						if (parentId == null ? pId != null : parentId
								.equals(pId)) {
							Box parent = parentId == null ? null : Box
									.loadById(db, parentId);

							box.setParent(parent);
							box.update(App.openWritableDB(getActivity()));

						}
						;
						return true;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						// 通知保存是否成功.
						String msg;
						if (result) {
							msg = "位置移动成功!";
						} else {
							msg = "位置移动失败!";
						}
						Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT)
								.show();
					}
				}.execute(box.getId(), parentId);
				// 退出画面.
				getActivity().finish();
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}

}
