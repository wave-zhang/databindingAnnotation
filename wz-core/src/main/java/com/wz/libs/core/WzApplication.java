package com.wz.libs.core;

import android.app.Activity;
import android.app.Application;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */
public class WzApplication extends Application{
    /**
     * 退出应用
     */
    public void onExit(){
        try {
            WzActivityManager.getInstances().closeAllActivity();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    public void closeAllActivity(){
        WzActivityManager.getInstances().closeAllActivity();
    }

    public void closeOtherAllClassName(Class<? extends Activity> cls){
        if(cls == null)return;
        WzActivityManager.getInstances().closeOtherAll(cls);
    }

    public Activity getTopActivity(){
        return WzActivityManager.getInstances().getTopActivity();
    }
}
