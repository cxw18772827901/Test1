package com.my.mymh.util;

/**
 * PackageName  com.hgd.hgdcomic
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2019/2/16.
 */
public class Test {
    int h;
    int m;
    String timeStr;

    public String getTime() {
        if (m == 0) {
            timeStr = h + "o'clock";
        } else if (m == 15) {
            timeStr = "quarter past" + h;
        } else if (m == 30) {
            timeStr = "half past" + h;
        } else if (m == 45) {
            if (h + 1 > 23) {
                timeStr = "quarter to + zero";
            } else {
                timeStr = "quarter to" + (h + 1);
            }
        } else if (m > 0 && m < 30) {
            timeStr = m + "past" + h;
        } else {
            if (h + 1 > 23) {
                timeStr = (60 - m) + "to zero";
            } else {
                timeStr = (60 - m) + "to" + (h + 1);
            }
        }
        return timeStr;
    }

}
