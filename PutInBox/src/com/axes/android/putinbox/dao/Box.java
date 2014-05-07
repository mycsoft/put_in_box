/**
 * 
 */
package com.axes.android.putinbox.dao;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 一个容器对象.任何对象都可能是个容器. 本对象是系统中的基本类型.
 * 
 * @author MaYichao
 * 
 */
public class Box {

	private Integer id;
	private String name;
	private String description;
	private Box parent;
	private String photoPath;
	private List<Box> children;
	private Date createDate;

	private Date modifyDate;

	public Box() {

	}

	private Box(Cursor c) {
		id = c.getInt(c.getColumnIndex("_id"));
		name = c.getString(c.getColumnIndex("name"));
		description = c.getString(c.getColumnIndex("description"));
		createDate = new Date(c.getLong(c.getColumnIndex("create_date")));
		modifyDate = new Date(c.getLong(c.getColumnIndex("modify_date")));
		photoPath = c.getString(c.getColumnIndex("photo_path"));

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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
			createDate = new Date();
			modifyDate = new Date();
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("description", description);
			cv.put("photo_path", photoPath);
			cv.put("parent", parent == null ? null : parent.id);
			cv.put("create_date", createDate.getTime());
			cv.put("modify_date", modifyDate.getTime());

			long id = db.insert("box", "", cv);
			db.setTransactionSuccessful();
			return id;
		} finally {
			db.endTransaction();
		}

	}

	public static void createTable(SQLiteDatabase db) {
		String sql = "create table box(_id Integer PRIMARY KEY AUTOINCREMENT,`name` text,description text,parent INTEGER,photo_path text,create_date Long,modify_date Long)";
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

	/**
	 * 查询根级列表.
	 * 
	 * @param db
	 * @return
	 */
	public static Cursor queryTopList(SQLiteDatabase db) {
		Cursor c;
		// SQLiteDatabase db = getReadableDatabase();
		c = queryByParent(db, null);

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

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	/**
	 * 删除自身对象.
	 * 
	 * @param db
	 */
	public void delete(SQLiteDatabase db) {
		if (id == null) {
			// 未保存对象不处理.
			return;
		}
		db.beginTransaction();
		try {
			// 删除本向对象.
			db.delete("box", "_id = ? ", new String[] { id.toString() });
			// 将下级对象都放到根级.
			ContentValues cv = new ContentValues();
			cv.put("parent", (Long) null);
			db.update("box", cv, "parent = ?", new String[] { id.toString() });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	public static Cursor queryByParent(SQLiteDatabase db, Integer parentId) {
		// assert parentId == null;
		Cursor c;
		// SQLiteDatabase db = getReadableDatabase();
		if (parentId != null) {
			c = db.query("box", null, "parent = ? ",
					new String[] { parentId.toString() }, null, null,
					"_id desc");
			// c = db.rawQuery("select _id,name from box", null);
			// db.close();
		} else {
			c = db.query("box", null, "parent is null ", null, null, null,
					"_id desc");
		}

		return c;
	}

	/**
	 * 查询所有子对象的总数量.
	 * 
	 * @param db
	 * @return
	 */

	public int getAllChildrenCount(SQLiteDatabase db) {
		int c = 0;
		Cursor cursor = queryByParent(db, id);
		c += cursor.getCount();
		if (cursor.moveToFirst()) {
			// 遍历子级.
			do {
				Box b = new Box(cursor);
				c += b.getAllChildrenCount(db);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return c;
	}

	public void update(SQLiteDatabase db) {
		assert id == null;
		
		assert (parent != null && parent.id == null);

		db.beginTransaction();
		try {
//			createDate = new Date();
			modifyDate = new Date();
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("description", description);
			cv.put("photo_path", photoPath);
			cv.put("parent", parent == null ? null : parent.id);
//			cv.put("create_date", createDate.getTime());
			cv.put("modify_date", modifyDate.getTime());

			db.update("box", cv,"_id = ?",new String[]{String.valueOf(id)});
			db.setTransactionSuccessful();
			return;
		} finally {
			db.endTransaction();
		}

		
	}

}
