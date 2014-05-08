package com.axes.android.putinbox;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.axes.android.putinbox.dao.Box;
import com.axes.android.putinbox.fragment.ListFrgt;

/**
 * 容器选择列表画面.
 * 
 * @author mayc
 * 
 */
public class SelectBoxListActivity extends ActionBarActivity {

	PlaceholderFragment currentFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_box_list);

		if (savedInstanceState == null) {
			PlaceholderFragment f = new PlaceholderFragment();
			// 设置父容器id.
			Bundle b = getIntent().getExtras();
			if (b != null) {
				f.parentBoxId = b.getInt("id", -1);
				if (f.parentBoxId < 0) {
					f.parentBoxId = null;
				}
			}

			// f.getListView().setOnItemClickListener(this);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, f).commit();
			currentFragment = f;
		}

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.select_box_list, menu);
		return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends ListFrgt {

		public PlaceholderFragment() {
		}

		// @Override
		// public View onCreateView(LayoutInflater inflater, ViewGroup
		// container,
		// Bundle savedInstanceState) {
		// // View rootView =
		// inflater.inflate(R.layout.fragment_select_box_list,
		// // container, false);
		// View rootView = super.onCreateView(inflater, container,
		// savedInstanceState);
		// return rootView;
		// }

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// 点击行,显示Box细节画面.
			// Intent i = new Intent(getActivity(), SelectBoxActivity.class);
			// i.putExtra("id", (int) id);
			// startActivity(i);
			parentBoxId = (int) id;
			updateData();
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			switch (id) {
			// Home钮
			case android.R.id.home:
				if (parentBoxId != null) {
					// 返回上级
					// getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
					SQLiteDatabase db = App.openReadableDB(getActivity());
					Box parentP = Box.loadById(db, parentBoxId);
					parentP.loadParent(db);
					parentP = parentP.getParent();
					parentBoxId = parentP == null ? null : parentP.getId();
					updateData();
				} else {
					// 根级时关闭画面.
					getActivity().setResult(RESULT_CANCELED);
					getActivity().finish();
				}
				return true;

				// 确定钮
			case R.id.action_ok:
				// 返回选中的项
				if (parentBoxId != null) {
					// 返回值
					Intent result = new Intent();
					result.putExtra("id", parentBoxId);
					getActivity().setResult(RESULT_OK, result);
					getActivity().finish();

				} else {
					// 根级时关闭画面.
					getActivity().setResult(RESULT_CANCELED);
					getActivity().finish();
				}
				return true;
			}
			return super.onOptionsItemSelected(item);
		}

		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			inflater.inflate(R.menu.select_box_list, menu);
		}
	}

}
