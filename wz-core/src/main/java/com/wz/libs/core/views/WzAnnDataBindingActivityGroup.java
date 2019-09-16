package com.wz.libs.core.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.NonNull;

import com.wz.libs.core.WzDataBindingActivity;
import com.wz.libs.core.annotations.Activity;
import com.wz.libs.core.annotations.Extra;
import com.wz.libs.core.annotations.Permission;
import com.wz.libs.core.models.ActivityResultInfo;
import com.wz.libs.core.models.BroadcastInfo;
import com.wz.libs.core.models.HandlerCallbackInfo;
import com.wz.libs.core.utils.StringUtils;
import com.wz.libs.core.utils.SystemUtils;
import com.wz.libs.core.utils.WzAnnUtils;
import com.wz.libs.core.utils.WzLog;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/28
 */
public class WzAnnDataBindingActivityGroup extends WzAnnBaseGroup<WzDataBindingActivity> {

    public Activity mWzAnnActivity;
    // 创建之后回调
    public Method mAfterMethod;

    public Permission mPermission;

    public Method mPermissionMethod;
    // Extras
    public ArrayList<Extra> mWzExtras;
    // Activity返回 回调
    public ArrayList<ActivityResultInfo> mWzActivityResultInfos;
    // Handler 回调
    public ArrayList<HandlerCallbackInfo> mWzHanderCallbackInfos;
    // 广播信息
    public ArrayList<BroadcastInfo> mWzBroadcastAnnInfos;

    public IntentFilter mBroIntentFilter = new IntentFilter();

    private BroadcastReceiver mBroadcastReceiver = null;

    public WzAnnDataBindingActivityGroup(WzDataBindingActivity mActivity) {
        super(mActivity);
        mWzAnnActivity = WzAnnUtils.getWzActivity(mActivity);
        WzAnnUtils.getWzAnnotationMethods(mActivity, this);
    }

    public boolean getIsloginValue() {
        return mWzAnnActivity != null && mWzAnnActivity.isLogin();
    }

    public int getLayoutId() {
        return mWzAnnActivity == null ? 0 : mWzAnnActivity.value();
    }

    public boolean isPullRefesh() {
        return mWzAnnActivity != null && mWzAnnActivity.isPullRefesh();
    }

    /**
     * 解析注解
     */
    public void onParseAnnotations() {
        onParseExtraAnnotations(null);
        onParseAfter();
        onParsePermission();
        onParseBroadcasts();
    }

    void onParseAfter() {
        if (mAfterMethod != null) {
            WzLog.d("执行 mAfterMethod ");
            onExecuteMethod(mAfterMethod, new Object[]{});
        }
    }

