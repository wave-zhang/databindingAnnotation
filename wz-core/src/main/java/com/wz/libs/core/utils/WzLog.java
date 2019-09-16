package com.wz.libs.core.utils;

import android.database.SQLException;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

/**
 * 
 * CLog.java
 * @author Wave.Zhang
 * @date 2013-3-7
 * @version 1.0
 * 日志工具类
 **/
public class WzLog {
	
	private static boolean IS_DEBUG = false;
	
	private static final String mTag = "WzLog";
	
	public static void setDebug(boolean isDebug){
		IS_DEBUG = isDebug;
	}
	
	public static boolean getDebug(){
		return IS_DEBUG;
	}
	
	public static void d(String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.d(mTag, msg);
	}
	
	public static void e(String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.e(mTag, msg);
	}
	
	public static void i(String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.i(mTag, msg);
	}
	
	public static void w(String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.w(mTag, msg);
	}
	
	public static void d(String tag,String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.d(tag, msg);
	}
	
	public static void e(String tag,String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.e(tag, msg);
	}
	
	public static void i(String tag,String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.i(tag, msg);
	}
	
	public static void w(String tag,String msg){
		if(IS_DEBUG && !TextUtils.isEmpty(msg))Log.w(tag, msg);
	}
	
	public static void d(Exception e){
		if(IS_DEBUG && e != null)d(getExceptionString(e));
	}
	
	public static void e(Exception e){
		if(IS_DEBUG && e != null)e(getExceptionString(e));
	}
	
	public static void i(Exception e){
		if(IS_DEBUG && e != null)i(getExceptionString(e));
	}
	
	public static void printStackTrace(Exception e) {
		if (IS_DEBUG && e != null)e.printStackTrace();
	}
	public static void printStackTrace(Throwable e) {
		if (IS_DEBUG && e != null)e.printStackTrace();
	}

	public static void printStackTrace(IOException e) {
		if (IS_DEBUG && e != null)e.printStackTrace();
	}

	public static void printStackTrace(SQLException e) {
		if (IS_DEBUG && e != null)e.printStackTrace();
	}
	
	public static void w(Exception e){
		if(IS_DEBUG)w(getExceptionString(e));
	}
	
	public static void d(String tag,Exception e){
		if(IS_DEBUG)d(tag,getExceptionString(e));
	}
	
	public static void e(String tag,Exception e){
		if(IS_DEBUG)e(tag,getExceptionString(e)); 
	}
	
	public static void i(String tag,Exception e){
		if(IS_DEBUG)i(tag,getExceptionString(e));
	}
	
	public static void w(String tag,Exception e){
		if(IS_DEBUG)w(tag,getExceptionString(e));
	}
	
	/**
	 * 将异常信息转为定符串
	 * @param e
	 * @return
	 */
	private static String getExceptionString(Exception e) {
		StringBuffer sb = new StringBuffer("");
		try {
			StackTraceElement[] s = e.getStackTrace();
			for (StackTraceElement s1 : s) {
				sb.append("\t\n").append(s1);
			}
			sb.append("\n").append(e.getMessage()).append(e.getCause());
		} catch (Exception es) {
			es.printStackTrace();
		}
		return sb.toString();
	}

    public static void println(String msg){
        if(getDebug())System.out.println(msg);
    }
	
}
