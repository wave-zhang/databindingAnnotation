package com.wz.libs.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 15/8/17
 */
public class WzNetworkUtils {

    public static boolean isWifi(Context m){
        if(m == null){
            return false;
        }
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) m.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
            return false;
        }
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return true;
        }
        return true;
    }

    public static boolean isNetWorkConnect(Context m){
        ConnectivityManager mConnectivityManager = (ConnectivityManager)m.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mInfo = mConnectivityManager.getActiveNetworkInfo();
        if(mInfo == null){
            return false;
        }
        if(!mInfo.isAvailable()){
            return false;
        }
        return true;
    }

}
