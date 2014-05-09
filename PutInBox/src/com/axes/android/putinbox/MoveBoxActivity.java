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
 * 移动容器Activity.
 * 
 * @author mayc
 * 
 */
public class MoveBoxActivity extends ActionBarActivity {

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
		 * 可选位置列表.
		 */
		private List<Map<String, ?>> parentSpinnerList = new ArrayList<Map<String, ?>>();
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
			box.loadParent(App.openReadableDB(getActivity()));
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

			// 根
			HashMap<String, Object> root = new HashMap<String, Object>();
			root.put("name", "单独放");
			root.put("_id", HOME);
			parentSpinnerList.add(root);
			// 当前
			if (parent == null) {
				// 当前 == 根

			} else {
				HashMap<String, Object> p = new HashMap<String, Object>();
				p.put("name", parent.getName());
				p.put("_id", parent.getId());
				parentSpinnerList.add(p);

			}
			// 其它
			HashMap<String, Object> other = new HashMap<String, Object>();
			other.put("name", "其它位置");
			other.put("_id", OTHER);
			parentSpinnerList.add(other);

			parentSpinner.setAdapter(new SimpleAdapter(getActivity(),
					parentSpinnerList,
					android.R.layout.simple_spinner_dropdown_item,
					new String[] { "name" }, new int[] { android.R.id.text1 }) {
				/**
				 * @see android.widget.Adapter#getItemId(int)
				 */
				public long getItemId(int position) {
					return ((Integer) ((Map<String, Object>) getItem(position))
							.get("_id")).longValue();
				}
			});
			// 确定选中项.
			parentSpinner.setSelection(parent == null ? 0 : 1);

			parentSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						// 缓存最后一次选择的位置.
						int lastSelected = 0;

						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							Map<String, Object> item = (Map<String, Object>) parentSpinner
									.getAdapter().getItem(position);
							Map<String, Object> map = item;
							int selectId = (Integer) map.get("_id");
							if (selectId == OTHER) {
								// 调出细节选择画面.
								startActivityForResult(new Intent(
										getActivity(),
										SelectBoxListActivity.class), 1);
								// 返回最后一次选择的位置.
								parent.setSelection(lastSelected);
								parent.invalidate();
							} else {
								lastSelected = position;
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
						box.loadParent(db);
						Integer pId = box.getParent() != null ? box.getParent()
								.getId() : null;
						if (parentId == null ? pId != null : !parentId
								.equals(pId)) {
							Box parent = parentId == null ? null : Box
									.loadById(db, parentId);
							box.setParent(parent);
							box.update(App.openWritableDB(getActivity()));
						}
						return true;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						getActivity().setResult(RESULT_OK);
						getActivity().finish();
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

				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			switch (requestCode) {
			// 选择其它位置返回.
			case 1:
				if (resultCode == RESULT_OK) {
					// 选择了新位置
					int id = data.getExtras().getInt("id");
					// 载入新的位置信息
					SQLiteDatabase db = App.openReadableDB(getActivity());
					Box c = Box.loadById(db, id);

					// 检查新位置是否有效.
					boolean canMove = (!box.getId().equals(id))
							&& (!box.isContained(db, c))
							;
					if (canMove) {
						// 如果有效,则加入新的位置,并选中它.
						int position = addNewOption(c);
						assert position <= 0;
						parentSpinner.setSelection(position);
					} else {
						Toast.makeText(getActivity(), "不能移到这个位置!",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;

			default:
				super.onActivityResult(requestCode, resultCode, data);

			}
		}

		private int addNewOption(Box c) {
			int position = 0;
			// 遍历检查是否有重复的.
			for (int i = 1; i < parentSpinnerList.size() - 1; i++) {
				Map<String, ?> row = parentSpinnerList.get(i);
				if (c.getId().equals(row.get("_id"))) {
					// 发现重复
					position = i;
					break;
				}
			}

			if (position == 0) {
				// 未找到.将新位置放在第二位.
				position = 1;
				HashMap<String, Object> p = new HashMap<String, Object>();
				p.put("name", c.getName());
				p.put("_id", c.getId());
				parentSpinnerList.add(position, p);
				// parentSpinner.invalidate();
			}
			return position;
		}
	}

}
