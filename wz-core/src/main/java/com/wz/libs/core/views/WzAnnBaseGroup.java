package com.wz.libs.core.views;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.wz.libs.core.annotations.Extra;
import com.wz.libs.core.utils.WzExtarUtils;
import com.wz.libs.core.utils.WzLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */

public class WzAnnBaseGroup<T> {

    private T mContext;

    public WzAnnBaseGroup(T mContext){
        this.mContext = mContext;
    }

    public T getContext(){
        return mContext;
    }

    /**
     * 执行反射
     * @param method
     * @param obj
     */
    protected void onExecuteMethod(Method method, Object... obj){
        if(mContext == null)return;
        try{
            // 执行回调
            method.invoke(mContext,obj);
        }catch (Exception e){
            Log.e("test","11111");
            e.printStackTrace();
            Log.e("test","22222-->" + e.getMessage());

        }
    }

    public void onParseExtraAnnotations(Intent mIntent) {
        Field[] mFields = mContext.getClass().getDeclaredFields();
        if (mFields == null || mFields.length <= 0) {
            WzLog.e(" 当前class没有包含变量");
            return;
        }
        for (Field field : mFields) {
            field.setAccessible(true);
            WzLog.d("变量名称 = " + field.getName());
            Extra mWzExtra = field.getAnnotation(Extra.class);
            if (mWzExtra != null) {
                try {
                    WzLog.d("返回变量名称 = " + mWzExtra);
                    WzExtarUtils.setExtraValue((Activity)mContext,  mIntent, field, mWzExtra);
                } catch (Exception e) {
                    WzLog.e(e);
                }
            }
        }
    }


    public void onParseAnnotations(){}

}
