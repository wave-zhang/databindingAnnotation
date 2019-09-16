package com.wz.libs.core.models;

import com.wz.libs.core.annotations.HandlerCallback;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/28
 */

public class HandlerCallbackInfo extends AnnMethodInnfo<HandlerCallback> {

    @Override
    public boolean isNull() {
        if(mAnntation == null)return true;
        return isMethodNull();
    }
}
