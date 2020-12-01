package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.model.record.CollectListRecord;
import com.my.mymh.model.record.CollectSomeRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PackageName  com.hgd.hgdnovel.ui
 * ProjectName  NovelProject
 * Author       chenxiaowu
 * Date         2018/12/26.
 */
public class ManagerActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "ManagerActivity";
    private ArrayList<CollectListRecord.Result> resultList;
    private List<Item> itemList = new ArrayList<>();
    @ViewInject(id = R.id.recycler_view)
    private RecyclerView recycler_view;
    @ViewInject(id = R.id.rl_bottom)
    private RelativeLayout rl_bottom;
    @ViewInject(id = R.id.tv_sel_all, needClick = true)
    private TextView tv_sel_all;
    @ViewInject(id = R.id.tv_delete, needClick = true)
    private TextView tv_delete;
    private CartoonReadPositionDao cartoonReadPositionDao;
    private MyAdapter myAdapter;

//    @Override
//    public void setThemeAndLayoutId() {
//        activity_theme = APP_THEME.BLUE_THEME;
//        activity_layoutId = R.layout.manager_activity;
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.manager_activity);
        init();
        initView();
        initData();
    }

    //    @Override
    public void init() {
        cartoonReadPositionDao = new CartoonReadPositionDao(this);
        Intent intent = getIntent();
        if (intent != null) {
            resultList = (ArrayList<CollectListRecord.Result>) intent.getSerializableExtra("resultList");
        }
    }

    //    @Override
    public void initView() {
        setTitleName("书架整理");
        setRightClickViews(Collections.singletonList("完成"), position -> finish());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recycler_view.setAdapter(myAdapter);
    }

    //    @Override
    public void initData() {
        if (itemList.size() > 0) {
            itemList.clear();
        }
        if (resultList != null && resultList.size() > 0) {
            for (CollectListRecord.Result result : resultList) {
                itemList.add(new Item(Item.TYPE_NORMAL, result));
            }
            itemList.add(new Item(Item.TYPE_NO_MORE, null));
        } else {
            if (itemList.size() > 0) {
                itemList.clear();
            }
            itemList.add(new Item(Item.TYPE_NO_DATA, null));
        }
        myAdapter.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sel_all:
                allClick();
                break;
            case R.id.tv_delete:
                List<CollectListRecord.Result> selList = getSelList();
                if (selList.size() > 0) {
                    delete(selList);
                }
                break;
        }
    }

    private void delete(List<CollectListRecord.Result> selList) {
        List<String> idList = new ArrayList<>();
        for (CollectListRecord.Result result : selList) {
            idList.add(result.cartoonId);
        }
        String params = GsonUtil.getGson().toJson(idList);
        @SuppressLint("SetTextI18n") ObjectRequest<CollectSomeRecord> response = new ObjectRequest<>(false, CollectSomeRecord.Input.buildInput(params),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
                        //view
                        rl_bottom.setSelected(false);
                        tv_sel_all.setSelected(false);
                        tv_delete.setSelected(false);
                        tv_delete.setText("删除（" + 0 + "）");
                        //list
                        for (CollectListRecord.Result result : selList) {
                            resultList.remove(result);
                            initData();
                        }
                        //tag
                        isManager = true;
                    }
                },
                volleyError -> MyDebugUtil.toastTest("没有数据~"));
        NetClient.request(response);
    }

    private void allClick() {
        if (resultList != null && resultList.size() > 0) {
            boolean isSelected = rl_bottom.isSelected();
            rl_bottom.setSelected(!isSelected);
            tv_sel_all.setSelected(!isSelected);
            tv_delete.setSelected(!isSelected);
            tv_delete.setText("删除（" + (!isSelected ? resultList.size() : 0) + "）");
            //data
            for (CollectListRecord.Result result : resultList) {
                result.isSel = !isSelected;
            }
            initData();
        }
    }

    static class Item {
        static final int TYPE_NORMAL = 0;
        static final int TYPE_NO_DATA = 1;
        static final int TYPE_NO_MORE = 2;
        public int type;
        public Object object;

        Item(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position).type;
        }

        @SuppressLint("InflateParams")
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case Item.TYPE_NORMAL:
                    return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_item, parent, false));
                case Item.TYPE_NO_MORE:
                    return new ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_all, parent, false));
                default:
                    return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_no_item, parent, false));
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1) {
                CollectListRecord.Result result = (CollectListRecord.Result) itemList.get(position).object;
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
                    GlideUtil.load(ManagerActivity.this, result.imgUrl, R.drawable.ic_holder1, viewHolder1.iv_cover);
                } else {
                    viewHolder1.iv_cover.setImageResource(R.drawable.ic_holder1);
                }
                if (!TextUtils.isEmpty(result.name)) {
                    viewHolder1.tv_name.setText(result.name);
                }
                if (!TextUtils.isEmpty(result.articleName)) {
                    String newName = result.articleName;
                    if (newName.contains(result.name)) {
                        newName = newName.replace(result.name, "");
                    }
                    viewHolder1.tv_update.setText("更新至" + newName);
                }
                viewHolder1.tv_update.setSelected(result.isNew);
                ReadPositionBean cartoonReadBean = cartoonReadPositionDao.query(result.cartoonId);
                String pos;
                if (cartoonReadBean != null) {
                    pos = "上次看到 : " + cartoonReadBean.articleName;
                } else {
                    pos = "上次看到 : 第1话";
                }
                viewHolder1.tv_read_position.setText(pos);
                viewHolder1.iv_sel.setSelected(result.isSel);
                viewHolder1.fl_view.setOnClickListener(v -> {
//                    if (result.isNew) {
//                        result.isNew = false;
//                        notifyDataSetChanged();
//                        updateTime(result.favoriteId);
//                    }
//                    getSize(result.cartoonId, result.name);

                    boolean isSel = result.isSel;
                    result.isSel = !isSel;
                    notifyDataSetChanged();
                    listClick();
                });
