package com.axes.android.putinbox;

import java.util.HashMap;
import java.util.Map;

import com.axes.android.putinbox.dao.Box;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditActivity extends Activity {
	private EditText nameTxt;
	private EditText descTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		nameTxt = (EditText) findViewById(R.id.name);
		descTxt = (EditText) findViewById(R.id.desc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
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
			return false;
		default:
			return super.onMenuItemSelected(featureId, item);

		}
	}

}
