package com.axes.android.putinbox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutActivity extends ActionBarActivity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//
//		// Inflate the menu; this adds items to the action bar if it is present.
////		getMenuInflater().inflate(R.menu.about, menu);
//		return false;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		
		private View homePage;
		private View emailView;

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about,
					container, false);
			homePage = rootView.findViewById(R.id.homePage);
			emailView = rootView.findViewById(R.id.email);
			
			homePage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//打开项目主页.
					openBrowser(getResources().getString(R.string.sys_home_page_url));
				}
			});
			
			emailView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//发送项目email.
					sendEmail();
					
				}
			});
			
			
			return rootView;
		}
		/**
		 * 打开网页
		 * @param url
		 */
		protected void openBrowser(String url) {  
	        Uri uri = Uri.parse(url);   
	        //通过Uri获得编辑框里的//地址，加上http://是为了用户输入时可以不要输入  
	        Intent intent = new Intent(Intent.ACTION_VIEW,uri);    
	        //建立Intent对象，传入uri  
	        startActivity(intent);    
	        //启动  
	    }  
		
		protected void sendEmail() {
			Intent i = new Intent(Intent.ACTION_SEND); 
			//i.setType("text/plain"); //模拟器请使用这行
			i.setType("message/rfc822") ; // 真机上使用这行
			i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.sys_email)});
			startActivity(Intent.createChooser(i, "Select email application."));

		}
	}
	
	
	

}
