package com.my.mymh.ui.base;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.util.KeyBoardUtil;
import com.my.mymh.util.TitleBarUtil;
import com.my.mymh.util.debugLog.MyDebugUtil;
import com.my.mymh.util.inject.ViewInjectUtil;

import java.util.List;

import static com.my.mymh.ui.base.BaseActivity.APP_THEME.NONE_THEME;
import static com.my.mymh.ui.base.BaseActivity.APP_THEME.WHITE_THEME;


/**
 * activity的基类,跟BaseFragmentActivity的区别在于父类不同:
 * 1.处理沉浸式风格的标题栏(可以设置三种风格黄色,黑色,默认无沉浸式无风格);
 * 2.使用注解处理控件的查找和点击事件
 * Created by Dave on 2016/11/10.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";
    private APP_THEME app_theme = NONE_THEME;
    protected boolean backClickFinish = true;//默认点击返回关闭界面
    protected boolean needFinishAnim = true;//默认带返回动画
    private LayoutInflater layoutInflater;
    private LinearLayout rootView;
    protected View title_view;
    private TextView tv_title_back;
    private TextView tv_title_back_no_arrow;
    private TextView tv_title_title;
    protected LinearLayout ll_right_click;
    private FrameLayout fl_left;
    private ImageView iv_left;

    protected enum APP_THEME {
        RED_THEME, WHITE_THEME, NONE_THEME
    }

    protected void setViewWithTheme(APP_THEME app_theme, int layoutResID) {
        this.app_theme = app_theme;
        setMyContentView(layoutResID);
    }

    @SuppressLint("InflateParams")
    public void setMyContentView(int layoutResID) {
        if (APP_THEME.RED_THEME == app_theme || APP_THEME.WHITE_THEME == app_theme) {
            layoutInflater = LayoutInflater.from(this);
            if (APP_THEME.RED_THEME == app_theme) {
                rootView = (LinearLayout) layoutInflater.inflate(R.layout.custom_red_title, null);
            } else {
                rootView = (LinearLayout) layoutInflater.inflate(R.layout.custom_white_title, null);
            }
            layoutInflater.inflate(layoutResID, rootView, true);
            setContentView(rootView);
            TitleBarUtil.initImmersiveBar(this, BaseActivity.APP_THEME.RED_THEME == app_theme ? R.color.cl_main_red_head : R.color.cl_white);
            initTitleView();
        } else {
            setContentView(layoutResID);
        }
        ViewInjectUtil.initInActivity(this);
    }

    private void initTitleView() {
        title_view = rootView.findViewById(R.id.title_view);
        //left
        tv_title_back = (TextView) rootView.findViewById(R.id.tv_title_back);
        tv_title_back_no_arrow = (TextView) rootView.findViewById(R.id.tv_title_back_no_arrow);
        fl_left = (FrameLayout) rootView.findViewById(R.id.fl_left);
        iv_left = (ImageView) rootView.findViewById(R.id.iv_left);
        //title
        tv_title_title = (TextView) rootView.findViewById(R.id.tv_title_title);
        //right
        ll_right_click = (LinearLayout) rootView.findViewById(R.id.ll_right_click);
        tv_title_back.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.tv_title_back:
                    titleBackClick();
                    if (backClickFinish) {
                        finish();
                    }
                    break;
            }
        });
    }

    //设置标题名字
    protected void setTitleName(String titleName) {
        if (hasNoneTheme()) return;
        tv_title_title.setText(titleName);
        if (!TextUtils.isEmpty(titleName) && titleName.length() > 6) {
            tv_title_title.setSelected(true);
        } else {
            tv_title_title.setSelected(false);
        }
    }

    //设置返回按钮的名字
    protected void setTitleLeftName(String leftName) {
        if (hasNoneTheme()) return;
        tv_title_back.setText(leftName);
    }

    //隐藏右侧点击区域
    protected void hideTitleRight() {
        if (hasNoneTheme()) return;
        if (View.GONE != ll_right_click.getVisibility())
            ll_right_click.setVisibility(View.GONE);
    }

    //显示右侧点击区域
    protected void showTitleRight() {
        if (hasNoneTheme()) return;
        if (View.VISIBLE != ll_right_click.getVisibility()) {
            ll_right_click.setVisibility(View.VISIBLE);
        }
    }

    //隐藏左侧返回按钮
    protected void hideTitleLeftTv() {
        if (hasNoneTheme()) return;
        tv_title_back.setVisibility(View.INVISIBLE);
    }

    //隐藏左侧带箭头的返回按钮,显示无箭头按钮
    protected void hideLeftArrow(String str) {
        if (hasNoneTheme()) return;
        tv_title_back.setVisibility(View.GONE);
        tv_title_back_no_arrow.setVisibility(View.VISIBLE);
        tv_title_back_no_arrow.setText(str);
        tv_title_back_no_arrow.setOnClickListener(v -> {
            switch (v.getId()) {
                case R.id.tv_title_back_no_arrow:
                    titleBackClick();
                    if (backClickFinish) {
                        finish();
                    }
                    break;
            }
        });
    }

    protected void setLeftIvClick(int resID, View.OnClickListener onClickListener) {
        if (hasNoneTheme()) return;
        tv_title_back.setVisibility(View.GONE);
        tv_title_back_no_arrow.setVisibility(View.GONE);
        fl_left.setVisibility(View.VISIBLE);
        iv_left.setImageResource(resID);
        fl_left.setOnClickListener(onClickListener);
    }

    protected void hideLeftIv(boolean shouldHide) {
        if (shouldHide) {
            if (fl_left.getVisibility() != View.GONE) {
                fl_left.setVisibility(View.GONE);
            }
        } else {
            if (fl_left.getVisibility() != View.VISIBLE) {
                fl_left.setVisibility(View.VISIBLE);
            }
        }
    }

    //设置右边按钮的点击事件
    protected void setRightClickViews(List<Object> rightClickViews, final OnRightClickViewsListener onRightClickViewsListener) {
        if (hasNoneTheme()) return;
        for (int i = 0; i < rightClickViews.size(); i++) {
            if (i > 2) {
                break;
            }
            Object o = rightClickViews.get(i);
            View view = null;
            if (o instanceof Integer) {
                view = layoutInflater.inflate(R.layout.title_right_click_iv_view, ll_right_click, true);
                ((ImageView) view.findViewById(R.id.iv_right)).setImageResource((Integer) o);
            } else if (o instanceof String) {
                view = layoutInflater.inflate(R.layout.title_right_click_tv_view, ll_right_click, true);
                ((TextView) view.findViewById(R.id.tv_right)).setText((String) o);
                ((TextView) view.findViewById(R.id.tv_right)).setTextColor(WHITE_THEME == app_theme ? getResources().getColor(R.color.cl_333333) : getResources().getColor(R.color.cl_white));
            }
            //click
            if (null != view) {
                final int finalI = i;
                view.setOnClickListener(v -> {
                    if (null != onRightClickViewsListener) {
                        onRightClickViewsListener.clickPosition(finalI);
                    }
                });
            }
        }
    }


    //修改右侧第一个按钮图标(第一个图标必须是图片按钮,可再修改)
    protected void setRightIvIcon(int resourseId) {
        if (hasNoneTheme()) return;
        try {
            if (ll_right_click.getChildCount() > 0) {
                ((ImageView) ll_right_click.getChildAt(0).findViewById(R.id.iv_right)).setImageResource(resourseId);
            }
        } catch (Exception e) {
            MyDebugUtil.toast("需要调整布局~");
            e.printStackTrace();
        }
    }

    private boolean hasNoneTheme() {
        if (APP_THEME.NONE_THEME == app_theme) {
            MyDebugUtil.toast("请先设置主题~");
            return true;
        }
        return false;
    }

    //右侧菜单的点击回调
    public interface OnRightClickViewsListener {
        void clickPosition(int position);
    }

    @Override
    public void finish() {
        super.finish();
        KeyBoardUtil.dismissSoftKeyboard(this);
        if (needFinishAnim) {
            overridePendingTransition(R.anim.trans_pre_in_back, R.anim.trans_pre_out_back);
        }
    }

    protected void titleBackClick() {

    }
}
