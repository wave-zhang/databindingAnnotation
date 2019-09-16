package com.wz.libs.core.models;

import com.wz.libs.core.annotations.ActivityResult;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/28
 */

public class ActivityResultInfo extends AnnMethodInnfo<ActivityResult> {

    @Override
    public boolean isNull() {
        if(mAnntation == null)return true;
        return isMethodNull();
    }
}
