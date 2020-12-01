package com.my.mymh.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.my.mymh.R;
import com.my.mymh.base.BaseApplication;
import com.my.mymh.base.Constants;
import com.my.mymh.db.bean.ReadPositionBean;
import com.my.mymh.db.dao.CartoonReadPositionDao;
import com.my.mymh.db.dao.SignDao;
import com.my.mymh.model.CartoonPicItem;
import com.my.mymh.model.UpdateBean;
import com.my.mymh.model.record.ChapterRecord;
import com.my.mymh.model.record.CollectIdsRecord;
import com.my.mymh.model.record.CollectRecord;
import com.my.mymh.model.record.HastSignedRecord;
import com.my.mymh.model.record.NextChapterRecord;
import com.my.mymh.model.record.ReportErrorRecord;
import com.my.mymh.model.record.UpdadteProgressRecord;
import com.my.mymh.request.volley.NetClient;
import com.my.mymh.request.volley.ObjectRequest;
import com.my.mymh.share.ShareInDialogUtil;
import com.my.mymh.share.VerifyDialog;
import com.my.mymh.util.CartoonUtil;
import com.my.mymh.util.CollectDialogUtil;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.util.JudgeNetUtil;
import com.my.mymh.util.LoginVerifyUtil;
import com.my.mymh.util.ReadChapterCountUtil;
import com.my.mymh.util.ReadDialog;
import com.my.mymh.util.SPUtil;
import com.my.mymh.util.ScreenUtil;
import com.my.mymh.util.ShareListener;
import com.my.mymh.util.ShareUtil;
import com.my.mymh.util.SignDialogUtil;
import com.my.mymh.util.SignOkDialogUtil;
import com.my.mymh.util.StatusBarHeightUtil;
import com.my.mymh.util.SystemBarUtils;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.gson.GsonUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.util.inject.ViewInjectUtil;
import com.my.mymh.wedjet.MyScrollListener;
import com.my.mymh.wedjet.ZoomAdvancedListView;
import com.tencent.tauth.Tencent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * PackageName  com.dave.project.ui
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/16.
 * 图片固定格式为header_index.jpg，index从1到章节图片的总count
 */

public class CartoonReadPreActivity extends /*TuCaoDownloadActivity*/AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "CartoonReadActivity";
    @ViewInject(id = R.id.listview)
    private ZoomAdvancedListView listview;
    //head
    @ViewInject(id = R.id.title_view)
    private FrameLayout title_view;
    @ViewInject(id = R.id.tv_title_back, needClick = true)
    private TextView tv_title_back;
    @ViewInject(id = R.id.tv_title_title)
    private TextView tv_title_title;
    @ViewInject(id = R.id.iv_title_right_home, needClick = true)
    private ImageView iv_title_right;
    @ViewInject(id = R.id.iv_title_right_share, needClick = true)
    private ImageView iv_title_right_2;
    //foot
    @ViewInject(id = R.id.ll_footer)
    private LinearLayout ll_footer;
    //    @ViewInject(id = R.id.progressBar)
