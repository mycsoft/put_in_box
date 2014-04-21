package com.axes.android.putinbox.dao;

import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据管理层
 * @author MaYichao
 *
 */
public class Dao extends SQLiteOpenHelper {

	public Dao(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//按顺序调用每个对象 的createTable(db)方法.
		Box.createTable(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table box");
		onCreate(db);
	}

//	/**
//	 * 保存一个Box.
//	 * @param map 行对象.{name,description,parent,saveDate}
//	 */
//	@Deprecated
//	public void saveItem(Map map) {
//		String sql = "insert into box (`name`) values(?)";
//
//		SQLiteDatabase db = getWritableDatabase();
//		db.beginTransaction();
//		try {
//			db.execSQL(sql, new Object[] { map.get("name") });
//			db.setTransactionSuccessful();
//		} finally {
//			db.endTransaction();
//		}
//		db.close();
//
//	}

//	@Deprecated
//	public Cursor queryAllBox() {
//		Cursor c;
//		SQLiteDatabase db = getReadableDatabase();
////		c = db.query("box", null, null, null, null, null, null);
//		c = db.rawQuery("select name as _id,name from box", null);
////		db.close();
//		
//		return c;
//	}

}
