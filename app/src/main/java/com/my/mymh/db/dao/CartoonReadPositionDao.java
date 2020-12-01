package com.my.mymh.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my.mymh.db.MyHelper;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.model.CartoonPicItem;

/**
 * PackageName  com.dave.project.db.dao
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/17.
 */
public class CartoonReadPositionDao {
    public static final String CARTOON_ID = "cartoon_id";
    public static final String ARTICLE_ID = "article_id";
    public static final String ARTICLE_NAME = "article_name";
    public static final String CURRENT_INDEX = "current_index";
    public static final String CARTOON_READ_TABLE_NAME = "table_cartoon_read_position";

    // 初始化数据库
    private MyHelper helper;

    public CartoonReadPositionDao(Context context) {
        helper = new MyHelper(context);
    }

    /**
     * 添加一条搜索记录
     *
     * @param cartoonPicItem
     */
    public boolean add(CartoonPicItem cartoonPicItem) {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(CARTOON_ID, cartoonPicItem.cartoonId);
            values.put(ARTICLE_ID, cartoonPicItem.articleId);
            values.put(ARTICLE_NAME, cartoonPicItem.articleName);
            values.put(CURRENT_INDEX, cartoonPicItem.catalogIndex);
            db.insert(CARTOON_READ_TABLE_NAME, null, values);
            flag = true;
            db.close();
        }
        return flag;
    }

    /**
     * 判断是不是已经添加过,添加过就添加一条新的记录,并且删除之前的记录
     *
     * @param cartoonId
     * @return
     */
    public boolean isAdded(String cartoonId) {
        boolean result = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(CARTOON_READ_TABLE_NAME, new String[]{CURRENT_INDEX},
                    CARTOON_ID + "=?", new String[]{cartoonId}, null, null, null);
            if (cursor.moveToNext()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 更新一条数据
     *
     * @param cartoonPicItem
     */
    public void update(CartoonPicItem cartoonPicItem) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ARTICLE_ID, cartoonPicItem.articleId);
            values.put(ARTICLE_NAME, cartoonPicItem.articleName);
            values.put(CURRENT_INDEX, cartoonPicItem.catalogIndex);
            db.update(CARTOON_READ_TABLE_NAME,
                    values,
                    CARTOON_ID + "=?",
                    new String[]{cartoonPicItem.cartoonId});
        }
        db.close();
    }

    /**
     * 查詢一條搜索記錄
     *
     * @param cartoonId
     * @return
     */
    public ReadPositionBean query(String cartoonId) {
        ReadPositionBean bean = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(CARTOON_READ_TABLE_NAME, new String[]{ARTICLE_ID, ARTICLE_NAME, CURRENT_INDEX/*, CARTOON_CONTENT*/},
                    CARTOON_ID + "=?", new String[]{cartoonId}, null, null, null);
            if (cursor.moveToNext()) {
                bean = new ReadPositionBean(cartoonId, cursor.getString(0), cursor.getString(1), cursor.getString(2)/*, cursor.getString(3)*/);
            }
            cursor.close();
            db.close();
        }
        return bean;
    }

    //删除某一条
    public synchronized void deleteById(String cartoonId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(CARTOON_READ_TABLE_NAME, CARTOON_ID + "=?", new String[]{cartoonId});
            db.close();
        }
    }

    //删除全部
    public synchronized void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
//            db.delete(TABLE_NAME, CALL_TIME + "=?", new String[]{time});
            db.delete(CARTOON_READ_TABLE_NAME, null, null);
            db.close();
        }
    }
}
