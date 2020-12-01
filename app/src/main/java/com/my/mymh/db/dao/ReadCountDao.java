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

public class ReadCountDao {
    public static final String COLUMN_TIME = "column_time";
    public static final String COLUMN_COUNT = "column_count";
    public static final String TABLE_READ_COUNT = "table_read_count";
    private final MyHelper myHelper;

    public ReadCountDao(Context context) {
        myHelper = new MyHelper(context);
    }

    public synchronized boolean add(int count) {
        boolean flag = false;
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIME, DateUtil.getTodayTime());
            values.put(COLUMN_COUNT, count);
            db.insert(TABLE_READ_COUNT, null, values);
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
    public synchronized boolean isAdd() {
        boolean result = false;
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(TABLE_READ_COUNT, new String[]{COLUMN_COUNT},
                    COLUMN_TIME + "=?", new String[]{DateUtil.getTodayTime()}, null, null, null);
            if (cursor.moveToNext()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    public int query() {
        int count = 0;
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(TABLE_READ_COUNT, new String[]{COLUMN_COUNT},
                    COLUMN_TIME + "=?", new String[]{DateUtil.getTodayTime()}, null, null, null);
            if (cursor.moveToNext()) {
                count = cursor.getInt(0);
            }
            cursor.close();
            db.close();
        }
        return count;
    }

    public void update(int count) {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COUNT, count);
            db.update(TABLE_READ_COUNT,
                    values,
                    COLUMN_TIME + "=?",
                    new String[]{DateUtil.getTodayTime()});
        }
        db.close();
    }

    public synchronized void deleteAll() {
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TABLE_READ_COUNT, null, null);
            db.close();
        }
    }
}
