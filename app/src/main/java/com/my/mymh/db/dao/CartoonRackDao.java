package com.my.mymh.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my.mymh.db.MyHelper;
import com.my.mymh.db.bean.RackBean;
import com.my.mymh.model.CartoonPicItem;

import java.util.ArrayList;
import java.util.List;

/**
 * PackageName  com.dave.project.db.dao
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/17.
 */

public class CartoonRackDao {
    public static final String CARTOON_ID = "cartoon_id";
    public static final String CARTOON_CONTENT = "cartoon_content";
    public static final String CARTOON_READ_TIME = "cartoon_read_time";
    public static final String CARTOON_RACK_TABLE_NAME = "table_cartoon_rack";

    // 初始化数据库
    private MyHelper helper;

    public CartoonRackDao(Context context) {
        helper = new MyHelper(context);
    }

    /**
     * 添加一条搜索记录
     *
     * @param rackBean
     */
    public boolean add(RackBean rackBean) {
        boolean flag = false;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(CARTOON_ID, rackBean.cartoonId);
            values.put(CARTOON_CONTENT, rackBean.cartoonContent);
            values.put(CARTOON_READ_TIME, rackBean.cartoonReadTime);
            db.insert(CARTOON_RACK_TABLE_NAME, null, values);
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
            Cursor cursor = db.query(CARTOON_RACK_TABLE_NAME, new String[]{CARTOON_CONTENT},
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
//        SQLiteDatabase db = helper.getWritableDatabase();
//        if (db.isOpen()) {
//            ContentValues values = new ContentValues();
//            values.put(ARTICLE_ID, cartoonPicItem.articleId);
//            values.put(CURRENT_INDEX, cartoonPicItem.catalogIndex);
//            db.update(CARTOON_RACK_TABLE_NAME,
//                    values,
//                    CARTOON_ID + "=?",
//                    new String[]{cartoonPicItem.cartoonId});
//        }
//        db.close();
    }

    /**
     * 查詢一條搜索記錄
     *
     * @param cartoonId
     * @return
     */
    public RackBean query(String cartoonId) {
        RackBean rackBean = null;
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(CARTOON_RACK_TABLE_NAME, new String[]{CARTOON_CONTENT, CARTOON_READ_TIME},
                    CARTOON_ID + "=?", new String[]{cartoonId}, null, null, null);
            if (cursor.moveToNext()) {
                rackBean = new RackBean(cartoonId, cursor.getString(0), cursor.getString(1));
            }
            cursor.close();
            db.close();
        }
        return rackBean;
    }


    public List<RackBean> queryAll() {
        List<RackBean> list = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(CARTOON_RACK_TABLE_NAME, new String[]{CARTOON_ID, CARTOON_CONTENT, CARTOON_READ_TIME},
                    null, null, null, null, CARTOON_READ_TIME + " desc");//按照搜索时间降序排列:+ " asc"
            while (cursor.moveToNext()) {
                RackBean result = new RackBean(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                list.add(result);
            }
            cursor.close();
            db.close();
        }
        return list;
    }

    //删除某一条
    public synchronized void deleteById(String cartoonId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(CARTOON_RACK_TABLE_NAME, CARTOON_ID + "=?", new String[]{cartoonId});
            db.close();
        }
    }

    //删除全部
    public synchronized void deleteAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(CARTOON_RACK_TABLE_NAME, null, null);
            db.close();
        }
    }
}
