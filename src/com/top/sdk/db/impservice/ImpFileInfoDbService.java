package com.top.sdk.db.impservice;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.top.sdk.db.DBConstant;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.service.FileInfoDbService;
import com.top.sdk.entity.FileInfo;
import com.top.sdk.log.LogUtil;

public class ImpFileInfoDbService implements FileInfoDbService {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;

	public ImpFileInfoDbService(Context context) {
		openHelper = DBHelper.getInstance(context);
		dataBase = openHelper.getWritableDatabase();
	}

	@Override
	public List<FileInfo> getListFileInfo() {
		String sql = "select * from " + DBConstant.TABLE_NAME_FILEINFO;
		List<FileInfo> list = null;
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, null);
			list = new ArrayList<FileInfo>();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					FileInfo info = new FileInfo();
					info.setFileId(cursor.getInt(cursor
							.getColumnIndex("fileId")));
					info.setFileName(cursor.getString(cursor
							.getColumnIndex("fileName")));
					info.setFileUrl(cursor.getString(cursor
							.getColumnIndex("fileUrl")));
					info.setIsSuccess(cursor.getInt(cursor
							.getColumnIndex("isSuccess")));

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
	public FileInfo getFileInfoByUrl(String url) {
		String sql = "select * from " + DBConstant.TABLE_NAME_FILEINFO
				+ " where fileUrl = ?";

		Cursor cursor = null;
		FileInfo info = null;
		try {
			info = new FileInfo();
			cursor = dataBase.rawQuery(sql, new String[] { url });
			if (cursor != null && cursor.moveToFirst()) {
				do {

					info.setFileId(cursor.getInt(cursor
							.getColumnIndex("fileId")));
					info.setFileName(cursor.getString(cursor
							.getColumnIndex("fileName")));
					info.setFileUrl(cursor.getString(cursor
							.getColumnIndex("fileUrl")));
					info.setIsSuccess(cursor.getInt(cursor
							.getColumnIndex("isSuccess")));

				} while (cursor.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null) {
				cursor.close();
			}

		}
		return info;
	}

	@Override
	public List<FileInfo> getAllSuccessFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addFileInfo(FileInfo info) {
		FileInfo i = getFileInfoByUrl(info.getFileUrl());
		if(null == i){
		ContentValues values = new ContentValues();
		values.put("fileId", info.getFileId());
		values.put("fileName", info.getFileName());
		values.put("fileUrl", info.getFileUrl());
		values.put("isSuccess", info.getIsSuccess());
		long result = dataBase.insert(DBConstant.TABLE_NAME_FILEINFO, null,
				values);
		if (result > 0) {
			return true;
		}
		}
		return false;
	}

	@Override
	public boolean addListFileInfo(List<FileInfo> list) {
		// TODO Auto-generated method stub
		try {
			dataBase.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				addFileInfo(list.get(i));
			}
			dataBase.setTransactionSuccessful();
			dataBase.endTransaction();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return false;
	}

	@Override
	public boolean updateFileInfo(FileInfo info) {
		int count = -1;
		try {
			ContentValues values = new ContentValues();
			values.put("fileId", info.getFileId());
			values.put("fileName", info.getFileName());
			values.put("fileUrl", info.getFileUrl());
			values.put("isSuccess", info.getIsSuccess());
			count = dataBase.update(DBConstant.TABLE_NAME_FILEINFO, values, "fileName = ?", new String[]{info.getFileName()});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtil.d("更新的条数为"+count);
		return false;
	}

}
