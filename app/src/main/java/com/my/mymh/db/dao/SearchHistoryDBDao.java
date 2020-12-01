package com.my.mymh.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my.mymh.db.MyHelper;
import com.my.mymh.db.bean.SearchHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 景点搜索历史dao
 * Created by Dave on 2016/1/21.
 */
public class SearchHistoryDBDao {
    public static final String SEARCH_CONTENT = "search_content";
    public static final String SEARCH_TIME = "search_content_time";
    public static final String CREATE_TIME = "search_content_create_time";
    public static final String SEARCH_COUNT = "search_count";
    public static final String SEARCH_TABLE_NAME = "search_history";
    // 初始化数据库
    private MyHelper helper;

    public SearchHistoryDBDao(Context context) {
        helper = new MyHelper(context);
    }

    /**
     * 添加一条搜索记录
     *
     * @param content
     * @param searchTime
     * @param createTime
     */
    public boolean add(String content, String searchTime, String createTime, String count) {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(SEARCH_CONTENT, content);
            values.put(SEARCH_TIME, searchTime);
            values.put(CREATE_TIME, createTime);
            values.put(SEARCH_COUNT, count);
            db.insert(SEARCH_TABLE_NAME, null, values);
            flag = true;
            db.close();
        }
        return flag;
    }

    /**
     * 判断当前搜索的內容是不是已经添加过,添加过就添加一条新的记录,并且删除之前的记录
     *
     * @param content
     * @return
     */
    public boolean isAdded(String content) {
        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(SEARCH_TABLE_NAME, new String[]{SEARCH_TIME},
                    SEARCH_CONTENT + "=?", new String[]{content}, null, null, null);
            if (cursor.moveToNext()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 查詢一條搜索記錄
     *
     * @param content
     * @return
     */
    public SearchHistoryBean query(String content) {
        SearchHistoryBean bean = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(SEARCH_TABLE_NAME, new String[]{SEARCH_TIME, CREATE_TIME, SEARCH_COUNT},
                    SEARCH_CONTENT + "=?", new String[]{content}, null, null, null);
            if (cursor.moveToNext()) {
                bean = new SearchHistoryBean(content, cursor.getString(0), cursor.getString(1), cursor.getString(2));
            }
            cursor.close();
            db.close();
        }
        return bean;
    }

    /**
     * 查询所有的搜索记录
     *
     * @return
     */
    public List<SearchHistoryBean> queryAll() {
        List<SearchHistoryBean> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(SEARCH_TABLE_NAME, new String[]{SEARCH_CONTENT, SEARCH_TIME, CREATE_TIME, SEARCH_COUNT},
                    null, null, null, null, SEARCH_TIME + " desc");//按照搜索时间降序排列:+ " asc"
            while (cursor.moveToNext()) {
                list.add(new SearchHistoryBean(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            cursor.close();
            db.close();
        }
        return list;
    }

    /**
     * 更新一条数据的搜索时间
     *
     * @param content
     * @param timeNow
     */
    public void update(String content, String timeNow, String count) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(SEARCH_TIME, timeNow);
            values.put(SEARCH_COUNT, count);
            db.update(SEARCH_TABLE_NAME,
                    values,
                    SEARCH_CONTENT + "=?",
                    new String[]{content});
        }
        db.close();
    }

    /**
     * 刪除一条搜索历史
     *
     * @param content
     * @return
     */
    public boolean delete(String content) {
        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(SEARCH_TABLE_NAME, SEARCH_CONTENT, new String[]{content});
            result = true;
            db.close();
        }
        return result;
    }

    /**
     * 清空表内容
     */
    public boolean deleteAll() {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(SEARCH_TABLE_NAME, null, null);
            flag = true;
            db.close();
        }
        return flag;
    }
}
