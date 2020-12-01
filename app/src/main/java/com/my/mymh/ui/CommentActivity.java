package com.my.mymh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.my.mymh.R;
import com.my.mymh.model.record.CommentRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;

import java.util.Collections;

/**
 * PackageName  com.test.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/2/23.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "CommentActivity";
    @ViewInject(id = R.id.et_content, needClick = true)
    private EditText et_content;
    private String cartoonId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.comment_activity);
        init();
    }

    private void init() {
        cartoonId = getIntent().getStringExtra("cartoonId");
        setTitleName("写评论");
        setRightClickViews(Collections.<Object>singletonList("提交"), position -> checkContent());
    }

    private void checkContent() {
        String content = et_content.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            comment(content);
        } else {
            MyDebugUtil.toast("提交评论不能为空");
        }
    }

    private void comment(String content) {
        ObjectRequest<CommentRecord> response = new ObjectRequest<>(false, CommentRecord.Input.buildInput(content, cartoonId),
                commentRecord -> {
                    if (null != commentRecord && 1000 == commentRecord.code) {
                        MyDebugUtil.toast("提交成功");
                        finish();
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> MyDebugUtil.toast("访问失败"));
        NetClient.request(response);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_content:
                et_content.setCursorVisible(true);
                break;
        }
    }
}
