package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.model.record.CareRecord;
import com.my.mymh.model.record.CartoonUsersRecord;
import com.my.mymh.model.record.GoodRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.model.record.UserCartoonsRecord;
import com.my.mymh.model.record.UserInfoRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.MoreDialogUtil;
import com.my.mymh.util.ScreenUtil;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.hgd.hgdcomic.ui
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/25.
 */
public class UserDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "UserDetailActivity";
    private CartoonUsersRecord.Result result;
    //head
    @ViewInject(id = R.id.iv_bg)
    private ImageView iv_bg;
    @ViewInject(id = R.id.tv_back_arrow, needClick = true)
    private TextView tv_back_arrow;
    @ViewInject(id = R.id.tv_title_center)
    private TextView tv_title_center;
    @ViewInject(id = R.id.iv_right1, needClick = true)
    private ImageView iv_right1;
    @ViewInject(id = R.id.iv_right, needClick = true)
    private ImageView iv_right;
    @ViewInject(id = R.id.iv_head)
    private ImageView iv_head;
    @ViewInject(id = R.id.tv_get_good)
    private TextView tv_get_good;
    @ViewInject(id = R.id.tv_get_care)
    private TextView tv_get_care;
    //content
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.recycler_view)
    private RecyclerView recycler_view;
    //foot
    @ViewInject(id = R.id.tv_care, needClick = true)
    private TextView tv_care;
    private int count = 20;
    private String lastId = "";
    private List<Item> itemList = new ArrayList<>();
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.user_detail_activity);
        init();
        intView();
        initData();
    }

    private void init() {
        result = (CartoonUsersRecord.Result) getIntent().getSerializableExtra("user");
        TitleBarUtil.initImmersiveBar(this, R.color.cl_no_color);
    }


    @SuppressLint("SetTextI18n")
    private void initData() {
        ObjectRequest<UserInfoRecord> response = new ObjectRequest<>(false, UserInfoRecord.Input.buildInput(result.userId),
                hotOrNewCartoonListRecord -> {
                    if (hotOrNewCartoonListRecord != null && hotOrNewCartoonListRecord.result != null &&
                            1000 == hotOrNewCartoonListRecord.code) {
                        tv_get_good.setText(hotOrNewCartoonListRecord.result.goodCounts + "个");
                        tv_get_care.setText(hotOrNewCartoonListRecord.result.attentionCounts + "个");
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("访问失败");
                });
        NetClient.request(response);
    }

    private void intView() {
        //head
        String headImg = result.headImg;
        if (!TextUtils.isEmpty(headImg)) {
            GlideUtil.load(this, headImg, R.drawable.ic_holder2, iv_bg);
            GlideUtil.load(this, headImg, R.drawable.ic_defult_header, iv_head);
        } else {
            iv_bg.setImageResource(R.drawable.ic_holder2);
            iv_head.setImageResource(R.drawable.ic_defult_header);
        }
        tv_title_center.setText(result.nickName);
        tv_title_center.setSelected(2 == result.sex);
        //content
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            refreshLayout.setNoMoreData(false);
            lastId = "";
            freshData();
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> loadData());
        //adapter
        recycler_view.addItemDecoration(new SpaceItemDecoration());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter();
        recycler_view.setAdapter(myAdapter);
        //auto refresh
        refreshLayout.autoRefresh(200, 100, 1);
        //footer
//        hasCare();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_bg.postDelayed(this::hasCare, 1000);
    }

    private void hasCare() {
        //care
        if (LoginVerifyUtil.isLogin()) {
            if (LoginVerifyUtil.isCared(result.userId)) {
                tv_care.setText("取消关注");
                tv_care.setVisibility(View.GONE);
            } else {
                tv_care.setText("十关注");
                tv_care.setVisibility(View.VISIBLE);
            }
        } else {
            tv_care.setText("十关注");
            tv_care.setVisibility(View.VISIBLE);
        }
        //good
        iv_right1.setSelected(LoginVerifyUtil.isLogin() && LoginVerifyUtil.isGood(result.userId));
    }

    private List<UserCartoonsRecord.Result> filterData(List<UserCartoonsRecord.Result> results) {
        Iterator<UserCartoonsRecord.Result> it = results.iterator();
        while (it.hasNext()) {
            UserCartoonsRecord.Result result = it.next();
            if (null == result ||
                    TextUtils.isEmpty(result.cartoonId) ||
                    TextUtils.isEmpty(result.name) ||
                    TextUtils.isEmpty(result.favoriteId)) {//如果回话存在,但是没有消息,那就删除此条会话
                it.remove();
            }
        }
        return results;
    }

    private void freshData() {
        ObjectRequest<UserCartoonsRecord> response = new ObjectRequest<>(false, UserCartoonsRecord.Input.buildInput(result.userId, count + "", lastId),
                hotOrNewCartoonListRecord -> {
                    refreshLayout.finishRefresh();
                    if (null != hotOrNewCartoonListRecord && 1000 == hotOrNewCartoonListRecord.code) {
                        if (null != hotOrNewCartoonListRecord.result && hotOrNewCartoonListRecord.result.size() > 0) {
                            List<UserCartoonsRecord.Result> dataList = filterData(hotOrNewCartoonListRecord.result);
                            if (dataList != null && dataList.size() > 0) {
                                lastId = dataList.get(dataList.size() - 1).favoriteId;
                                itemList.clear();
                                itemList.add(new Item(Item.ITEM_TITLE, 0));
                                for (UserCartoonsRecord.Result result : dataList) {
                                    itemList.add(new Item(Item.ITEM_CONTENT, result));
                                }
                                myAdapter.notifyDataSetChanged();
                            } else {
                                setNoData();
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                            setNoData();
                        }
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                        setNoData();
                    }
                },
                volleyError -> {
                    refreshLayout.finishRefresh();
                    MyDebugUtil.toastTest("访问失败");
                    setNoData();
                });
        NetClient.request(response);
    }

    private void setNoData() {
        itemList.clear();
        itemList.add(new Item(Item.ITEM_TITLE, 0));
        itemList.add(new Item(Item.ITEM_HOLDER, null));
        myAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        ObjectRequest<UserCartoonsRecord> response = new ObjectRequest<>(false, UserCartoonsRecord.Input.buildInput(result.userId, count + "", lastId),
                hotOrNewCartoonListRecord -> {
                    if (null != hotOrNewCartoonListRecord && 1000 == hotOrNewCartoonListRecord.code) {
                        if (null != hotOrNewCartoonListRecord.result && hotOrNewCartoonListRecord.result.size() > 0) {
                            List<UserCartoonsRecord.Result> dataList = filterData(hotOrNewCartoonListRecord.result);
                            if (dataList != null && dataList.size() > 0) {
                                refreshLayout.finishLoadMore();
                                lastId = dataList.get(dataList.size() - 1).favoriteId;
                                for (UserCartoonsRecord.Result result : dataList) {
                                    itemList.add(new Item(Item.ITEM_CONTENT, result));
                                }
                                myAdapter.notifyDataSetChanged();
                            } else {
                                setNoMore();
                            }
                        } else {
                            MyDebugUtil.toastTest("暂时没有数据");
                            setNoMore();
                        }
                    } else {
                        MyDebugUtil.toastTest("访问失败");
                        setNoMore();
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("访问失败");
                    setNoMore();
                });
        NetClient.request(response);
    }

    private void setNoMore() {
        refreshLayout.finishLoadMoreWithNoMoreData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_right1:
                if (LoginVerifyUtil.isLogin()) {
                    goodOrNot();
                } else {
                    IntentUtil.startActivity(UserDetailActivity.this, LoginActivity.class);
                }
                break;
            case R.id.iv_right:
                MoreDialogUtil.showDeleteDialog(this, result.userId, () -> {
                    if (LoginVerifyUtil.isLogin()) {
                        careOrNot();
                    } else {
                        IntentUtil.startActivity(UserDetailActivity.this, LoginActivity.class);
                    }
                });
                break;
            case R.id.tv_back_arrow:
                finish();
                break;
            case R.id.tv_care:
                if (LoginVerifyUtil.isLogin()) {
                    careOrNot();
                } else {
                    IntentUtil.startActivity(this, LoginActivity.class);
                }
                break;
        }
    }

    private void careOrNot() {
        int operate = LoginVerifyUtil.isCared(result.userId) ? -1 : 1;
        @SuppressLint("SetTextI18n") ObjectRequest<CareRecord> response = new ObjectRequest<>(false, CareRecord.Input.buildInput(result.userId, operate),
                careIdsRecord -> {
                    if (careIdsRecord != null && 1000 == careIdsRecord.code) {
                        MyDebugUtil.toast("操作成功");
                        LoginVerifyUtil.isAddCareId(1 == operate, result.userId);
                        String num = tv_get_care.getText().toString();
                        num = num.replace("个", "");
                        int number = Integer.parseInt(num);
                        if (operate == 1) {
                            tv_care.setText("取消关注");
                            tv_care.setVisibility(View.GONE);
                            number++;
                        } else {
                            tv_care.setText("十关注");
//                            tv_care.setVisibility(View.VISIBLE);
                            if (number > 0) {
                                number--;
                            }
                        }
                        tv_get_care.setText(String.valueOf(number) + "个");
                    } else {
                        MyDebugUtil.toast("访问失败，请稍后再试");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                });
        NetClient.request(response);
    }

    private void goodOrNot() {
        int operate = LoginVerifyUtil.isGood(result.userId) ? 2 : 1;
        @SuppressLint("SetTextI18n") ObjectRequest<GoodRecord> response = new ObjectRequest<>(false, GoodRecord.Input.buildInput(result.userId, operate),
                careIdsRecord -> {
                    if (careIdsRecord != null && 1000 == careIdsRecord.code) {
                        MyDebugUtil.toast("操作成功");
                        LoginVerifyUtil.isAddGoodId(1 == operate, result.userId);
                        iv_right1.setSelected(operate == 1);
                        String num = tv_get_good.getText().toString();
                        num = num.replace("个", "");
                        int number = Integer.parseInt(num);
                        if (operate == 1) {
                            number++;
                        } else {
                            if (number > 0) {
                                number--;
                            }
                        }
                        tv_get_good.setText(String.valueOf(number) + "个");
                    } else {
                        MyDebugUtil.toast("访问失败，请稍后再试");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("访问失败");
                });
        NetClient.request(response);
    }

    class MyAdapter extends RecyclerView.Adapter {
        private int itemW = (ScreenUtil.getScreenWid() - UIUtil.dip2px(50)) / 4;
        private int itemH = (int) (itemW / 0.761);

        @Override
        public int getItemViewType(int position) {
            return itemList.get(position).type;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            final GridLayoutManager gridManager = ((GridLayoutManager) recyclerView.getLayoutManager());
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = itemList.get(position).type;
                    if (type == Item.ITEM_TITLE ||
                            type == Item.ITEM_HOLDER) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }

        @SuppressLint("InflateParams")
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case Item.ITEM_TITLE:
                    return new VH1(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_title, null));
                case Item.ITEM_CONTENT:
                    return new VH2(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_content, null));
                default:
                    return new VH3(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_holder, null));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VH1) {
            } else if (holder instanceof VH2) {
                VH2 vh2 = (VH2) holder;
                UserCartoonsRecord.Result result1 = (UserCartoonsRecord.Result) itemList.get(position).object;
                //view
                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) vh2.ll_root.getLayoutParams();
                layoutParams1.width = itemW;
                vh2.ll_root.setLayoutParams(layoutParams1);
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) vh2.iv_cover.getLayoutParams();
                layoutParams2.height = itemH;
                vh2.iv_cover.setLayoutParams(layoutParams2);
                //url
                if (!TextUtils.isEmpty(result1.imgUrl) && result1.imgUrl.contains("http")) {
                    GlideUtil.load(UserDetailActivity.this, result1.imgUrl, R.drawable.ic_holder1, vh2.iv_cover);
                } else {
                    vh2.iv_cover.setImageResource(R.drawable.ic_holder1);
                }
                //name
                vh2.tv_name.setText(result1.name);
                //click
                vh2.ll_root.setOnClickListener(v -> {
                    LastUpdateCartoonListRecord.Result result2 = new LastUpdateCartoonListRecord.Result();
                    result2.id = result1.cartoonId;
                    result2.articleName = result1.articleName;
                    result2.author = result1.author;
                    result2.imgUrl = result1.imgUrl;
                    result2.longDesc = result1.longDesc;
                    result2.lzStatus = result1.lzStatus + "";
                    result2.name = result1.name;
                    result2.updateTime = result1.updateTime;
//                    EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result2));
//                    IntentUtil.startActivity(UserDetailActivity.this, CartoonInfoAndCatalogActivity.class);
                    IntentUtil.startRead(UserDetailActivity.this, result2);
                });
            } else if (holder instanceof VH3) {
            }
        }

        @Override
        public int getItemCount() {
            return itemList != null ? itemList.size() : 0;
        }
    }

    static class VH1 extends RecyclerView.ViewHolder {

        public VH1(View itemView) {
            super(itemView);
            ViewInjectUtil.initNotInActivity(this, itemView);
        }
    }

    static class VH2 extends RecyclerView.ViewHolder {
        @ViewInject(id = R.id.ll_root)
        LinearLayout ll_root;
        @ViewInject(id = R.id.iv_cover)
        ImageView iv_cover;
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;

        public VH2(View itemView) {
            super(itemView);
            ViewInjectUtil.initNotInActivity(this, itemView);
        }
    }

    static class VH3 extends RecyclerView.ViewHolder {

        public VH3(View itemView) {
            super(itemView);
            ViewInjectUtil.initNotInActivity(this, itemView);
        }
    }

    public static class Item {
        static final int ITEM_TITLE = 0;
        static final int ITEM_CONTENT = 1;
        static final int ITEM_HOLDER = 2;
        public int type;
        public Object object;

        Item(int type, Object object) {
            this.type = type;
            this.object = object;
        }
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        final int margin = UIUtil.dip2px(10);
//        private int outSide;
//        private int inSide;

//        SpaceItemDecoration(int outSide, int inSide) {
//            this.outSide = outSide;
//            this.inSide = inSide;
//        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int childPosition = parent.getChildLayoutPosition(view);
            int type = itemList.get(childPosition).type;
            if (type == Item.ITEM_TITLE ||
                    type == Item.ITEM_HOLDER) {
                outRect.left = 0;
                outRect.right = 0;
            } else {
             /*   if (childPosition % 4 == 1) {
                    outRect.left = UIUtil.dip2px(10);
                    outRect.right = 0;
                } else*/
                if (childPosition % 4 == 0) {
                    outRect.left = margin;
                    outRect.right = margin;
                } else {
                    outRect.left = margin;
                    outRect.right = 0;
                }
            }
        }
    }
}
