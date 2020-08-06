package com.liansu.boduowms.utils.function;

import java.util.List;

/**
 * @ Des:
 * @ Created by yangyiqing on 2018/12/30.
 */
public class CommonCheckUtil {


    public static boolean isEmptyOrNull(String str) {
        if (str == null || str.isEmpty()) return true;
        return false;

    }


    public static <T> boolean isListEmptyOrNull(List<T> list) {
        if (list == null || list.size() == 0) return true;
        return false;
    }


}
