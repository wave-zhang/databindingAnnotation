package com.wz.libs.core;

import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.Window;

import com.wz.libs.core.views.WzAnnDataBindingActivityGroup;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/22
 */

public class WzDataBindingActivity<T extends ViewDataBinding> extends WzHandlerActivity {

    private T mViewDataBinding;

    int layoutId;

    private WzAnnDataBindingActivityGroup mAnnGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // 获取所有注解
        mAnnGroup = new WzAnnDataBindingActivityGroup(this);

        layoutId = onCreateLayoutId();
        if(layoutId <= 0)throw new RuntimeException("Layout ID is null!!!");
        mViewDataBinding = DataBindingUtil.setContentView(this,layoutId);
        // 有使用注解
        if(mAnnGroup == null)return;
        mAnnGroup.onParseAnnotations();
    }

    public boolean isWzLoginValue(){
        return mAnnGroup == null ? false : mAnnGroup.getIsloginValue();
    }

    public boolean isPullRefesh(){
        return  mAnnGroup == null ? false : mAnnGroup.isPullRefesh();
    }

    /**
     * 界面根视图
     * @return
     */
    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public WzAnnDataBindingActivityGroup getAnnotationGroup(){
        return this.mAnnGroup;
    }

    @Override
    public void onMainHandleMessage(Message msg) {
        if(mAnnGroup != null)mAnnGroup.onHandleMessage(msg);
    }

    public int onCreateLayoutId(){
        return mAnnGroup == null || mAnnGroup.mWzAnnActivity == null ? 0 : mAnnGroup.mWzAnnActivity.value();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mAnnGroup != null)mAnnGroup.onPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mAnnGroup != null)mAnnGroup.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        if(mAnnGroup != null)mAnnGroup.unregisterReceiver();
        super.onDestroy();
    }



}
