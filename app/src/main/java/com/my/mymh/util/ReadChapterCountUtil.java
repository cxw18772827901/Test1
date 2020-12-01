package com.my.mymh.util;

import android.text.TextUtils;

import com.my.mymh.base.BaseApplication;
import com.my.mymh.base.Constants;
import com.my.mymh.db.dao.ReadCountDao;

/**
 * 记录每个漫画阅读的章节累加,超过五个章节需要分享后才能继续查看
 * PackageName  com.hgd.hgdcomic.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/3/14.
 */

public class ReadChapterCountUtil {
    public static void addCount() {
        ReadCountDao readCountDao = new ReadCountDao(BaseApplication.getContext());
        if (readCountDao.isAdd()) {
            int count = readCountDao.query();
            readCountDao.update(count + 1);
        } else {
            readCountDao.add(1);
        }
    }

    public static boolean canRead() {
        ReadCountDao readCountDao = new ReadCountDao(BaseApplication.getContext());
        if (readCountDao.isAdd()) {
            int count = readCountDao.query();
            if (count > 10) {
                SPUtil.put(Constants.TAG_IS_FIRST_TIME, "has");
            }
            return count <= 10 && TextUtils.isEmpty(SPUtil.getString(Constants.TAG_IS_FIRST_TIME));
        } else {
            return true;
        }
    }
}
