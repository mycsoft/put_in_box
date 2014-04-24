package com.axes.android.putinbox;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.axes.android.putinbox.dao.Box;

/**
 * 编辑画面.
 * 
 * @author MaYichao
 * 
 */
public class EditActivity extends ActionBarActivity {
	private EditText nameTxt;
	private EditText descTxt;
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		nameTxt = (EditText) findViewById(R.id.name);
		descTxt = (EditText) findViewById(R.id.desc);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setDisplayShowCustomEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			save();
			return false;

		case android.R.id.home:
			finish();
			return false;
		default:
			return super.onOptionsItemSelected(item);

		}
	}
	
	public void save(View v) {
		save();
	}

	private void save() {
		// Map map = new HashMap();
		// map.put("name", nameTxt.getText().toString());
		Box box = new Box();
		box.setName(nameTxt.getText().toString());
		box.setDescription(descTxt.getText().toString());
		SQLiteDatabase db = App.openWritableDB(this);
		try {
			box.save(db);
		} finally {
			db.close();
		}
		// ((App)getApplication()).saveItem(map);
		finish();
	}

}
