package com.my.mymh.ui.cartoonDetailFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.db.bean.RackBean;
import com.my.mymh.db.dao.CartoonRackDao;
import com.my.mymh.model.record.CatalogListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.CartoonInfoAndCatalogActivity;
import com.my.mymh.ui.CartoonReadActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.dave.project.ui.cartoonDetailFragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/11.
 */

public class CartoonCatalogFragment1 extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "CartoonCatalogFragment1";
    @SuppressLint("StaticFieldLeak")
    private static CartoonCatalogFragment1 cartoonCatalogFragment1;
    @ViewInject(id = R.id.tv_range, needClick = true)
    private TextView tv_range;
    @ViewInject(id = R.id.recycle_view)
    private RecyclerView recyclerView;
    private LastUpdateCartoonListRecord.Result result;
    private List<CatalogListRecord.Result> resultList;
    private MyAdapter myAdapter;
    private CartoonRackDao cartoonRackDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cartoon_catalog_fragment, null);
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
        this.result = ((CartoonInfoAndCatalogActivity) getActivity()).result;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1, LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    private void initData() {
        ObjectRequest<CatalogListRecord> response = new ObjectRequest<>(false, CatalogListRecord.Input.buildInput(result.id),
                commentListRecord -> {
                    if (null != commentListRecord && 1000 == commentListRecord.code) {
                        if (null != commentListRecord.result && commentListRecord.result.size() > 0) {
                            List<CatalogListRecord.Result> dataList = filterData(commentListRecord.result);
                            if (dataList.size() > 0) {
                                CartoonCatalogFragment1.this.resultList = dataList;
                                myAdapter.notifyDataSetChanged();
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

    private List<CatalogListRecord.Result> filterData(List<CatalogListRecord.Result> results) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_range:
                if (null != resultList && resultList.size() > 0) {
                    boolean isSelected = tv_range.isSelected();
                    tv_range.setSelected(!isSelected);
                    Collections.reverse(resultList);
                    tv_range.setText(tv_range.isSelected() ? "正序" : "逆序");
                    myAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_catalog.setText(resultList.get(position).articleName);
            final int finalI = position;
            viewHolder.tv_catalog.setOnClickListener(v -> {
                //存书架
                RackBean rackBean = cartoonRackDao.query(/*CartoonCatalogFragment1.this.*/result.id);
                if (null == rackBean) {
                    cartoonRackDao.add(new RackBean(result.id, GsonUtil.getGson().toJson(result), System.currentTimeMillis() + ""));
                }
                //跳转
                int currentIndex;
                boolean selected = tv_range.isSelected();
                if (selected) {//正序直接加1
                    currentIndex = finalI + 1;
                } else {//逆序resultList.size()-position
                    currentIndex = resultList.size() - position;
                }
                IntentUtil.startActivityWithSevString(getActivity(), CartoonReadActivity.class,
                        "type", "0",
                        "cartoonId", result.id,
                        "articleId", resultList.get(finalI).articleId,
                        "currentIndex", currentIndex + "",
                        "size", resultList.size() + "",
                        "name", result.name,
                        "titleName", result.articleName
                );
            });
        }

        @Override
        public int getItemCount() {
            return null == resultList ? 0 : resultList.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_catalog)
        TextView tv_catalog;

        ViewHolder(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    public static synchronized CartoonCatalogFragment1 getInstance() {
        if (null == cartoonCatalogFragment1) {
            cartoonCatalogFragment1 = new CartoonCatalogFragment1();
        }
        return cartoonCatalogFragment1;
    }
}
