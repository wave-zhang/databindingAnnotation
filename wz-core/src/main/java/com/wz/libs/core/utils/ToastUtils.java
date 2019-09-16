package com.wz.libs.core.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 15/7/15
 */
public class ToastUtils {

    public static Toast mToast;

    public static void showToaskMsg(Context c,String msg){
        if(mToast == null){
            mToast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showToastLongMsg(Context c,String msg){
        if(mToast == null){
            mToast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        }else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showToaskMsg(Context c,int res){
        showToaskMsg(c,c.getString(res));
    }
}
