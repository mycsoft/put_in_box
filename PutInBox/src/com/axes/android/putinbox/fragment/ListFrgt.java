/**
 * 
 */
package com.axes.android.putinbox.fragment;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.axes.android.putinbox.App;
import com.axes.android.putinbox.R;
import com.axes.android.putinbox.ViewWithTabActivity;
import com.axes.android.putinbox.dao.Box;

/**
 * 列表的画面. 本画面是很多列表显示物品画面的基类.
 * 
 * @author MaYichao
 * 
 */
public class ListFrgt extends BaseBoxListFrgt {
	/**
	 * 自动连拍的请求代码.
	 */
	public static final int REQUEST_CODE_AUTO_ADD = 33;

	private File autoPhotoFile = null;

	@Override
	public void onStart() {
		super.onStart();
		// 打开菜单.
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		App app = App.getApp(getActivity());

		switch (item.getItemId()) {
		case R.id.add:
			app.startAddView(parentBoxId);
			return true;

		case R.id.autoAdd:
			autoPhotoFile = App.openCamera(getActivity(), this,
					REQUEST_CODE_AUTO_ADD);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	// @Override
	// public void onListItemClick(ListView l, View v, int position, long id) {
	// // 点击行,显示Box细节画面.
	// Intent i = new Intent(getActivity(), ViewActivity.class);
	// i.putExtra("id", (int) id);
	// startActivity(i);
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 点击行,显示Box细节画面.
		// Intent i = new Intent(getActivity(), ViewActivity.class);
		Intent i = new Intent(getActivity(), ViewWithTabActivity.class);
		i.putExtra("id", (int) id);
		startActivity(i);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE_AUTO_ADD:

			if (resultCode == Activity.RESULT_OK) {
				// 确认
				// 保存
				SQLiteDatabase db = App.openWritableDB(getActivity());
				Box box = new Box();
				if (parentBoxId != null) {
					// 保存位置.
					box.setParent(Box.loadById(db, parentBoxId));
				}
				box.setPhotoPath(autoPhotoFile.getAbsolutePath());
				box.save(db);
				db.close();
				// 继续连拍
				autoPhotoFile = App.openCamera(getActivity(), this,
						REQUEST_CODE_AUTO_ADD);

			} else {
				// 刷新.
				updateData();
			}

			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);

		}
	}

}
