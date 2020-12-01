package com.my.mymh.util;

import android.app.Activity;
import android.graphics.Color;

import com.my.mymh.R;
import com.my.mymh.util.choosePic.ISNav;
import com.my.mymh.util.choosePic.config.ISCameraConfig;
import com.my.mymh.util.choosePic.config.ISListConfig;

/**
 * PackageName  com.laiy.lyapp.utils
 * ProjectName  MyCustomeTitle1
 * Author       chenxiaowu
 * Date         2018/3/21.
 */

public class PhotoSelUtil {
    public static final int REQUEST_LIST_CODE = 0;
    public static final int REQUEST_CAMERA_CODE = 1;

    public static void singlePhoto(Activity activity, boolean needCrop) {
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选
                .multiSelect(false)
                .btnText("Confirm")
                // 确定按钮背景色
                //.btnBgColor(Color.parseColor(""))
                // 确定按钮文字颜色
                .btnTextColor(Color.WHITE)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 返回图标ResId
                .backResId(R.drawable.ic_back)
                .title("Images")
                .titleColor(Color.WHITE)
                .titleBgColor(Color.parseColor("#3F51B5"))
                .allImagesText("All Images")
                .needCrop(needCrop)
                .cropSize(1, 1, 400, 400)
                // 第一个是否显示相机
                .needCamera(true)
                // 最大选择图片数量
                .maxNum(9)
                .build();

        ISNav.getInstance().toListActivity(activity, config, REQUEST_LIST_CODE);
    }

    public static void toCamera(Activity activity, boolean needCrop) {
        ISCameraConfig config = new ISCameraConfig.Builder()
                .needCrop(needCrop)
                .cropSize(1, 1, 400, 400)
                .build();

        ISNav.getInstance().toCameraActivity(activity, config, REQUEST_CAMERA_CODE);
    }

    public static void multiSelect(Activity activity, int limit) {
        ISListConfig config = new ISListConfig.Builder()
                .multiSelect(true)
                // 是否记住上次选中记录
                .rememberSelected(false)
                //数量
                .maxNum(limit)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#3F51B5")).build();

        ISNav.getInstance().toListActivity(activity, config, REQUEST_LIST_CODE);
    }
}
