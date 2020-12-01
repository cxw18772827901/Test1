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
import com.my.mymh.model.record.HotOrNewCartoonListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.RankHeaderView;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/17.
 */
public class RankFragment extends BaseFragment {
    public static final String TAG = "RankFragment";
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private ListView listview;
    private List<LastUpdateCartoonListRecord.Result> resultList = new ArrayList<>();
    private int tag = -1;
    private int lastId = 0;
    private MyAdapter myAdapter;
    private RankHeaderView rank_header;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rank_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
        initData();
    }

    private void init() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            tag = bundle.getInt("tag");
        }
    }

    @SuppressLint("InflateParams")
    private void initView() {
        //header
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.rank_header, null);
        rank_header = (RankHeaderView) headerView.findViewById(R.id.rank_header);
        listview.addHeaderView(headerView);
        //adapter
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshLayout.setNoMoreData(false);
            lastId = 0;
            initData();
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> loadData());
        listview.setOnItemClickListener((parent, view, position, id) -> {
            if (position > 0 && null != resultList && resultList.size() > 0) {
                LastUpdateCartoonListRecord.Result result = resultList.get(position - 1);
                if (result != null) {
//                    EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                    IntentUtil.startActivity(getActivity(), CartoonInfoAndCatalogActivity.class);
                    IntentUtil.startRead(getActivity(), result);
                }
            }
        });
//        refreshLayout.autoRefresh(200, 100, 1);
    }

    private void initData() {
        ObjectRequest<HotOrNewCartoonListRecord> response = new ObjectRequest<>(false, HotOrNewCartoonListRecord.Input.buildInput("10", lastId + "", tag + ""),
                hotOrNewCartoonListRecord -> {
                    refreshLayout.finishRefresh();
                    if (null != hotOrNewCartoonListRecord && 1000 == hotOrNewCartoonListRecord.code) {
                        if (null != hotOrNewCartoonListRecord.result && hotOrNewCartoonListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(hotOrNewCartoonListRecord.result);
                            if (dataList.size() > 0) {
                                resultList = dataList;
                                lastId += resultList.size();//get(resultList.size() - 1).id;
                                LastUpdateCartoonListRecord.Result result1 = resultList.remove(0);
                                LastUpdateCartoonListRecord.Result result2 = null;
                                if (resultList.size() > 0) {
                                    result2 = resultList.remove(0);
                                }
                                LastUpdateCartoonListRecord.Result result3 = null;
                                if (resultList.size() > 0) {
                                    result3 = resultList.remove(0);
                                }
                                rank_header.setDatas(getActivity(), result1, result2, result3);
                                if (resultList.size() > 0) {
                                    myAdapter.notifyDataSetChanged();
                                }
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
        ObjectRequest<HotOrNewCartoonListRecord> response = new ObjectRequest<>(false, HotOrNewCartoonListRecord.Input.buildInput("10", lastId + "", tag + ""),
                newCartoonListRecord -> {
                    if (null != newCartoonListRecord && 1000 == newCartoonListRecord.code) {
                        if (null != newCartoonListRecord.result && newCartoonListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(newCartoonListRecord.result);
                            if (dataList.size() > 0) {
                                resultList.addAll(dataList);
                                lastId += resultList.size();//.get(resultList.size() - 1).id;
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

        @SuppressLint({"SetTextI18n", "InflateParams"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_book_item, null);
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
            switch (position) {
//                case 0:
//                    viewHolder.iv_rank.setImageResource(R.drawable.ic_rank_1);
//                    break;
//                case 1:
//                    viewHolder.iv_rank.setImageResource(R.drawable.ic_rank_2);
//                    break;
//                case 2:
//                    viewHolder.iv_rank.setImageResource(R.drawable.ic_rank_3);
//                    break;
                default:
                    viewHolder.iv_rank.setImageResource(R.drawable.ic_rank_4);
                    break;
            }
            viewHolder.tv_rank.setText(String.valueOf(position + 1 + 3));
            return convertView;
        }
    }

    private static class ViewHolder {
        @ViewInject(id = R.id.iv_cover)
        ImageView iv_cover;
        @ViewInject(id = R.id.iv_rank)
        ImageView iv_rank;
        @ViewInject(id = R.id.tv_rank)
        TextView tv_rank;
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
