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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.my.mymh.R;
import com.my.mymh.base.BaseApplication;
import com.my.mymh.db.bean.RackBean;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.dao.CartoonRackDao;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.model.record.CatalogListRecord;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.share.ShareOutDialogUtil;
import com.my.mymh.ui.base.BaseActivity;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.ui.cartoonDetailFragment.CartoonCatalogFragment;
import com.my.mymh.ui.cartoonDetailFragment.CommentListFragment;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.JudgeNetUtil;
import com.my.mymh.util.ScreenUtil;
import com.my.mymh.util.ShareListener;
import com.my.mymh.util.ShareUtil;
import com.my.mymh.util.TrimUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.glideTrans.trans.MyBlurTransformation;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.tencent.connect.common.Constants.REQUEST_QQ_SHARE;
import static com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE;

/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/11.
 */

public class CartoonInfoAndCatalogPreActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "CartoonInfoActivity";
    private List<String> STRING_LIST = Arrays.asList("详情", "目录");
    private List<? extends BaseFragment> FRAGMENT_LIST;// = Arrays.asList(new CartoonCatalogFragment(), new CommentListFragment());
    @ViewInject(id = R.id.tv_tab1, needClick = true)
    private TextView tv_tab1;
    @ViewInject(id = R.id.tv_tab2, needClick = true)
    private TextView tv_tab2;
    @ViewInject(id = R.id.view_indicator)
    private View view_indicator;
    @ViewInject(id = R.id.view_pager)
    private ViewPager view_pager;
    @ViewInject(id = R.id.iv_header)
    private ImageView iv_header;
    @ViewInject(id = R.id.view_cover)
    private View view_cover;
    @ViewInject(id = R.id.iv_header1)
    private ImageView iv_header1;
    @ViewInject(id = R.id.tv_sub_title)
    private TextView tv_sub_title;
    @ViewInject(id = R.id.tv_title)
    private TextView tv_title;
    @ViewInject(id = R.id.tv_sub_time)
    private TextView tv_sub_time;
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventResultData(EventBusEvent.CartoonInfoStickyEvent cartoonInfoStickyEvent) {
        LastUpdateCartoonListRecord.Result result = cartoonInfoStickyEvent.result;
        if (null != result) {
            this.result = result;
            MyDebugUtil.log(TAG, "id=" + result.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void showHeader() {
        if (!TextUtils.isEmpty(result.imgUrl) && result.imgUrl.contains("http")) {
            Glide.with(this).load(result.imgUrl).placeholder(R.drawable.ic_holder1).bitmapTransform(new MyBlurTransformation(this, 5)).into(iv_header);
            GlideUtil.load(this, result.imgUrl, R.drawable.ic_holder1, iv_header1);
        } else {
            iv_header.setImageResource(R.drawable.ic_holder1);
        }
        if (!TextUtils.isEmpty(result.name)) {
            setTitleName(TrimUtil.trim(result.name));
            tv_title.setText(TrimUtil.trim(result.name));
            if (!TextUtils.isEmpty(result.name) && result.name.length() > 14) {
                tv_title.setSelected(true);
            } else {
                tv_title.setSelected(false);
            }
        }
        if (!TextUtils.isEmpty(result.lzStatus)) {
            tv_sub_title.setText("状态 : " + ("2".equals(result.lzStatus) ? "完结" : "连载"));
        }
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
        outState.putInt("index", tv_tab1.isSelected() ? 0 : 1);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setViewWithTheme(APP_THEME.RED_THEME, R.layout.cartoon_info_catalog_pre_activity);
        init();
        initView(bundle);
        initData();
    }

    private void initData() {
        showHeader();
        showRecord();
        ObjectRequest<CatalogListRecord> response = new ObjectRequest<>(false, CatalogListRecord.Input.buildInput(result.id),
                catalogListRecord -> {
                    if (null != catalogListRecord && 1000 == catalogListRecord.code) {
                        if (null != catalogListRecord.result && catalogListRecord.result.size() > 0) {
                            List<CatalogListRecord.Result> dataList = filterData(catalogListRecord.result);
                            if (dataList.size() > 0) {
                                CartoonInfoAndCatalogPreActivity.this.size = dataList.size();
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

    private void init() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iv_header.getLayoutParams();
        layoutParams.height = 5 * ScreenUtil.getScreenHei() / 24;
        iv_header.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) view_cover.getLayoutParams();
        layoutParams3.height = 5 * ScreenUtil.getScreenHei() / 24;
        view_cover.setLayoutParams(layoutParams3);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) iv_header1.getLayoutParams();
        layoutParams1.height = 5 * ScreenUtil.getScreenHei() / 24 - UIUtil.dip2px(30);
        layoutParams1.width = (int) ((5 * ScreenUtil.getScreenHei() / 24 - UIUtil.dip2px(30)) * 0.75);
        iv_header1.setLayoutParams(layoutParams1);
        cartoonReadPositionDao = new CartoonReadPositionDao(this);
        cartoonRackDao = new CartoonRackDao(this);
        EventBus.getDefault().register(this);
        //分享
        shareListener = new ShareListener();
        setRightClickViews(Collections.singletonList(R.drawable.share_selector), position -> share());
    }

    private void share() {
        ShareOutDialogUtil.showDialog(this, false, position -> {
            if (null == result) {
                MyDebugUtil.toast("请重试~");
                return;
            }
            if (!JudgeNetUtil.hasNet(CartoonInfoAndCatalogPreActivity.this)) {
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
                    ShareUtil.shareToQQ(2, BaseApplication.mTencent, shareListener, CartoonInfoAndCatalogPreActivity.this, logoUrl, shareUrl, title, content);
                    break;
                case 1://qq朋友圈
                    ShareUtil.shareToQZone(2, BaseApplication.mTencent, shareListener, CartoonInfoAndCatalogPreActivity.this, logoUrl, shareUrl, title, content);
                    break;
                case 2://微信好友
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 0, CartoonInfoAndCatalogPreActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    break;
                case 3://微信朋友圈
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 1, CartoonInfoAndCatalogPreActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    break;
            }
        });
    }

    private void initView(Bundle bundle) {
        FRAGMENT_LIST = Arrays.asList(new CartoonCatalogFragment(), new CommentListFragment());
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        view_pager.setAdapter(myAdapter);
        if (bundle != null) {
            int index = bundle.getInt("index");
            selTab(index);
            if (index != view_pager.getCurrentItem()) {
                view_pager.setCurrentItem(index, false);
            }
        } else {
            selTab(0);
        }
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view_indicator.getLayoutParams();
                params.leftMargin = (int) (positionOffset * UIUtil.dip2px(160)) + position * UIUtil.dip2px(160);
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_tab1 || v.getId() == R.id.tv_tab2) {
            boolean isTab1 = v.getId() == R.id.tv_tab1;
            view_pager.setCurrentItem(isTab1 ? 0 : 1);
        } else if (v.getId() == R.id.tv_read) {
            //存书架
            RackBean rackBean = cartoonRackDao.query(result.id);
            if (null == rackBean) {
                cartoonRackDao.add(new RackBean(result.id, GsonUtil.getGson().toJson(result), System.currentTimeMillis() + ""));
            }
            //跳转
            if (size != 0) {
                IntentUtil.startActivityWithSixString(CartoonInfoAndCatalogPreActivity.this, CartoonReadActivity.class,
                        "type", "1",
                        "cartoonId", result.id,
                        "articleId", "",
                        "currentIndex", "",
                        "size", size + "",
                        "name", result.name + "");
            } else {
                MyDebugUtil.toast("访问异常");
            }
        }
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

        @Override
        public CharSequence getPageTitle(int position) {
            return STRING_LIST.get(position);
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
        EventBus.getDefault().removeStickyEvent(EventBusEvent.class);
        EventBus.getDefault().unregister(this);
    }
}
