package com.top.sdk.db.impservice;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.top.sdk.db.DBConstant;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.service.PopKeyDbService;
import com.top.sdk.entity.PopKey;
import com.top.sdk.log.LogUtil;

public class ImpPopKeyDbService implements PopKeyDbService {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;

	public ImpPopKeyDbService(Context context) {
		openHelper = DBHelper.getInstance(context);
		dataBase = openHelper.getWritableDatabase();
	}

	@Override
	public PopKey getPopKey(String packageName, String version) {
		Cursor cursor = null;
		PopKey data = null;
		try {
			cursor = dataBase.query(DBConstant.TABLE_NAME_POPKEYDATA, null,
					"packageName = ? and version = ?", new String[] {
							packageName, version }, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				data = new PopKey();
				do {
					data.setAdKey(cursor.getInt(cursor.getColumnIndex("adKey")));
					data.setPackageName(cursor.getString(cursor
							.getColumnIndex("packageName")));
					data.setVersion(cursor.getString(cursor
							.getColumnIndex("version")));
					data.setChannelKey(cursor.getString(cursor
							.getColumnIndex("channleKey")));
					data.setChannelValue(cursor.getString(cursor
							.getColumnIndex("channleValue")));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (cursor != null) {
				cursor.close();
			}
		}

		return data;

	}

	@Override
	public boolean insertListPopKey(List<PopKey> list) {
		if (list != null && list.size() > 0) {
			try {
			   LogUtil.d("list.size"+list.size());
				for (int i = 0; i < list.size(); i++) {
					PopKey pop = list.get(i);
					LogUtil.d("插入数据库的 popKey"+pop.toString());
					if (!selectPopKey(pop.getAdKey())) {
						ContentValues values = new ContentValues();
						values.put("adKey", pop.getAdKey());
						values.put("packageName", pop.getPackageName());
						values.put("version", pop.getVersion() + "");
						values.put("channleKey", pop.getChannelKey());
						values.put("channleValue", pop.getChannelValue());
					 dataBase.insert(
								DBConstant.TABLE_NAME_POPKEYDATA, null, values);
					}
				}
				dataBase.setTransactionSuccessful();
				dataBase.endTransaction();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
		return true;
	}

	private boolean selectPopKey(int adKey) {
		String sql = "select * from " + DBConstant.TABLE_NAME_POPKEYDATA
				+ " where adKey = ?";
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, new String[] { "" + adKey });
			if (cursor != null && cursor.getCount() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			if (cursor != null) {
				cursor.close();
			}
		}
		return true;

	}

	@Override
	public boolean deletePopKey(String adKey) {
		int count = dataBase.delete(DBConstant.TABLE_NAME_POPKEYDATA,
				"adKey = ? ", new String[] { "" + adKey });
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

}
