package com.axes.android.putinbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		getMenuInflater().inflate(R.menu.select_box_list, menu);
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
		switch (id) {
		case android.R.id.home:
			// 返回上级

			// 根级时关闭画面.
			break;
		}
		return super.onOptionsItemSelected(item);
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
		public void onStart() {
			super.onStart();
			
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// 点击行,显示Box细节画面.
//			Intent i = new Intent(getActivity(), SelectBoxActivity.class);
//			i.putExtra("id", (int) id);
//			startActivity(i);
			parentBoxId = (int)id;
			updateData();
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			switch(id){
			case android.R.id.home:
				//返回上级
				if(parentBoxId != null){
//					getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
					Box parentP = Box.loadById(App.openReadableDB(getActivity()), parentBoxId).getParent();
					parentBoxId = parentP == null? null : parentP.getId();
					updateData();
				}else{
					//根级时关闭画面.
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
