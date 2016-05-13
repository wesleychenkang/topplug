package com.top.sdk.db.impservice;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.top.sdk.db.DBConstant;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.service.WhiteDbService;
import com.top.sdk.entity.WhiteData;

public class ImpWhiteDbService implements WhiteDbService {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;

	public ImpWhiteDbService(Context context) {
		openHelper = DBHelper.getInstance(context);
		dataBase = openHelper.getWritableDatabase();
	}

	@Override
	public List<WhiteData> getWhiteDataList() {
		String sql = "select * from " + DBConstant.TABLE_NAME_WHITEDATA;
		List<WhiteData> list = null;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, null);
			list = new ArrayList<WhiteData>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					WhiteData data = new WhiteData();
					data.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					data.setChannleKey(cursor.getString(cursor
							.getColumnIndex("channleKey")));
					data.setListPackageName(cursor.getString(cursor
							.getColumnIndex("listPackageName")));
					list.add(data);

				} while (cursor.moveToNext());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (cursor != null)
				cursor.close();
		}
		return list;
	}

	@Override
	public WhiteData getWhiteDataFromId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertWhiteData(WhiteData data) {
		ContentValues values = new ContentValues();
		values.put("popId", data.getPopId());
		values.put("channleKey", data.getChannleKey());
		values.put("listPackageName", data.getListPackageName());
		long count = dataBase.insert(DBConstant.TABLE_NAME_WHITEDATA, null,
				values);
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteWhiteData(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteAllWhiteData() {
		dataBase.execSQL("delete from whitedata;");
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean updateWhiteData(WhiteData data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getAdIdByPackageName(String packageName) {
		int popId = -1;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery("select * from "
					+ DBConstant.TABLE_NAME_WHITEDATA
					+ "where listPackageName like '%?%' ORDER BY popId DESC",
					new String[] { packageName });
			if (cursor != null && cursor.moveToFirst()) {
				popId = cursor.getInt(cursor.getColumnIndex("popId"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return popId;
	}

	@Override
	public boolean saveWhiteDataList(List<WhiteData> list) {
		if (list != null) {
			dataBase.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				insertWhiteData(list.get(i));
			}
			dataBase.setTransactionSuccessful();
			dataBase.endTransaction();

		}
		return false;
	}

}
