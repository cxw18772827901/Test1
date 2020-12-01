package com.my.mymh.ui.cartoonSelectFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.UIUtil;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PackageName  com.dave.project.ui.fragment
 * ProjectName  Project
 * Author       chenxiaowu
 * Date         2018/1/9.
 */
@Deprecated
public class SelectorFragment1 extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "SelectorFragment1";
    private static final List<String> STRINGS_CLUM1 = Arrays.asList("写作进度", "全部", "连载", "完结");
    private static final List<String> STRINGS_CLUM2 = Arrays.asList("作品属性", "全部", "国漫", "日漫", "港漫", "其他");
    private static final List<String> STRINGS_CLUM3 = Arrays.asList("作品分类", "全部", "玄幻", "都市", "灵异", "古风",
            "总裁", "科幻", "热血", "爆笑", "纯爱", "少年", "后宫");
    @ViewInject(id = R.id.flow1)
    private FlowLayout flow1;
    @ViewInject(id = R.id.flow2)
    private FlowLayout flow2;
    @ViewInject(id = R.id.flow3)
    private FlowLayout flow3;
    @ViewInject(id = R.id.ll_arrow, needClick = true)
    private LinearLayout ll_arrow;
    @ViewInject(id = R.id.tv_arrow)
    private TextView tv_arrow;
    @ViewInject(id = R.id.iv_arrow)
    private ImageView iv_arrow;
    private List<TextView> textViewList1 = new ArrayList<>();
    private List<TextView> textViewList2 = new ArrayList<>();
    private List<TextView> textViewList3 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selector_fragment1, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initHeight();
    }

    private int flowHeight;

    private void initHeight() {
        flow3.post(new Runnable() {
            @Override
            public void run() {
                flowHeight = flow3.getHeight();
            }
        });
    }

    private void initView() {
        //clear
        flow1.removeAllViews();
        flow2.removeAllViews();
        flow3.removeAllViews();
        textViewList1.clear();
        textViewList2.clear();
        textViewList3.clear();
        //tab1
        for (int i = 0; i < STRINGS_CLUM1.size(); i++) {
            if (i == 0) {
                View viewTitle = LayoutInflater.from(getActivity()).inflate(R.layout.clum_selector_layout_title, null);
                TextView tvSelTitle = (TextView) viewTitle.findViewById(R.id.tv_sel_title);
                tvSelTitle.setText(STRINGS_CLUM1.get(0));
                flow1.addView(viewTitle);
            } else {
                View viewContent = LayoutInflater.from(getActivity()).inflate(R.layout.clum_selector_layout_content, null);
                final TextView tvSel = (TextView) viewContent.findViewById(R.id.tv_sel);
                tvSel.setText(STRINGS_CLUM1.get(i));
                textViewList1.add(tvSel);
                final int finalI = i;
                tvSel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (TextView textView : textViewList1) {
                            textView.setSelected(false);
                        }
                        tvSel.setSelected(!tvSel.isSelected());
                        if (null != onItemChoiceListener) {
                            onItemChoiceListener.onItemChoice(1, STRINGS_CLUM1.get(finalI));
                        }
                    }
                });
                flow1.addView(viewContent);
            }
        }
        //tab2
        for (int i = 0; i < STRINGS_CLUM2.size(); i++) {
            if (i == 0) {
                View viewTitle = LayoutInflater.from(getActivity()).inflate(R.layout.clum_selector_layout_title, null);
                TextView tvSelTitle = (TextView) viewTitle.findViewById(R.id.tv_sel_title);
                tvSelTitle.setText(STRINGS_CLUM2.get(0));
                flow2.addView(viewTitle);
            } else {
                View viewContent = LayoutInflater.from(getActivity()).inflate(R.layout.clum_selector_layout_content, null);
                final TextView tvSel = (TextView) viewContent.findViewById(R.id.tv_sel);
                tvSel.setText(STRINGS_CLUM2.get(i));
                textViewList2.add(tvSel);
                final int finalI = i;
                tvSel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (TextView textView : textViewList2) {
                            textView.setSelected(false);
                        }
                        tvSel.setSelected(!tvSel.isSelected());
                        if (null != onItemChoiceListener) {
                            onItemChoiceListener.onItemChoice(2, STRINGS_CLUM2.get(finalI));
                        }
                    }
                });
                flow2.addView(viewContent);
            }
        }
        //tab3
        for (int i = 0; i < STRINGS_CLUM3.size(); i++) {
            if (i == 0) {
                View viewTitle = LayoutInflater.from(getActivity()).inflate(R.layout.clum_selector_layout_title, null);
                TextView tvSelTitle = (TextView) viewTitle.findViewById(R.id.tv_sel_title);
                tvSelTitle.setText(STRINGS_CLUM3.get(0));
                flow3.addView(viewTitle);
            } else {
                View viewContent = LayoutInflater.from(getActivity()).inflate(R.layout.clum_selector_layout_content, null);
                final TextView tvSel = (TextView) viewContent.findViewById(R.id.tv_sel);
                tvSel.setText(STRINGS_CLUM3.get(i));
                textViewList3.add(tvSel);
                final int finalI = i;
                tvSel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (TextView textView : textViewList3) {
                            textView.setSelected(false);
                        }
                        tvSel.setSelected(!tvSel.isSelected());
                        if (null != onItemChoiceListener) {
                            onItemChoiceListener.onItemChoice(3, STRINGS_CLUM3.get(finalI));
                        }
                    }
                });
                flow3.addView(viewContent);
            }
        }
        //首选
        textViewList1.get(0).setSelected(true);
        textViewList2.get(0).setSelected(true);
        textViewList3.get(0).setSelected(true);
    }

    private boolean isShrink = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_arrow:
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) flow3.getLayoutParams();
                if (isShrink) {
                    tv_arrow.setText("收起");
                    iv_arrow.setSelected(false);
                    layoutParams.height = flowHeight;
                } else {
                    tv_arrow.setText("展开");
                    iv_arrow.setSelected(true);
                    layoutParams.height = UIUtil.dip2px(35);
                }
                isShrink = !isShrink;
                flow3.setLayoutParams(layoutParams);
                flow3.invalidate();
                break;
        }
    }

    private OnItemChoiceListener onItemChoiceListener;

    public void setOnItemChoiceListener(OnItemChoiceListener onItemChoiceListener) {
        this.onItemChoiceListener = onItemChoiceListener;
    }

    public interface OnItemChoiceListener {
        void onItemChoice(int clum, String clumName);
    }
}
