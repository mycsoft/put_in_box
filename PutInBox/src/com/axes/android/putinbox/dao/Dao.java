package com.axes.android.putinbox.dao;

import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Dao extends SQLiteOpenHelper {

	public Dao(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table box(`name` text)";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table box");
		onCreate(db);
	}

	public void saveItem(Map map) {
		String sql = "insert into box (`name`) values(?)";

		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			db.execSQL(sql, new Object[] { map.get("name") });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();

	}

	public Cursor queryAllBox() {
		Cursor c;
		SQLiteDatabase db = getReadableDatabase();
//		c = db.query("box", null, null, null, null, null, null);
		c = db.rawQuery("select name as _id,name from box", null);
//		db.close();
		
		return c;
	}

}
