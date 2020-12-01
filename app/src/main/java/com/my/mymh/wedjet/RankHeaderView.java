package com.my.mymh.wedjet;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.my.mymh.R;
import com.my.mymh.model.record.LastUpdateCartoonListRecord;
import com.my.mymh.util.GlideUtil;
import com.my.mymh.util.IntentUtil;
import com.my.mymh.wedjet.roundedimageview.CustomShapeImageView;

/**
 * PackageName  com.hgd.hgdcomic.wedjet
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/11/15.
 */
public class RankHeaderView extends FrameLayout implements View.OnClickListener {
    public static final String TAG = "RankHeaderView";
    private CustomShapeImageView iv_first;
    private CustomShapeImageView iv_second;
    private CustomShapeImageView iv_third;

    public RankHeaderView(@NonNull Context context) {
        this(context, null);
    }

    public RankHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.rank_header_view, this);
        iv_first = (CustomShapeImageView) this.findViewById(R.id.iv_first);
        iv_second = (CustomShapeImageView) this.findViewById(R.id.iv_second);
        iv_third = (CustomShapeImageView) this.findViewById(R.id.iv_third);
        iv_first.setOnClickListener(this);
        iv_second.setOnClickListener(this);
        iv_third.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LastUpdateCartoonListRecord.Result result = null;
        switch (v.getId()) {
            case R.id.iv_first:
                result = result1;
                break;
            case R.id.iv_second:
                result = result2;
                break;
            case R.id.iv_third:
                result = result3;
                break;
        }
        if (result != null) {
//            EventBus.getDefault().postSticky(new EventBusEvent.CartoonInfoStickyEvent(result));
//            IntentUtil.startActivity(activity, CartoonInfoAndCatalogActivity.class);
            IntentUtil.startRead(activity, result);
        }
    }

    private Activity activity;
    private LastUpdateCartoonListRecord.Result result1;
    private LastUpdateCartoonListRecord.Result result2;
    private LastUpdateCartoonListRecord.Result result3;

    public void setDatas(Activity activity, LastUpdateCartoonListRecord.Result result1,
                         LastUpdateCartoonListRecord.Result result2,
                         LastUpdateCartoonListRecord.Result result3) {
        this.activity = activity;
        this.result1 = result1;
        this.result2 = result2;
        this.result3 = result3;
        if (result1 != null && !TextUtils.isEmpty(result1.imgUrl) && result1.imgUrl.contains("http")) {
            GlideUtil.load(getContext(), result1.imgUrl, R.drawable.ic_holder1, iv_first);
        } else {
            iv_first.setImageResource(R.drawable.ic_holder1);
        }
        if (result2 != null && !TextUtils.isEmpty(result2.imgUrl) && result2.imgUrl.contains("http")) {
            GlideUtil.load(getContext(), result2.imgUrl, R.drawable.ic_holder1, iv_second);
        } else {
            iv_second.setImageResource(R.drawable.ic_holder1);
        }
        if (result3 != null && !TextUtils.isEmpty(result3.imgUrl) && result3.imgUrl.contains("http")) {
            GlideUtil.load(getContext(), result3.imgUrl, R.drawable.ic_holder1, iv_third);
        } else {
            iv_third.setImageResource(R.drawable.ic_holder1);
        }
    }
}
