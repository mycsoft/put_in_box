/**
 * 
 */
package com.axes.android.putinbox.dao;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

/**
 * 一个容器对象.任何对象都可能是个容器. 本对象是系统中的基本类型.
 * 
 * @author MaYichao
 * 
 */
public class Box {

	private Long id;
	private String name;
	private String description;
	private Box parent;
	private List<Box> children;
	
	public Box() {
		
	}

	private Box(Cursor c) {
		id = c.getLong(c.getColumnIndex("_id"));
		name = c.getString(c.getColumnIndex("name"));
		description = c.getString(c.getColumnIndex("description"));
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Box getParent() {
		return parent;
	}

	public void setParent(Box parent) {
		this.parent = parent;
	}

	public List<Box> getChildren() {
		return children;
	}

	public void setChildren(List<Box> children) {
		this.children = children;
	}

	public long save(SQLiteDatabase db) {
		if (id != null) {
			assert id != null;
			// "this box has been saved! can";
		}

		assert (parent != null && parent.id == null);

		// String sql =
		// "insert into box (`id`,`name`,`description`,`parent`) values(?,?,?,?)";
		// create id;
		db.beginTransaction();
		try {
			// db.execSQL(sql, new Object[] { id, name,description,parent ==
			// null ? null : parent.id});
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("description", description);
			cv.put("parent", parent == null ? null : parent.id);

			long id = db.insert("box", "", cv);
			db.setTransactionSuccessful();
			return id;
		} finally {
			db.endTransaction();
		}

	}

	public static void createTable(SQLiteDatabase db) {
		String sql = "create table box(_id Integer PRIMARY KEY AUTOINCREMENT,`name` text,description text,parent INTEGER)";
		db.execSQL(sql);
	}

	public static Cursor queryAllBox(SQLiteDatabase db) {
		Cursor c;
		// SQLiteDatabase db = getReadableDatabase();
		c = db.query("box", null, null, null, null, null, "_id desc");
		// c = db.rawQuery("select _id,name from box", null);
		// db.close();

		return c;
	}

	public static Box loadById(SQLiteDatabase db, Integer id) {
		Cursor c = db.query("box", null, "_id = ? ",
				new String[] { id.toString() }, null, null, null);
		if (c.getCount() <= 0)
			return null;
		c.moveToFirst();
		Box box = new Box(c);
		c.close();
		return box;
	}

}
