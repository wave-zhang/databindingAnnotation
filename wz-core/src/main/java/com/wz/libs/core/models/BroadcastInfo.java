package com.wz.libs.core.models;

import com.wz.libs.core.annotations.Broadcast;
import com.wz.libs.core.utils.StringUtils;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/28
 */

public class BroadcastInfo extends AnnMethodInnfo<Broadcast> {
    /**
     * 只要有一项不存在,则为null
     * @return
     */
    @Override
    public boolean isNull() {
        if(mAnntation == null || StringUtils.isEmpty(mAnntation.value()))return true;
        return isMethodNull();
    }
}
