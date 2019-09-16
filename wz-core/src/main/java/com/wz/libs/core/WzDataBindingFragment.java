package com.wz.libs.core;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wz.libs.core.views.WzAnnDataBindingFragmentGroup;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/22
 */

public class WzDataBindingFragment<T extends ViewDataBinding> extends WzHandlerFragment {

    private T mViewDataBinding;

    private WzAnnDataBindingFragmentGroup mAnnGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAnnGroup = new WzAnnDataBindingFragmentGroup(this);
        int layoutId = onCreateLayoutId();
        mViewDataBinding = DataBindingUtil.inflate(inflater,layoutId,null,false);
        if(mAnnGroup != null)mAnnGroup.onParseAnnotations();
        return mViewDataBinding.getRoot();
    }

    /**
     * 是否允许下拉刷新
     * @return
     */
    public boolean isPullRefresh(){
        return  mAnnGroup == null ? false : mAnnGroup.isPullRefresh();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public int onCreateLayoutId(){
        return mAnnGroup == null ? 0 : mAnnGroup.getLayoutId();
    }

    @Override
    void onMainHandleDispatchMessage(Message msg) {
        if(mAnnGroup != null)mAnnGroup.onDispatchMessage(msg);
    }

    @Override
    public void onDestroy() {
        if(mAnnGroup != null)mAnnGroup.unregisterReceiver();
        super.onDestroy();
    }
}

