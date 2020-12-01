package com.my.mymh.ui;

import android.annotation.SuppressLint;
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
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.DomesticListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/9.
 */

public class CityEmotionListActivity extends BaseActivity {
    public static final String TAG = "UpdateListActivity";
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private ListView listview;
    private int page = 1;
    private List<LastUpdateCartoonListRecord.Result> resultList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.update_list_activity);
        init();
        initView();
    }

    private void init() {
        setTitleLeftName(getResources().getString(R.string.tv_back));
        setTitleName("国产精品");
    }

    private void initView() {
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.setNoMoreData(false);
            page = 1;
            initData();
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> loadData());
        listview.setOnItemClickListener((parent, view, position, id) -> {
            if (null != resultList && resultList.size() > 0) {
                LastUpdateCartoonListRecord.Result result = resultList.get(position);
                if (result != null) {
//                    EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                    IntentUtil.startActivity(CityEmotionListActivity.this, CartoonInfoAndCatalogActivity.class);
                    IntentUtil.startRead(CityEmotionListActivity.this, result);
                }
            }
        });
        refreshLayout.autoRefresh(200, 100, 1);
    }

    private void initData() {
        ObjectRequest<DomesticListRecord> response = new ObjectRequest<>(false, DomesticListRecord.Input.buildInput(page * 20 + "", 20 + ""),
                domesticListRecord -> {
                    refreshLayout.finishRefresh();
                    if (null != domesticListRecord && 1000 == domesticListRecord.code) {
                        if (null != domesticListRecord.result && domesticListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(domesticListRecord.result);
                            if (dataList.size() > 0) {
                                resultList = dataList;
//                                page += resultList.size();//get(resultList.size() - 1).id;
                                myAdapter.notifyDataSetChanged();
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> {
                    refreshLayout.finishRefresh();
                    MyDebugUtil.toast("访问失败");
                });
        NetClient.request(response);
    }

    private void loadData() {
        ObjectRequest<DomesticListRecord> response = new ObjectRequest<>(false, DomesticListRecord.Input.buildInput(page * 20 + "", 20 + ""),
                domesticListRecord -> {
                    if (null != domesticListRecord && 1000 == domesticListRecord.code) {
                        if (null != domesticListRecord.result && domesticListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(domesticListRecord.result);
                            if (dataList.size() > 0) {
                                resultList.addAll(dataList);
//                                page += resultList.size();//.get(resultList.size() - 1).id;
                                page++;
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

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return null == resultList ? 0 : resultList.size();
        }

        @Override
        public LastUpdateCartoonListRecord.Result getItem(int position) {
            return resultList.get(position);
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            LastUpdateCartoonListRecord.Result result = getItem(position);
            if (!TextUtils.isEmpty(result.name)) {
                viewHolder.tv_name.setText(result.name);
            }
            if (!TextUtils.isEmpty(result.articleName)) {
                viewHolder.tv_num.setText("更新至" + result.articleName);
            }
            if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
                GlideUtil.load(parent.getContext(), result.imgUrl, R.drawable.ic_holder1, viewHolder.iv_cover);
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
            return convertView;
        }
    }

    private static class ViewHolder {
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
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }
}
