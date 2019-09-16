package com.wz.libs.core;

import android.app.Activity;
import com.wz.libs.core.utils.WzLog;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/22
 */

public class WzActivityManager {

    private Map<String,Activity> mActivitys = new TreeMap<>();

    private WzActivityManager(){}

    static WzActivityManager mManager;

    public static WzActivityManager getInstances(){
        if(mManager == null)mManager = new WzActivityManager();
        return mManager;
    }

    public void closeOtherAll(Activity m){
        Iterator titer = mActivitys.entrySet().iterator();
        while(titer.hasNext()){
            Map.Entry ent = (Map.Entry)titer.next();
            if(ent.getKey() != m.getClass().getName()){
                Activity mActivity = (Activity)ent.getValue();
                if (mActivity != null && !mActivity.isFinishing())mActivity.finish();
            }
        }
        mActivitys.clear();
        addActivity(m);
    }

    public void closeOtherAll(Class<? extends Activity> cls){
        Iterator titer = mActivitys.entrySet().iterator();
        while(titer.hasNext()){
            Map.Entry ent = (Map.Entry)titer.next();
            WzLog.e("closeOtherAll cls = "+cls.getName() +" ent.getKey() = "+ent.getKey().toString());
            if(ent.getKey() != cls.getName()){
                Activity mActivity = (Activity)ent.getValue();
                if (mActivity != null && !mActivity.isFinishing())mActivity.finish();
            }
        }
    }

    /**
     * 关闭所有界面
     */
    public void closeAllActivity(){
        Iterator titer = mActivitys.entrySet().iterator();
        while(titer.hasNext()){
            Map.Entry ent = (Map.Entry)titer.next();
            //String mName = ent.getKey().toString();
            Activity mActivity = (Activity)ent.getValue();
            if (mActivity != null && !mActivity.isFinishing())mActivity.finish();
        }
        mActivitys.clear();
    }

    public void addActivity(Activity mActivity){
        WzLog.e(" addActivity : "+mActivity.getClass().getName());
        mActivitys.put(mActivity.getClass().getName(),mActivity);
    }

    public void removeActivity(Activity mActivity){
        WzLog.e(" removeActivity : "+mActivity.getClass().getName());
        mActivitys.remove(mActivity.getClass().getName());
    }

    public Activity getTopActivity(){
        if(mActivitys == null || mActivitys.size() == 0)
            return null;
        Iterator titer = mActivitys.entrySet().iterator();
        if(titer.hasNext()){
            return (Activity) ((Map.Entry)titer.next()).getValue();
        }
        return null;
    }
}
