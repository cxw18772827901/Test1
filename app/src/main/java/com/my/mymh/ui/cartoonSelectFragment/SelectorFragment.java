package com.my.mymh.ui.cartoonSelectFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.model.record.ClumDataListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PackageName  com.dave.project.ui.fragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/9.
 */

public class SelectorFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "SelectorFragment1";
    private static final List<String> STRINGS_CLUM1 = Arrays.asList("全部", "连载", "完结");
    private static final List<String> STRINGS_CLUM2 = Arrays.asList("全部", "国漫", "日漫", "港漫", "其他");
    private List<String> STRINGS_CLUM3 = new ArrayList<>();
    @ViewInject(id = R.id.recycle1)
    private RecyclerView recycle1;
    @ViewInject(id = R.id.recycle2)
    private RecyclerView recycle2;
    @ViewInject(id = R.id.recycle3)
    private RecyclerView recycle3;
    public static final int orientation_h = LinearLayout.HORIZONTAL;
    private int position1 = 0;
    private int position2 = 0;
    private int position3 = 0;
    private MyAdapter3 myAdapter3;
    private boolean isFree = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selector_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        //1
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), orientation_h, false);
        recycle1.setLayoutManager(layoutManager1);
        recycle1.setAdapter(new MyAdapter1());
        //2
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), orientation_h, false);
        recycle2.setLayoutManager(layoutManager2);
        recycle2.setAdapter(new MyAdapter2());
        //3
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), orientation_h, false);
        recycle3.setLayoutManager(layoutManager);
        myAdapter3 = new MyAdapter3();
        recycle3.setAdapter(myAdapter3);
    }

    //请求完成后通知CartoonClumRackFragment刷新数据
    public void initData() {
        ObjectRequest<ClumDataListRecord> response = new ObjectRequest<>(false, ClumDataListRecord.Input.buildInput(),
                clumDataListRecord -> {
                    if (null != clumDataListRecord && 1000 == clumDataListRecord.code) {
                        if (null != clumDataListRecord.result && clumDataListRecord.result.size() > 0) {
                            STRINGS_CLUM3 = clumDataListRecord.result;
                            STRINGS_CLUM3.add(0, "全部");
                            myAdapter3.notifyDataSetChanged();
                            EventBus.getDefault().post(new EventBusEvent.SelectorEvent("全部", "全部", "全部"));
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                    EventBus.getDefault().post(new EventBusEvent.SelectorEvent("全部", "全部", ""));
                });
//        response.setShouldCache(true);
//        response.setCacheTime(24 * 60 * 60 * 1000);
        NetClient.request(response);
    }

    @Override
    public void onClick(View v) {
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }

    private class MyAdapter1 extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.clum_selector_layout_content, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            viewHolder1.textView.setText(STRINGS_CLUM1.get(position));
            viewHolder1.textView.setSelected(position1 == position);
            viewHolder1.textView.setOnClickListener(v -> {
                if (!isFree) {
                    return;
                }
                position1 = position;
                notifyDataSetChanged();
                if (null != STRINGS_CLUM3 && STRINGS_CLUM3.size() > 0) {
                    EventBus.getDefault().post(new EventBusEvent.SelectorEvent(STRINGS_CLUM1.get(position1), STRINGS_CLUM2.get(position2), STRINGS_CLUM3.get(position3)));
                } else {
                    EventBus.getDefault().post(new EventBusEvent.SelectorEvent(STRINGS_CLUM1.get(position1), STRINGS_CLUM2.get(position2), ""));
                }
                setFree(false);
            });
        }

        @Override
        public int getItemCount() {
            return STRINGS_CLUM1.size();
        }
    }

    private static class ViewHolder1 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_sel)
        TextView textView;

        ViewHolder1(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private class MyAdapter2 extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.clum_selector_layout_content, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ViewHolder2 viewHolder2 = (ViewHolder2) holder;
            viewHolder2.textView.setText(STRINGS_CLUM2.get(position));
            viewHolder2.textView.setSelected(position2 == position);
            viewHolder2.textView.setOnClickListener(v -> {
                if (!isFree) {
                    return;
                }
                position2 = position;
                notifyDataSetChanged();
                if (null != STRINGS_CLUM3 && STRINGS_CLUM3.size() > 0) {
                    EventBus.getDefault().post(new EventBusEvent.SelectorEvent(STRINGS_CLUM1.get(position1), STRINGS_CLUM2.get(position2), STRINGS_CLUM3.get(position3)));
                } else {
                    EventBus.getDefault().post(new EventBusEvent.SelectorEvent(STRINGS_CLUM1.get(position1), STRINGS_CLUM2.get(position2), ""));
                }
                setFree(false);
            });
        }

        @Override
        public int getItemCount() {
            return STRINGS_CLUM2.size();
        }
    }

    private static class ViewHolder2 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_sel)
        TextView textView;

        ViewHolder2(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private class MyAdapter3 extends RecyclerView.Adapter {


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(R.layout.clum_selector_layout_content, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            ViewHolder3 viewHolder3 = (ViewHolder3) holder;
            viewHolder3.textView.setText(STRINGS_CLUM3.get(position));
            viewHolder3.textView.setSelected(position3 == position);
            viewHolder3.textView.setOnClickListener(v -> {
                if (!isFree) {
                    return;
                }
                position3 = position;
                notifyDataSetChanged();
                EventBus.getDefault().post(new EventBusEvent.SelectorEvent(STRINGS_CLUM1.get(position1), STRINGS_CLUM2.get(position2), STRINGS_CLUM3.get(position3)));
                setFree(false);
            });
        }

        @Override
        public int getItemCount() {
            return null == STRINGS_CLUM3 ? 0 : STRINGS_CLUM3.size();
        }
    }

    private static class ViewHolder3 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_sel)
        TextView textView;

        ViewHolder3(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isFree = true;
    }
}
