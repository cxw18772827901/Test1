package com.my.mymh.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.wedjet.PinnedSectionListView;
import com.my.mymh.wedjet.smartrefresh.layout.api.RefreshLayout;
import com.my.mymh.wedjet.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Package  com.refresh
 * Project  Refresh
 * Author   chenxiaowu
 * Date     2017/10/27.
 */

public class FreshActivity extends Activity {
    static List<Integer> integers = new ArrayList<>();

    static {
        for (int i = 0; i < 20; i++) {
            integers.add(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        TitleBarUtil.initImmersiveBar(this, R.color.cl_3F51B5);
        PinnedSectionListView listView = (PinnedSectionListView) findViewById(R.id.listview);
        MyAdapter mAdapter = new MyAdapter();
        listView.setAdapter(mAdapter);
        final RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(refreshlayouts -> {
            refreshLayout.finishRefresh();
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });
        //触发自动刷新
//        refreshLayout.autoRefresh();
    }


    private class MyAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return 0 == viewType;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return (getItem(position)) % 2;
        }

        @Override
        public int getCount() {
            return integers.size();
        }

        @Override
        public Integer getItem(int i) {
            return integers.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            switch (type) {
                case 0://置顶条
                    SectionHolder sectionHolder;
                    if (convertView == null) {
                        convertView = View.inflate(viewGroup.getContext(), R.layout.user_section_item, null);
                        sectionHolder = new SectionHolder();
                        sectionHolder.textView = (TextView) convertView.findViewById(R.id.tv);
                        convertView.setTag(sectionHolder);
                    } else {
                        sectionHolder = (SectionHolder) convertView.getTag();
                    }
                    sectionHolder.textView.setText(getItem(i) + "条目");
                    break;
                case 1://好友条
                    UserHolder userHolder = null;
                    if (convertView == null) {
                        convertView = View.inflate(viewGroup.getContext(), R.layout.friend_swipe_item, null);
                        userHolder = new UserHolder();
                        userHolder.textView = (TextView) convertView.findViewById(R.id.tv);
                        convertView.setTag(userHolder);
                    } else {
                        userHolder = (UserHolder) convertView.getTag();
                    }
                    userHolder.textView.setText(getItem(i) + "条目");
                    break;
            }
            return convertView;
        }
    }

    private class SectionHolder {
        TextView textView;
    }

    private class UserHolder {
        TextView textView;
    }
}
