package com.wz.libs.core.utils;

import android.content.ClipboardManager;
import android.content.Context;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wave.Zhang
 * @version 1.0
 * @data 2016/9/17
 */
public class StringUtils {

    public static boolean isEmpty(String text) {
        return text == null || text.equals("") || text.trim().equals("") || text.equalsIgnoreCase("null");
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(Context context,String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            //String check = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 手机号简单比较 1,开头. 满足11位
     * @param mobile
     * @return
     */
    public static boolean isSimplePhoneNumber(String mobile){
        return !StringUtils.isEmpty(mobile) && mobile.length() > 0 && mobile.length() == 11 && mobile.startsWith("1");
    }

    /**
     *
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198 </p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166 </p>
     * <p>电信：133、153、173、177、180、181、189、199 </p>
     * <p>全球星：1349 </p>
     * <p>虚拟运营商：170 </p>
     *
     * 验证手机号码
     * @param mobileNumber
     * @return
     */
    public static boolean isPhoneNumber(String mobileNumber){
        boolean flag = false;
        try{
            String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
            return mobileNumber != null && mobileNumber.length() > 0 && Pattern.matches(regex, mobileNumber);
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 默认保留一位小数
     * @param value
     * @return
     */
    public static String toDecimalFormat(Double value){
        return toDecimalFormat(value,null);
    }
    /**
     * 默认保留一位小数
     * @param value
     * @param format
     * @return
     */
    public static String toDecimalFormat(Double value,String format){
        if(format == null || "".equals(format))format = "#.0";
        if(value == null)value = 0D;
        return new DecimalFormat(format).format(value);
    }

    public static String toSimplDateFormat(int year,int month,int day){
        String months = month < 10 ? "0"+month : month+"";
        String dates = day < 10 ? "0"+day : day+"";
        return year+"-"+months+"-"+dates;
    }

    public static String toString(int value){
        return value + "";
    }

    public static String toString(Integer value){
        return value == null ? "" : value + "";
    }

    public static String toString(float value){
        return value + "";
    }

    public static String toString(Float value){
        return value == null ? "" : value + "";
    }

    public static String toString(double value){
        return value + "";
    }

    public static String toString(Double value){
        return value == null ? "" : value + "";
    }
}
