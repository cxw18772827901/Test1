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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.model.CartoonInfoItem;
import com.my.mymh.model.record.CommentListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.CartoonInfoAndCatalogActivity;
import com.my.mymh.ui.CommentActivity;
import com.my.mymh.ui.CommentListActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.dave.project.ui.cartoonDetailFragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/11.
 */
@Deprecated
public class CartoonInfoFragment1 extends BaseFragment {
    public static final String TAG = "CartoonInfoFragment1";
    @SuppressLint("StaticFieldLeak")
    private static CartoonInfoFragment1 cartoonInfoFragment1;
    @ViewInject(id = R.id.recycle_view)
    private RecyclerView recyclerView;
    private LastUpdateCartoonListRecord.Result result;
    private List<CartoonInfoItem> itemResultList;
    private MyAdapter myAdapter;
    private int pageSize = 5;
    private String lastId = "";

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cartoon_info_fragment1, null);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    private void initData() {
        ObjectRequest<CommentListRecord> response = new ObjectRequest<>(false, CommentListRecord.Input.buildInput(pageSize + "", lastId, result.id),
                commentListRecord -> {
                    if (null != commentListRecord && 1000 == commentListRecord.code) {
                        if (null != commentListRecord.list && commentListRecord.list.size() > 0) {
                            List<CommentListRecord.Result> dataList = filterData(commentListRecord.list);
                            appendData(dataList);
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                            appendData(null);
                        }
                    } else {
                        MyDebugUtil.toast("访问失败");
                        appendData(null);
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                    appendData(null);
                });
        NetClient.request(response);
    }

    private void appendData(List<CommentListRecord.Result> dataList) {
        itemResultList = new ArrayList<>();
        //desc
        appendDesc();
        //comment
        appendComment(dataList);
        //notify
        myAdapter.notifyDataSetChanged();
    }

    private void appendComment(List<CommentListRecord.Result> dataList) {
        if (null != dataList && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (i > 3) {
                    break;
                }
                CartoonInfoItem cartoonInfoItem = new CartoonInfoItem();
                cartoonInfoItem.itemType = CartoonInfoItem.ITEM_COMMENT;
                cartoonInfoItem.object = dataList.get(i);
                itemResultList.add(cartoonInfoItem);
            }
            CartoonInfoItem cartoonInfoItem = new CartoonInfoItem();
            cartoonInfoItem.itemType = CartoonInfoItem.ITEM_COMMENT_MORE;
            cartoonInfoItem.object = (dataList.size() > 4 ? "has" : "has_not");
            itemResultList.add(cartoonInfoItem);
        } else {
            CartoonInfoItem cartoonInfoItem = new CartoonInfoItem();
            cartoonInfoItem.itemType = CartoonInfoItem.ITEM_COMMENT_NONE;
            cartoonInfoItem.object = "no_data";
            itemResultList.add(cartoonInfoItem);
        }
    }

    private void appendDesc() {
        if (null != result) {
            //标题1:简介
            CartoonInfoItem cartoonInfoItem1 = new CartoonInfoItem();
            cartoonInfoItem1.itemType = CartoonInfoItem.ITEM_TITLE;
            cartoonInfoItem1.shouldShow = false;
            cartoonInfoItem1.object = "简介";
            itemResultList.add(cartoonInfoItem1);
            //简介内容
            CartoonInfoItem cartoonInfoItem2 = new CartoonInfoItem();
            cartoonInfoItem2.itemType = CartoonInfoItem.ITEM_DESC;
            cartoonInfoItem2.object = result;
            itemResultList.add(cartoonInfoItem2);
            //标题2:评论
            CartoonInfoItem cartoonInfoItem3 = new CartoonInfoItem();
            cartoonInfoItem3.itemType = CartoonInfoItem.ITEM_TITLE;
            cartoonInfoItem3.shouldShow = true;
            cartoonInfoItem3.object = "精彩点评";
            itemResultList.add(cartoonInfoItem3);
            //简介内容
            MyDebugUtil.log(TAG, "id=" + result.toString());
        }
    }

    private List<CommentListRecord.Result> filterData(List<CommentListRecord.Result> results) {
        Iterator<CommentListRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            CommentListRecord.Result result = it.next();
            if (null == result || TextUtils.isEmpty(result.commentContent)) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return results;
    }

    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public int getItemCount() {
            return null == itemResultList ? 0 : itemResultList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return itemResultList.get(position).itemType;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case CartoonInfoItem.ITEM_TITLE:
                    return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_info_item1, null));
                case CartoonInfoItem.ITEM_DESC:
                    return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_info_item2, null));
                case CartoonInfoItem.ITEM_COMMENT:
                    return new ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_info_item3, null));
                case CartoonInfoItem.ITEM_COMMENT_NONE:
                    return new ViewHolder4(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_info_item4, null));
                case CartoonInfoItem.ITEM_COMMENT_MORE:
                    return new ViewHolder5(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_info_item5, null));
                default:
                    return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            CartoonInfoItem item = itemResultList.get(position);
            if (holder instanceof ViewHolder1) {
                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
//                viewHolder1.view_holder.setVisibility(item.shouldShow ? View.VISIBLE : View.GONE);
                viewHolder1.tv_title_left.setText((String) item.object);
                viewHolder1.tv_title_right.setVisibility(item.shouldShow ? View.VISIBLE : View.GONE);
                viewHolder1.tv_title_right.setOnClickListener(v -> IntentUtil.startActivityWithString(getActivity(), CommentActivity.class, "cartoonId", result.id));
            } else if (holder instanceof ViewHolder2) {
                ViewHolder2 viewHolder2 = (ViewHolder2) holder;
                viewHolder2.tv_desc.setText(TrimUtil.trim(result.longDesc + ""));
                String author = "作者 : " + result.author;
//                viewHolder2.tv_author.setText(Html.fromHtml(author.replace("作者 : ", "<font color=\"#999999\">" + "作者 : " + "</font>")));
            } else if (holder instanceof ViewHolder3) {
                CommentListRecord.Result result = (CommentListRecord.Result) item.object;
                ViewHolder3 viewHolder3 = (ViewHolder3) holder;
                viewHolder3.tv_name.setText(result.nickName + "");
                viewHolder3.tv_time.setText(result.commentTime + "");
                viewHolder3.tv_content.setText(result.commentContent + "");
            } else if (holder instanceof ViewHolder4) {
                ViewHolder4 viewHolder4 = (ViewHolder4) holder;
                viewHolder4.tv_holder.setOnClickListener(v -> IntentUtil.startActivityWithString(getActivity(), CommentActivity.class, "cartoonId", result.id));
            } else {
                ViewHolder5 viewHolder5 = (ViewHolder5) holder;
                //data
                String str = (String) item.object;
                final boolean has = ("has".equals(str));
                viewHolder5.tv_more.setText(has ? "查看更多评论" : "没有更多评论了");
                viewHolder5.tv_more.setOnClickListener(v -> {
                    if (has) {
                        IntentUtil.startActivityWithString(getActivity(), CommentListActivity.class, "cartoonId", result.id);
                    }
                });
            }
        }
    }

    private static class ViewHolder1 extends RecyclerView.ViewHolder {
        //        @ViewInject(id = R.id.view_holder)
//        View view_holder;
        @ViewInject(id = R.id.tv_title_left)
        TextView tv_title_left;
        @ViewInject(id = R.id.tv_title_right)
        TextView tv_title_right;

        ViewHolder1(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder2 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_desc)
        TextView tv_desc;
//        @ViewInject(id = R.id.tv_author)
//        TextView tv_author;

        ViewHolder2(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder3 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;
        @ViewInject(id = R.id.tv_time)
        TextView tv_time;
        @ViewInject(id = R.id.tv_content)
        TextView tv_content;

        ViewHolder3(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder4 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.tv_holder)
        TextView tv_holder;

        ViewHolder4(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    private static class ViewHolder5 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.ll_root)
        LinearLayout ll_root;
        @ViewInject(id = R.id.tv_more)
        TextView tv_more;

        ViewHolder5(View convertView) {
            super(convertView);
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (itemResultList != null && itemResultList.size() > 0) {
            itemResultList.clear();
            itemResultList = null;
        }
    }

    public static CartoonInfoFragment1 getInstance() {
        if (null == cartoonInfoFragment1) {
            cartoonInfoFragment1 = new CartoonInfoFragment1();
        }
        return cartoonInfoFragment1;
    }
}
