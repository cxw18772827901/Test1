package com.my.mymh.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my.mymh.R;
import com.my.mymh.base.BaseApplication;
import com.my.mymh.db.bean.RackBean;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.dao.CartoonRackDao;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.model.record.CatalogListRecord;
import com.my.mymh.model.record.CollectIdsRecord;
import com.my.mymh.model.record.CollectOnlyRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.share.ShareOutDialogUtil;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.ui.cartoonDetailFragment.CartoonCatalogFragment;
import com.my.mymh.ui.cartoonDetailFragment.CommentListFragment;
import com.my.mymh.ui.cartoonDetailFragment.CommonListFragment;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.JudgeNetUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.ShareListener;
import com.my.mymh.util.ShareUtil;
import com.my.mymh.util.SignJumpUtil;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.glideTrans.trans.MyBlurTransformation;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.tencent.connect.common.Constants.REQUEST_QQ_SHARE;
import static com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/11.
 */

public class CartoonInfoAndCatalogActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "CartoonInfoActivity";
    //private List<String> STRING_LIST = Arrays.asList("详情", "目录");
    private List<? extends BaseFragment> FRAGMENT_LIST;/*= Arrays.asList(CommentListFragment.getInstance(), CartoonCatalogFragment.getInstance());*/
    @ViewInject(id = R.id.tv_tab1, needClick = true)
    private TextView tv_tab1;
    @ViewInject(id = R.id.tv_tab2, needClick = true)
    private TextView tv_tab2;
    @ViewInject(id = R.id.tv_tab3, needClick = true)
    private TextView tv_tab3;
    @ViewInject(id = R.id.view_indicator)
    private View view_indicator;
    @ViewInject(id = R.id.view_pager)
    private ViewPager view_pager;
    @ViewInject(id = R.id.iv_header)
    private ImageView iv_header;
    @ViewInject(id = R.id.iv_header1)
    private ImageView iv_header1;
    @ViewInject(id = R.id.tv_sub_title)
    private TextView tv_sub_title;
    @ViewInject(id = R.id.tv_title)
    private TextView tv_title;
    @ViewInject(id = R.id.tv_sub_time)
    private TextView tv_sub_time;
    @ViewInject(id = R.id.tv_back_arrow, needClick = true)
    private TextView tv_back_arrow;
    //    @ViewInject(id = R.id.tv_title_center)
//    private TextView tv_title_center;
    @ViewInject(id = R.id.iv_right, needClick = true)
    private ImageView iv_right;
    @ViewInject(id = R.id.iv_right1, needClick = true)
    private ImageView iv_right1;
    @ViewInject(id = R.id.fl_right2, needClick = true)
    private FrameLayout fl_right2;
    @ViewInject(id = R.id.tv_right2)
    private TextView tv_right2;
    @ViewInject(id = R.id.tv_read_title)
    private TextView tv_read_title;
    @ViewInject(id = R.id.tv_read, needClick = true)
    private TextView tv_read;
    public LastUpdateCartoonListRecord.Result result;
    private int size;
    private CartoonReadPositionDao cartoonReadPositionDao;
    private CartoonRackDao cartoonRackDao;
    private ReadPositionBean cartoonReadBean;
    private ShareListener shareListener;

    //    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventResultData(/*EventBusEvent.CartoonInfoStickyEvent cartoonInfoStickyEvent*/) {
