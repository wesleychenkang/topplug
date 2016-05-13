package com.top.sdk.db.impservice;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.top.sdk.db.DBConstant;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.service.InstallDbService;
import com.top.sdk.entity.FileInfo;
import com.top.sdk.entity.PopInstallData;
import com.top.sdk.utils.LogUtil;

public class ImpInstallDataService implements InstallDbService {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;

	public ImpInstallDataService(Context context) {
		openHelper = DBHelper.getInstance(context);
		dataBase = openHelper.getWritableDatabase();
	}

	@Override
	public List<PopInstallData> getListInstallData() {

		String sql = "select * from " + DBConstant.TABLE_NAME_INTALLDATA;
		List<PopInstallData> list = null;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, null);
			list = new ArrayList<PopInstallData>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					PopInstallData info = new PopInstallData();
					info.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					info.setInstallTime(cursor.getString(cursor
							.getColumnIndex("installTime")));
					info.setInstallMonth(cursor.getString(cursor
							.getColumnIndex("installMonth")));
					list.add(info);

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}

		}

		return list;
	}

	@Override
	public int getInstallCountByMonth(String month) {
		String sql = "select * from " + DBConstant.TABLE_NAME_INTALLDATA
				+ " where installMonth = ?";
		Cursor cursor = null;
		int count = -1;
		try {
			cursor = dataBase.rawQuery(sql, new String[] {  month });
			count = cursor.getCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();

		}

		return count;
	}

	@Override
	public boolean addInstallData(PopInstallData data) {
		//PopInstallData pop = null;
		PopInstallData pop = getPopInstallById(data.getPopId());// 保证安装的广告ID唯一
		if (null == pop) {
			ContentValues values = new ContentValues();
			values.put("popId", data.getPopId());
			values.put("installMonth",""+data.getInstallMonth());
			long result = dataBase.insert(DBConstant.TABLE_NAME_INTALLDATA,
					null, values);
			if (result > 0) {
				return true;
			}

		}
		return false;
	}

	@Override
	public boolean deleteInstallDataByMonth(String month) {
		int count = dataBase.delete(DBConstant.TABLE_NAME_INTALLDATA,
				"installMonth", new String[] { "" + month });
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public PopInstallData getPopInstallById(int popId) {
		String sql = "select * from " + DBConstant.TABLE_NAME_INTALLDATA
				+ " where popId = ?";
		Cursor cursor = null;
		PopInstallData info = null;
		try {
			cursor = dataBase.rawQuery(sql, new String[] { "" + popId });
			if (cursor != null && cursor.moveToFirst()) {
				do {
					info = new PopInstallData();
					info.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					info.setInstallTime(cursor.getString(cursor
							.getColumnIndex("installTime")));
					info.setInstallMonth(cursor.getString(cursor
							.getColumnIndex("installMonth")));
				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();

		}
		return info;
	}

}
