package com.my.mymh.ui.mainFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.base.Constants;
import com.my.mymh.model.AnyObjectItem;
import com.my.mymh.model.record.CityEmotionListRecord;
import com.my.mymh.model.record.HotOrNewCartoonListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.ui.CityEmotionListActivity;
import com.my.mymh.ui.ClassicsListActivity;
import com.my.mymh.ui.HotListActivity;
import com.my.mymh.ui.NewListActivity;
import com.my.mymh.ui.UpdateListActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.ScreenUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.AutoRollLayout;
import com.my.mymh.wedjet.PinnedSectionListView;
import com.my.mymh.wedjet.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PackageName  com.dave.project.ui.fragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/8.
 * <p>
 * 漫库
 */
@Deprecated
public class CartoonRackPinnedFragment extends BaseFragment implements View.OnClickListener, AutoRollLayout.OnItemClickListener {
    public static final String TAG = "CartoonRackFragment";
    @ViewInject(id = R.id.refreshLayout)
    private SmartRefreshLayout refreshLayout;
    @ViewInject(id = R.id.listview)
    private PinnedSectionListView mRecycleView;
    private AutoRollLayout arl;
    private List<AnyItem> anyItemList = new ArrayList<>();
    private MyAdapter myAdapter;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cartoon_rack_pinned_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initView();
    }

    private void init() {

    }

    private void initView() {
        refreshLayout.setHeaderHeight(Constants.FRESH_HEADER_HEIGHT);
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            initData();
        });
        mRecycleView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecycleView.setFadingEdgeLength(0);
