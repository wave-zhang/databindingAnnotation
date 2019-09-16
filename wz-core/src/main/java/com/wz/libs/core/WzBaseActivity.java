package com.wz.libs.core;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */

public class WzBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WzActivityManager.getInstances().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        WzActivityManager.getInstances().removeActivity(this);
        super.onDestroy();
    }

    public WzActivityManager getWzActivityManager(){
        return WzActivityManager.getInstances();
    }

}
