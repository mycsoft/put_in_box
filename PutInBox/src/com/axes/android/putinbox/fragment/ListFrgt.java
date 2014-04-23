/**
 * 
 */
package com.axes.android.putinbox.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.axes.android.putinbox.App;
import com.axes.android.putinbox.R;
import com.axes.android.putinbox.dao.Box;

/**
 * 列表的画面.
 * 
 * @author MaYichao
 * 
 */
public class ListFrgt extends ListFragment {

	// private Button addBtn;
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		View v = inflater.inflate(R.layout.activity_main, null);
//		listView = (ListView) v.findViewById(R.id.listView);
//		return v;
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		setHasOptionsMenu(true);
		Cursor c = Box.queryAllBox(App.openReadableDB(getActivity()));
		
//		listView.setAdapter(
		setListAdapter(
				new SimpleCursorAdapter(getActivity(),
				R.layout.box_list_item, c, new String[] { "name", "_id",
						"description" }, new int[] { R.id.name, R.id.id,
						R.id.desc }, 0)

		);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.add) {
			App app = (App) getActivity().getApplication();
			app.startAddView();
			return false;
		} else {
			return super.onOptionsItemSelected(item);

		}
	}

}
