/**
 * 
 */
package com.axes.android.putinbox.fragment;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.axes.android.putinbox.App;
import com.axes.android.putinbox.R;
import com.axes.android.putinbox.ViewActivity;

/**
 * 列表的画面.
 * 本画面是很多列表显示物品画面的基类.
 * @author MaYichao
 * 
 */
public class ListFrgt extends BaseBoxListFrgt {
	
	@Override
	public void onStart() {
		super.onStart();
		//打开菜单.
		setHasOptionsMenu(true);
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