//                viewHolder1.tv_delete.setOnClickListener(v -> {
//                    viewHolder1.swipe_menu_layout.quickClose();
//                    collectNot(result.cartoonId);
//                });
            } else if (holder instanceof ViewHolder2) {
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder2.ll_holder.getLayoutParams();
                if (UIUtil.getDensity() > 2.8f) {
                    layoutParams.topMargin = UIUtil.dip2px(110);
                } else {
                    layoutParams.topMargin = UIUtil.dip2px(150);
                }
                viewHolder2.ll_holder.setLayoutParams(layoutParams);
            } else if (holder instanceof ViewHolder3) {
                ViewHolder3 viewHolder3 = (ViewHolder3) holder;
                int height;
                if (itemList.size() >= 5) {
                    height = UIUtil.dip2px(60);
                } else {
                    height = UIUtil.dip2px(500) - (itemList.size() - 1) * UIUtil.dip2px(120);
                }
                ViewGroup.LayoutParams layoutParams1 = viewHolder3.ll.getLayoutParams();
                layoutParams1.height = height;
                viewHolder3.ll.setLayoutParams(layoutParams1);
            }
        }

        @Override
        public int getItemCount() {
            return itemList != null ? itemList.size() : 0;
        }
    }

    @SuppressLint("SetTextI18n")
    private void listClick() {
        List<CollectListRecord.Result> results = getSelList();
        if (results.size() == 0) {
            rl_bottom.setSelected(false);
            tv_sel_all.setSelected(false);
            tv_delete.setSelected(false);
            tv_delete.setText("删除（" + 0 + "）");
        } else if (results.size() == this.resultList.size()) {
            rl_bottom.setSelected(true);
            tv_sel_all.setSelected(true);
            tv_delete.setSelected(true);
            tv_delete.setText("删除（" + results.size() + "）");
        } else {
            rl_bottom.setSelected(false);
            tv_sel_all.setSelected(false);
            tv_delete.setSelected(true);
            tv_delete.setText("删除（" + results.size() + "）");
        }
    }

    private List<CollectListRecord.Result> getSelList() {
        List<CollectListRecord.Result> results = new ArrayList<>();
        for (CollectListRecord.Result result : resultList) {
            if (result.isSel) {
                results.add(result);
            }
        }
        return results;
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        //        @ViewInject(id = R.id.swipe_menu_layout)
//        SwipeMenuLayout swipe_menu_layout;
        @ViewInject(id = R.id.fl_view)
        LinearLayout fl_view;
        @ViewInject(id = R.id.iv_cover)
        ImageView iv_cover;
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;
        @ViewInject(id = R.id.tv_update)
        TextView tv_update;
        @ViewInject(id = R.id.tv_read_position)
        TextView tv_read_position;
        //        @ViewInject(id = R.id.tv_delete)
//        TextView tv_delete;
        @ViewInject(id = R.id.iv_sel)
        ImageView iv_sel;

        ViewHolder1(View itemView) {
            super(itemView);
            ViewInjectUtil.initNotInActivity(this, itemView);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.ll_holder)
        LinearLayout ll_holder;

        ViewHolder2(View itemView) {
            super(itemView);
            ViewInjectUtil.initNotInActivity(this, itemView);
        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.ll)
        LinearLayout ll;

        ViewHolder3(View itemView) {
            super(itemView);
            ViewInjectUtil.initNotInActivity(this, itemView);
        }
    }

    private boolean isManager = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isManager) {
            EventBus.getDefault().post(new EventBusEvent.CollectEvent(EventBusEvent.CollectEvent.TYPE_MANAGER));
            isManager = false;
        }
    }
}
