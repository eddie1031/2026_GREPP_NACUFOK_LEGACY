package io.eddie.common.util;


public class StringUtil {

    public static String lpad(String str, int len, char pad) {
        if (str == null) str = "";
        if (str.length() >= len) return str;
        StringBuffer sb = new StringBuffer(len);
        for (int i = str.length(); i < len; i++) {
            sb.append(pad);
        }
        return sb.append(str).toString();
    }

    public static String rpad(String str, int len, char pad) {
        if (str == null) str = "";
        if (str.length() >= len) return str;
        StringBuffer sb = new StringBuffer(len).append(str);
        for (int i = str.length(); i < len; i++) {
            sb.append(pad);
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
