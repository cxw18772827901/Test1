package com.my.mymh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.model.record.CatalogListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/22.
 */

public class CatalogActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "CatalogActivity";
    private List<CatalogListRecord.Result> resultList = new ArrayList<>();
    ;
    private String cartoonId;
    @ViewInject(id = R.id.listview)
    private ListView listview;
    private MyAdapter myAdapter;
    private ImageView ivRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.catalog_activity);
        init();
        initView();
        initData();
    }

    private void init() {
        setTitleName("目录");
        cartoonId = getIntent().getStringExtra("cartoonId");
        setRightClickViews(Collections.<Object>singletonList(R.drawable.catalog_sel), position -> {
            if (null != resultList && resultList.size() > 0) {
                boolean isSelected = ivRight.isSelected();
                ivRight.setSelected(!isSelected);
                Collections.reverse(resultList);
                myAdapter.notifyDataSetChanged();
            }
        });
        ivRight = (ImageView) ll_right_click.getChildAt(0).findViewById(R.id.iv_right);
    }

    private void initView() {
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        listview.setOnItemClickListener((parent, view, position, id) -> {
            if (null != resultList && resultList.size() > 0) {
                int currentIndex;
                boolean selected = ivRight.isSelected();
                if (selected) {//正序直接加1
                    currentIndex = position + 1;
                } else {//逆序resultList.size()-position
                    currentIndex = resultList.size() - position;
                }
                CatalogListRecord.Result result = resultList.get(position);
                Intent intent = new Intent();
                intent.putExtra("articleId", result.articleId);
                intent.putExtra("currentIndex", currentIndex + "");
                intent.putExtra("titleName", result.articleName + "");
                setResult(1000, intent);
                IntentUtil.finishActivityWithAnim(CatalogActivity.this);
            }
        });
    }

    private void initData() {
        ObjectRequest<CatalogListRecord> response = new ObjectRequest<>(false, CatalogListRecord.Input.buildInput(cartoonId),
                commentListRecord -> {
                    if (null != commentListRecord && 1000 == commentListRecord.code) {
                        if (null != commentListRecord.result && commentListRecord.result.size() > 0) {
                            List<CatalogListRecord.Result> dataList = filterData(commentListRecord.result);
                            if (dataList.size() > 0) {
                                CatalogActivity.this.resultList = dataList;
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

    /*private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.tv_catalog.setText(resultList.get(position).articleName);
            *//*final int finalI = position;
            viewHolder.tv_catalog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //存书架
                    RackBean rackBean = cartoonRackDao.query(*//**//*this.*//**//*result.id);
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
                    IntentUtil.startActivityWithFiveString(getActivity(), CartoonReadActivity.class,
                            "type", "0",
                            "cartoonId", result.id,
                            "articleId", resultList.get(finalI).articleId,
                            "currentIndex", currentIndex + "",
                            "size", resultList.size() + "");
                }
            });*//*
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
    }*/

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return null == resultList ? 0 : resultList.size();
        }

        @Override
        public CatalogListRecord.Result getItem(int position) {
            return resultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item1, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_catalog.setText(resultList.get(position).articleName);
            return convertView;
        }
    }

    private static class ViewHolder {
        @ViewInject(id = R.id.tv_catalog)
        TextView tv_catalog;

        ViewHolder(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
