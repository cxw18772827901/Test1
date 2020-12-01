package com.my.mymh.model;

import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.bean.RackBean;

/**
 * PackageName  com.dave.project.model
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/19.
 */

public class LocalRackBean {
    public RackBean rackBean;
    public ReadPositionBean cartoonReadBean;

    public LocalRackBean() {
    }

    public LocalRackBean(RackBean rackBean, ReadPositionBean cartoonReadBean) {
        this.rackBean = rackBean;
        this.cartoonReadBean = cartoonReadBean;
    }
}
