package com.axes.android.putinbox;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.axes.android.putinbox.dao.Dao;

public class App extends Application {

	public Dao dao;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		dao = new Dao(this, "putInBox", null, 1);
	}

	/**
	 * 打开新增顶级容器画面.
	 * 
	 * @see #startAddView(Long);
	 */
	public void startAddView() {
		startAddView(null);

	}

	/**
	 * 启动新增画面.
	 * 
	 * @param parentId
	 *            父级id. 空为顶级.
	 */
	public void startAddView(Integer parentId) {
		Intent i = new Intent(this, EditActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (parentId != null) {
			i.putExtra("parent", parentId);
		}
		startActivity(i);

	}

	// @Deprecated
	// public void saveItem(Map map) {
	// dao.saveItem(map);
	//
	// }

	public static App getApp(Activity act) {
		return (App) act.getApplication();
	}

	/**
	 * 打开一个可写数据库.
	 * 
	 * @param act
	 * @return
	 */
	public static SQLiteDatabase openWritableDB(Activity act) {
		App app = getApp(act);
		return app.dao.getWritableDatabase();
	}

	/**
	 * 打开一个只读数据库.
	 * 
	 * @param act
	 * @return
	 */
	public static SQLiteDatabase openReadableDB(Activity act) {
		App app = getApp(act);
		return app.dao.getReadableDatabase();
	}

}
