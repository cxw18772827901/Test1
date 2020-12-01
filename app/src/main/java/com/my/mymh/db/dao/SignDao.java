package com.my.mymh.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my.mymh.db.MyHelper;
import com.my.mymh.util.DateUtil;

/**
 * PackageName  com.hgd.hgdnovel.db.dao
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/3/19.
 */

public class SignDao {
    public static final String COLUMN_USER_SIGN_TIME = "column_user_sign_time";
    public static final String TABLE_USER_SIGN = "table_user_sign";
    private final MyHelper myHelper;

    public SignDao(Context context) {
        myHelper = new MyHelper(context);
    }

    public synchronized boolean add() {
        if (isSignedToday()) {
            return false;
        }
        boolean flag = false;
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_SIGN_TIME, DateUtil.getTodayTime());
            db.insert(TABLE_USER_SIGN, null, values);
            flag = true;
            db.close();
        }
        return flag;
    }

    /**
     * 判断是不是已经添加过,添加过就添加一条新的记录,并且删除之前的记录
     *
     * @return
     */
    public synchronized boolean isSignedToday() {
        boolean result = false;
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(TABLE_USER_SIGN, null,
                    COLUMN_USER_SIGN_TIME + "=?", new String[]{DateUtil.getTodayTime()}, null, null, null);
            if (cursor.moveToNext()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    public synchronized void deleteAll() {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_USER_SIGN, null, null);
            db.close();
        }
    }
}
