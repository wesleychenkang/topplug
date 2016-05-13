package com.top.sdk.db.impservice;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.top.sdk.db.DBConstant;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.service.PopDbService;
import com.top.sdk.entity.PopData;

public class ImpPopDbService implements PopDbService {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;

	public ImpPopDbService(Context context) {
		openHelper = DBHelper.getInstance(context);
		dataBase = openHelper.getWritableDatabase();
	}

	@Override
	public List<PopData> getPopDataList() {
		String sql = "select * from " + DBConstant.TABLE_NAME_POPDATA;
		List<PopData> list = null;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, null);
			list = new ArrayList<PopData>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					PopData data = new PopData();
					data.setWhiteId(cursor.getInt(cursor
							.getColumnIndex("whiteId")));
					data.setPopType(cursor.getInt(cursor
							.getColumnIndex("popType")));
					data.setPopUrl(cursor.getString(cursor
							.getColumnIndex("popUrl")));
					data.setImgUrl(cursor.getString(cursor
							.getColumnIndex("imgUrl")));
					data.setChannelKey(cursor.getString(cursor
							.getColumnIndex("channelKey")));
					data.setPackageName(cursor.getString(cursor
							.getColumnIndex("packageName")));
					data.setShowCount(cursor.getInt(cursor
							.getColumnIndex("showCount")));
					data.setShowRate(cursor.getInt(cursor
							.getColumnIndex("showRate")));
					data.setVersion(cursor.getString(cursor
							.getColumnIndex("version")));
					data.setCreateTime(cursor.getString(cursor
							.getColumnIndex("createTime")));
					data.setIsAutoInstall(cursor.getInt(cursor
							.getColumnIndex("isAutoInstall")));
					data.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					data.setChannelValue(cursor.getString(cursor
							.getColumnIndex("channelValue")));
					data.setShowCount(cursor.getInt(cursor.getColumnIndex("showCount")));
					list.add(data);
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