//        mRecycleView.addItemDecoration(new SpaceItemDecoration(UIUtil.dip2px(12), UIUtil.dip2px(10)));
//        RecyclerView.LayoutManager onLineLayoutManager = new GridLayoutManager(getActivity(), 3, Constants.ORIENTATION_V, false);
//        mRecycleView.setLayoutManager(onLineLayoutManager);
        myAdapter = new MyAdapter();
        mRecycleView.setAdapter(myAdapter);
        refreshLayout.autoRefresh(200, 100, 1);
    }

    private void initData() {
        getCityEmotionData();
    }

    private void getCityEmotionData() {
        ObjectRequest<CityEmotionListRecord> response = new ObjectRequest<>(false, CityEmotionListRecord.Input.buildInput("", 24 + ""),
                this::addCityData,
                volleyError -> setNoData());
        NetClient.request(response);
    }

    private void setNoData() {
        MyDebugUtil.toast("访问失败");
        anyItemList.clear();
        anyItemList.add(new AnyItem(AnyItem.TYPE_NO_NET, null));
        mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_f0f0f0));
        finishAndNotify();
    }

    private void addCityData(CityEmotionListRecord cityEmotionListRecord) {
        if (null != cityEmotionListRecord && 1000 == cityEmotionListRecord.code) {
            if (null != cityEmotionListRecord.result && cityEmotionListRecord.result.size() > 0) {
                List<LastUpdateCartoonListRecord.Result> dataList = filterData(cityEmotionListRecord.result);
                if (dataList.size() > 0) {
                    anyItemList.clear();
                    anyItemList.add(new AnyItem(AnyItem.TYPE_HEADER, "热门推荐"));
                    for (LastUpdateCartoonListRecord.Result result : dataList) {
                        anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                    }
                    anyItemList.add(new AnyItem(AnyItem.TYPE_FOOT, 0, null));
                    mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_white));
                }
            }
        }
        getCustomData();
    }

    public void getCustomData() {
        ObjectRequest<HotOrNewCartoonListRecord> response = new ObjectRequest<>(false, HotOrNewCartoonListRecord.Input.buildInput("24", "", "7"),
                this::addCustomData,
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                    getLastData();
                });
        NetClient.request(response);
    }

    private void addCustomData(HotOrNewCartoonListRecord hotOrNewCartoonListRecord) {
        if (null != hotOrNewCartoonListRecord && 1000 == hotOrNewCartoonListRecord.code) {
            if (null != hotOrNewCartoonListRecord.result && hotOrNewCartoonListRecord.result.size() > 0) {
                List<LastUpdateCartoonListRecord.Result> dataList = filterData(hotOrNewCartoonListRecord.result);
                if (dataList.size() > 0) {
                    anyItemList.add(new AnyItem(AnyItem.TYPE_HEADER, "经典作品"));
                    for (LastUpdateCartoonListRecord.Result result : dataList) {
                        anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                    }
                    anyItemList.add(new AnyItem(AnyItem.TYPE_FOOT, 4, null));
                    mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_white));
                }
            }
        }
        getLastData();
    }

    private void getLastData() {
        ObjectRequest<LastUpdateCartoonListRecord> response = new ObjectRequest<>(false, LastUpdateCartoonListRecord.Input.buildInput("24", ""),
                this::addLastData,
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                    getHotData();
                });
        NetClient.request(response);
    }

    private void addLastData(LastUpdateCartoonListRecord newCartoonListRecord) {
        if (null != newCartoonListRecord && 1000 == newCartoonListRecord.code) {
            if (null != newCartoonListRecord.result && newCartoonListRecord.result.size() > 0) {
                List<LastUpdateCartoonListRecord.Result> dataList = filterData(newCartoonListRecord.result);
                if (dataList.size() > 0) {
                    anyItemList.add(new AnyItem(AnyItem.TYPE_HEADER, "最近更新"));
                    for (LastUpdateCartoonListRecord.Result result : dataList) {
                        anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                    }
                    anyItemList.add(new AnyItem(AnyItem.TYPE_FOOT, 1, null));
                    mRecycleView.setBackgroundColor(getResources().getColor(R.color.cl_white));
                }
            }
        }
        getHotData();
    }

    private void finishFresh() {
        refreshLayout.finishRefresh();
    }

    private void getHotData() {
        ObjectRequest<HotOrNewCartoonListRecord> response = new ObjectRequest<>(false, HotOrNewCartoonListRecord.Input.buildInput("24", "", "2"),
                this::addHotData,
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                    getNewData();
                });
        NetClient.request(response);
    }

    private void addHotData(HotOrNewCartoonListRecord record) {
        if (null != record && 1000 == record.code) {
            if (null != record.result && record.result.size() > 0) {
                List<LastUpdateCartoonListRecord.Result> dataList = filterData(record.result);
                if (dataList.size() > 0) {
                    anyItemList.add(new AnyItem(AnyItem.TYPE_HEADER, "人气作品"));
                    for (LastUpdateCartoonListRecord.Result result : dataList) {
                        anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                    }
                    anyItemList.add(new AnyItem(AnyItem.TYPE_FOOT, 2, null));
                }
            }
        }
        getNewData();
    }

    private void getNewData() {
        ObjectRequest<HotOrNewCartoonListRecord> response = new ObjectRequest<>(false, HotOrNewCartoonListRecord.Input.buildInput("24", "", "1"),
                this::addNewData,
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                    finishAndNotify();
                });
        NetClient.request(response);
    }

    private void finishAndNotify() {
        finishFresh();
        myAdapter.notifyDataSetChanged();
    }

    private void addNewData(HotOrNewCartoonListRecord record) {
        if (null != record && 1000 == record.code) {
            if (null != record.result && record.result.size() > 0) {
                List<LastUpdateCartoonListRecord.Result> dataList = filterData(record.result);
                if (dataList.size() > 0) {
                    anyItemList.add(new AnyItem(AnyItem.TYPE_HEADER, "新作推荐"));
                    for (LastUpdateCartoonListRecord.Result result : dataList) {
                        anyItemList.add(new AnyItem(AnyItem.TYPE_ITEM, result));
                    }
                    anyItemList.add(new AnyItem(AnyItem.TYPE_FOOT, 3, null));
                }
            }
        }
        finishAndNotify();
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

    private void getAutorollData() {
        List<AnyObjectItem> items = new ArrayList<>();// Arrays.asList(new AnyObjectItem(),new AnyObjectItem(),new AnyObjectItem());
        items.add(new AnyObjectItem());
        items.add(new AnyObjectItem());
        items.add(new AnyObjectItem());
        items.add(new AnyObjectItem());
        AnyObjectItem anyItemObjectFirst = items.get(0);
        AnyObjectItem anyItemObjectLast = items.get(items.size() - 1);
        items.add(0, anyItemObjectLast);
        items.add(anyItemObjectFirst);
        arl.setItems(items);//给轮播图设置数据
        arl.setAllowAutoRoll(true);//是否自滚动
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(int index) {

    }

    static class AnyItem {
        static final int TYPE_HEADER = 0;
        static final int TYPE_ITEM = 1;
        static final int TYPE_FOOT = 2;
        static final int TYPE_NO_NET = 3;
        public int type;
        public int footType;
        public Object object;

        AnyItem(int type, Object object) {
            this.type = type;
            this.object = object;
        }

        public AnyItem(int type, int footType, Object object) {
            this.type = type;
            this.footType = footType;
            this.object = object;
        }
    }

//    public class MyAdapter extends RecyclerView.Adapter {
//        private int itemW = (ScreenUtil.getScreenWid() - UIUtil.dip2px(44)) / 3;
//        private int itemH = (int) (itemW / 0.761);
//
//        @Override
//        public int getItemCount() {
//            return null == anyItemList ? 0 : anyItemList.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return anyItemList.get(position).type;
//        }
//
//        //让头图占据一格的位置
//        @Override
//        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//            super.onAttachedToRecyclerView(recyclerView);
//            final GridLayoutManager gridManager = ((GridLayoutManager) recyclerView.getLayoutManager());
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    if (getItemViewType(position) == TYPE_HEADER ||
//                            getItemViewType(position) == TYPE_FOOT ||
//                            getItemViewType(position) == TYPE_NO_NET) {
//                        return gridManager.getSpanCount();
//                    } else {
//                        return 1;
//                    }
//                }
//            });
//        }
//
//        @SuppressLint("InflateParams")
//        @Override
//        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            switch (viewType) {
//                case TYPE_HEADER:
//                    return new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.found_head_layout, null));
//                case TYPE_ITEM:
//                    return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_item, null));
//                case TYPE_NO_NET:
//                    return new ViewHolder3(LayoutInflater.from(parent.getContext()).inflate(R.layout.no_net_item, null));
//                case TYPE_FOOT:
//                    return new ViewHolder4(LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, null));
//                default:
//                    return null;
//            }
//        }
//
//        @SuppressLint("SetTextI18n")
//        @Override
//        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            AnyItem anyItem = anyItemList.get(position);
//            if (getItemViewType(position) == TYPE_HEADER) {
//                ViewHolder1 viewHolder1 = (ViewHolder1) holder;
//                /*RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder1.autoRollLayout.getLayoutParams();//設置轮播图的高度
//                int screenWidth = ScreenUtil.getScreenWid();//当前屏幕的宽度
//                layoutParams.height = (int) ((screenWidth - UIUtil.dip2px(24)) * 0.265);
//                viewHolder1.autoRollLayout.setLayoutParams(layoutParams);
//                viewHolder1.autoRollLayout.setOnItemClickListener(new AutoRollLayout.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(int index) {
//                        AnyItemObject anyItemObject = items.get(index);
//                        String hrefUrl = anyItemObject.getHrefUrl();
//                        if (!TextUtils.isEmpty(hrefUrl)) {
//                            MyDebugUtil.logTest(TAG, "链接为=" + hrefUrl);
////            hrefUrl = "http://blog.csdn.net/about_time/article/details/50583233";
//                            IntentUtil.startActivityWithTwoString(getActivity(), WebActivity.class, "url", hrefUrl, "title", anyItemObject.getTitle());
//                        } else {
//                            MyDebugUtil.toastTest("链接为空");
//                            MyDebugUtil.logTest(TAG, "链接为空");
//                        }
//                    }
//                });
//                //data
//                if (items.size() > 0) {
//                    viewHolder1.autoRollLayout.setItems(items);//给轮播图设置数据
//                    viewHolder1.autoRollLayout.setAllowAutoRoll(true);//是否自滚动
//                }*/
////                viewHolder1.holder.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
//                String title = (String) anyItem.object;
//                viewHolder1.tv_title.setText(title);
//                viewHolder1.rl_title.setOnClickListener(v -> {
//                    Class aClass = null;
//                    switch (title) {
//                        case "热门推荐":
//                            aClass = CityEmotionListActivity.class;
//                            break;
//                        case "最近更新":
//                            aClass = UpdateListActivity.class;
//                            break;
//                        case "人气作品":
//                            aClass = HotListActivity.class;
//                            break;
//                        case "经典作品":
//                            aClass = ClassicsListActivity.class;
//                            break;
//                        case "新作推荐":
//                            aClass = NewListActivity.class;
//                            break;
//                    }
//                    if (aClass == null) {
//                        return;
//                    }
//                    IntentUtil.startActivity(getActivity(), aClass);
//                });
//            } else if (getItemViewType(position) == TYPE_ITEM) {
//                ViewHolder2 cartoonHolder = (ViewHolder2) holder;
//                //view
//                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) cartoonHolder.ll_root.getLayoutParams();
//                layoutParams1.width = itemW;
//                cartoonHolder.ll_root.setLayoutParams(layoutParams1);
//                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) cartoonHolder.iv_cover.getLayoutParams();
//                layoutParams2.height = itemH;
//                cartoonHolder.iv_cover.setLayoutParams(layoutParams2);
//                //data
//                final LastUpdateCartoonListRecord.Result result = (LastUpdateCartoonListRecord.Result) anyItem.object;
//                if (!TextUtils.isEmpty(result.name)) {
//                    cartoonHolder.tv_name.setText(result.name);
//                }
//                if (!TextUtils.isEmpty(result.articleName)) {
//                    String name = result.articleName;
//                    if (name.startsWith("了")) {
//                        name = name.replace("了", "");
//                    }
//                    cartoonHolder.tv_num.setText("更新至" + name);
//                }
//                if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
//                    GlideUtil.load(getActivity(), result.imgUrl, R.drawable.ic_holder1, cartoonHolder.iv_cover);
//                } else {
//                    cartoonHolder.iv_cover.setImageResource(R.drawable.ic_holder1);
//                }
//                cartoonHolder.ll_root.setOnClickListener(v -> {
//                    EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                    IntentUtil.startActivity(getActivity(), CartoonInfoAndCatalogActivity.class);
//                });
//            } else if (getItemViewType(position) == TYPE_NO_NET) {
//                ViewHolder3 viewHolder3 = (ViewHolder3) holder;
//                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder3.ll_holder.getLayoutParams();
//                if (UIUtil.getDensity() > 2.8f) {
//                    layoutParams.topMargin = UIUtil.dip2px(110);
//                } else {
//                    layoutParams.topMargin = UIUtil.dip2px(150);
//                }
//                viewHolder3.ll_holder.setLayoutParams(layoutParams);
//                viewHolder3.tv_fresh.setOnClickListener(v -> initData());
//            } else if (getItemViewType(position) == TYPE_FOOT) {
//                ViewHolder4 viewHolder4 = (ViewHolder4) holder;
//                viewHolder4.tv_more.setOnClickListener(v -> {
//                    Class aClass = null;
//                    switch (anyItem.footType) {
//                        case 0:
//                            aClass = CityEmotionListActivity.class;
//                            break;
//                        case 1:
//                            aClass = UpdateListActivity.class;
//                            break;
//                        case 2:
//                            aClass = HotListActivity.class;
//                            break;
//                        case 3:
//                            aClass = NewListActivity.class;
//                            break;
//                        case 4:
//                            aClass = ClassicsListActivity.class;
//                            break;
//                    }
//                    if (aClass == null) {
//                        return;
//                    }
//                    IntentUtil.startActivity(getActivity(), aClass);
//                });
//            }
//        }
//    }

    private class MyAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
        private int itemW = (ScreenUtil.getScreenWid() - UIUtil.dip2px(44)) / 3;
        private int itemH = (int) (itemW / 0.761);

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return AnyItem.TYPE_HEADER == viewType;
        }

        @Override
        public int getCount() {
            return null == anyItemList ? 0 : anyItemList.size();
        }

        @Override
        public AnyItem getItem(int i) {
            return anyItemList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            switch (getItemViewType(i)) {
                case AnyItem.TYPE_HEADER: {
                    ViewHolder1 viewHolder1;
                    if (view == null) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.found_head_layout, null);
                        viewHolder1 = new ViewHolder1(view);
                    } else {
                        viewHolder1 = (ViewHolder1) view.getTag();
                    }
                    String title = (String) getItem(i).object;
                    viewHolder1.tv_title.setText(title);
                    viewHolder1.rl_title.setOnClickListener(v -> {
                        Class aClass = null;
                        switch (title) {
                            case "热门推荐":
                                aClass = CityEmotionListActivity.class;
                                break;
                            case "最近更新":
                                aClass = UpdateListActivity.class;
                                break;
                            case "人气作品":
                                aClass = HotListActivity.class;
                                break;
                            case "经典作品":
                                aClass = ClassicsListActivity.class;
                                break;
                            case "新作推荐":
                                aClass = NewListActivity.class;
                                break;
                        }
                        if (aClass == null) {
                            return;
                        }
                        IntentUtil.startActivity(getActivity(), aClass);
                    });
                    break;
                }
                case AnyItem.TYPE_ITEM: {
                    ViewHolder2 viewHolder2;
                    if (view == null) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cartoon_item, null);
                        viewHolder2 = new ViewHolder2(view);
                    } else {
                        viewHolder2 = (ViewHolder2) view.getTag();
                    }
                    //view
                    FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) viewHolder2.ll_root.getLayoutParams();
                    layoutParams1.width = itemW;
                    viewHolder2.ll_root.setLayoutParams(layoutParams1);
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) viewHolder2.iv_cover.getLayoutParams();
                    layoutParams2.height = itemH;
                    viewHolder2.iv_cover.setLayoutParams(layoutParams2);
                    //data
                    final LastUpdateCartoonListRecord.Result result = (LastUpdateCartoonListRecord.Result) getItem(i).object;
                    if (!TextUtils.isEmpty(result.name)) {
                        viewHolder2.tv_name.setText(result.name);
                    }
                    if (!TextUtils.isEmpty(result.articleName)) {
                        String name = result.articleName;
                        if (name.startsWith("了")) {
                            name = name.replace("了", "");
                        }
                        viewHolder2.tv_num.setText("更新至" + name);
                    }
                    if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
                        GlideUtil.load(getActivity(), result.imgUrl, R.drawable.ic_holder1, viewHolder2.iv_cover);
                    } else {
                        viewHolder2.iv_cover.setImageResource(R.drawable.ic_holder1);
                    }
                    viewHolder2.ll_root.setOnClickListener(v -> {
//                        EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//                        IntentUtil.startActivity(getActivity(), CartoonInfoAndCatalogActivity.class);
                        IntentUtil.startRead(getActivity(), result);
                    });
                    break;
                }
                case AnyItem.TYPE_FOOT: {
                    ViewHolder3 viewHolder3;
                    if (view == null) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.load_more, null);
                        viewHolder3 = new ViewHolder3(view);
                    } else {
                        viewHolder3 = (ViewHolder3) view.getTag();
                    }
                    break;
                }
                case AnyItem.TYPE_NO_NET: {
                    ViewHolder4 viewHolder4;
                    if (view == null) {
                        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.no_net_item, null);
                        viewHolder4 = new ViewHolder4(view);
                    } else {
                        viewHolder4 = (ViewHolder4) view.getTag();
                    }
                    break;
                }
            }
            return view;
        }
    }

    private static class ViewHolder1 {
        @ViewInject(id = R.id.arl)
        AutoRollLayout autoRollLayout;
        @ViewInject(id = R.id.rl_title)
        RelativeLayout rl_title;
        @ViewInject(id = R.id.tv_title)
        TextView tv_title;

        ViewHolder1(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    public static class ViewHolder2 {
        @ViewInject(id = R.id.ll_root)
        LinearLayout ll_root;
        @ViewInject(id = R.id.iv_cover)
        ImageView iv_cover;
        @ViewInject(id = R.id.tv_name)
        TextView tv_name;
        @ViewInject(id = R.id.tv_num)
        TextView tv_num;

        ViewHolder2(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    public static class ViewHolder3 {
        @ViewInject(id = R.id.ll_holder)
        LinearLayout ll_holder;
        @ViewInject(id = R.id.tv_fresh)
        TextView tv_fresh;

        ViewHolder3(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    public static class ViewHolder4 {
        @ViewInject(id = R.id.tv_more)
        TextView tv_more;

        ViewHolder4(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }
}
