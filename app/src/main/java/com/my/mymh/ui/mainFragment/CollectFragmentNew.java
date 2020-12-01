package com.my.mymh.ui.mainFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.model.record.CatalogListRecord;
import com.my.mymh.model.record.CollectListRecord;
import com.my.mymh.model.record.CollectOnlyRecord;
import com.my.mymh.model.record.CollectUpdateRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.CartoonReadActivity;
import com.my.mymh.ui.LoginActivity;
import com.my.mymh.ui.ManagerActivity;
import com.my.mymh.ui.RecordActivity;
import com.my.mymh.ui.SearchActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.DateUtil;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.ui.mainFragment
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/11.
 */
public class CollectFragmentNew extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "CollectFragment";
    @ViewInject(id = R.id.tv_title_title)
    private TextView tv_title_title;
    @ViewInject(id = R.id.iv_title_right, needClick = true)
    private ImageView iv_title_right;
    @ViewInject(id = R.id.iv_title_right1, needClick = true)
    private ImageView iv_title_right1;
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.recycler_view)
    private RecyclerView recycler_view;
    private List<Item> itemList = new ArrayList<>();
    private MyAdapter myAdapter;
    private CartoonReadPositionDao cartoonReadPositionDao;
    private ArrayList<CollectListRecord.Result> resultList;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collect_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
    }

    private void init() {
        EventBus.getDefault().register(this);
        cartoonReadPositionDao = new CartoonReadPositionDao(getActivity());
//        PopupWindow
    }

    private void initView() {
        tv_title_title.setText("书架");
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recycler_view.setAdapter(myAdapter);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            initData();
        });
        refreshLayout.autoRefresh(200, 100, 1);
    }

    public void clearList() {
        if (resultList != null && resultList.size() > 0) {
            resultList.clear();
        }
    }

    private void initData() {
        if (!LoginVerifyUtil.isLogin()) {
            setNoData();
            return;
        }
        ObjectRequest<CollectListRecord> response = new ObjectRequest<>(false, CollectListRecord.Input.buildInput("20", ""),
                this::setData,
                volleyError -> setNoData());
        NetClient.request(response);
    }

    private void setData(CollectListRecord collectRecord) {
        refreshLayout.finishRefresh();
        if (collectRecord != null && collectRecord.code == 1000) {
            if (collectRecord.result != null && collectRecord.result.size() > 0) {
                ArrayList<CollectListRecord.Result> resultList = filterData(collectRecord.result);
                if (resultList.size() > 0) {
                    this.resultList = resultList;
                    if (itemList.size() > 0) {
                        itemList.clear();
                    }
                    for (CollectListRecord.Result result : resultList) {
                        if (!TextUtils.isEmpty(result.favoriteTime) &&
                                !TextUtils.isEmpty(result.updateTime)) {
                            long collectTime = DateUtil.getStringToLong(result.favoriteTime);
                            long updateTime = DateUtil.getStringToLong(result.updateTime);
                            result.isNew = collectTime < updateTime;
                        }
                        itemList.add(new Item(Item.TYPE_NORMAL, result));
                    }
                    itemList.add(new Item(Item.TYPE_NO_MORE, null));
                    myAdapter.notifyDataSetChanged();
                } else {
                    setNoData();
                }
            } else {
                setNoData();
            }
        } else if (collectRecord != null && (collectRecord.code == 3006 || collectRecord.code == 3007)) {
            LoginVerifyUtil.login();
            setNoData();
        } else {
            setNoData();
        }
    }

    private void setNoData() {
        refreshLayout.finishRefresh();
        clearList();
        if (itemList.size() > 0) {
            itemList.clear();
        }
        itemList.add(new Item(Item.TYPE_NO_DATA, null));
        myAdapter.notifyDataSetChanged();
    }

    private ArrayList<CollectListRecord.Result> filterData(ArrayList<CollectListRecord.Result> results) {
        Iterator<CollectListRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            CollectListRecord.Result result = it.next();
            if (null == result || TextUtils.isEmpty(result.name) ||
                    TextUtils.isEmpty(result.cartoonId) ||
                    TextUtils.isEmpty(result.favoriteId) ||
                    TextUtils.isEmpty(result.updateTime) ||
                    TextUtils.isEmpty(result.favoriteTime)) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return results;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_title_right://更多
                showMorePop(v);
                break;
            case R.id.iv_title_right1://搜索
                IntentUtil.startActivity(getActivity(), SearchActivity.class);
                break;
        }
    }

    private void showMorePop(View view) {
        View contentView = View.inflate(getActivity(), R.layout.rack_pop_layout, null);
        PopupWindow pw = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        // 给pw设置动画
//        pw.setAnimationStyle(R.style.pw_style);
        // 获取view在父亲的位置
        int[] location = new int[2];
        view.getLocationInWindow(location);
        // 给listview获得焦点,设置背景,就只能同时存在一个了
        pw.setFocusable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        // 参数一:view:popu寄生的父亲的view
        // 参数二:默认左上角
        // 参数三:具体位置,即偏移量
        pw.showAtLocation(view, /*Gravity.LEFT | Gravity.TOP,*/Gravity.TOP,
                location[0] /*+ 50*/, location[1] + UIUtil.dip2px(50));
        //click
        contentView.findViewById(R.id.tv_manager).setOnClickListener(v -> {
            pw.dismiss();
            if (LoginVerifyUtil.isLogin()) {
                if (resultList != null && resultList.size() > 0) {
//                    IntentUtil.startActivity(getActivity(), ManagerActivity.class);
                    Intent intent = new Intent(getActivity(), ManagerActivity.class);
                    intent.putExtra("resultList", resultList);
                    getActivity().startActivity(intent);
                    IntentUtil.startAnim(getActivity());
                } else {
                    MyDebugUtil.toast("书架是空的~");
                }
            } else {
                IntentUtil.startActivity(getActivity(), LoginActivity.class);
            }
        });
        contentView.findViewById(R.id.tv_record).setOnClickListener(v -> {
            pw.dismiss();
            IntentUtil.startActivity(getActivity(), RecordActivity.class);
        });
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
                    return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.collect_item_new, parent, false));
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
                    GlideUtil.load(getActivity(), result.imgUrl, R.drawable.ic_holder1, viewHolder1.iv_cover);
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
                viewHolder1.fl_view.setOnClickListener(v -> {
                    if (result.isNew) {
                        result.isNew = false;
                        notifyDataSetChanged();
                        updateTime(result.favoriteId);
                    }
                    getSize(result.cartoonId, result.name);
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

    private void updateTime(String favoriteId) {
        ObjectRequest<CollectUpdateRecord> response = new ObjectRequest<>(false, CollectUpdateRecord.Input.buildInput(favoriteId + ""),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
                        MyDebugUtil.toastTest("更新成功~");
                    }
                },
                volleyError -> MyDebugUtil.toastTest("没有数据~"));
        NetClient.request(response);
    }

    private void collectNot(String id) {
        ObjectRequest<CollectOnlyRecord> response = new ObjectRequest<>(false, CollectOnlyRecord.Input.buildInput(id + "", "2"),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
                        initData();
                    }
                },
                volleyError -> MyDebugUtil.toastTest("没有数据~"));
        NetClient.request(response);
    }

    private void getSize(final String id, final String name) {
        ObjectRequest<CatalogListRecord> response = new ObjectRequest<>(false, CatalogListRecord.Input.buildInput(id),
                catalogListRecord -> {
                    if (null != catalogListRecord && 1000 == catalogListRecord.code) {
                        if (null != catalogListRecord.result && catalogListRecord.result.size() > 0) {
                            List<CatalogListRecord.Result> dataList = filterCatalog(catalogListRecord.result);
                            if (dataList.size() > 0) {
                                int size = dataList.size();
                                IntentUtil.startActivityWithSixString(getActivity(), CartoonReadActivity.class,
                                        "type", "1",
                                        "cartoonId", id,
                                        "articleId", "",
                                        "currentIndex", "",
                                        "size", size + "",
                                        "name", name);
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> MyDebugUtil.toast("访问失败"));
        NetClient.request(response);
    }

    private List<CatalogListRecord.Result> filterCatalog(List<CatalogListRecord.Result> results) {
        Iterator<CatalogListRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            CatalogListRecord.Result result = it.next();
            if (null == result || TextUtils.isEmpty(result.articleId)) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCollect(EventBusEvent.CollectEvent collectEvent) {
        if (collectEvent.type == EventBusEvent.CollectEvent.TYPE_FRESH) {
            initData();
        } else if (collectEvent.type == EventBusEvent.CollectEvent.TYPE_OUT) {
            itemList.clear();
            itemList.add(new Item(Item.TYPE_NO_DATA, null));
            myAdapter.notifyDataSetChanged();
        } else if (collectEvent.type == EventBusEvent.CollectEvent.TYPE_MANAGER) {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
