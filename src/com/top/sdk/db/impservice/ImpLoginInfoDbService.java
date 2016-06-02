package com.top.sdk.db.impservice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.top.sdk.db.DBConstant;
import com.top.sdk.db.DBHelper;
import com.top.sdk.db.service.LoginDBService;
import com.top.sdk.entity.LoginInfo;

public class ImpLoginInfoDbService implements LoginDBService {
	private DBHelper openHelper;
	private SQLiteDatabase dataBase;

	public ImpLoginInfoDbService(Context context) {
		openHelper = DBHelper.getInstance(context);
		dataBase = openHelper.getWritableDatabase();
	}

	@Override
	public boolean saveLoginInfo(LoginInfo infor) {
		String time = infor.getLoginTime();
		ContentValues values = new ContentValues();
		values.put("loginTime", time);
		values.put("userId", infor.getUserId());
		dataBase.insert(DBConstant.TABLE_NAME_LOGIN, null, values);
		dataBase.delete(DBConstant.TABLE_NAME_LOGIN, "loginTime < ?",
				new String[] {time});
		return false;
	}

	@Override
	public LoginInfo getLoginInfo() {
		LoginInfo info = new LoginInfo();
		String time = info.getLoginTime();
		String sql = "select * from " + DBConstant.TABLE_NAME_LOGIN
				+ " where loginTime = ?";
		Cursor cursor = null;
		try {
			cursor = dataBase.rawQuery(sql, new String[] { time });
			if (cursor != null && cursor.getCount() > 0) {
				return info;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}

		return null;
	}

}
