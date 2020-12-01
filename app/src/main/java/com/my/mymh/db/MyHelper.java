package com.my.mymh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.my.mymh.db.dao.CartoonRackDao;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.db.dao.ReadCountDao;
import com.my.mymh.db.dao.SearchHistoryDBDao;
import com.my.mymh.db.dao.SignDao;
import com.my.mymh.util.DBTableUtil;

import java.util.Arrays;
import java.util.Collections;

/**
 * 表1:SEARCH_TABLE_NAME,搜索记录
 * 表2:CARTOON_READ_TABLE_NAME,阅读记录
 * 表2:CARTOON_RACK_TABLE_NAME,书架记录
 *
 * @author dave 2017-4-6
 */
public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context) {
        super(context, "cartoon.db", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                SearchHistoryDBDao.SEARCH_TABLE_NAME, Arrays.asList(
                        SearchHistoryDBDao.SEARCH_CONTENT,
                        SearchHistoryDBDao.SEARCH_TIME,
                        SearchHistoryDBDao.CREATE_TIME,
                        SearchHistoryDBDao.SEARCH_COUNT)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                CartoonReadPositionDao.CARTOON_READ_TABLE_NAME, Arrays.asList(
                        CartoonReadPositionDao.CARTOON_ID,
                        CartoonReadPositionDao.ARTICLE_ID,
                        CartoonReadPositionDao.ARTICLE_NAME,
                        CartoonReadPositionDao.CURRENT_INDEX)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                CartoonRackDao.CARTOON_RACK_TABLE_NAME, Arrays.asList(
                        CartoonRackDao.CARTOON_ID,
                        CartoonRackDao.CARTOON_CONTENT,
                        CartoonRackDao.CARTOON_READ_TIME)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                SignDao.TABLE_USER_SIGN, Collections.singletonList(
                        SignDao.COLUMN_USER_SIGN_TIME)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                ReadCountDao.TABLE_READ_COUNT, Arrays.asList(
                        ReadCountDao.COLUMN_COUNT,
                        ReadCountDao.COLUMN_TIME)));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBTableUtil.dropTable(SearchHistoryDBDao.SEARCH_TABLE_NAME));
        db.execSQL(DBTableUtil.dropTable(CartoonReadPositionDao.CARTOON_READ_TABLE_NAME));
        db.execSQL(DBTableUtil.dropTable(CartoonRackDao.CARTOON_RACK_TABLE_NAME));
        db.execSQL(DBTableUtil.dropTable(SignDao.TABLE_USER_SIGN));
        db.execSQL(DBTableUtil.dropTable(ReadCountDao.TABLE_READ_COUNT));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                SearchHistoryDBDao.SEARCH_TABLE_NAME, Arrays.asList(
                        SearchHistoryDBDao.SEARCH_CONTENT,
                        SearchHistoryDBDao.SEARCH_TIME,
                        SearchHistoryDBDao.CREATE_TIME,
                        SearchHistoryDBDao.SEARCH_COUNT)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                CartoonReadPositionDao.CARTOON_READ_TABLE_NAME, Arrays.asList(
                        CartoonReadPositionDao.CARTOON_ID,
                        CartoonReadPositionDao.ARTICLE_ID,
                        CartoonReadPositionDao.ARTICLE_NAME,
                        CartoonReadPositionDao.CURRENT_INDEX)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                CartoonRackDao.CARTOON_RACK_TABLE_NAME, Arrays.asList(
                        CartoonRackDao.CARTOON_ID,
                        CartoonRackDao.CARTOON_CONTENT,
                        CartoonRackDao.CARTOON_READ_TIME)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                SignDao.TABLE_USER_SIGN, Collections.singletonList(
                        SignDao.COLUMN_USER_SIGN_TIME)));
        db.execSQL(DBTableUtil.appendCreateTableSqString(
                ReadCountDao.TABLE_READ_COUNT, Arrays.asList(
                        ReadCountDao.COLUMN_COUNT,
                        ReadCountDao.COLUMN_TIME)));
    }
}
