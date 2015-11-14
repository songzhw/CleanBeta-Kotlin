package cn.six.payx.util;

/**
 * @author songzhw
 * @date 2015/11/14
 * Copyright 2015 Six. All rights reserved.
 */
public class JniUtil {

    public static native String getKey();
    public native int getNo();

    static{
        System.loadLibrary("firstNdk");
    }


}
