package com.my.mymh.ui.mainFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my.mymh.R;
import com.my.mymh.ui.base.BaseFragment;
import com.my.mymh.util.EventBusEvent;
import com.my.mymh.util.inject.ViewInject;
import com.my.mymh.wedjet.FoundTopHost;

import org.greenrobot.eventbus.EventBus;

/**
 * PackageName  com.hgd.hgdcomic.ui.mainFragment
 * ProjectName  CartoonProject
 * Author       chenxiaowu
 * Date         2018/10/11.
 */
public class LocalAndCollectFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "LocalAndCollectFragment";
    @ViewInject(id = R.id.found_tab_host)
    private FoundTopHost found_tab_host;
    @ViewInject(id = R.id.iv_delete, needClick = true)
    private ImageView iv_delete;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.local_and_collect_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(bundle);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        if (found_tab_host != null) {
            bundle.putInt(FoundTopHost.TAB, found_tab_host.getCurrentTab());
        }
        super.onSaveInstanceState(bundle);
    }

    public void initView(Bundle bundle) {
        iv_delete.setVisibility(View.GONE);
        found_tab_host.init(getActivity(), getChildFragmentManager(), R.id.fl_container, bundle);
        found_tab_host.setOnTabChangedListener(tabIndex -> {
            if (0 == tabIndex) {
                if (View.GONE != iv_delete.getVisibility()) {
                    iv_delete.setVisibility(View.GONE);
                }
            } else {
                if (View.VISIBLE != iv_delete.getVisibility()) {
                    iv_delete.setVisibility(View.VISIBLE);
                }
                EventBus.getDefault().post(new EventBusEvent.FreshOrDeleteEvent(EventBusEvent.FreshOrDeleteEvent.TYPE_FRESH));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_delete:
                EventBus.getDefault().post(new EventBusEvent.FreshOrDeleteEvent(EventBusEvent.FreshOrDeleteEvent.TYPE_DELETE));
                break;
        }
    }
}
