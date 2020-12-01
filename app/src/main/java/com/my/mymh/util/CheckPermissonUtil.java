package com.my.mymh.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.my.mymh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限,获取权限检查工具类
 * Created by Dave on 2016/10/12.
 */
public class CheckPermissonUtil {

    /**
     * 先检查权限,如果其中一个权限还没有就返回false
     *
     * @param context     环境变量
     * @param permissions 高危权限的集合
     * @return
     */
    public static boolean hasPermission(Context context, List<String> permissions) {
        for (String s : permissions) {
            //PERMISSIONyi①
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 申请权限
     *
     * @param context     环境变量
     * @param permissions 权限集合
     * @param requestCode 请求码
     * @param descript    描述
     */

    public static void requestPermissions(final Context context, List<String> permissions, final int requestCode, String descript, CloseListener closeListener) {
        if (permissions.size() == 1) {//单个
            requestOnePermission(context, permissions, requestCode, descript);
        } else if (permissions.size() > 1) {//多个
            requestMorePermissions(context, permissions, requestCode, descript, closeListener);
        } else {
            Toast.makeText(context, "请求权限不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private static void requestOnePermission(final Context context, final List<String> permissions, final int requestCode, String descript) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissions.get(0))) {
            CommonDialog.showDialog(context, descript, "", "", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonDialog.close();
                    switch (v.getId()) {
                        case R.id.bt_ok:
                            ActivityCompat.requestPermissions((Activity) context, new String[]{permissions.get(0)}, requestCode);
                            break;
                        case R.id.bt_cancel:
                            ((Activity) context).finish();
                            break;
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{permissions.get(0)}, requestCode);
        }
    }

    private static void requestMorePermissions(final Context context, List<String> permissions, final int requestCode, String descript, CloseListener closeListener) {
        final List<String> permisonNeededList = new ArrayList<>();
        for (String s : permissions) {
            addPermision(context, permisonNeededList, s);
        }
        if (permisonNeededList.size() > 0) {
            //取消
            //请求权限
            CommonDialog.showDialog(context, descript, "", "", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonDialog.close();
                    switch (v.getId()) {
                        case R.id.bt_ok:
                            ActivityCompat.requestPermissions((Activity) context, permisonNeededList.toArray(new String[permisonNeededList.size()]), requestCode);
                            break;
                        case R.id.bt_cancel:
//                            ((Activity) context).finish();
                            if (closeListener != null) {
                                closeListener.close();
                            }
                            break;
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions((Activity) context, permisonNeededList.toArray(new String[permisonNeededList.size()]), requestCode);
        }
    }

    private static void addPermision(final Context context, List<String> permisonNeededList, String permission) {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context, permission)) {
            permisonNeededList.add(permission);
        }
    }

    public interface CloseListener {
        void close();
    }
}
