package com.xd11z.myserver.util;

public class StringUtil {

    public static String removePointZero(String str) {
        if (str == null) {
            // 处理异常情况，返回原字符串或空字符串
            return "";
        }
        //从后往前找小数点
        int index = str.lastIndexOf('.');
        //找到了返回找到位置的前一位，没找到返回原字符串
        if(index == -1) {
            return str;
        } else {
            return str.substring(0, index);
        }
    }
}
