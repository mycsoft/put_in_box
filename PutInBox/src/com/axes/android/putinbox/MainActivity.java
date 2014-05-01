package com.axes.android.putinbox;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Toast;

/**
 * 主应用画面.
 * 
 * @author MaYichao
 * 
 */
public class MainActivity extends ActionBarActivity {

	/** 最后一次按下Back键的时间 */
	private long last_back = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		long t = System.currentTimeMillis();
		if (t - last_back < 2000) {
			System.exit(0);
		} else {
			last_back = t;
			Toast.makeText(this, "再次点击退出!", Toast.LENGTH_SHORT).show();
		}
	}

}
