package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.CareRecord;
import com.my.mymh.model.record.CareUsersRecord;
import com.my.mymh.model.record.CartoonUsersRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.RoundImageView;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/26.
 */
public class CaresActivity extends BaseActivity {
    public static final String TAG = "CaresActivity";
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private ListView listview;
    private String lastAttentionId;
    private MyAdapter myAdapter;
    private List<Item> itemList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.cares_activity);
        init();
        initView();
//        initData();
    }

    private void init() {
        setTitleName("关注");
    }

    private void initView() {
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.setNoMoreData(false);
            lastAttentionId = "";
            initData();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> loadData());
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        refreshLayout.autoRefresh(200, 100, 1);
    }

    private void initData() {
        ObjectRequest<CareUsersRecord> response = new ObjectRequest<>(false, CareUsersRecord.Input.buildInput("20", lastAttentionId),
                hotOrNewCartoonListRecord -> {
                    refreshLayout.finishRefresh();
                    if (null != hotOrNewCartoonListRecord && 1000 == hotOrNewCartoonListRecord.code) {
                        if (null != hotOrNewCartoonListRecord.result && hotOrNewCartoonListRecord.result.size() > 0) {
                            List<CareUsersRecord.Result> dataList = filterData(hotOrNewCartoonListRecord.result);
                            if (dataList != null && dataList.size() > 0) {
                                lastAttentionId = dataList.get(dataList.size() - 1).attentionId;
                                itemList.clear();
                                for (CareUsersRecord.Result result : dataList) {
                                    itemList.add(new Item(Item.ITEM_USER, result));
                                }
                                myAdapter.notifyDataSetChanged();
                            } else {
                                setNoData();
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                            setNoData();
                        }
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                        setNoData();
                    }
                },
                volleyError -> {
                    refreshLayout.finishRefresh();
                    MyDebugUtil.toastTest("访问失败");
                    setNoData();
                });
        NetClient.request(response);
    }

    private void setNoData() {
        itemList.clear();
        itemList.add(new Item(Item.ITEM_HOLDER, null));
        myAdapter.notifyDataSetChanged();
    }

    private List<CareUsersRecord.Result> filterData(List<CareUsersRecord.Result> results) {
        Iterator<CareUsersRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            CareUsersRecord.Result result = it.next();
            if (null == result ||
                    TextUtils.isEmpty(result.userId) ||
                    TextUtils.isEmpty(result.attentionId) ||
                    TextUtils.isEmpty(result.nickName) /*||
                    TextUtils.isEmpty(result.favoriteId) ||
                    LoginVerifyUtil.isMe(result.userId)||
                    TextUtils.isEmpty(result.readChapterId)*/) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return results;
    }

    private void loadData() {
        ObjectRequest<CareUsersRecord> response = new ObjectRequest<>(false, CareUsersRecord.Input.buildInput("20", lastAttentionId),
                hotOrNewCartoonListRecord -> {
                    if (null != hotOrNewCartoonListRecord && 1000 == hotOrNewCartoonListRecord.code) {
                        if (null != hotOrNewCartoonListRecord.result && hotOrNewCartoonListRecord.result.size() > 0) {
                            List<CareUsersRecord.Result> dataList = filterData(hotOrNewCartoonListRecord.result);
                            if (dataList != null && dataList.size() > 0) {
                                refreshLayout.finishLoadMore();
                                lastAttentionId = dataList.get(dataList.size() - 1).attentionId;
                                for (CareUsersRecord.Result result : dataList) {
                                    itemList.add(new Item(Item.ITEM_USER, result));
                                }
                                myAdapter.notifyDataSetChanged();
                            } else {
                                setNoMore();
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                            setNoMore();
                        }
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                        setNoMore();
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("访问失败");
                    setNoMore();
                });
        NetClient.request(response);
    }

    private void setNoMore() {
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position).type;
        }

        @Override
        public int getCount() {
            return itemList != null ? itemList.size() : 0;
        }

        @Override
        public Item getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case Item.ITEM_USER:
                    ViewHolder1 viewHolder1;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.care_user_item, null);
                        viewHolder1 = new ViewHolder1(convertView);
                        convertView.setTag(viewHolder1);
                    } else {
                        viewHolder1 = (ViewHolder1) convertView.getTag();
                    }
                    CareUsersRecord.Result result = (CareUsersRecord.Result) itemList.get(position).object;
                    if (!TextUtils.isEmpty(result.headImg)) {
                        GlideUtil.load(parent.getContext(), result.headImg, R.drawable.ic_defult_header, viewHolder1.iv_head);
                    } else {
                        viewHolder1.iv_head.setImageResource(R.drawable.ic_defult_header);
                    }
                    viewHolder1.tv_name.setText(result.nickName);
                    viewHolder1.tv_name.setSelected(2 == result.sex);
                    viewHolder1.rl_root.setOnClickListener(v -> {
                        CartoonUsersRecord.Result result1 = new CartoonUsersRecord.Result();
                        result1.userId = result.userId;
                        result1.sex = result.sex;
                        result1.headImg = result.headImg;
                        result1.nickName = result.nickName;
                        Intent intent = new Intent(CaresActivity.this, UserDetailActivity.class);
                        intent.putExtra("user", result1);
                        startActivity(intent);
                        overridePendingTransition(R.anim.trans_pre_in, R.anim.trans_pre_out);
                    });
//                    viewHolder1.tv_care.setText();
                    if (LoginVerifyUtil.isCared(result.userId)) {
                        viewHolder1.tv_care.setText("取消关注");
//                        viewHolder1.tv_care.setVisibility(View.GONE);
                    } else {
                        viewHolder1.tv_care.setText("关注");
//                        viewHolder1.tv_care.setVisibility(View.VISIBLE);
                    }
                    viewHolder1.tv_care.setOnClickListener(v -> {
                        careOrNot(result.userId, position);
                    });
                    break;
                default:
                    ViewHolder2 viewHolder2;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_data_holder_item, null);
                        viewHolder2 = new ViewHolder2(convertView);
                        convertView.setTag(viewHolder2);
                    } else {
                        viewHolder2 = (ViewHolder2) convertView.getTag();
                    }
                    viewHolder2.tv_no_data.setText("还没有关注~");
                    break;
            }
            return convertView;
        }
    }

    private void careOrNot(String userId, int position) {
        int operate = LoginVerifyUtil.isCared(userId) ? -1 : 1;
        ObjectRequest<CareRecord> response = new ObjectRequest<>(false, CareRecord.Input.buildInput(userId, operate),
                careIdsRecord -> {
                    if (careIdsRecord != null && 1000 == careIdsRecord.code) {
                        MyDebugUtil.toast("操作成功");
                        LoginVerifyUtil.isAddCareId(1 == operate, userId);
                        itemList.remove(position);
                        if (itemList.size() == 0) {
                            setNoData();
                        } else {
                            myAdapter.notifyDataSetChanged();
                        }
                    } else {
                        MyDebugUtil.toast("访问失败，请稍后再试");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                });
        NetClient.request(response);
    }

    static class ViewHolder1 {
        @ViewInject(id = R.id.rl_root)
        RelativeLayout rl_root;
        @ViewInject(id = R.id.iv_head)
        RoundImageView iv_head;
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;
        @ViewInject(id = R.id.tv_care)
        TextView tv_care;

        public ViewHolder1(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }


    static class ViewHolder2 {
        @ViewInject(id = R.id.tv_no_data)
        TextView tv_no_data;

        ViewHolder2(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    public static class Item {
        static final int ITEM_USER = 0;
        static final int ITEM_HOLDER = 1;
        public int type;
        public Object object;

        public Item(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }
}