    public void onPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mPermission == null) return;
        if (mPermissionMethod == null) return;
        if (StringUtils.isEmpty(mPermission.value())) return;
        if (requestCode == mPermission.requestCode()) {
            onExecutePermissionCallback(SystemUtils.verifyPermissions(grantResults));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mWzActivityResultInfos == null) return;
        ActivityResultInfo mInfo = onFindWzActivityResultInfo(requestCode);
        if (mInfo == null || mInfo.isNull()) return;
        if (mInfo.mAnntation.value() == requestCode) {
            onExecuteActivityResultMethod(mInfo, data);
        }
    }

    public void onHandleMessage(Message msg) {
        WzLog.e(" WzAnnDataBindingActivityGroup -- > onHandleMessage !!! " + msg.what);
        try {
            if (onfindHandleMessageCallback(msg)) return;
            if (mWzHanderCallbackInfos == null) return;
            HandlerCallbackInfo mInfo = onFindWzHanderCallbackInfo(msg.what);
            if (mInfo == null) return;
            if (mInfo.mAnntation.value() == msg.what) {
                WzLog.e(" WzAnnDataBindingActivityGroup -- > onExecuteHandlerCallbackMethod !!! ");
                onExecuteMethod(mInfo.mMethod, msg);
            }
        } catch (Exception e) {
            WzLog.d(e);
        }
    }

    boolean onfindHandleMessageCallback(Message msg) throws Exception {
        Bundle mBundle = msg.getData();
        if (mBundle == null) return false;
        String callbackMethodName = mBundle.getString("methodName");
        if (StringUtils.isEmpty(callbackMethodName)) return false;
        Method method = getContext().getClass().getMethod(callbackMethodName, new Class[]{
                Message.class
        });
        WzLog.d("onfindHandleMessageCallback !!!!!");
        if (method == null) return false;
        onExecuteMethod(method, msg);
        return true;
    }

    private void onExecuteActivityResultMethod(ActivityResultInfo mInfo, Intent data) {
        onParseExtraAnnotations(data);
        onExecuteMethod(mInfo.mMethod, data);
    }

    /**
     * 执行 动态权限 注解
     */
    private void onParsePermission() {
        if (mPermission == null || mPermissionMethod == null) return;
        if (StringUtils.isEmpty(mPermission.value())) return;

        if (!SystemUtils.checkPermission(getContext(), mPermission.value())) {
            SystemUtils.requestPermission(getContext(), mPermission.value(), mPermission.requestCode());
            return;
        }
        onExecutePermissionCallback(true);
    }

    /**
     * 执行 Permission 回调
     *
     * @param isFlag
     */
    private void onExecutePermissionCallback(boolean isFlag) {
        if (mPermissionMethod == null) return;
        // 执行回调
        onExecuteMethod(mPermissionMethod, isFlag);
    }

    /**
     * 解除广播
     */
    public void unregisterReceiver() {
        if (getContext() == null) return;
        if (mBroadcastReceiver == null) return;
        getContext().unregisterReceiver(mBroadcastReceiver);
    }

    private boolean broadcastAnnInfoIsNull(BroadcastInfo mBroadcastInfo) {
        if (mBroadcastInfo == null) return true;
        if (mBroadcastInfo.mAnntation == null || StringUtils.isEmpty(mBroadcastInfo.mAnntation.value()) || mBroadcastInfo.mMethod == null)
            return true;
        return false;
    }

    /**
     * 解析广播注解并注册广播
     */
    private void onParseBroadcasts() {
        if (getContext() == null) return;
        if (mWzBroadcastAnnInfos == null || mWzBroadcastAnnInfos.size() <= 0) return;
        // 注册广播信息
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WzLog.d("收到新的广播....." + intent.getAction());
                onExecuteBroadcastsCallback(intent);
            }
        };

        for (int i = 0; i < mWzBroadcastAnnInfos.size(); i++) {
            BroadcastInfo mBroadcastInfo = mWzBroadcastAnnInfos.get(i);
            onParseBroadcastItem(mBroadcastInfo);
        }
        WzLog.d("注册广播信息.....");
        getContext().registerReceiver(mBroadcastReceiver, mBroIntentFilter);
    }

    /**
     * 解析广播注解并添加action
     */
    private void onParseBroadcastItem(BroadcastInfo mBroadcastInfo) {
        if (broadcastAnnInfoIsNull(mBroadcastInfo)) return;
        WzLog.d("添加一条广播Action....." + mBroadcastInfo.mAnntation.value());
        mBroIntentFilter.addAction(mBroadcastInfo.mAnntation.value());
    }

    /**
     * 广播回调
     *
     * @param intent
     */
    private void onExecuteBroadcastsCallback(Intent intent) {
        if (mWzBroadcastAnnInfos == null || mWzBroadcastAnnInfos.size() <= 0) return;
        String action = intent.getAction();
        BroadcastInfo mBroadcastInfo = onFindWzBroadcastAnnInfo(action);
        if (mBroadcastInfo != null) onExecuteBroadcastItemCallback(mBroadcastInfo, intent);
    }

    /**
     * 执行某个广播回调
     *
     * @param mBroadcastInfo
     * @param intent
     */
    private void onExecuteBroadcastItemCallback(BroadcastInfo mBroadcastInfo, Intent intent) {
        if (broadcastAnnInfoIsNull(mBroadcastInfo)) return;
        onParseExtraAnnotations(intent);
        onExecuteMethod(mBroadcastInfo.mMethod, intent);
    }

    /**
     * 查找某个广播
     *
     * @param action
     * @return
     */
    private BroadcastInfo onFindWzBroadcastAnnInfo(String action) {
        int size = mWzBroadcastAnnInfos.size();
        for (int i = 0; i < size; i++) {
            BroadcastInfo mBroadcastInfo = mWzBroadcastAnnInfos.get(i);
            if (broadcastAnnInfoIsNull(mBroadcastInfo)) break;

            if (mBroadcastInfo.mAnntation.value().equals(action)) {
                return mWzBroadcastAnnInfos.get(i);
            }
        }
        return null;
    }

    /**
     * 查找WzActivityResultInfo
     *
     * @param code
     * @return
     */
    private ActivityResultInfo onFindWzActivityResultInfo(int code) {
        int size = mWzActivityResultInfos.size();
        for (int i = 0; i < size; i++) {
            ActivityResultInfo mInfo = mWzActivityResultInfos.get(i);
            if (mInfo == null || mInfo.isNull()) continue;
            if (mInfo.mAnntation.value() == code) {
                return mInfo;
            }
        }
        return null;
    }

    /**
     * 查找WzActivityResultInfo
     *
     * @param code
     * @return
     */
    private HandlerCallbackInfo onFindWzHanderCallbackInfo(int code) {
        int size = mWzHanderCallbackInfos.size();
        for (int i = 0; i < size; i++) {
            HandlerCallbackInfo mInfo = mWzHanderCallbackInfos.get(i);
            if (mInfo == null || mInfo.isNull()) continue;
            if (mInfo.mAnntation.value() == code) {
                return mInfo;
            }
        }
        return null;
    }
}
