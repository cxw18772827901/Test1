package com.my.mymh.ui.mainFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.db.bean.RackBean;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.dao.CartoonRackDao;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.model.LocalItem;
import com.my.mymh.model.LocalRackBean;
import com.my.mymh.model.record.CatalogListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.CartoonReadActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.CommonDialog;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;
import com.my.mymh.wedjet.swipe.SwipeMenuLayout;

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
 * 本地书架
 */

public class LocalRackFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "CartoonRackFragment";
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private ListView listview;
    private CartoonRackDao cartoonRackDao;
    private CartoonReadPositionDao cartoonReadPositionDao;
    private List<LocalItem> localItemList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFreshOrDeleteEvent(EventBusEvent.FreshOrDeleteEvent deleteEvent) {
        if (EventBusEvent.FreshOrDeleteEvent.TYPE_DELETE == deleteEvent.type) {
            shouldDeleteAll();
        } else if (EventBusEvent.FreshOrDeleteEvent.TYPE_FRESH == deleteEvent.type) {
            initData();
        }
    }

    public void shouldDeleteAll() {
        List<RackBean> rackBeanList = cartoonRackDao.queryAll();
        if (null != rackBeanList && rackBeanList.size() > 0) {
            clearBook();
        } else {
            MyDebugUtil.toast("书架是空的~");
        }
    }

    private void clearBook() {
        CommonDialog.showDialog(getActivity(), "是否清空书架?", "确定", "取消", v -> {
            CommonDialog.close();
            switch (v.getId()) {
                case R.id.bt_ok:
                    cartoonRackDao.deleteAll();
                    cartoonReadPositionDao.deleteAll();
                    initData();
                    break;
                case R.id.bt_cancel:
                    break;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.book_rack_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
        initData();
    }

    private void init() {
        cartoonRackDao = new CartoonRackDao(getActivity());
        cartoonReadPositionDao = new CartoonReadPositionDao(getActivity());
        EventBus.getDefault().register(this);
    }

    private void initView() {
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> initData());
    }

    private void initData() {
        List<RackBean> rackBeanList = cartoonRackDao.queryAll();
        localItemList.clear();
        if (null != rackBeanList && rackBeanList.size() > 0) {
            for (RackBean rackBean : rackBeanList) {
                ReadPositionBean cartoonReadBean = cartoonReadPositionDao.query(rackBean.cartoonId);
                localItemList.add(new LocalItem(LocalItem.ITEM_BOOK, new LocalRackBean(rackBean, cartoonReadBean)));
            }
            localItemList = filterData(localItemList);
            if (localItemList.size() > 0) {
                localItemList.add(new LocalItem(LocalItem.ITEM_ALL, null));
            } else {
                addHolder();
            }
        } else {
            MyDebugUtil.toastTest("书架还是空的");
            addHolder();
        }
        myAdapter.notifyDataSetChanged();
        refreshLayout.finishRefresh();
    }

    private void addHolder() {
        localItemList.add(new LocalItem(LocalItem.ITEM_HOLDER, null));
    }

    private List<LocalItem> filterData(List<LocalItem> localItemList) {
        Iterator<LocalItem> it = localItemList.iterator();
        while (it.hasNext()) {
            LocalRackBean bean = (LocalRackBean) it.next().object;
            if (null == bean.cartoonReadBean) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return localItemList;
    }

    @Override
    public void tabClick() {
        if (null != getView())
            initData();
    }

    /* private void loadData() {
        refreshLayout.finishLoadmore();
    }*/

    @Override
    public void onClick(View v) {

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getCount() {
            return localItemList.size();
        }

        @Override
        public LocalItem getItem(int position) {
            return localItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case LocalItem.ITEM_BOOK:
                    ViewHolder viewHolder;
                    if (convertView == null) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_book_item, null);
                        viewHolder = new ViewHolder(convertView);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = (ViewHolder) convertView.getTag();
                    }
                    LocalRackBean localRackBean = (LocalRackBean) getItem(position).object;
                    ReadPositionBean cartoonReadBean = localRackBean.cartoonReadBean;
                    RackBean rackBean = localRackBean.rackBean;
                    String cartoonContent = rackBean.cartoonContent;
//                    String cartoonReadTime = rackBean.cartoonReadTime;
                    final LastUpdateCartoonListRecord.Result result = GsonUtil.getGson().fromJson(cartoonContent, LastUpdateCartoonListRecord.Result.class);
                    //
                    if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
                        GlideUtil.load(parent.getContext(), result.imgUrl, R.drawable.ic_holder1, viewHolder.iv_cover);
                    } else {
                        viewHolder.iv_cover.setImageResource(R.drawable.ic_holder1);
                    }
                    if (!TextUtils.isEmpty(result.name)) {
                        viewHolder.tv_name.setText(result.name);
                    }
                    if (!TextUtils.isEmpty(result.articleName)) {
                        viewHolder.tv_update.setText("更新至 " + result.articleName /*+ "话"*/);
                    }
//                    if (!TextUtils.isEmpty(cartoonReadBean.currentIndex)) {
                    if (!TextUtils.isEmpty(cartoonReadBean.articleName)) {
                        viewHolder.tv_read_position.setText("上次看到 : " + /*cartoonReadBean.currentIndex + "话"*/cartoonReadBean.articleName);
                    }
                    viewHolder.fl_view.setOnClickListener(v -> {
//                            EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                            IntentUtil.startActivity(getActivity(), CartoonInfoAndCatalogActivity.class);
                        getSize(result.id, result.name);
                    });
                    final View finalConvertView = convertView;
                    viewHolder.tv_delete.setOnClickListener(v -> {
                        ((SwipeMenuLayout) finalConvertView).quickClose();
                        cartoonRackDao.deleteById(result.id);
                        cartoonReadPositionDao.deleteById(result.id);
                        initData();
                    });
                    break;
                case LocalItem.ITEM_HOLDER:
                    ViewHolder1 viewHolder1;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_holder_item, null);
                        viewHolder1 = new ViewHolder1(convertView);
                        convertView.setTag(viewHolder1);
                    } else {
                        viewHolder1 = (ViewHolder1) convertView.getTag();
                    }
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder1.ll_holder.getLayoutParams();
                    if (UIUtil.getDensity() > 2.8f) {
                        layoutParams.topMargin = UIUtil.dip2px(110);
                    } else {
                        layoutParams.topMargin = UIUtil.dip2px(150);
                    }
                    viewHolder1.ll_holder.setLayoutParams(layoutParams);
                    break;
                case LocalItem.ITEM_ALL:
                    ViewHolder2 viewHolder2;
                    if (null == convertView) {
                        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_all, parent, false);
                        viewHolder2 = new ViewHolder2(convertView);
                        convertView.setTag(viewHolder2);
                    } else {
                        viewHolder2 = (ViewHolder2) convertView.getTag();
                    }
                    int height;
                    if (localItemList.size() >= 5) {
                        height = UIUtil.dip2px(60);
                    } else {
                        height = UIUtil.dip2px(500) - (localItemList.size() - 1) * UIUtil.dip2px(120);
                    }
                    ViewGroup.LayoutParams layoutParams1 = viewHolder2.ll.getLayoutParams();
                    layoutParams1.height = height;
                    viewHolder2.ll.setLayoutParams(layoutParams1);
                    break;
            }
            return convertView;
        }
    }

    private static class ViewHolder {
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
        @ViewInject(id = R.id.tv_delete)
        TextView tv_delete;

        ViewHolder(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder1 {
        @ViewInject(id = R.id.ll_holder)
        LinearLayout ll_holder;

        ViewHolder1(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder2 {
        @ViewInject(id = R.id.ll)
        LinearLayout ll;

        ViewHolder2(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