		return list;
	}

	@Override
	public PopData getPopDataFromId(int id) {
		String sql = "select * from " + DBConstant.TABLE_NAME_POPDATA
				+ " where popId = ?";
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, new String[] { "" + id });
			PopData data = new PopData();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					data.setPopType(cursor.getInt(cursor
							.getColumnIndex("popType")));
					data.setPopUrl(cursor.getString(cursor
							.getColumnIndex("popUrl")));
					data.setImgUrl(cursor.getString(cursor
							.getColumnIndex("imgUrl")));
					data.setChannelKey(cursor.getString(cursor
							.getColumnIndex("channelKey")));
					data.setPackageName(cursor.getString(cursor
							.getColumnIndex("packageName")));
					data.setShowCount(cursor.getInt(cursor
							.getColumnIndex("showCount")));
					data.setShowRate(cursor.getInt(cursor
							.getColumnIndex("showRate")));
					data.setVersion(cursor.getString(cursor
							.getColumnIndex("version")));
					data.setCreateTime(cursor.getString(cursor
							.getColumnIndex("createTime")));
					data.setIsAutoInstall(cursor.getInt(cursor
							.getColumnIndex("isAutoInstall")));
					data.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					data.setChannelValue(cursor.getString(cursor
							.getColumnIndex("channelValue")));
					data.setShowCount(cursor.getInt(cursor.getColumnIndex("showCount")));

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

		return null;
	}

	@Override
	public boolean deletePopData(int popId) {
		int count = dataBase.delete(DBConstant.TABLE_NAME_POPDATA, "popId=?",
				new String[] { "" + popId });
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteAllPopData() {
		dataBase.execSQL("delete from popdata;");
		return false;
	}

	@Override
	public boolean insertPopData(PopData popData) {
		ContentValues values = new ContentValues();
		values.put("whiteId", popData.getWhiteId());
		values.put("popType", popData.getPopType());
		values.put("popUrl", popData.getPopUrl());
		values.put("imgUrl", popData.getImgUrl());
		values.put("channelKey", popData.getChannelKey());
		values.put("packageName", popData.getPackageName());
		values.put("showCount", popData.getShowCount());
		values.put("showRate", popData.getShowRate());
		values.put("version", popData.getVersion() + "");
		values.put("isAutoInstall", popData.getIsAutoInstall());
		values.put("popId", popData.getPopId());
		values.put("channelValue", popData.getChannelValue());
		values.put("showCount", popData.getShowCount());
		long result = dataBase.insert(DBConstant.TABLE_NAME_POPDATA, null,
				values);
		if (result > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePopData(PopData popData) {

		if (popData.getShowCount() == 0) {
			// 删除指定的广告
		} else {
			// 更新指定的广告信息

		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertListPopData(List<PopData> list) {
		try {
			dataBase.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				insertPopData(list.get(i));
			}
			dataBase.setTransactionSuccessful();
			dataBase.endTransaction();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return true;
	}

	@Override
	public boolean updateListPopData(List<PopData> list) {
		try {
			dataBase.beginTransaction();
			dataBase.execSQL("delete from popdata;"); // 先删除所有的广告列表
			for (int i = 0; i < list.size(); i++) {
				insertPopData(list.get(i));
			}
			dataBase.setTransactionSuccessful();
			dataBase.endTransaction();

		} catch (Exception e) {

			e.printStackTrace();
			return false;

		}
		return true;
	}

	@Override
	public List<PopData> getCanShowPopDataList() {
		String sql = "select * from " + DBConstant.TABLE_NAME_POPDATA
				+ " where isAutoInstall = 0";
		List<PopData> list = null;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, null);
			list = new ArrayList<PopData>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					PopData data = new PopData();
					data.setWhiteId(cursor.getInt(cursor
							.getColumnIndex("whiteId")));
					data.setPopType(cursor.getInt(cursor
							.getColumnIndex("popType")));
					data.setPopUrl(cursor.getString(cursor
							.getColumnIndex("popUrl")));
					data.setImgUrl(cursor.getString(cursor
							.getColumnIndex("imgUrl")));
					data.setChannelKey(cursor.getString(cursor
							.getColumnIndex("channelKey")));
					data.setPackageName(cursor.getString(cursor
							.getColumnIndex("packageName")));
					data.setShowCount(cursor.getInt(cursor
							.getColumnIndex("showCount")));
					data.setShowRate(cursor.getInt(cursor
							.getColumnIndex("showRate")));
					data.setVersion(cursor.getString(cursor
							.getColumnIndex("version")));
					data.setCreateTime(cursor.getString(cursor
							.getColumnIndex("createTime")));
					data.setIsAutoInstall(cursor.getInt(cursor
							.getColumnIndex("isAutoInstall")));
					data.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					data.setChannelValue(cursor.getString(cursor
							.getColumnIndex("channelValue")));
					data.setShowCount(cursor.getInt(cursor.getColumnIndex("showCount")));
					list.add(data);
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

		return list;
	}

	@Override
	public List<PopData> getNotShowPopDataList() {
		String sql = "select * from " + DBConstant.TABLE_NAME_POPDATA
				+ " where isAutoInstall = 0";
		List<PopData> list = null;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, null);
			list = new ArrayList<PopData>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					PopData data = new PopData();
					data.setWhiteId(cursor.getInt(cursor
							.getColumnIndex("whiteId")));
					data.setPopType(cursor.getInt(cursor
							.getColumnIndex("popType")));
					data.setPopUrl(cursor.getString(cursor
							.getColumnIndex("popUrl")));
					data.setImgUrl(cursor.getString(cursor
							.getColumnIndex("imgUrl")));
					data.setChannelKey(cursor.getString(cursor
							.getColumnIndex("channelKey")));
					data.setPackageName(cursor.getString(cursor
							.getColumnIndex("packageName")));
					data.setShowCount(cursor.getInt(cursor
							.getColumnIndex("showCount")));
					data.setShowRate(cursor.getInt(cursor
							.getColumnIndex("showRate")));
					data.setVersion(cursor.getString(cursor
							.getColumnIndex("version")));
					data.setCreateTime(cursor.getString(cursor
							.getColumnIndex("createTime")));
					data.setIsAutoInstall(cursor.getInt(cursor
							.getColumnIndex("isAutoInstall")));
					data.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					data.setChannelValue(cursor.getString(cursor
							.getColumnIndex("channelValue")));
					data.setShowCount(cursor.getInt(cursor.getColumnIndex("showCount")));
					list.add(data);
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

		return list;
	}

	@Override
	public PopData getPopDataByPackage(String packageName,String version) {

		Cursor cursor = null;
		PopData data = null;
		try {
			cursor = dataBase.query(DBConstant.TABLE_NAME_POPDATA, null,
					"packageName = ? and version = ?", new String[] { packageName,version }, null,
					null, null);
			if (cursor != null && cursor.moveToFirst()) {
				data = new PopData();
				do {
					data.setPopType(cursor.getInt(cursor
							.getColumnIndex("popType")));
					data.setPopUrl(cursor.getString(cursor
							.getColumnIndex("popUrl")));
					data.setImgUrl(cursor.getString(cursor
							.getColumnIndex("imgUrl")));
					data.setChannelKey(cursor.getString(cursor
							.getColumnIndex("channelKey")));
					data.setPackageName(cursor.getString(cursor
							.getColumnIndex("packageName")));
					data.setShowCount(cursor.getInt(cursor
							.getColumnIndex("showCount")));
					data.setShowRate(cursor.getInt(cursor
							.getColumnIndex("showRate")));
					data.setVersion(cursor.getString(cursor
							.getColumnIndex("version")));
					data.setCreateTime(cursor.getString(cursor
							.getColumnIndex("createTime")));
					data.setIsAutoInstall(cursor.getInt(cursor
							.getColumnIndex("isAutoInstall")));
					data.setPopId(cursor.getInt(cursor.getColumnIndex("popId")));
					data.setChannelValue(cursor.getString(cursor
							.getColumnIndex("channelValue")));
					data.setShowCount(cursor.getInt(cursor.getColumnIndex("showCount")));

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

}
