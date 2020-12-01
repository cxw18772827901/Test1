package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.CartoonUsersRecord;
import com.my.mymh.model.record.CommentListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.RoundImageView;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.test.project.ui.cartoonDetailFragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/2/23.
 */

public class CommentListActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "CommentListActivity";
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private ListView listview;
    @ViewInject(id = R.id.fab, needClick = true)
    private FloatingActionButton fab;
    private String cartoonId;
    private MyAdapter myAdapter;
    private int pageSize = 20;
    private int lastId = 0;
    private List<CommentListRecord.Result> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.comment_list_activity);
        init();
        initView();
//        initData();
    }

    private void init() {
        setTitleName("评论");
        cartoonId = getIntent().getStringExtra("cartoonId");
    }

    private void initView() {
        myAdapter = new MyAdapter();
        listview.setDivider(null);
        listview.setAdapter(myAdapter);
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshLayout.setNoMoreData(false);
            lastId = 0;
            initData();
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> loadData());
        refreshLayout.autoRefresh(200, 100, 1);
    }


    private void initData() {
        ObjectRequest<CommentListRecord> response = new ObjectRequest<>(false, CommentListRecord.Input.buildInput(pageSize + "", "", cartoonId),
                commentListRecord -> {
                    refreshLayout.finishRefresh();
                    if (null != commentListRecord && 1000 == commentListRecord.code) {
                        if (null != commentListRecord.list && commentListRecord.list.size() > 0) {
                            dataList = filterData(commentListRecord.list);
                            lastId += dataList.size();//.get(dataList.size() - 1).id;
                            myAdapter.notifyDataSetChanged();
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                    refreshLayout.finishRefresh();
                });
        NetClient.request(response);
    }

    private List<CommentListRecord.Result> filterData(List<CommentListRecord.Result> results) {
        Iterator<CommentListRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            CommentListRecord.Result result = it.next();
            if (null == result || TextUtils.isEmpty(result.commentContent)) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return results;
    }

    private void loadData() {
        ObjectRequest<CommentListRecord> response = new ObjectRequest<>(false, CommentListRecord.Input.buildInput(pageSize + "", lastId + "", cartoonId),
                commentListRecord -> {
                    if (null != commentListRecord && 1000 == commentListRecord.code) {
                        if (null != commentListRecord.list && commentListRecord.list.size() > 0) {
                            List<CommentListRecord.Result> results = filterData(commentListRecord.list);
                            dataList.addAll(results);
                            lastId += dataList.size();//.get(dataList.size() - 1).id;
                            myAdapter.notifyDataSetChanged();
                            if (results.size() < 20) {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                refreshLayout.finishLoadMore();
                            }
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                        refreshLayout.finishLoadMore();
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                    refreshLayout.finishLoadMore();
                });
        NetClient.request(response);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                IntentUtil.startActivityWithString(this, CommentActivity.class, "cartoonId", cartoonId);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public CommentListRecord.Result getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CommentListRecord.Result result = getItem(position);
            if (!TextUtils.isEmpty(result.headImg)) {
                GlideUtil.load(parent.getContext(), result.headImg, R.drawable.ic_defult_header, viewHolder.iv_head);
            } else {
                viewHolder.iv_head.setImageResource(R.drawable.ic_defult_header);
            }
            viewHolder.tv_name.setText(result.nickName + "");
            viewHolder.tv_name.setSelected(2 == result.sex);
            viewHolder.tv_time.setText(result.commentTime + "");
            viewHolder.tv_content.setText(result.commentContent + "");
            viewHolder.iv_head.setOnClickListener(v -> toDetail(result));
            viewHolder.tv_name.setOnClickListener(v -> toDetail(result));
            return convertView;
        }
    }

    private void toDetail(CommentListRecord.Result result) {
        CartoonUsersRecord.Result result1 = new CartoonUsersRecord.Result();
        result1.userId = result.userId;
        result1.sex = result.sex;
        result1.headImg = result.headImg;
        result1.nickName = result.nickName;
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("user", result1);
        startActivity(intent);
        overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
    }

    private static class ViewHolder {
        @ViewInject(id = R.id.iv_head)
        RoundImageView iv_head;
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;
        @ViewInject(id = R.id.tv_time)
        TextView tv_time;
        @ViewInject(id = R.id.tv_content)
        TextView tv_content;

        ViewHolder(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }
}
