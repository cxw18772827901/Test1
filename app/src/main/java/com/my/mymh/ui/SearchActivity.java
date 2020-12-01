package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.db.bean.SearchHistoryBean;
import com.my.mymh.db.dao.SearchHistoryDBDao;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.model.record.SearchRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.KeyBoardUtil;
import com.my.mymh.util.MyTextChangedListener;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.flowlayout.FlowLayout;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.Iterator;
import java.util.List;


/**
 * Package  com.dave.project.ui
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/12/1.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "SearchActivity";
    @ViewInject(id = R.id.et_search, needClick = true)
    private EditText et_search;
    @ViewInject(id = R.id.iv_delete, needClick = true)
    private ImageView iv_delete;
    @ViewInject(id = R.id.tv_cancle, needClick = true)
    private TextView tv_search;
    @ViewInject(id = R.id.view1_data_list)
    private FrameLayout view1_data_list;
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private ListView view1_listview;
    @ViewInject(id = R.id.view2_history)
    private LinearLayout view2_history;
    @ViewInject(id = R.id.flow_layout)
    private FlowLayout flow_layout;
    @ViewInject(id = R.id.view3_no_data)
    private FrameLayout view3_no_data;
    private SearchHistoryDBDao dao;
    private int pageSize = 20;
    private int lastId = 0;
    private List<LastUpdateCartoonListRecord.Result> resultList;
    private String searContent;
    private MyAdapter myAdapter;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.search_activity);
        init();
        initView();
    }

    private void init() {
        TitleBarUtil.initImmersiveBar(this, R.color.cl_white);
        dao = new SearchHistoryDBDao(this);
        layoutInflater = LayoutInflater.from(this);
    }

    private void initView() {
        et_search.addTextChangedListener(new MyTextChangedListener() {
            @Override
            protected void changed(String str) {
                if (TextUtils.isEmpty(str)) {
                    if (View.GONE != view1_data_list.getVisibility()) {
                        view1_data_list.setVisibility(View.GONE);
                    }
                    if (View.VISIBLE != view2_history.getVisibility()) {
                        view2_history.setVisibility(View.VISIBLE);
                    }
                    if (View.GONE != view3_no_data.getVisibility()) {
                        view3_no_data.setVisibility(View.GONE);
                    }
                    showSearList();
                    iv_delete.setVisibility(View.INVISIBLE);
                } else {
                    iv_delete.setVisibility(View.VISIBLE);
                }
            }
        });
        et_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH /*&& KeyEvent.KEYCODE_ENTER == event.getKeyCode()*/) {
                String str = et_search.getText().toString().trim();
                if (!TextUtils.isEmpty(str)) {
                    updateSearchData(str);
                } else {
                    MyDebugUtil.toast("不能为空");
                }
                return true;
            }
            return false;
        });
        view1_listview.setDivider(null);
        myAdapter = new MyAdapter();
        view1_listview.setAdapter(myAdapter);
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshLayout.setNoMoreData(false);
            lastId = 0;
            initData();
        });
        refreshLayout.setOnLoadMoreListener(refreshlayout -> loadData());
        view1_listview.setOnItemClickListener((parent, view, position, id) -> {
            if (null != resultList && resultList.size() > 0) {
                LastUpdateCartoonListRecord.Result result = resultList.get(position);
                if (result != null) {
//                    EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                    IntentUtil.startActivity(SearchActivity.this, CartoonInfoAndCatalogActivity.class);
                    IntentUtil.startRead(SearchActivity.this, result);
                }
            }
        });
        showSearList();
    }

    private void showSearList() {
        List<SearchHistoryBean> historyList = dao.queryAll();
        if (null != historyList && historyList.size() > 0) {
            flow_layout.removeAllViews();
            for (int i = 0; i < historyList.size(); i++) {
//                if (i > 20) {
//                    return;
//                }
                final SearchHistoryBean searchHistoryBean = historyList.get(i);
                View inflate = layoutInflater.inflate(R.layout.search_item, null);
                TextView tv_item = (TextView) inflate.findViewById(R.id.tv_item);
                tv_item.setText(searchHistoryBean.content);
                flow_layout.addView(inflate);
                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateSearchData(searchHistoryBean.content);
                        et_search.setText(searchHistoryBean.content);
                        et_search.setSelection(searchHistoryBean.content.length());
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                et_search.setCursorVisible(true);
                break;
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.iv_delete:
                et_search.setText("");
                break;
        }
    }

    private void updateSearchData(String content) {
        searContent = content;
        //更新或者添加搜索记录
        if (!dao.isAdded(content)) {//没有添加过
            dao.add(content, System.currentTimeMillis() + "", System.currentTimeMillis() + "", 1 + "");
        } else {//添加过,更新数据库,增加搜索的次数
            SearchHistoryBean searchHistoryBean = dao.query(content);
            dao.update(content, System.currentTimeMillis() + "", searchHistoryBean.count + 1);
        }
        //清除上次数据
        lastId = 0;
        if (null != resultList && resultList.size() > 0) {
            resultList.clear();
            myAdapter.notifyDataSetChanged();
        }
        //发起搜索
        initData();
    }

    private void initData() {
        ObjectRequest<SearchRecord> response = new ObjectRequest<>(false, SearchRecord.Input.buildInput(searContent, pageSize + "", ""),
                searchRecord -> {
                    refreshLayout.finishRefresh();
                    if (null != searchRecord && 1000 == searchRecord.code) {
                        if (null != searchRecord.result && searchRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(searchRecord.result);
                            if (dataList.size() > 0) {
                                //view
                                if (View.VISIBLE != view1_data_list.getVisibility()) {
                                    view1_data_list.setVisibility(View.VISIBLE);
                                }
                                if (View.GONE != view2_history.getVisibility()) {
                                    view2_history.setVisibility(View.GONE);
                                }
                                if (View.GONE != view3_no_data.getVisibility()) {
                                    view3_no_data.setVisibility(View.GONE);
                                }
                                KeyBoardUtil.closeKeybord(et_search);
                                //data
                                resultList = dataList;
                                lastId += resultList.size();//.get(resultList.size() - 1).id;
                                myAdapter.notifyDataSetChanged();
                            } else {
                                noData();
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                            noData();
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                        noData();
                    }
                },
                volleyError -> {
                    refreshLayout.finishRefresh();
                    MyDebugUtil.toast("访问失败");
                    noData();
                });
        NetClient.request(response);
    }

    private void noData() {
        if (View.GONE != view1_data_list.getVisibility()) {
            view1_data_list.setVisibility(View.GONE);
        }
        if (View.GONE != view2_history.getVisibility()) {
            view2_history.setVisibility(View.GONE);
        }
        if (View.VISIBLE != view3_no_data.getVisibility()) {
            view3_no_data.setVisibility(View.VISIBLE);
        }
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
        ObjectRequest<SearchRecord> response = new ObjectRequest<>(false, SearchRecord.Input.buildInput(searContent, pageSize + "", lastId + ""),
                searchRecord -> {
                    if (null != searchRecord && 1000 == searchRecord.code) {
                        if (null != searchRecord.result && searchRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(searchRecord.result);
                            if (dataList.size() > 0) {
                                resultList.addAll(dataList);
                                lastId += resultList.size();//.get(resultList.size() - 1).id;
                                myAdapter.notifyDataSetChanged();
                                refreshLayout.finishLoadMore();
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

        @SuppressLint("InflateParams")
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
