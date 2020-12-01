package com.my.mymh.util;

import com.my.mymh.model.CartoonPicItem;

import java.util.List;

/**
 * 慢话每个章节图片太多,不能一次加载,设置固定张数每次加载固定张数的图片
 * PackageName  com.dave.project.util
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/18.
 */

public class CartoonUtil {
    public static final int ONCE_SHOW_MAX_COUNT = 5;

    public static boolean hasData(List<CartoonPicItem> totalItemList, List<CartoonPicItem> showItemList, boolean isLoad) {
        if (null != totalItemList && null != showItemList && totalItemList.size() > 0 && showItemList.size() > 0) {
            if (isLoad) {//展示之后的,判断条件:总列表中的与展示列表最后一条相等的Url的条目不是最后一条即可
                String endUrl = showItemList.get(showItemList.size() - 1).currentPicUrl;
                for (int i = 0; i < totalItemList.size(); i++) {
                    if (endUrl.equals(totalItemList.get(i).currentPicUrl) && totalItemList.size() - 1 > i) {
                        return true;
                    }
                }
            } else {//展示之前的,判断条件:总列表中的与展示列表第一条相等的Url的条目的角标大于0即可
                String startUrl = showItemList.get(0).currentPicUrl;
                for (int i = 0; i < totalItemList.size(); i++) {
                    if (startUrl.equals(totalItemList.get(i).currentPicUrl) && i > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static List<CartoonPicItem> addData(List<CartoonPicItem> totalItemList, List<CartoonPicItem> showItemList, boolean isLoad) {
        if (null != totalItemList && null != showItemList && totalItemList.size() > 0 /*&& showItemList.size() > 0*/) {
            if (showItemList.size() > 0) {
                if (isLoad) {//展示之后的,判断条件:总列表中的与展示列表最后一条相等的Url的条目不是最后一条即可
                    String endUrl = showItemList.get(showItemList.size() - 1).currentPicUrl;
                    for (int i = 0; i < totalItemList.size(); i++) {
                        if (endUrl.equals(totalItemList.get(i).currentPicUrl) && totalItemList.size() - 1 > i) {
                            int addedCount = 0;
                            for (int j = i + 1; j < totalItemList.size(); j++) {
                                if (addedCount < ONCE_SHOW_MAX_COUNT) {
                                    showItemList.add(totalItemList.get(j));
                                    addedCount++;
                                } else {
                                    break;
                                }
                            }
                            return showItemList;
                        }
                    }
                } else {//展示之前的,判断条件:总列表中的与展示列表第一条相等的Url的条目的角标大于0即可
                    String startUrl = showItemList.get(0).currentPicUrl;
                    for (int i = 0; i < totalItemList.size(); i++) {
                        if (startUrl.equals(totalItemList.get(i).currentPicUrl) && i > 0) {
                            int addedCount = 0;
                            for (int j = i - 1; j >= 0; j--) {
                                if (addedCount < ONCE_SHOW_MAX_COUNT) {
                                    showItemList.add(0, totalItemList.get(j));
                                    addedCount++;
                                } else {
                                    break;
                                }
                            }
                            return showItemList;
                        }
                    }
                }
            } else {
                if (totalItemList.size() > CartoonUtil.ONCE_SHOW_MAX_COUNT) {
                    for (int i = 0; i < CartoonUtil.ONCE_SHOW_MAX_COUNT; i++) {
                        showItemList.add(totalItemList.get(i));
                    }
                } else {
                    showItemList.addAll(totalItemList);
                }
                return showItemList;
            }
        }
        return null;
    }
}
