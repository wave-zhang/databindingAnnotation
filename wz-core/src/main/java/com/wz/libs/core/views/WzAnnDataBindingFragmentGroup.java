package com.wz.libs.core.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;

import com.wz.libs.core.annotations.After;
import com.wz.libs.core.annotations.Broadcast;
import com.wz.libs.core.annotations.Fragment;
import com.wz.libs.core.annotations.HandlerCallback;
import com.wz.libs.core.models.BroadcastInfo;
import com.wz.libs.core.models.HandlerCallbackInfo;
import com.wz.libs.core.utils.StringUtils;
import com.wz.libs.core.utils.WzAnnUtils;
import com.wz.libs.core.utils.WzLog;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */
// 所有注解
public class WzAnnDataBindingFragmentGroup extends WzAnnBaseGroup<androidx.fragment.app.Fragment> {

    public Fragment mAnnFragment;

    public Method mAfterMethod;

    public ArrayList<HandlerCallbackInfo> mHandlerCallbackMethods;

    //广播信息
    public ArrayList<BroadcastInfo> mWzBroadcastAnnInfos;

    public WzAnnDataBindingFragmentGroup(androidx.fragment.app.Fragment mContext) {
        super(mContext);
        mAnnFragment = WzAnnUtils.getWzAnnFragment(mContext);

        Method[] methods = getContext().getClass().getDeclaredMethods();

        if(methods == null || methods.length < 0)return;
        for(Method method : methods){
            if(method == null)continue;
            WzLog.d(" method name is "+method.getName());
            method.setAccessible(true);

            After after = method.getAnnotation(After.class);
            // 找到对应回调方法
            if(after != null){
                mAfterMethod = method;
            }

            HandlerCallback mHandlerAnnotation = method.getAnnotation(HandlerCallback.class);
            // 找到对应回调方法
            if(mHandlerAnnotation != null){
                if(mHandlerCallbackMethods == null)mHandlerCallbackMethods = new ArrayList<>();
                HandlerCallbackInfo mHandlerCallbackInfo = new HandlerCallbackInfo();
                mHandlerCallbackInfo.mAnntation = mHandlerAnnotation;
                mHandlerCallbackInfo.mMethod = method;
                mHandlerCallbackMethods.add(mHandlerCallbackInfo);
            }

            Broadcast broadcastAction = method.getAnnotation(Broadcast.class);
            // 找到对应回调方法
            if(broadcastAction != null){
                if(mWzBroadcastAnnInfos == null)mWzBroadcastAnnInfos = new ArrayList<>();

                BroadcastInfo mBroAnnInfo = new BroadcastInfo();
                mBroAnnInfo.mAnntation = broadcastAction;
                mBroAnnInfo.mMethod = method;
                mWzBroadcastAnnInfos.add(mBroAnnInfo);
            }
        }
    }

    public int getLayoutId(){
        return mAnnFragment == null ? 0 : mAnnFragment.value();
    }

    public boolean isPullRefresh(){
        return mAnnFragment == null ? false : mAnnFragment.isPullRefesh();
    }

    public void onDispatchMessage(Message msg) {
        if(msg == null)return;
        if(mHandlerCallbackMethods == null)return;
        HandlerCallbackInfo mInfo = onFindWzHanderCallbackInfo(msg.what);
        if(mInfo == null)return;
        if(mInfo.mAnntation.value() == msg.what){
            onExecuteMethod(mInfo.mMethod,msg);
        }
    }

    /**
     * 查找WzActivityResultInfo
     * @param code
     * @return
     */
    private HandlerCallbackInfo onFindWzHanderCallbackInfo(int code){
        int size = mHandlerCallbackMethods.size();
        for (int i = 0; i < size; i++) {
            HandlerCallbackInfo mInfo = mHandlerCallbackMethods.get(i);
            if(mInfo == null || mInfo.isNull())continue;
            if(mInfo.mAnntation.value() == code){
                return mInfo;
            }
        }
        return null;
    }

    // ------------------------------广播信息
    private BroadcastReceiver mBroadcastReceiver = null;

    private IntentFilter mBroIntentFilter = new IntentFilter();
    /**
     * 解除广播
     */
    public void unregisterReceiver(){
        if(getContext() == null)return;
        if(mBroadcastReceiver == null)return;
        getContext().getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    private boolean broadcastAnnInfoIsNull(BroadcastInfo mBroadcastInfo) {
        if(mBroadcastInfo == null)return true;
        if(mBroadcastInfo.mAnntation == null || StringUtils.isEmpty(mBroadcastInfo.mAnntation.value()) || mBroadcastInfo.mMethod == null)return true;
        return false;
    }

    /**
     * 解析广播注解并注册广播
     */
    private void onParseBroadcasts(){
        if(getContext() == null)return;
        if(mWzBroadcastAnnInfos == null || mWzBroadcastAnnInfos.size() <= 0)return;


        // 注册广播信息
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                WzLog.d("收到新的广播....."+intent.getAction());
                onExecuteBroadcastsCallback(intent);
            }
        };

        for (int i = 0; i < mWzBroadcastAnnInfos.size(); i++) {
            BroadcastInfo mBroadcastInfo = mWzBroadcastAnnInfos.get(i);
            onParseBroadcastItem(mBroadcastInfo);
        }
        WzLog.d("注册广播信息.....");
        getContext().getActivity().registerReceiver(mBroadcastReceiver,mBroIntentFilter);
    }
    /**
     * 解析广播注解并添加action
     */
    private void onParseBroadcastItem(BroadcastInfo mBroadcastInfo){
        if(broadcastAnnInfoIsNull(mBroadcastInfo))return;
        WzLog.d("添加一条广播Action....."+mBroadcastInfo.mAnntation.value());
        mBroIntentFilter.addAction(mBroadcastInfo.mAnntation.value());
    }

    /**
     * 广播回调
     * @param intent
     */
    private void onExecuteBroadcastsCallback(Intent intent){
        if(mWzBroadcastAnnInfos == null || mWzBroadcastAnnInfos.size() <= 0)return;
        String action = intent.getAction();
        BroadcastInfo mBroadcastInfo = onFindWzBroadcastAnnInfo(action);
        if(mBroadcastInfo != null)onExecuteBroadcastItemCallback(mBroadcastInfo,intent);
    }

    /**
     * 执行某个广播回调
     * @param mBroadcastInfo
     * @param intent
     */
    private void onExecuteBroadcastItemCallback(BroadcastInfo mBroadcastInfo, Intent intent){
        if(broadcastAnnInfoIsNull(mBroadcastInfo))return;
        onExecuteMethod(mBroadcastInfo.mMethod,intent);
    }

    /**
     * 查找某个广播
     * @param action
     * @return
     */
    private BroadcastInfo onFindWzBroadcastAnnInfo(String action){
        int size = mWzBroadcastAnnInfos.size();
        for (int i = 0; i < size; i++) {
            BroadcastInfo mBroadcastInfo = mWzBroadcastAnnInfos.get(i);
            if(broadcastAnnInfoIsNull(mBroadcastInfo))break;

            if(mBroadcastInfo.mAnntation.value().equals(action)){
                return mWzBroadcastAnnInfos.get(i);
            }
        }
        return null;
    }

    protected void onParseAfter(){
        if(mAfterMethod != null)onExecuteMethod(mAfterMethod,new Object[]{});
    }

    @Override
    public void onParseAnnotations() {
        onParseAfter();
        onParseBroadcasts();
    }
}
