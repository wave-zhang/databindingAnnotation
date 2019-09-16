package com.wz.libs.core.utils;

import android.view.View;

import com.wz.libs.core.WzDataBindingActivity;
import com.wz.libs.core.annotations.Activity;
import com.wz.libs.core.annotations.ActivityResult;
import com.wz.libs.core.annotations.After;
import com.wz.libs.core.annotations.Broadcast;
import com.wz.libs.core.annotations.Fragment;
import com.wz.libs.core.annotations.HandlerCallback;
import com.wz.libs.core.annotations.Layout;
import com.wz.libs.core.annotations.Permission;
import com.wz.libs.core.models.ActivityResultInfo;
import com.wz.libs.core.models.BroadcastInfo;
import com.wz.libs.core.models.HandlerCallbackInfo;
import com.wz.libs.core.views.WzAnnDataBindingActivityGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/27
 */

public class WzAnnUtils {

    public static Activity getWzActivity(android.app.Activity mActivity){
        Annotation[] mAnnotations = mActivity.getClass().getAnnotations();
        if(mAnnotations == null || mAnnotations.length <= 0){
            return null;
        }
        Activity mWzAnnActivity = null;
        for (Annotation annotation : mAnnotations){
            if(annotation instanceof Activity){
                mWzAnnActivity = (Activity)annotation;
            }
        }
        return mWzAnnActivity;
    }

    public static Fragment getWzAnnFragment(androidx.fragment.app.Fragment mFragment){
        Annotation[] mAnnotations = mFragment.getClass().getAnnotations();
        if(mAnnotations == null || mAnnotations.length <= 0){
            return null;
        }
        Fragment mWzAnnFragment = null;
        for (Annotation annotation : mAnnotations){
            if(annotation instanceof Fragment){
                mWzAnnFragment = (Fragment)annotation;
            }
        }
        return mWzAnnFragment;
    }

    public static Method getWzAnnAfter(View mView){
        Method[] methods = mView.getClass().getDeclaredMethods();
        if(methods == null || methods.length < 0)return null;
        for(Method method : methods){
            if(method == null)continue;
            WzLog.d(" method name is "+method.getName());
            method.setAccessible(true);
            After mWzAnnAfter = method.getAnnotation(After.class);
            // 找到对应回调方法
            if(mWzAnnAfter != null){
                return method;
            }
        }
        return null;
    }


    public static Layout getWzLayout(View mView){
        Annotation[] mAnnotations = mView.getClass().getAnnotations();
        if(mAnnotations == null || mAnnotations.length <= 0){
            return null;
        }
        Layout mWzLayout = null;
        for (Annotation annotation : mAnnotations){
            if(annotation instanceof Layout){
                mWzLayout = (Layout)annotation;
            }
        }
        return  mWzLayout;
    }


    /**
     * 找回调方式
     * @param mActivity
     * @return
     */
    public static Method getWzPermission(android.app.Activity mActivity){
        Method[] methods = mActivity.getClass().getDeclaredMethods();
        if(methods == null || methods.length < 0)return null;
        for(Method method : methods){
            if(method == null)continue;
            WzLog.d(" method name is "+method.getName());
            method.setAccessible(true);
            Permission permission = method.getAnnotation(Permission.class);
            // 找到对应回调方法
            if(permission != null){
                return method;
            }
        }
        return null;
    }

    public static WzAnnDataBindingActivityGroup getWzAnnotationMethods(WzDataBindingActivity mActivity, WzAnnDataBindingActivityGroup mInfo){
        Method[] methods = mActivity.getClass().getDeclaredMethods();
        if(methods == null || methods.length < 0) return null;
        for(Method method : methods){
            if(method == null)continue;
            WzLog.d(" method name is "+method.getName());
            method.setAccessible(true);

            After after = method.getAnnotation(After.class);

            if(after != null){
                mInfo.mAfterMethod = method;
            }

            Permission permission = method.getAnnotation(Permission.class);
            // 找到对应回调方法
            if(permission != null){
                mInfo.mPermission = permission;
                mInfo.mPermissionMethod = method;
            }
            ActivityResult activityResult = method.getAnnotation(ActivityResult.class);
            // 找到对应回调方法
            if(activityResult != null){

                if(mInfo.mWzActivityResultInfos == null)mInfo.mWzActivityResultInfos = new ArrayList<>();
                ActivityResultInfo mActivityResultInfo = new ActivityResultInfo();
                mActivityResultInfo.mAnntation = activityResult;
                mActivityResultInfo.mMethod = method;
                mInfo.mWzActivityResultInfos.add(mActivityResultInfo);
            }

            HandlerCallback handerCallback = method.getAnnotation(HandlerCallback.class);
            // 找到对应回调方法
            if(handerCallback != null){
                if(mInfo.mWzHanderCallbackInfos == null)mInfo.mWzHanderCallbackInfos = new ArrayList<>();

                HandlerCallbackInfo mHandlerCallbackInfo = new HandlerCallbackInfo();
                mHandlerCallbackInfo.mAnntation = handerCallback;
                mHandlerCallbackInfo.mMethod = method;
                mInfo.mWzHanderCallbackInfos.add(mHandlerCallbackInfo);

            }

            Broadcast broadcastAction = method.getAnnotation(Broadcast.class);
            // 找到对应回调方法
            if(broadcastAction != null){
                if(mInfo.mWzBroadcastAnnInfos == null)mInfo.mWzBroadcastAnnInfos = new ArrayList<>();

                BroadcastInfo mBroAnnInfo = new BroadcastInfo();
                mBroAnnInfo.mAnntation = broadcastAction;
                mBroAnnInfo.mMethod = method;
                mInfo.mWzBroadcastAnnInfos.add(mBroAnnInfo);
            }
        }
        return mInfo;
    }

}
