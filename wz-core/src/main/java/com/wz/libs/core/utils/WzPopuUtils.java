package com.wz.libs.core.utils;

import android.content.Context;
import androidx.databinding.ViewDataBinding;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/9/21
 */
public class WzPopuUtils {

    public static PopupWindow createPopuWindow(Context m, ViewDataBinding mViewDataBingding){
        //View contentView = LayoutInflater.from(m).inflate(resLayoutId,null);
        //DataBindingUtil.inflate(LayoutInflater.from(m),resId,null,false)
        PopupWindow popupWindow = new PopupWindow(mViewDataBingding.getRoot(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(0);
        //popupWindow.getContentView().start
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 全透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        //ColorDrawable dw = new ColorDrawable(0x00000000);
        // 实例化一个ColorDrawable颜色为半透明
        popupWindow.setBackgroundDrawable(dw);
        return popupWindow;
    }
}
