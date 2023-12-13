package com.xd11z.myserver.util;

public class StringUtil {
    public static String removeLastNChars(String str, int n) {
        if (str == null || n <= 0 || str.length() <= n) {
            // 处理异常情况，返回原字符串或空字符串
            return "";
        }

        return str.substring(0, str.length() - n);
    }
}
