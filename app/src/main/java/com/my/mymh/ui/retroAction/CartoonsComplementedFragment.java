package com.my.mymh.ui.retroAction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.model.record.RetroActionRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;

/**
 * PackageName  com.hgd.hgdcomic.ui.retroAction
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/11/19.
 */
public class CartoonsComplementedFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "AppSuggestionsFragment";
    @ViewInject(id = R.id.et_name)
    private EditText et_name;
    @ViewInject(id = R.id.et_author)
    private EditText et_author;
    @ViewInject(id = R.id.et_desc)
    private EditText et_desc;
    @ViewInject(id = R.id.tv_commit, needClick = true)
    private TextView tv_commit;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cartoons_complemented_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
        initData();
    }

    private void init() {

    }

    private void initView() {

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                checkInfo();
                break;
        }
    }

    private void checkInfo() {
        String name = et_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            MyDebugUtil.toast("作品名不能为空！");
            return;
        }
        String author = et_author.getText().toString();
        if (TextUtils.isEmpty(author)) {
            MyDebugUtil.toast("作者不能为空！");
            return;
        }
        String desc = et_desc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            desc = "";
        }
        commit(name, author, desc);
    }

    private void commit(String name, String author, String desc) {
        ObjectRequest<RetroActionRecord> response = new ObjectRequest<>(false, RetroActionRecord.Input.buildInput(name, author, desc),
                suggestionRecord -> {
                    if (null != suggestionRecord && 1000 == suggestionRecord.code) {
                        MyDebugUtil.toast("提交成功！");
                        getActivity().finish();
                    } else if (null != suggestionRecord && !TextUtils.isEmpty(suggestionRecord.info)) {
                        MyDebugUtil.toast(suggestionRecord.info);
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> MyDebugUtil.toast("访问失败"));
        NetClient.request(response);
    }
}
