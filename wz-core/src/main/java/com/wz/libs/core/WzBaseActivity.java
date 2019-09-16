package com.wz.libs.core;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.wz.libs.core.utils.SysUtils;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */

public class WzBaseActivity extends AppCompatActivity {

    private boolean isOpenWeakLock = false;//是否加锁，不让设备休眠
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WzActivityManager.getInstances().addActivity(this);
    }

    public void openWakeLock(){
        isOpenWeakLock = true;
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(wakeLock == null){
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WzBaseActivity.class.getName());
        }
        wakeLock.acquire();
    }

    @Override
    protected void onDestroy() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        WzActivityManager.getInstances().removeActivity(this);
        super.onDestroy();
    }

    public WzActivityManager getWzActivityManager(){
        return WzActivityManager.getInstances();
    }


    // ------------------------------------------ 读写权限判别 ------------------------------------------

    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUEST_EXTERNAL_STORAGE_WRITE = 1316;

    private OnRequestPermission mOnRequestPermission;

    public interface OnRequestPermission {
        void agreeStoragePermission();
        void denyStoragePermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_WRITE://请求获取位置权限
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mOnRequestPermission != null){
                        mOnRequestPermission.agreeStoragePermission();
                    }
                } else {
                    if(mOnRequestPermission != null){
                        mOnRequestPermission.denyStoragePermission();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setOnRequestPermission(OnRequestPermission mOnRequestPermission) {
        this.mOnRequestPermission = mOnRequestPermission;
    }

    /**
     * 跳转到miui的权限管理页面
     */
    public void gotoMiuiPermission(int permission) {
        // 只兼容miui v5/v6 的应用权限设置页面，否则的话跳转应用设置页面（权限设置上一级页面）
        Intent intent = null;
        try {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", getPackageName());
            startActivityForResult(intent, permission);
        }catch (ActivityNotFoundException e){
            try {
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", getPackageName());
                startActivityForResult(intent, permission);
            }catch (ActivityNotFoundException e1){
                startActivityForResult(getAppDetailSettingIntent(), permission);
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission(int permission) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            startActivityForResult(intent, permission);
        } catch (Exception e) {
            e.printStackTrace();
            startActivityForResult(getAppDetailSettingIntent(), permission);
        }
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission(int permission) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            startActivityForResult(intent, permission);
        } catch (Exception e) {
            e.printStackTrace();
            startActivityForResult(getAppDetailSettingIntent(), permission);
        }

    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }

    /**
     * 判断是否有读取文件的权限
     */
    public void setOpenExternalRW() {
        if (Build.VERSION.SDK_INT > 22) {
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE_WRITE);
            } else { //说明已经获取到摄像头权限了
                if (mOnRequestPermission != null) {
                    mOnRequestPermission.agreeStoragePermission();
                }
            }
        } else { //这个说明系统版本在6.0之下，不需要动态获取权限。
            if (mOnRequestPermission != null) {
                mOnRequestPermission.agreeStoragePermission();
            }
        }
    }


    public void gotoManagePermission(int permission){
        if(SysUtils.getCurSys().equals(SysUtils.SYS_EMUI)){
            gotoHuaweiPermission(permission);
        }else if(SysUtils.getCurSys().equals(SysUtils.SYS_FLYME)){
            gotoMeizuPermission(permission);
        }else if(SysUtils.getCurSys().equals(SysUtils.SYS_MIUI)){
            gotoMiuiPermission(permission);
        }else {
            startActivityForResult(getAppDetailSettingIntent(), permission);
        }
    }

    public void openSettingPermission(int permission){
        startActivityForResult(getAppDetailSettingIntent(), permission);
    }

    public void openGpsPermission(int locationPermission){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try
        {
            startActivityForResult(intent, locationPermission);
        } catch(ActivityNotFoundException ex)
        {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                startActivityForResult(intent, locationPermission);
            } catch (Exception e) {
            }
        }
    }
}
