package com.alibaba.chenyin.sophixtest.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenyin on 17/10/31.
 */

public class ObjectUtil {
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNull(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    public static boolean notNull(Object obj) {
        return !isNull(obj);
    }

    //判断map集合是否为空
    public static boolean isNotMapNull(HashMap hashMap) {
        boolean flag = true;
        if (hashMap == null || hashMap.size() <= 0) flag = false;
        return flag;
    }
}
