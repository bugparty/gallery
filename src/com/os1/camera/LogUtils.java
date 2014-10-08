package com.os1.camera;

/**
 * Created by bowman on 13-10-30.
 */
public class LogUtils {
    public static final String LogTagPrefix = "Campus";
    public static final int LogTagPrefixLen = LogTagPrefix.length();
    public static final int MaxLogTagLen = 23;

    /**
     * 用于调试单个Activity,通常继承在BaseActivity中
     *
     * @param str 要显示的名字
     * @return 应用前缀+要显示的名字，长度不超过MaxLogTagLen
     */
    public static String makeLogTag(String str) {
        if (str.length() + LogTagPrefixLen < MaxLogTagLen) {
            return LogTagPrefix + str;

        } else
            return LogTagPrefix + str.substring(0, MaxLogTagLen - LogTagPrefixLen + 1);

    }

    /**
     * 用于调试单个Activity,通常继承在BaseActivity中
     *
     * @param cls 当前Activity的Class
     * @return 应用前缀+当前Activity的名字，长度不超过MaxLogTagLen
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

}