//    private ProgressBar progressBar;
    @ViewInject(id = R.id.tv_pre, needClick = true)
    private ImageView tv_pre;
    @ViewInject(id = R.id.tv_change, needClick = true)
    private ImageView tv_change;
    @ViewInject(id = R.id.tv_catalog, needClick = true)
    private ImageView tv_catalog;
    @ViewInject(id = R.id.tv_next, needClick = true)
    private ImageView tv_next;
    private CartoonReadPositionDao cartoonReadPositionDao;
    private String cartoonId;//卡通id
    private String articleId;//章节id
    private int catalogIndex;//章节索引
    private List<CartoonPicItem> totalItemList = new ArrayList<>();
    private List<CartoonPicItem> showItemList = new ArrayList<>();
    private MyAdapter myAdapter;
    private String size;
    private boolean isShow = true;
    private ShareListener shareListener;
    private String cartoonName;
    private String titleName;
    private int scrollTag = -1;

    private void showSystemBar() {
        //显示
        if (!StatusBarHeightUtil.isFullScreen()) {
            SystemBarUtils.showUnStableStatusBar(this);
        }
//        if (isFullScreen) {
//            SystemBarUtils.showUnStableNavBar(this);
//        }
    }

    private void hideSystemBar() {
        //隐藏
        if (!StatusBarHeightUtil.isFullScreen()) {
            SystemBarUtils.hideStableStatusBar(this);
        }
//        if (isFullScreen) {
//            SystemBarUtils.hideStableNavBar(this);
//        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cartoon_activity);
        init();
        initView();
        checkNet();
//        initData();
    }

    //    private List<String> isdListColl;
    private boolean hasColl = false;

    private void initData() {
        ObjectRequest<CollectIdsRecord> response = new ObjectRequest<>(false, CollectIdsRecord.Input.buildInput(),
                idsRecord -> {
                    if (idsRecord != null && idsRecord.code == 1000 && idsRecord.result != null) {
//                        isdListColl = idsRecord.result;
                        if (idsRecord.result.contains(cartoonId)) {
                            hasColl = true;
                        }
                    } else if (idsRecord != null && (idsRecord.code == 3006 || idsRecord.code == 3007)) {
                        LoginVerifyUtil.login();
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    } else {
                        MyDebugUtil.toastTest("操作失败，请稍后重试~");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                });
        NetClient.request(response);
    }

    private void init() {
//        TitleBarUtil.initImmersiveBar(this, R.color.cl_black);
        TitleBarUtil.initImmersiveBar(this, R.color.cl_no_color);
        ViewInjectUtil.initInActivity(this);
        cartoonReadPositionDao = new CartoonReadPositionDao(this);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        cartoonId = intent.getStringExtra("cartoonId");
        size = intent.getStringExtra("size");
        cartoonName = intent.getStringExtra("name");
        if ("0".equals(type)) {//章节跳转
            articleId = intent.getStringExtra("articleId");
            catalogIndex = Integer.parseInt(intent.getStringExtra("currentIndex"));
            titleName = intent.getStringExtra("titleName");
        } else {//数据库查询
            ReadPositionBean cartoonReadBean = cartoonReadPositionDao.query(cartoonId);
            if (null != cartoonReadBean) {
                articleId = cartoonReadBean.articleId;
                titleName = cartoonReadBean.articleName;
                catalogIndex = Integer.parseInt(cartoonReadBean.currentIndex);
            } else {
                articleId = "";
                catalogIndex = 1;
            }
        }
        shareListener = new ShareListener();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        myAdapter = new MyAdapter();
        listview.setAdapter(myAdapter);
        listview.setOnTriggerListener(new ZoomAdvancedListView.OnTriggerListener() {
            @Override
            public void triggerFresh() {
                scrollTag = 0;
                MyDebugUtil.log(TAG, "fresh");
                if (showItemList.size() == 0) {
                    MyDebugUtil.toast("访问失败");
                    return;
                }
                boolean hasData = CartoonUtil.hasData(totalItemList, showItemList, false);
                if (hasData) {
                    listview.isIdel = true;
                    int posPre = showItemList.size();
                    showItemList = CartoonUtil.addData(totalItemList, showItemList, false);
                    myAdapter.notifyDataSetChanged();
                    int addedPos = showItemList.size() - posPre;
                    listview.setSelection(addedPos - 1);
                } else {
                    int firstVisiblePosition = listview.getFirstVisiblePosition();
                    if (firstVisiblePosition < 0) {
                        MyDebugUtil.toast("访问失败");
                        return;
                    }
                    int catalogIndex = showItemList.get(firstVisiblePosition).catalogIndex;
                    if (catalogIndex > 1) {
                        getPreOrNextData(-1);
                    } else {
                        MyDebugUtil.toast("已经是第一章了");
                        listview.isIdel = true;
                    }
                }
            }

            @Override
            public void triggerLoad() {
                scrollTag = 1;
                MyDebugUtil.log(TAG, "load");
                MyDebugUtil.log("timeeee", "end=" + System.currentTimeMillis() / 1000);
                if (showItemList.size() == 0) {
                    MyDebugUtil.toast("访问失败");
                    return;
                }
                boolean hasData = CartoonUtil.hasData(totalItemList, showItemList, true);
                if (hasData) {
                    listview.isIdel = true;
//                    int pos = showItemList.size();
                    showItemList = CartoonUtil.addData(totalItemList, showItemList, true);
                    myAdapter.notifyDataSetChanged();
//                    listview.setSelection(pos);
                } else {
                    int lastVisiblePosition = listview.getLastVisiblePosition();
                    if (-1 == lastVisiblePosition) {
                        return;
                    }
                    int catalogIndex = showItemList.get(lastVisiblePosition).catalogIndex;
                    if (catalogIndex < Integer.parseInt(size)) {
                        getPreOrNextData(1);
                    } else {
                        MyDebugUtil.toast("已经是最后一章了");
                        listview.isIdel = true;
                    }
                }
            }
        });
        listview.setOnItemClickListener((parent, view, position, id) -> {
            //显示隐藏header和footer
            if (isShow) {
                isShow = false;
                hideOrShow(0);
                hideSystemBar();
            } else {
                isShow = true;
                hideOrShow(1);
                showSystemBar();
            }
        });
        listview.setOnScrollListener(new MyScrollListener() {
            @Override
            protected void scroll(int scrollState) {
                //设置标题
                if (null != showItemList && showItemList.size() > 0) {
                    int firstVisiblePosition = listview.getFirstVisiblePosition();
                    CartoonPicItem picItem = showItemList.get(firstVisiblePosition);
                    setTitleStr(picItem);
                    //set last index
                    if (scrollTag != -1) {
                        if (scrollTag == 0) {
                            catalogIndex = picItem.catalogIndex;
                        } else {
                            int lastVisiblePosition = listview.getLastVisiblePosition();
                            catalogIndex = showItemList.get(lastVisiblePosition).catalogIndex;
                        }
                    }
                }
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (isShow) {
                            isShow = false;
                            hideOrShow(0);
                        }
                        break;
                }
            }
        });
    }

    private synchronized void hideOrShow(final int i) {
        //title
        float translationY1 = title_view.getTranslationY();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(title_view, "translationY", translationY1, 0 == i ? translationY1 - UIUtil.dip2px(75) : translationY1 + UIUtil.dip2px(75));
        animator1.setDuration(50);
        animator1.start();
        //foot
        float translationY2 = ll_footer.getTranslationY();
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ll_footer, "translationY", translationY2, 0 == i ? translationY2 + UIUtil.dip2px(66) : translationY2 - UIUtil.dip2px(66));
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ll_footer, "translationY", translationY2, 0 == i ? translationY2 + UIUtil.dip2px(53) : translationY2 - UIUtil.dip2px(53));
        animator2.setDuration(50);
        animator2.start();
    }

    private void checkNet() {
        boolean isWifi = JudgeNetUtil.isWifi(this);
        if (isWifi) {
            getStartReadData();
        } else {
            boolean isWifiSwitchOpen = SPUtil.getSharedPreference().getBoolean(Constants.WIFI_SWITCH, true);
            boolean noMore = SPUtil.getBoolean(Constants.WIFI_SWITCH_NO_MORE);
            if (isWifiSwitchOpen && !noMore) {
                ReadDialog.showDialog(this, "当前使用的是非WIFI数据,是否继续浏览?", "确认", "取消", v -> {
                    ReadDialog.close();
                    switch (v.getId()) {
                        case R.id.bt_ok:
                            getStartReadData();
                            break;
                        case R.id.bt_cancel:
//                            ll_footer.setVisibility(View.GONE);
                            finish();
                            break;
                    }
                });
            } else {
                getStartReadData();
            }
        }
    }

    private void getStartReadData() {
        ObjectRequest<ChapterRecord> response = new ObjectRequest<>(false, ChapterRecord.Input.buildInput(articleId, cartoonId),
                chapterListRecord -> {
                    if (null != chapterListRecord && 1000 == chapterListRecord.code &&
                            !TextUtils.isEmpty(chapterListRecord.articleId) &&
                            !TextUtils.isEmpty(chapterListRecord.imgUrl) &&
                            chapterListRecord.imgUrl.contains("1.")) {
                        setStartReadData(chapterListRecord);
                        shouldAddCountAndShowShareDialog();
                    } else {
                        MyDebugUtil.toast("访问失败");
                    }
                },
                volleyError -> MyDebugUtil.toast("访问失败"));
        NetClient.request(response);
    }

    //累计未读数量,判断是否显示分享对话框
    private void shouldAddCountAndShowShareDialog() {
        if (MyDebugUtil.isFree) {
            return;
        }
        if (!MyDebugUtil.isHideShareOrSign) {
            SignDao signDao = new SignDao(this);
            if (!signDao.isSignedToday()) {
                ReadChapterCountUtil.addCount();
                if (!ReadChapterCountUtil.canRead()) {
                    sign();
                }
            }
        }
    }

    private void sign() {
        SignDialogUtil.showDialog(this, new SignDialogUtil.SignListener() {
            @Override
            public void download() {
//                checkDirAndStart();
            }

            @Override
            public void close() {
                back(-1);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SignDao signDao = new SignDao(this);
        if (!signDao.isSignedToday() && SignDialogUtil.isShow()) {
            SignDialogUtil.updateView();
            checkHasSign();
        }
        if (LoginVerifyUtil.isLogin()) {
            initData();
        }
    }

    public void checkHasSign() {
        String code = SPUtil.getString(Constants.SIGN_TUCAO_CODE);
        if (TextUtils.isEmpty(code)) {
            return;
        }
        ObjectRequest<HastSignedRecord> response = new ObjectRequest<>(false, HastSignedRecord.Input.buildInput(code),
                getSignCodeRecord -> {
                    if (null != getSignCodeRecord) {
                        MyDebugUtil.toastTest("提交成功");
                        //(1:未签到,2：已签到)
                        int signState = getSignCodeRecord.signState;
                        sign(signState == 2);
                    } else {
                        MyDebugUtil.toast("网络异常");
                    }
                },
                volleyError -> MyDebugUtil.toast("网络异常"));
        NetClient.request(response);
    }

    public void sign(boolean isSigned) {
        SignDao signDao = new SignDao(this);
        if (!signDao.isSignedToday() && isSigned) {
            signDao.add();
            EventBus.getDefault().post(new EventBusEvent.ShareEvent(EventBusEvent.ShareEvent.TYPE_SIGN_OK));
        }
    }

    private void setStartReadData(ChapterRecord chapterListRecord) {
        String imgUrl = chapterListRecord.imgUrl;
        String articleId = chapterListRecord.articleId;
        String articleName = chapterListRecord.articleName;
        int totalCount = chapterListRecord.imgIndex;
        for (int i = 1; i <= totalCount; i++) {
            int lastIndexOf = imgUrl.lastIndexOf("1");
            if (lastIndexOf > 0) {
                String urlHeader = imgUrl.substring(0, lastIndexOf);
                String urlEnd = imgUrl.substring(lastIndexOf + 1);
                String currentUrl = urlHeader + i + urlEnd;
                MyDebugUtil.log(TAG, "currentUrl=" + currentUrl);
                CartoonPicItem cartoonPicItem = new CartoonPicItem(cartoonId, articleId, currentUrl, articleName, totalCount, i, catalogIndex);
                totalItemList.add(cartoonPicItem);
            }
        }
        addStartReadData();
    }

    //初始化默认展示三张,低于CartoonUtil.ONCE_SHOW_MAX_COUNT张全部展示
    private void addStartReadData() {
        if (totalItemList.size() > CartoonUtil.ONCE_SHOW_MAX_COUNT) {
            for (int i = 0; i < CartoonUtil.ONCE_SHOW_MAX_COUNT; i++) {
                showItemList.add(totalItemList.get(i));
            }
        } else {
            showItemList.addAll(totalItemList);
        }
        myAdapter.notifyDataSetChanged();
        setTitleStr(showItemList.get(0));
    }

    private void getPreOrNextData(final int isFresh) {
        int type = (-1 == isFresh) ? 1 : 2;
        ObjectRequest<NextChapterRecord> response = new ObjectRequest<>(false, NextChapterRecord.Input.buildInput(catalogIndex + "", cartoonId, type + ""),
                nextChapterRecord -> {
                    listview.isIdel = true;
                    changeState();
                    if (null != nextChapterRecord && 1000 == nextChapterRecord.code &&
                            !TextUtils.isEmpty(nextChapterRecord.articleId) &&
                            !TextUtils.isEmpty(nextChapterRecord.imgUrl) &&
                            nextChapterRecord.imgUrl.contains("1.") &&
                            nextChapterRecord.imgIndex >= 1) {
                        setPreOrNextData(isFresh, nextChapterRecord);
                        shouldAddCountAndShowShareDialog();
                    } /*else {
                        MyDebugUtil.toastTest("访问失败");
                    }*/
                },
                volleyError -> {
                    listview.isIdel = true;
                    changeState();
//                    MyDebugUtil.toastTest("访问失败");
                });
        NetClient.request(response);
    }

    private void changeState() {
        if (!tv_pre.isEnabled()) {
            tv_pre.setEnabled(true);
        }
        if (!tv_next.isEnabled()) {
            tv_next.setEnabled(true);
        }
    }

    private void setPreOrNextData(int isFresh, NextChapterRecord nextChapterRecord) {
        String imgUrl = nextChapterRecord.imgUrl;
        String articleId = nextChapterRecord.articleId;
        String articleName = nextChapterRecord.articleName;
        int totalCount = nextChapterRecord.imgIndex;
        if (-1 == isFresh) {
            if (catalogIndex > 1) {
                catalogIndex -= 1;
            }
            for (int i = totalCount; i >= 1; i--) {
                int lastIndexOf = imgUrl.lastIndexOf("1");
                if (lastIndexOf > 0) {
                    String urlHeader = imgUrl.substring(0, lastIndexOf);
                    String urlEnd = imgUrl.substring(lastIndexOf + 1);
                    String currentUrl = urlHeader + i + urlEnd;
                    MyDebugUtil.log(TAG, "currentUrl=" + currentUrl);
                    CartoonPicItem cartoonPicItem = new CartoonPicItem(cartoonId, articleId, currentUrl, articleName, totalCount, i, catalogIndex);
                    totalItemList.add(0, cartoonPicItem);
                }
            }
            int posPre = showItemList.size();
            showItemList = CartoonUtil.addData(totalItemList, showItemList, false);
            myAdapter.notifyDataSetChanged();
            int addedPos = showItemList.size() - posPre;
            listview.setSelection(addedPos - 1);
        } else {
            catalogIndex += 1;
            for (int i = 1; i <= totalCount; i++) {
                int lastIndexOf = imgUrl.lastIndexOf("1");
                if (lastIndexOf > 0) {
                    String urlHeader = imgUrl.substring(0, lastIndexOf);
                    String urlEnd = imgUrl.substring(lastIndexOf + 1);
                    String currentUrl = urlHeader + i + urlEnd;
                    MyDebugUtil.log(TAG, "currentUrl=" + currentUrl);
                    CartoonPicItem cartoonPicItem = new CartoonPicItem(cartoonId, articleId, currentUrl, articleName, totalCount, i, catalogIndex);
                    totalItemList.add(cartoonPicItem);
                }
            }
//            int pos = showItemList.size();
            showItemList = CartoonUtil.addData(totalItemList, showItemList, true);
            myAdapter.notifyDataSetChanged();
//            listview.setSelection(pos);
        }
        if (isChocked) {
            isChocked = false;
            listview.smoothScrollToPosition(0);
            int firstVisiblePosition = listview.getFirstVisiblePosition();
            if (firstVisiblePosition < showItemList.size()) {
                setTitleStr(showItemList.get(firstVisiblePosition));
            }
        }
    }

    private void setTitleStr(CartoonPicItem cartoonPicItem) {
        titleName = cartoonPicItem.articleName;
        //progress
//        progressBar.setMax(cartoonPicItem.picTotalCount);
//        progressBar.setProgress(cartoonPicItem.picIndex);
        //title
        setTitleName(cartoonPicItem.articleName + "(" + cartoonPicItem.picIndex + "/" + cartoonPicItem.picTotalCount + ")");
    }

    private void setTitleName(String s) {
        tv_title_title.setText(s);
        if (!TextUtils.isEmpty(s) && s.length() > 14) {
            tv_title_title.setSelected(true);
        } else {
            tv_title_title.setSelected(false);
        }
    }

    private boolean isChocked = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                shouldCollect();
                break;
            case R.id.iv_title_right_home:
                save();
                IntentUtil.startActivityWithString(this, MainActivity.class, "index", 1 + "");
                break;
            case R.id.iv_title_right_share:
                share(false);
                break;
            case R.id.tv_pre:
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                if (firstVisiblePosition < 0 || showItemList.size() <= 0) {
                    MyDebugUtil.toast("访问失败");
                    return;
                }
                int catalogIndex = showItemList.get(firstVisiblePosition).catalogIndex;
                if (catalogIndex > 1) {
                    totalItemList.clear();
                    showItemList.clear();
                    myAdapter.notifyDataSetChanged();
                    isChocked = true;
                    tv_pre.setEnabled(false);
                    getPreOrNextData(-1);
                } else {
                    MyDebugUtil.toast("已经是第一章了");
                }
                break;
            case R.id.tv_catalog:
                Intent intent = new Intent(this, CatalogActivity.class);
                intent.putExtra("cartoonId", cartoonId);
                startActivityForResult(intent, 10001);
                IntentUtil.startAnim(this);
                break;
            case R.id.tv_next:
                int lastVisiblePosition = listview.getLastVisiblePosition();
                if (lastVisiblePosition < 0 || showItemList.size() <= 0) {
                    MyDebugUtil.toast("访问失败");
                    return;
                }
                int catalogIndex1 = showItemList.get(lastVisiblePosition).catalogIndex;
                if (catalogIndex1 < Integer.parseInt(size)) {
                    totalItemList.clear();
                    showItemList.clear();
                    myAdapter.notifyDataSetChanged();
                    isChocked = true;
                    tv_next.setEnabled(false);
                    getPreOrNextData(1);
                } else {
                    MyDebugUtil.toast("已经是最后一章了");
                }
                break;
            case R.id.tv_change:
                //判断当前屏幕方向
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    //切换竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    tv_change.setImageResource(R.drawable.ic_change_land);
                } else {
                    //切换横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    tv_change.setImageResource(R.drawable.ic_change_portrait);
                }
                break;
        }
    }

    private void shouldCollect() {
        //未登录
//        if (!LoginVerifyUtil.isLogin()) {
//            back(0);
//            return;
//        }
        //拒绝过收藏
        String id = SPUtil.getString(cartoonId);
        if (!TextUtils.isEmpty(id)) {
            back(0);
            return;
        }
        //已经收藏
        if (hasColl) {
            back(0);
            return;
        }
        CollectDialogUtil.showDialog(this, new CollectDialogUtil.CollectListener() {
            @Override
            public void collect() {
                if (LoginVerifyUtil.isLogin()) {
                    collectBook();
                } else {
                    IntentUtil.startActivity(CartoonReadPreActivity.this, LoginActivity.class);
                }
            }

            @Override
            public void collectNot() {
                back(-1);
                SPUtil.put(cartoonId, cartoonId);
            }
        });
    }

    private void collectBook() {
        int lastVisiblePosition = listview.getLastVisiblePosition();
        if (lastVisiblePosition == -1 || showItemList == null ||
                lastVisiblePosition >= showItemList.size() || showItemList.size() == 0) {
            back(-1);
            return;
        }
        CartoonPicItem cartoonPicItem = showItemList.get(lastVisiblePosition);
        String chapterId = cartoonPicItem.articleId;
        String chapterTitle = cartoonPicItem.articleName;
        ObjectRequest<CollectRecord> response = new ObjectRequest<>(false, CollectRecord.Input.buildInput(cartoonId + "", "1", chapterId, chapterTitle),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
                        MyDebugUtil.toast("收藏成功~");
                        EventBus.getDefault().post(new EventBusEvent.CollectEvent(EventBusEvent.CollectEvent.TYPE_FRESH));
                    } else if (collectRecord != null && (collectRecord.code == 3006 || collectRecord.code == 3007)) {
                        LoginVerifyUtil.login();
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    } else {
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    }
                    back(0);
                },
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                    back(0);
                });
        NetClient.request(response);
    }

    private Handler handler = new Handler();

    private void back(int code) {
        save();
        if (code == -1) {
            handler.postDelayed(this::finish, 300);
        } else {
            finish();
        }
    }

    private void share(boolean isCenter) {
        if (!JudgeNetUtil.hasNet(CartoonReadPreActivity.this)) {
            MyDebugUtil.toast("请连接网络后重试~");
            return;
        }
        if (TextUtils.isEmpty(cartoonName) || TextUtils.isEmpty(titleName)) {
            MyDebugUtil.toast("请重试~");
            return;
        }
//        ShareOutDialogUtil.showDialog(this, isCenter, position -> {
        ShareInDialogUtil.showDialog(this, isCenter, position -> {
            String logoUrl = "https://mmbiz.qlogo.cn/mmbiz_png/RTMaDfovwlq1whwR5Tm1O5qbJWogsmnaNmbStotJvGVsSzqcJEynicITDLmUS52AEDAEeRofic7etdgn9icwB1vmA/0?wx_fmt=png";// Constants.QINIU_GET_ICON_HEADER_BASE + "share_logo.png";//分享用logo
//            String shareUrl = "http://manga.zishupai.cn/share/share.html"; //分享的链接
            String shareUrl = "http://post.zishupai.cn/c/share.html"; //分享的链接
            String title = "快和我一起看《" + cartoonName + "》漫画！";//分享的标题
            String content = "我正在看《" + cartoonName + "》作品的" + titleName + ",看漫画就到咔米漫画，更新快、漫画全，小伙伴们一起来看吧！";//描述
            switch (position) {
                case 0://qq好友
                    ShareUtil.shareToQQ(2, BaseApplication.mTencent, shareListener, CartoonReadPreActivity.this, logoUrl, shareUrl, title, content);
                    break;
                case 1://qq朋友圈
                    ShareUtil.shareToQZone(2, BaseApplication.mTencent, shareListener, CartoonReadPreActivity.this, logoUrl, shareUrl, title, content);
                    break;
                case 2://微信好友
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 0, CartoonReadPreActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    break;
                case 3://微信朋友圈
                    ShareUtil.shareToWX(true, 2, BaseApplication.mIWXAPI, 1, CartoonReadPreActivity.this, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "", shareUrl, title, content);
                    break;
                case 4://收藏
                    if (LoginVerifyUtil.isLogin()) {
                        collectOrNot(hasColl ? 2 : 1);
                    } else {
                        IntentUtil.startActivity(this, LoginActivity.class);
                    }
                    break;
                case 5://报错
                    report();
                    break;
            }
        });
    }

    private void report() {
        String chapterId = null;
        if (null != showItemList && showItemList.size() > 0) {
            int firstVisiblePosition = listview.getFirstVisiblePosition();
            CartoonPicItem picItem = showItemList.get(firstVisiblePosition);
            chapterId = picItem.articleId;
        }
        if (TextUtils.isEmpty(chapterId)) {
            return;
        }
        ObjectRequest<ReportErrorRecord> response = new ObjectRequest<>(false, ReportErrorRecord.Input.buildInput(cartoonId, chapterId),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
                        MyDebugUtil.toast("提交成功");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toast("提交失败，请稍后重试~");
                });
        NetClient.request(response);
    }

    private void collectOrNot(int operate) {
        int lastVisiblePosition = listview.getLastVisiblePosition();
        if (lastVisiblePosition == -1 || showItemList == null ||
                lastVisiblePosition >= showItemList.size() || showItemList.size() == 0) {
//            back(-1);
            MyDebugUtil.toast("操作失败，请稍后重试~");
            return;
        }
        CartoonPicItem cartoonPicItem = showItemList.get(lastVisiblePosition);
        String chapterId = cartoonPicItem.articleId;
        String chapterTitle = cartoonPicItem.articleName;
        ObjectRequest<CollectRecord> response = new ObjectRequest<>(false, CollectRecord.Input.buildInput(cartoonId, operate + "", chapterId, chapterTitle),
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

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return showItemList.size();
        }

        @Override
        public CartoonPicItem getItem(int position) {
            return showItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_item, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final CartoonPicItem item = getItem(position);
            //pic
            viewHolder.iv_cartoon.setImageResource(R.drawable.ic_holder3);
            Glide.with(CartoonReadPreActivity.this).load(item.currentPicUrl).asBitmap().into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int imageWidth = resource.getWidth();
                    int imageHeight = resource.getHeight();
                    int height = ScreenUtil.getScreenWid() * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = viewHolder.iv_cartoon.getLayoutParams();
                    para.height = height;
                    para.width = ScreenUtil.getScreenWid();
                    viewHolder.iv_cartoon.setLayoutParams(para);
                    viewHolder.iv_cartoon.setImageBitmap(resource);
                }
            });
            //title
            viewHolder.tv_title.setText(item.articleName);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.tv_title.getLayoutParams();
            if (position == 0) {
                viewHolder.tv_title.setVisibility(item.picIndex == 1 ? View.VISIBLE : View.GONE);
                layoutParams.topMargin = 0;
            } else {
                String thisName = showItemList.get(position).articleName;
                String preName = showItemList.get(position - 1).articleName;
                viewHolder.tv_title.setVisibility(thisName.equals(preName) ? View.GONE : View.VISIBLE);
                layoutParams.topMargin = ((thisName.equals(preName)) ? 0 : UIUtil.dip2px(5));
            }
            viewHolder.tv_title.setLayoutParams(layoutParams);
            return convertView;
        }
    }

    static class ViewHolder {
        @ViewInject(id = R.id.tv_title)
        TextView tv_title;
        @ViewInject(id = R.id.iv_cartoon)
        ImageView iv_cartoon;

        ViewHolder(View convertView) {
            ViewInjectUtil.initNotInActivity(this, convertView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
//            save();
            shouldCollect();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void save() {
        //每次记录最后离开时候的位置
        if (showItemList.size() > 0) {
            int lastVisiblePosition = listview.getLastVisiblePosition();
            if (-1 == lastVisiblePosition) {
                return;
            }
            CartoonPicItem cartoonPicItem = showItemList.get(lastVisiblePosition);
            ReadPositionBean cartoonReadBean = cartoonReadPositionDao.query(cartoonId);
            if (null != cartoonReadBean) {
                if (!cartoonReadBean.articleId.equals(cartoonPicItem.articleId)) {
                    cartoonReadPositionDao.update(cartoonPicItem);
                }
            } else {
                cartoonReadPositionDao.add(cartoonPicItem);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && requestCode == 10001 && resultCode == 1000) {
            catalogIndex = Integer.parseInt(data.getStringExtra("currentIndex"));
            articleId = data.getStringExtra("articleId");
            totalItemList.clear();
            showItemList.clear();
            myAdapter.notifyDataSetChanged();
            titleName = data.getStringExtra("titleName");
            getStartReadData();
        } else if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE ||
                requestCode == com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.trans_pre_in_back, R.anim.trans_pre_out_back);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareEvent(EventBusEvent.ShareEvent shareEvent) {
        if (EventBusEvent.ShareEvent.TYPE_SHARE == shareEvent.type) {
            SPUtil.put(cartoonId + "share", true);
            handler.postDelayed(() -> {
//                ShareOutDialogUtil.close();
                ShareInDialogUtil.close();
                VerifyDialog.showVerifyDialog(CartoonReadPreActivity.this, "分享成功", "继续阅读", v -> VerifyDialog.close());
            }, 500);
        } else {
            if (SignDialogUtil.isShow()) {
                SignDialogUtil.closeDialog();
                SignOkDialogUtil.showDialog(this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!LoginVerifyUtil.isLogin()) {
            return;
        }
        if (!hasColl) {
            return;
        }
        int lastVisiblePosition = listview.getLastVisiblePosition();
        if (showItemList == null || lastVisiblePosition < 0) {
            return;
        }
        if (lastVisiblePosition < showItemList.size()) {
            CartoonPicItem cartoonPicItem = showItemList.get(lastVisiblePosition);
            UpdateBean updateBean = new UpdateBean();
            updateBean.cartoonId = cartoonId;
            updateBean.chapterId = cartoonPicItem.articleId;
            updateBean.chapterTitle = cartoonPicItem.articleName;
            String params = GsonUtil.getGson().toJson(updateBean);
            updateProgress("[" + params + "]");
        }
    }

    private void updateProgress(String params) {
        ObjectRequest<UpdadteProgressRecord> response = new ObjectRequest<>(false, UpdadteProgressRecord.Input.buildInput(params),
                collectRecord -> {
                    if (collectRecord != null && collectRecord.code == 1000) {
//                        EventBus.getDefault().post(new EventBusEvent.CollectEvent(EventBusEvent.CollectEvent.TYPE_FRESH));
                        MyDebugUtil.toastTest("ok~");
                    }/* else if (collectRecord != null && (collectRecord.code == 3006 || collectRecord.code == 3007)) {
                        LoginVerifyUtil.login();
                        MyDebugUtil.toast("操作失败，请稍后重试~");
                    } */ else {
                        MyDebugUtil.toastTest("操作失败，请稍后重试~");
                    }
                },
                volleyError -> {
                    MyDebugUtil.toastTest("没有数据~");
                });
        NetClient.request(response);
    }

    @Override
    protected void onDestroy() {
//        ShareOutDialogUtil.close();
        ShareInDialogUtil.close();
        SignDialogUtil.closeDialog();
        CollectDialogUtil.closeDialog();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        if (isdListColl != null) {
//            isdListColl.clear();
//            isdListColl = null;
//        }
        hasColl = false;
        scrollTag = -1;
    }
}
