package com.top.sdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper db = null;
	private static final String DB_NAME = "ltv.db";
	private static final int version = 1;
	// 创建广告表
	private static final String CREATE_POPDATA = "create table popdata ("
			+ "id integer primary key autoincrement, "
			+ "popId int,"
			+ "whiteId int,"
			+ "popType int,"
			+ "popUrl text, "
			+ "imgUrl text,"
			+ "version text,"
			+ "channelKey text,"
			+ "channelValue text,"
			+ "packageName text,"
			+ "showCount integer,"
			+ "showRate int,"
			+ "isAutoInstall int,"
			+ "createTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))"
			+ ")";
	// 创建白名单列表
	private static final String CREATE_WHITEDATA = "create table whitedata ("
			+ "id integer primary key autoincrement, "
			+ "popId int, "
			+ "channleKey text,"
			+ "listPackageName text,"
			+ "createTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))"
			+ ")";

	private static final String CREATE_FILE = "create table filedata ("
			+ "id integer primary key autoincrement, "
			+ "fileId int, "
			+ "fileName text,"
			+ "fileUrl text,"
			+ "isSuccess int,"
			+ "createTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))"
			+ ")";

	/**
	 * 广告的安装记录
	 */
	private static final String CREATE_INSTALL_DATA = "create table installdata ("
			+ "id integer primary key autoincrement, "
			+ "popId int, "
			+ "installMonth int,"
			+ "installTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime'))"
			+ ")";

	public static DBHelper getInstance(Context context) {
		if (db == null) {
			db = new DBHelper(context, DB_NAME, null, version);
		}
		return db;
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_POPDATA);
		db.execSQL(CREATE_WHITEDATA);
		db.execSQL(CREATE_FILE);
		db.execSQL(CREATE_INSTALL_DATA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
