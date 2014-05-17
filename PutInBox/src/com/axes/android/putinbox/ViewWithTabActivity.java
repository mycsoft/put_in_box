package com.axes.android.putinbox;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.axes.android.putinbox.ViewActivity.BoxViewFragment;
import com.axes.android.putinbox.fragment.ListFrgt;

/**
 * 分Tab页查看容器信息.
 * 
 * @author MaYichao
 * 
 */
public class ViewWithTabActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	public int boxId;

	BoxViewFragment f;

	private ChildrenMainFragment cMF;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_with_tab);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// init fragment
		// childrenListFragment = new ChildrenListFragment();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		boxId = getIntent().getIntExtra("id", -1);

	}

	@Override
	protected void onStart() {
		super.onStart();
		updateData();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// // Inflate the menu; this adds items to the action bar if it is present.
	// // getMenuInflater().inflate(R.menu.view_with_tab, menu);
	// return false;
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		f.onBackPressed();
		boxId = f.getBoxId();
		updateData();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).

			switch (position) {
			case 0:
				if (cMF == null) {
					cMF = new ChildrenMainFragment();
					// childrenListFragment.parentBoxId = boxId;
					// cMF.activity = ViewWithTabActivity.this;
				}
				return cMF;
			case 1:
				f = new BoxViewFragment();
				f.setBoxId(boxId);
				// f.setHasOptionsMenu(true);
				return f;

			}
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {

			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_view_with_tab,
					container, false);
			// TextView textView = (TextView) rootView
			// .findViewById(R.id.section_label);
			// textView.setText(Integer.toString(getArguments().getInt(
			// ARG_SECTION_NUMBER)));
			return rootView;
		}

	}

	public void updateData() {
		if (cMF != null && cMF.clf != null) {
			cMF.clf.parentBoxId = boxId;
			cMF.clf.updateData();

		}
		if (f != null) {
			f.setBoxId(boxId);
			f.updateData();

		}

	}

	/**
	 * 子列表框架画面. 未知的原因,{@link ChildrenListFragment}不能被直接生成.所以引入本框架类,通过layout文件调用
	 * {@link ChildrenListFragment}.
	 * 
	 * @author mayc
	 * 
	 */
	public static class ChildrenMainFragment extends Fragment {
		/**
		 * 子列表.
		 */
		ChildrenListFragment clf;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.fragment_view_with_tab_child,
					null);
			clf = (ChildrenListFragment) getFragmentManager().findFragmentById(
					R.id.fragment2);
			return root;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			setHasOptionsMenu(true);
			// clf.activity = (ViewWithTabActivity) activity;
		}

	}

	/**
	 * 子列表.
	 * 
	 * @author mayc
	 * 
	 */
	public static class ChildrenListFragment extends ListFrgt {

		// private PlaceholderFragment parentFragment = null;
		private ViewWithTabActivity activity;

		// public ChildrenListFragment() {
		//
		// }

		@Override
		public void onAttach(Activity activity) {
			this.activity = (ViewWithTabActivity) activity;
			parentBoxId = this.activity.boxId;
			super.onAttach(activity);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// 刷新本画面.
			activity.boxId = (int) id;
			activity.updateData();
		}
	}

}
