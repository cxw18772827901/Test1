package com.my.mymh.ui.cartoonDetailFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.model.record.CommonListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.CartoonInfoAndCatalogActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.ui.cartoonDetailFragment
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/9/6.
 */
public class CommonListFragment extends BaseFragment {
    public static final String TAG = "CommentListFragment";
    @ViewInject(id = R.id.tv_desc)
    private TextView tv_desc;
    @ViewInject(id = R.id.recycle_view)
    private RecyclerView recyclerView;
    private LastUpdateCartoonListRecord.Result result;
    private MyAdapter myAdapter;
    private List<LastUpdateCartoonListRecord.Result> resultList;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_list_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
        initData();
    }

    private void init() {
        this.result = ((CartoonInfoAndCatalogActivity) getActivity()).result;
    }

    private void initView() {
        tv_desc.setText(TrimUtil.trim(result.longDesc + ""));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    private void initData() {
        ObjectRequest<CommonListRecord> response = new ObjectRequest<>(false, CommonListRecord.Input.buildInput(result.label + ""),
                commonListRecord -> {
                    if (null != commonListRecord && 1000 == commonListRecord.code) {
                        if (null != commonListRecord.result && commonListRecord.result.size() > 0) {
                            List<LastUpdateCartoonListRecord.Result> dataList = filterData(commonListRecord.result);
                            if (dataList.size() > 0) {
                                CommonListFragment.this.resultList = dataList;
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

    private class MyAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return resultList == null ? 0 : resultList.size();
//            return 10;
        }

        @SuppressLint("InflateParams")
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder1 viewHolder1 = (ViewHolder1) holder;
            LastUpdateCartoonListRecord.Result result = resultList.get(position);
            if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
                GlideUtil.load(getActivity(), result.imgUrl, R.drawable.ic_holder1, viewHolder1.iv_car);
            } else {
                viewHolder1.iv_car.setImageResource(R.drawable.ic_holder1);
            }
            viewHolder1.iv_car.setOnClickListener(v -> IntentUtil.startRead(getActivity(), result));
        }
    }

    private static class ViewHolder1 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.iv_car)
        ImageView iv_car;

        ViewHolder1(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (resultList != null && resultList.size() > 0) {
            resultList.clear();
        }
    }

}
