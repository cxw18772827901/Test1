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
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.ui.cartoonDetailFragment
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/9/6.
 */
public class CartoonCatalogFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "CartoonCatalogFragment";
    @ViewInject(id = R.id.tv_range, needClick = true)
    private TextView tv_range;
    @ViewInject(id = R.id.recycler_view)
    private RecyclerView recycler_view;
    private MyAdapter myAdapter;
    private LastUpdateCartoonListRecord.Result result;
    private List<Item> itemList = new ArrayList<>();
    private CartoonRackDao cartoonRackDao;
    private boolean isZX = false;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cartoon_info_fragment, null);
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

    @SuppressLint("InflateParams")
    private void initView() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        myAdapter = new MyAdapter();
        recycler_view.setAdapter(myAdapter);
    }

    private void initData() {
        ObjectRequest<CatalogListRecord> response = new ObjectRequest<>(false, CatalogListRecord.Input.buildInput(result.id),
                commentListRecord -> {
                    if (null != commentListRecord && 1000 == commentListRecord.code) {
                        if (null != commentListRecord.result && commentListRecord.result.size() > 0) {
                            List<CatalogListRecord.Result> dataList = filterData(commentListRecord.result);
                            if (dataList.size() > 0) {
                                itemList = new ArrayList<>();
                                //add
//                                itemList.add(new Item(Item.TYPE_HEAD, null));
                                for (CatalogListRecord.Result result : dataList) {
                                    itemList.add(new Item(Item.TYPE_ITEM, result));
                                }
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

//    @SuppressLint("StaticFieldLeak")
//    private static CartoonCatalogFragment cartoonCatalogFragment;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_range:
                if (itemList == null || itemList.size() == 0) {
                    return;
                }
                isZX = !isZX;
                tv_range.setSelected(isZX);
                tv_range.setText(isZX ? "正序" : "倒序");
                //add
//                Item removeItem = itemList.remove(0);
                Collections.reverse(itemList);
//                itemList.add(0, removeItem);
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position).type;
        }

        @SuppressLint("InflateParams")
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
//                case Item.TYPE_HEAD:
//                    return new ViewHolder1(LayoutInflater.from(getActivity()).inflate(R.layout.info_header_layout, null));
                default:
                    return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, null));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder1) {
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
                viewHolder1.tv_desc.setText(TrimUtil.trim(result.longDesc + ""));
                viewHolder1.tv_range.setOnClickListener(view -> {
                    if (null != itemList && itemList.size() > 0) {
                        isZX = !isZX;
                        viewHolder1.tv_range.setSelected(isZX);
                        viewHolder1.tv_range.setText(isZX ? "正序" : "倒序");
                        //add
                        Item removeItem = itemList.remove(0);
                        Collections.reverse(itemList);
                        itemList.add(0, removeItem);
                        myAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.view_line.setVisibility(position == itemList.size() - 1 ? View.GONE : View.VISIBLE);
                //data
                CatalogListRecord.Result resultBean = (CatalogListRecord.Result) itemList.get(position).object;
                viewHolder2.tv_catalog.setText(resultBean.articleName);
                final int finalI = position;
                viewHolder2.tv_catalog.setOnClickListener(v -> {
                    //存书架
                    RackBean rackBean = cartoonRackDao.query(result.id);
                    if (null == rackBean) {
                        cartoonRackDao.add(new RackBean(result.id, GsonUtil.getGson().toJson(result), System.currentTimeMillis() + ""));
                    }
                    //跳转
                    int currentIndex;
                    if (!isZX) {//正序直接加1
                        currentIndex = itemList.size() - position;
                    } else {//逆序resultList.size()-position
                        currentIndex = finalI;
                    }
                    IntentUtil.startActivityWithSevString(getActivity(), CartoonReadActivity.class,
                            "type", "0",
                            "cartoonId", result.id,
                            "articleId", resultBean.articleId,
                            "currentIndex", currentIndex + "",
                            "size", (itemList.size() /*- 1*/) + "",
                            "name", result.name,
                            "titleName", result.articleName
                    );
                });
            }
        }

        @Override
        public int getItemCount() {
            return itemList != null ? itemList.size() : 0;
        }
    }

    private static class ViewHolder1 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_desc)
        TextView tv_desc;
        @ViewInject(id = R.id.tv_range)
        TextView tv_range;

        ViewHolder1(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder2 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_catalog)
        TextView tv_catalog;
        @ViewInject(id = R.id.view_line)
        View view_line;

        ViewHolder2(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    static class Item {
        public static final int TYPE_HEAD = 0;
        public static final int TYPE_ITEM = 1;
        public int type;
        public Object object;

        public Item(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (itemList != null && itemList.size() > 0) {
            itemList.clear();
            itemList = null;
        }
        isZX = false;
    }


//    public static CartoonCatalogFragment getInstance() {
//        if (cartoonCatalogFragment == null) {
//            cartoonCatalogFragment = new CartoonCatalogFragment();
//        }
//        return cartoonCatalogFragment;
//    }
}
