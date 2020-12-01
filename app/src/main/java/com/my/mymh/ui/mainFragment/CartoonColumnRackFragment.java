package com.my.mymh.ui.mainFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.CartoonColumnListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.SearchActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.ui.cartoonSelectFragment.SelectorFragment;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.TrimUtil;
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
 * PackageName  com.dave.project.ui.fragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/8.
 * <p>
 * 分类
 */

public class CartoonColumnRackFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "CartoonColumnRackFragment";
    @ViewInject(id = R.id.iv_left)
    private ImageView iv_left;
    @ViewInject(id = R.id.tv_title_title)
    private TextView tv_title_title;
    @ViewInject(id = R.id.iv_right, needClick = true)
    private ImageView iv_right;
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.recycler_view)
    private RecyclerView mRecycleView;
    private int pageSize = 20;
    private int page = 1;
    private List<AnyItem> anyItemList = new ArrayList<>();
    private SelectorFragment selectorFragment;
    private MyAdapter myAdapter;
    private boolean hasColumnData = false;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clum_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
    }

    private void init() {
        EventBus.getDefault().register(this);
    }

    private void initView() {
        iv_left.setVisibility(View.GONE);
        tv_title_title.setText("分类");
        selectorFragment = (SelectorFragment) getChildFragmentManager().findFragmentById(R.id.selector_fragment);
        RecyclerView.LayoutManager onLineLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        mRecycleView.setLayoutManager(onLineLayoutManager);
        myAdapter = new MyAdapter();
        mRecycleView.setAdapter(myAdapter);
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            page = 1;
            if (!hasColumnData) {
                selectorFragment.initData();
            } else {
                refreshLayout.setNoMoreData(false);
                initData();
            }
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> loadData());
    }

    private void initData() {
        ObjectRequest<CartoonColumnListRecord> response = new ObjectRequest<>(false, CartoonColumnListRecord.Input.buildInput(lzStatus, address, label, pageSize + "", ""),
                columnListRecord -> {
                    stopFresh();
                    if (null != columnListRecord && 1000 == columnListRecord.code) {
//                            selectorFragment.showOrHideView(true);
                        anyItemList.clear();
                        if (null != columnListRecord.result && columnListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(columnListRecord.result);
                            if (dataList.size() > 0) {
//                                page += dataList.size();
                                for (LastUpdateCartoonListRecord.Result result : dataList) {
                                    anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                                }
                            }
                        }
                        mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_white));
                        myAdapter.notifyDataSetChanged();
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                    setFree();
                },
                volleyError -> {
                    stopFresh();
                    MyDebugUtil.toast("访问失败");
//                        selectorFragment.showOrHideView(false);
                    anyItemList.clear();
                    anyItemList.add(new AnyItem(AnyItem.TYPE_NO_NET, null));
                    mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_f0f0f0));
                    myAdapter.notifyDataSetChanged();
                    setFree();
                });
        NetClient.request(response);
    }

    private void setFree() {
        mRecycleView.postDelayed(() -> selectorFragment.setFree(true), 1000);
    }

    private void stopFresh() {
        handler.postDelayed(() -> refreshLayout.finishRefresh(), 200);
    }

    private List<LastUpdateCartoonListRecord.Result> filterData(List<LastUpdateCartoonListRecord.Result> results) {
        Iterator<LastUpdateCartoonListRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            LastUpdateCartoonListRecord.Result result = it.next();
            if (null == result || TextUtils.isEmpty(result.id)) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return results;
    }

    private void loadData() {
        ObjectRequest<CartoonColumnListRecord> response = new ObjectRequest<>(false, CartoonColumnListRecord.Input.buildInput(lzStatus, address, label, pageSize + "", page * pageSize + ""),
                columnListRecord -> {
                    if (null != columnListRecord && 1000 == columnListRecord.code) {
                        if (null != columnListRecord.result && columnListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(columnListRecord.result);
                            if (dataList.size() > 0) {
//                                page += dataList.size();
                                page++;
                                for (LastUpdateCartoonListRecord.Result result : dataList) {
                                    anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                                }
                                myAdapter.notifyDataSetChanged();
                                if (dataList.size() < 20) {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                } else {
                                    refreshLayout.finishLoadMore();
                                }
                            } else {
                                refreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        } else {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            MyDebugUtil.toastTest("暂时没有数据");
                        }
                    } else {
                        refreshLayout.finishLoadMore();
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> {
                    refreshLayout.finishLoadMore();
                    MyDebugUtil.toast("访问失败");
                });
        NetClient.request(response);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right:
                IntentUtil.startActivity(getActivity(), SearchActivity.class);
                break;
        }
    }

    static class AnyItem {
        static final int TYPE_ITEM = 1;
        static final int TYPE_NO_NET = 2;
        public int type;
        public Object object;

        AnyItem(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public int getItemViewType(int position) {
            return anyItemList.get(position).type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case AnyItem.TYPE_ITEM:
                    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, null));
                case AnyItem.TYPE_NO_NET:
                    return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.no_net_item, parent, false));
                default:
                    return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (anyItemList.get(position).type == AnyItem.TYPE_ITEM) {
                ViewHolder viewHolder = (ViewHolder) holder;
                final LastUpdateCartoonListRecord.Result result = (LastUpdateCartoonListRecord.Result) anyItemList.get(position).object;
                if (!TextUtils.isEmpty(result.name)) {
                    viewHolder.tv_name.setText(result.name);
                }
                if (!TextUtils.isEmpty(result.articleName)) {
                    viewHolder.tv_num.setText("更新至" + result.articleName);
                }
                if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
                    GlideUtil.load(getActivity(), result.imgUrl, R.drawable.ic_holder1, viewHolder.iv_cover);
                } else {
                    viewHolder.iv_cover.setImageResource(R.drawable.ic_holder1);
                }
                if (!TextUtils.isEmpty(result.author)) {
                    viewHolder.tv_author_name.setText("作者 : " + result.author);
                }
                if (!TextUtils.isEmpty(result.longDesc)) {
                    viewHolder.tv_desc.setText(TrimUtil.trim(result.longDesc));
                }
                if (!TextUtils.isEmpty(result.lzStatus)) {
                    viewHolder.tv_state.setText("状态 : " + ("2".equals(result.lzStatus) ? "完结" : "连载"));
                }
                viewHolder.ll_root.setOnClickListener(v -> {
                    if (null != anyItemList && anyItemList.size() > 0) {
//                        EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                        IntentUtil.startActivity(getActivity(), CartoonInfoAndCatalogActivity.class);
                        IntentUtil.startRead(getActivity(), result);
                    }
                });
            } else {
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder1.ll_holder.getLayoutParams();
                if (UIUtil.getDensity() > 2.8f) {
                    layoutParams.topMargin = UIUtil.dip2px(50);
                } else {
                    layoutParams.topMargin = UIUtil.dip2px(90);
                }
                viewHolder1.ll_holder.setLayoutParams(layoutParams);
                viewHolder1.tv_fresh.setOnClickListener(v -> {
                    if (!hasColumnData) {
                        selectorFragment.initData();
                    } else {
                        initData();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return anyItemList.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.ll_root)
        LinearLayout ll_root;
        @ViewInject(id = R.id.iv_cover)
        ImageView iv_cover;
        @ViewInject(id = R.id.tv_num)
        TextView tv_num;
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;
        @ViewInject(id = R.id.tv_author_name)
        TextView tv_author_name;
        @ViewInject(id = R.id.tv_state)
        TextView tv_state;
        @ViewInject(id = R.id.tv_desc)
        TextView tv_desc;

        ViewHolder(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.ll_holder)
        LinearLayout ll_holder;
        @ViewInject(id = R.id.tv_fresh)
        TextView tv_fresh;

        ViewHolder1(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private String lzStatus = "";
    private String address = "";
    private String label = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(EventBusEvent.SelectorEvent selectorEvent) {
        //没有获取到数据
        page = 1;
        if (TextUtils.isEmpty(selectorEvent.str3)) {
            stopFresh();
            hasColumnData = false;
            mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_f0f0f0));
            anyItemList.clear();
            anyItemList.add(new AnyItem(AnyItem.TYPE_NO_NET, null));
            myAdapter.notifyDataSetChanged();
        } else {
            hasColumnData = true;
            //获取到数据
            if (selectorEvent.str1.equals("全部")) {
                lzStatus = "";
            } else {
                lzStatus = (selectorEvent.str1.equals("连载")) ? "1" : "2";
            }
            if (selectorEvent.str2.equals("全部")) {
                address = "";
            } else {
                address = selectorEvent.str2;
            }
            if (selectorEvent.str3.equals("全部")) {
                label = "";
            } else {
                label = selectorEvent.str3;
            }
            MyDebugUtil.log(TAG, "str1=" + lzStatus + ",str2=" + address + ",str3=" + label);
            refreshLayout.autoRefresh(0, 100, 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
