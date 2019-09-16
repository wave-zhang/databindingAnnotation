package com.wz.libs.core.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.wz.libs.core.annotations.Extra;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/11/1
 */
public class WzExtarUtils {

    /**
     * // 是不是人工合成的类?
     * //mFieldType.isSynthetic()
     * // 是不是匿名类?
     * //mFieldType.isAnonymousClass()
     *
     * @param m
     * @param mIntent
     * @param mField
     * @param mWzExtra
     * @throws Exception
     */
    public static void setExtraValue(Activity m, Intent mIntent, Field mField, Extra mWzExtra) throws Exception {
        if (mIntent == null) mIntent = m.getIntent();
        Class mFieldType = mField.getType();
        if (mFieldType.isPrimitive()) {
            WzLog.d("为基本类型 == " + mWzExtra.value() + " mFieldType == " + mFieldType);
            setPrimitiveValue(m, mIntent, mField, mWzExtra);
            return;
        }
        if (mFieldType.isArray()) {
            WzLog.d("为组数类型 == " + mWzExtra.value() + " mFieldType == " + mFieldType);
            setArrayValue(m, mIntent, mField, mWzExtra);
            return;
        }
        if (mFieldType.isAssignableFrom(ArrayList.class)) {
            WzLog.d("为集合类型 == " + mWzExtra.value() + " mFieldType == " + mFieldType);
            Type type = mField.getGenericType();
            if (type == null) return;
            //ParameterizedType // 泛型
            if (type instanceof ParameterizedType) {
                //【4】 得到泛型里的class类型对象。
                ParameterizedType pt = (ParameterizedType) type;
                Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                setExtarListVaule(m, mIntent, mField, genericClazz, mWzExtra);
                return;
            }
            return;
        }
        //CharSequence[]
        if (mFieldType == CharSequence.class) {
            ArrayList<Integer> value = mIntent.getIntegerArrayListExtra(mWzExtra.value());
            WzLog.d(" CharSequence变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        // Parcelable
        Map<String, Class<?>[]> map = getAllInterfaces(mFieldType);
        //遍历所有的接口
        for (Class<?> interfaces[] : map.values()) {
            for (Class<?> inte : interfaces) {
                WzLog.d(" interface == " + inte);
                if (inte == Parcelable.class) {
                    Parcelable value = mIntent.getParcelableExtra(mWzExtra.value());
                    WzLog.d(" Parcelable 变量 value===  " + value);
                    mField.set(m, value);
                    return;
                }
                if (inte == Serializable.class) {
                    Serializable value = mIntent.getSerializableExtra(mWzExtra.value());
                    WzLog.d(" Serializable 变量 value===  " + value + "  extraName = " + mWzExtra.value());
                    mField.set(m, value);
                    return;
                }
            }
        }
    }

    private static Map<String, Class<?>[]> getAllInterfaces(Class mFieldType) {
        Map<String, Class<?>[]> map = new HashMap<>();
        Class<?> interfaces[] = mFieldType.getInterfaces();
        if (interfaces.length > 0) map.put(mFieldType.getName(), interfaces);
        return getAllInterfaces(map, mFieldType);
    }

    private static Map<String, Class<?>[]> getAllInterfaces(Map<String, Class<?>[]> map, Class mFieldType) {
        Class<?> superClass = mFieldType.getSuperclass();
        if (superClass == null || superClass == Object.class) return map;
        Class<?> interfaces[] = superClass.getInterfaces();
        if (interfaces.length > 0) map.put(superClass.getName(), interfaces);
        getAllInterfaces(map, superClass);
        return map;
    }

    /**
     * 基本类型
     *
     * @param m
     * @param mIntent
     * @param mField
     * @param mWzExtra
     * @throws Exception
     */
    private static void setPrimitiveValue(Activity m, Intent mIntent, Field mField, Extra mWzExtra) throws Exception {
        if (mIntent == null) mIntent = m.getIntent();
        if (mField.getType() == Integer.class || mField.getType() == int.class) {
            int value = mIntent.getIntExtra(mWzExtra.value(), 0);
            WzLog.d(" int变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == String.class) {
            String value = mIntent.getStringExtra(mWzExtra.value());
            WzLog.d(" String变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Double.class || mField.getType() == double.class) {
            Double value = mIntent.getDoubleExtra(mWzExtra.value(), 0d);
            WzLog.d(" Double变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Float.class || mField.getType() == float.class) {
            float value = mIntent.getFloatExtra(mWzExtra.value(), 0f);
            WzLog.d(" float变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Byte.class || mField.getType() == byte.class) {
            byte defByte = -1;
            Byte value = mIntent.getByteExtra(mWzExtra.value(), defByte);
            WzLog.d(" Byte变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == char.class) {
            char c = 'w';
            char value = mIntent.getCharExtra(mWzExtra.value(), c);
            WzLog.d(" char变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Long.class || mField.getType() == long.class) {
            Long value = mIntent.getLongExtra(mWzExtra.value(), 0);
            WzLog.d(" Long变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Short.class || mField.getType() == short.class) {
            Short s = 0;
            Short value = mIntent.getShortExtra(mWzExtra.value(), s);
            WzLog.d(" Short变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Boolean.class || mField.getType() == boolean.class) {
            boolean value = mIntent.getBooleanExtra(mWzExtra.value(), false);
            WzLog.d(" boolean变量Value===  " + value);
            mField.set(m, value);
        }
    }

    private static void setArrayValue(Activity m, Intent mIntent, Field mField, Extra mWzExtra) throws Exception {
        if (mIntent == null) mIntent = m.getIntent();
        if (mField.getType() == int[].class || mField.getType() == Integer[].class) {
            int[] value = mIntent.getIntArrayExtra(mWzExtra.value());
            WzLog.d(" int[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == short[].class) {
            short[] value = mIntent.getShortArrayExtra(mWzExtra.value());
            WzLog.d(" byte[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == String[].class) {
            String[] value = mIntent.getStringArrayExtra(mWzExtra.value());
            WzLog.d(" String[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == Double[].class || mField.getType() == double[].class) {
            double[] value = mIntent.getDoubleArrayExtra(mWzExtra.value());
            WzLog.d(" double[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == float[].class || mField.getType() == Float[].class) {
            float[] value = mIntent.getFloatArrayExtra(mWzExtra.value());
            WzLog.d(" float[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == long[].class || mField.getType() == Long[].class) {
            long[] value = mIntent.getLongArrayExtra(mWzExtra.value());
            WzLog.d(" long[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == char[].class) {
            char[] value = mIntent.getCharArrayExtra(mWzExtra.value());
            WzLog.d(" char[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        if (mField.getType() == byte[].class) {
            byte[] value = mIntent.getByteArrayExtra(mWzExtra.value());
            WzLog.d(" byte[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        //CharSequence[]
        if (mField.getType() == CharSequence[].class) {
            ArrayList<Integer> value = mIntent.getIntegerArrayListExtra(mWzExtra.value());
            WzLog.d(" CharSequence[]变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        // Parcelable[]
        if (mField.getType() == Parcelable[].class) {
            Parcelable[] value = mIntent.getParcelableArrayExtra(mWzExtra.value());
            WzLog.d(" Parcelable[]变量Value===  " + value + " >>>>>>> " + mWzExtra.value());
            mField.set(m, value);
        }
    }

    protected static void setExtarListVaule(Activity m, Intent mIntent, Field mField, Class returnTypeClass, Extra mWzExtra) throws Exception {
        if (mIntent == null) mIntent = m.getIntent();
        // Integer
        if (returnTypeClass == Integer.class || returnTypeClass == int.class) {
            ArrayList<Integer> value = mIntent.getIntegerArrayListExtra(mWzExtra.value());
            WzLog.d("List int变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        // String
        if (returnTypeClass == String.class) {
            ArrayList<String> value = mIntent.getStringArrayListExtra(mWzExtra.value());
            WzLog.d("List String变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        // CharSequence
        if (returnTypeClass == CharSequence.class) {
            ArrayList<CharSequence> value = mIntent.getCharSequenceArrayListExtra(mWzExtra.value());
            WzLog.d("List CharSequence变量Value===  " + value);
            mField.set(m, value);
            return;
        }
        // Parcelable
        Map<String, Class<?>[]> map = getAllInterfaces(returnTypeClass);
        for (Class<?> interfaces[] : map.values()) {
            for (Class<?> inte : interfaces) {
                if (inte == Parcelable.class) {
                    ArrayList value = mIntent.getParcelableArrayListExtra(mWzExtra.value());
                    WzLog.d("List Parcelable变量Value===  " + value);
                    mField.set(m, value);
                    return;
                }
            }
        }
    }
}