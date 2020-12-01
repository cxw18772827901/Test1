package com.my.mymh.ui.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.ZoomRecyclerView;


/**
 * Package  com.dave.project
 * Project  Project
 * Author   chenxiaowu
 * Date     2017/10/27.
 */
public class TestActivity1 extends BaseActivity {
    public static final String TAG = "TestActivity";
    @ViewInject(id = R.id.recycler_view)
    private ZoomRecyclerView zoomRecyclerView;

    @SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    int left = zoomRecyclerView.getLeft();
//                    MyDebugUtil.log("left", "left=" + left);
//                    sendEmptyMessageDelayed(0, 1000);
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.WHITE_THEME, R.layout.test_activity1);
        RecyclerView.LayoutManager onLineLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        zoomRecyclerView.setLayoutManager(onLineLayoutManager);
        MyAdapter myAdapter = new MyAdapter();
        zoomRecyclerView.setAdapter(myAdapter);
//        handler.sendEmptyMessage(0);
//        zoomRecyclerView.setOnTriggerListener(new CartoonOverScrollListView.OnTriggerListener() {
//            @Override
//            public void triggerFresh() {
//                MyDebugUtil.log("setOnTriggerListener", "triggerFresh");
//            }
//
//            @Override
//            public void triggerLoad() {
//                MyDebugUtil.log("setOnTriggerListener", "triggerLoad");
//
//            }
//        });
    }

    /*private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, null);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }*/

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }
}
