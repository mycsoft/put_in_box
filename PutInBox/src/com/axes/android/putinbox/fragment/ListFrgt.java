/**
 * 
 */
package com.axes.android.putinbox.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.axes.android.putinbox.App;
import com.axes.android.putinbox.R;
import com.axes.android.putinbox.ViewActivity;
import com.axes.android.putinbox.dao.Box;
import com.axes.android.putinbox.task.LoadImageTask;

/**
 * 列表的画面.
 * 
 * @author MaYichao
 * 
 */
public class ListFrgt extends ListFragment {

	// private Button addBtn;
	// private ListView listView;
	public Integer parentBoxId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// View v = inflater.inflate(R.layout.activity_main, null);
		// listView = (ListView) v.findViewById(R.id.listView);
		// return v;
		View root = super.onCreateView(inflater, container, savedInstanceState);
//		// 隐藏分隔线.
//		getListView().setFooterDividersEnabled(false);
		return root;
	}

	@Override
	public void onStart() {
		super.onStart();
		setHasOptionsMenu(true);

		// setMenuVisibility(true);
		Cursor c = parentBoxId == null ? Box.queryAllBox(App
				.openReadableDB(getActivity())) : Box.queryByParent(
				App.openReadableDB(getActivity()), parentBoxId);

		// listView.setAdapter(
		setListAdapter(new SimpleCursorAdapter(getActivity(),
				R.layout.box_list_item, c, new String[] { "name", "_id",
						"description" }, new int[] { R.id.name, R.id.id,
						R.id.desc }, 0) {
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				View v = super.getView(arg0, arg1, arg2);
				// 显示图片.
				String path = ((Cursor) getItem(arg0)).getString(4);
				if (path != null) {
					ImageView imgV = (ImageView) v.findViewById(R.id.imageView);
					new LoadImageTask(imgV).execute(path);

				}
				return v;
			};
		}

		);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 隐藏分隔线.
		getListView().setFooterDividersEnabled(false);
		getListView().setDivider(new ColorDrawable(Color.BLUE));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			App app = (App) getActivity().getApplication();
			app.startAddView(parentBoxId);
			return false;
		} else {
			return super.onOptionsItemSelected(item);

		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// 点击行,显示Box细节画面.
		Intent i = new Intent(getActivity(), ViewActivity.class);
		i.putExtra("id", (int) id);
		startActivity(i);
	}

}
