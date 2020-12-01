package com.my.mymh.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.model.record.ReportRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.CheckPermissonUtil;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.PhotoDialogUtil;
import com.my.mymh.util.PhotoSelUtil;
import com.my.mymh.util.QiNiuUploadUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.roundedimageview.CustomShapeImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/11/15.
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "ReportActivity";
    @ViewInject(id = R.id.btn_1)
    private TextView btn_1;
    @ViewInject(id = R.id.btn_2)
    private TextView btn_2;
    @ViewInject(id = R.id.btn_3)
    private TextView btn_3;
    @ViewInject(id = R.id.btn_4)
    private TextView btn_4;
    //    @ViewInject(id = R.id.btn_5)
//    private TextView btn_5;
    @ViewInject(id = R.id.btn_6)
    private TextView btn_6;
    @ViewInject(id = R.id.et_desc)
    private EditText et_desc;
    @ViewInject(id = R.id.iv_error, needClick = true)
    private CustomShapeImageView iv_error;
    @ViewInject(id = R.id.iv_delete_photo, needClick = true)
    private ImageView iv_delete_photo;
    @ViewInject(id = R.id.tv_commit, needClick = true)
    private TextView tv_commit;
    //data
    private String localPath;
    private List<TextView> viewList = new ArrayList<>();
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.report_activity);
        init();
        initView();
        initData();
    }

    private void init() {
        setTitleName("问题反馈");
        id = getIntent().getStringExtra("id");
    }

    private void initView() {
        viewList.add(btn_1);
        viewList.add(btn_2);
        viewList.add(btn_3);
        viewList.add(btn_4);
//        viewList.add(btn_5);
        viewList.add(btn_6);
        for (TextView view : viewList) {
            view.setOnClickListener(v -> {
                for (TextView view1 : viewList) {
                    if (!view1.equals(view) && view1.isSelected()) {
                        view1.setSelected(false);
                    }
                }
                boolean isSelected = view.isSelected();
                view.setSelected(!isSelected);
            });
        }
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_error:
                checkPermissions();
                break;
            case R.id.iv_delete_photo:
                if (!TextUtils.isEmpty(localPath)) {
                    localPath = "";
                    iv_error.setImageResource(R.drawable.ic_pic_holder);
                    iv_delete_photo.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.tv_commit:
                checkInfo();
                break;
        }
    }

    private void checkInfo() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        //error
        int problemType = -1;
        for (TextView textView : viewList) {
            if (textView.isSelected()) {
                String type = textView.getText().toString();
                switch (type) {
                    case "章节乱序":
                        problemType = 1;
                        break;
                    case "缺少内容":
                        problemType = 3;
                        break;
                    case "没有更新":
                        problemType = 4;
                        break;
                    case "图片乱序":
                        problemType = 2;
                        break;
                    default:
                        problemType = 0;
                        break;
                }
            }
        }
        if (problemType == -1) {
            MyDebugUtil.toast("请选择出现的问题！");
            return;
        }
        //remark
        String remark = et_desc.getText().toString();
        if (!TextUtils.isEmpty(remark)) {
            remark = "";
        }
        //url
        if (!TextUtils.isEmpty(localPath)) {
            commitPhoto(String.valueOf(problemType), remark);
        } else {
            commit(String.valueOf(problemType), remark, "");
        }
    }

    private void commitPhoto(String problemType, String remark) {
        QiNiuUploadUtil.upLoadImg(localPath, "feed/dm/", (key, info, response) -> {
            if (info.isOK()) {
                commit(problemType, remark, key);
            } else {
                MyDebugUtil.toast("访问失败,请稍后重试！");
            }
        }, null);
    }

    private void commit(String problemType, String remark, String photos) {
        ObjectRequest<ReportRecord> response = new ObjectRequest<>(false, ReportRecord.Input.buildInput(id, problemType, remark, photos),
                reportRecord -> {
                    if (null != reportRecord && 1000 == reportRecord.code) {
                        MyDebugUtil.toast("提交成功！");
                        ReportActivity.this.finish();
                    } else if (null != reportRecord && !TextUtils.isEmpty(reportRecord.info)) {
                        MyDebugUtil.toast(reportRecord.info);
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> MyDebugUtil.toast("访问失败"));
        NetClient.request(response);
    }

    private static final int PERMISSION_REQUEST_CODE = 1;//检查权限的code

    private void checkPermissions() {
        List<String> permissionsList = Collections.singletonList(Manifest.permission.CAMERA);
        if (!CheckPermissonUtil.hasPermission(this, permissionsList)) {
            CheckPermissonUtil.requestPermissions(this, permissionsList, PERMISSION_REQUEST_CODE, "是否允许权限？", null);
        } else {
            addImg();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Map<String, Integer> pers = new HashMap<>();
                pers.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++) {
                    pers.put(permissions[i], grantResults[i]);
                }
                if (pers.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    addImg();
                } else {
                    MyDebugUtil.toast("无权限（如果您已经勾选不再提醒,需要手动打开权限）！");
                }
                break;
        }
    }

    public void addImg() {
        PhotoDialogUtil.showChoosePicDialog(this, new PhotoDialogUtil.SelPhotoListener() {
            @Override
            public void camera() {
                PhotoDialogUtil.close();
                PhotoSelUtil.toCamera(ReportActivity.this, false);
            }

            @Override
            public void photo() {
                PhotoDialogUtil.close();
//                PhotoSelUtil.multiSelect(ReportActivity.this, 1);
                PhotoSelUtil.singlePhoto(ReportActivity.this, false);
            }

            @Override
            public void cancel() {
                PhotoDialogUtil.close();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PhotoSelUtil.REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            localPath = pathList.get(0);
            loadLocalPic();
        } else if (requestCode == PhotoSelUtil.REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
            localPath = data.getStringExtra("result");
            loadLocalPic();
        }
    }

    private void loadLocalPic() {
        GlideUtil.load(this, localPath, -1, iv_error);
        iv_delete_photo.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewList.clear();
    }
}
