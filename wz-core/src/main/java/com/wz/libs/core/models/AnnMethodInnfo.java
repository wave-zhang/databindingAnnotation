package com.wz.libs.core.models;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/10/29
 */

public abstract class AnnMethodInnfo<T extends Annotation> {

    public T mAnntation;

    public Method mMethod;

    public boolean isMethodNull(){
        if(mMethod == null)return true;
        return false;
    }

    public abstract boolean isNull();

}