//        LastUpdateCartoonListRecord.Result result = cartoonInfoStickyEvent.result;
//        if (null != result) {
//            this.result = result;
//            MyDebugUtil.log(TAG, "id=" + result.toString());
//        }
        this.result = (LastUpdateCartoonListRecord.Result) getIntent().getSerializableExtra("result");
    }

    @SuppressLint("SetTextI18n")
    private void showHeader() {
        if (result == null) {
            return;
        }
        if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
            Glide.with(this).load(result.imgUrl).placeholder(R.drawable.ic_holder1).bitmapTransform(new MyBlurTransformation(this, 5)).into(iv_header);
            GlideUtil.load(this, result.imgUrl, R.drawable.ic_holder1, iv_header1);
        } else {
            iv_header.setImageResource(R.drawable.ic_holder1);
        }
        if (!TextUtils.isEmpty(result.name)) {
//            setTitleName(TrimUtil.trim(result.name));
//            tv_title_center.setText(TrimUtil.trim(result.name));
//            if (!TextUtils.isEmpty(result.name) && result.name.length() > 14) {
//                tv_title_center.setSelected(true);
//            } else {
//                tv_title_center.setSelected(false);
//            }
            tv_title.setText(TrimUtil.trim(result.name));
        }
        String lzStatues;
        if (!TextUtils.isEmpty(result.lzStatus)) {
            lzStatues = "状态：" + ("2".equals(result.lzStatus) ? "完结" : "连载");
        } else {
            lzStatues = "状态：连载";
        }
        tv_sub_title.setText(lzStatues);
        if (!TextUtils.isEmpty(result.updateTime)) {
            String time = result.updateTime;
            if (result.updateTime.contains(" ")) {
                time = time.split(" ")[0];
            }
            tv_sub_time.setText(time);
        }
    }

    private void showRecord() {
        cartoonReadBean = cartoonReadPositionDao.query(result.id);
        if (null != cartoonReadBean) {
            tv_read.setSelected(false);
            tv_read.setText("继续阅读");
            String title = cartoonReadBean.articleName;
            if (!TextUtils.isEmpty(title)) {
                tv_read_title.setText(title);
                if (!TextUtils.isEmpty(title) && title.length() > 14) {
                    tv_read_title.setSelected(true);
                } else {
                    tv_read_title.setSelected(false);
                }
            }
        } else {
            tv_read.setSelected(true);
            tv_read.setText("开始阅读");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int tabNum;
        if (tv_tab1.isSelected()) {
            tabNum = 0;
        } else if (tv_tab2.isSelected()) {
            tabNum = 1;
        } else {
            tabNum = 2;
        }
        outState.putInt("index", tabNum);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setViewWithTheme(APP_THEME.NONE_THEME, R.layout.cartoon_info_catalog_activity);
        onEventResultData();
        init();
        initView(bundle);
        initData();
    }

    private void init() {
        //title
        TitleBarUtil.initImmersiveBar(this, R.color.cl_no_color);
        cartoonReadPositionDao = new CartoonReadPositionDao(this);
        cartoonRackDao = new CartoonRackDao(this);
//        EventBus.getDefault().register(this);
        //分享
        shareListener = new ShareListener();
    }

    private void share() {
        ShareOutDialogUtil.showDialog(this, false, position -> {
            if (null == result) {
                MyDebugUtil.toast("请重试~");
                return;
            }
            if (!JudgeNetUtil.hasNet(CartoonInfoAndCatalogActivity.this)) {
                MyDebugUtil.toast("请连接网络后重试~");
                return;
            }
            String logoUrl = "https://mmbiz.qlogo.cn/mmbiz_png/RTMaDfovwlq1whwR5Tm1O5qbJWogsmnaNmbStotJvGVsSzqcJEynicITDLmUS52AEDAEeRofic7etdgn9icwB1vmA/0?wx_fmt=png";// Constants.QINIU_GET_ICON_HEADER_BASE + "share_logo.png";//分享用logo
//            String shareUrl = "http://manga.zishupai.cn/share/share.html"; //分享的链接
            String shareUrl = "http://post.zishupai.cn/c/share.html"; //分享的链接
            String title = "快和我一起看《" + result.name + "》漫画！";//分享的标题
            String content = "我正在看《" + result.name + "》，看漫画就到咔米漫画，更新快、漫画全，小伙伴们一起来看吧！";//描述
            switch (position) {
                case 0://qq好友
                    ShareUtil.shareToQQ(2, BaseApplication.mTencent, shareListener, CartoonInfoAndCatalogActivity.this, logoUrl, shareUrl, title, content);
                    break;
                case 1://qq朋友圈
                    ShareUtil.shareToQZone(2, BaseApplication.mTencent, shareListener, CartoonInfoAndCatalogActivity.this, logoUrl, shareUrl, title, content);
                    break;
                case 2://微信好友
//                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 0, CartoonInfoAndCatalogActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    tiMini();
                    break;
                case 3://微信朋友圈
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 1, CartoonInfoAndCatalogActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    break;
            }
        });
    }

    private void tiMini() {
        if (result != null) {
            Map<String, String> map = new HashMap<>();
            map.put("id", result.id);
            map.put("name", result.name);
            map.put("imgUrl", result.imgUrl);
            map.put("updateTime", result.updateTime);
            map.put("articleName", result.articleName);
            map.put("author", result.author);
            map.put("lzStatus", result.lzStatus);
            map.put("longDesc", result.longDesc);
            SignJumpUtil.openMiniCartoon(map);
        }
    }

    private void initView(Bundle bundle) {
        FRAGMENT_LIST = Arrays.asList(new CommonListFragment(), new CommentListFragment(), new CartoonCatalogFragment());
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        view_pager.setAdapter(myAdapter);
        if (bundle != null) {
            int index = bundle.getInt("index");
            selTab(index);
            if (index != view_pager.getCurrentItem()) {
                view_pager.setCurrentItem(index, false);
            }
        } else {
            selTab(2);
            view_pager.setCurrentItem(2, false);
        }
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_indicator.getLayoutParams();
                params.leftMargin = (int) (positionOffset * UIUtil.dip2px(100)) + position * UIUtil.dip2px(100);
                view_indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                selTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void selTab(int index) {
        tv_tab1.setSelected(0 == index);
        tv_tab2.setSelected(1 == index);
        tv_tab3.setSelected(2 == index);
    }

    private void initData() {
        showHeader();
        showRecord();
        showTitle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LoginVerifyUtil.isLogin()) {
            showHasCollected();
        }
    }

    private boolean hasColl = false;

    private void showHasCollected() {
        ObjectRequest<CollectIdsRecord> response = new ObjectRequest<>(false, CollectIdsRecord.Input.buildInput(),
                idsRecord -> {
                    if (idsRecord != null && idsRecord.code == 1000 && idsRecord.result != null) {
                        if (idsRecord.result.contains(result.id)) {
                            hasColl = true;
                            iv_right1.setSelected(true);
                        }
                    } else if (idsRecord != null && (idsRecord.code == 3006 || idsRecord.code == 3007)) {
                        LoginVerifyUtil.login();
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    } else if (idsRecord != null && idsRecord.code != 1001) {
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    }
                },
                volleyError -> MyDebugUtil.toast("操作失败，请稍后重试~"));
        NetClient.request(response);
    }

    private void showTitle() {
        ObjectRequest<CatalogListRecord> response = new ObjectRequest<>(false, CatalogListRecord.Input.buildInput(result.id),
                catalogListRecord -> {
                    if (null != catalogListRecord && 1000 == catalogListRecord.code) {
                        if (null != catalogListRecord.result && catalogListRecord.result.size() > 0) {
                            List<CatalogListRecord.Result> dataList = filterData(catalogListRecord.result);
                            if (dataList.size() > 0) {
                                CartoonInfoAndCatalogActivity.this.size = dataList.size();
                                String firstTitle = dataList.get(dataList.size() - 1).articleName;
                                if (null == cartoonReadBean) {
                                    tv_read.setSelected(true);
                                    tv_read.setText("开始阅读");
                                    tv_read_title.setText(firstTitle);
                                    if (!TextUtils.isEmpty(firstTitle) && firstTitle.length() > 14) {
                                        tv_read_title.setSelected(true);
                                    } else {
                                        tv_read_title.setSelected(false);
                                    }
                                }
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

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (v.getId() == R.id.tv_tab1 || v.getId() == R.id.tv_tab2 || v.getId() == R.id.tv_tab3) {
//            boolean isTab1 = v.getId() == R.id.tv_tab1;
//            view_pager.setCurrentItem(isTab1 ? 0 : 1);
            int tabNum;
            if (v.getId() == R.id.tv_tab1) {
                tabNum = 0;
            } else if (v.getId() == R.id.tv_tab2) {
                tabNum = 1;
            } else {
                tabNum = 2;
            }
            view_pager.setCurrentItem(tabNum);
        } else if (viewId == R.id.tv_read) {
            //存书架
            RackBean rackBean = cartoonRackDao.query(result.id);
            if (null == rackBean) {
                cartoonRackDao.add(new RackBean(result.id, GsonUtil.getGson().toJson(result), System.currentTimeMillis() + ""));
            }
            //跳转
            if (size != 0) {
                IntentUtil.startActivityWithSixString(CartoonInfoAndCatalogActivity.this, CartoonReadActivity.class,
                        "type", "1",
                        "cartoonId", result.id,
                        "articleId", "",
                        "currentIndex", "",
                        "size", size + "",
                        "name", result.name + "");
            } else {
                MyDebugUtil.toast("访问异常");
            }
        } else if (viewId == R.id.tv_back_arrow) {
            finish();
        } else if (viewId == R.id.iv_right) {
            share();
        } else if (viewId == R.id.iv_right1) {
            if (LoginVerifyUtil.isLogin()) {
                collectOrNot(hasColl ? 2 : 1);
            } else {
                IntentUtil.startActivity(this, LoginActivity.class);
            }
        } else if (viewId == R.id.fl_right2) {
            IntentUtil.startActivityWithTwoString(this, CartoonUsersActivity.class,
                    "name", result.name, "id", result.id);
        }
    }

    private void collectOrNot(int operate) {
        ObjectRequest<CollectOnlyRecord> response = new ObjectRequest<>(false, CollectOnlyRecord.Input.buildInput(result.id, operate + ""),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
                        EventBus.getDefault().post(new EventBusEvent.CollectEvent(EventBusEvent.CollectEvent.TYPE_FRESH));
                        String info;
                        if (operate == 1) {
                            info = "收藏成功~";
                            hasColl = true;
                        } else {
                            info = "取消收藏成功~";
                            hasColl = false;
                        }
                        iv_right1.setSelected(hasColl);
                        MyDebugUtil.toast(info);
                    } else if (collectRecord != null && (collectRecord.code == 3006 || collectRecord.code == 3007)) {
                        LoginVerifyUtil.login();
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    } else {
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                });
        NetClient.request(response);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return FRAGMENT_LIST.get(position);
        }

        @Override
        public int getCount() {
            return FRAGMENT_LIST.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QQ_SHARE || requestCode == REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareOutDialogUtil.close();
//        EventBus.getDefault().removeStickyEvent(EventBusEvent.class);
//        EventBus.getDefault().unregister(this);
        hasColl = false;
    }
}
