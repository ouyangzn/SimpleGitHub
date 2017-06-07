/*
 * Copyright (c) 2016.  ouyangzn   <ouyangzn@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ouyangzn.github.utils;

import com.ouyangzn.github.BuildConfig;

/**
 * <p>日志类,用于代替android提供的Log,以达到更好的控制日志打印的目的
 * 用{@link #LOG_LEVER}来控制最低打印的日志等级,{@link #LOG_LEVER}可在application中自由设置
 * <p>开发中调试所需打印的日志最高等级不应超过<pre>Log.d();</pre>
 * 
 * @author ouyangzn
 */
public class Log {

    public final static short VERBOSE = android.util.Log.VERBOSE;
    public final static short DEBUG = android.util.Log.DEBUG;
    public final static short INFO = android.util.Log.INFO;
    public final static short WARN = android.util.Log.WARN;
    public final static short ERROR = android.util.Log.ERROR;
    /** 打印log日志的最低等级 */
    public static short LOG_LEVER = VERBOSE;

    private Log() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /** verbose和debug等级的日志是否要打印 */
    //    public static boolean DEBUG = BuildConfig.DEBUG;
    

    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG && LOG_LEVER <= VERBOSE)
            android.util.Log.v(tag, msg);
    }
    
    public static void v(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG && LOG_LEVER <= VERBOSE)
            android.util.Log.v(tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG && LOG_LEVER <= DEBUG)
            android.util.Log.d(tag, msg);
    }
    
    public static void d(String tag, String msg, Throwable tr) {
        if (BuildConfig.DEBUG && LOG_LEVER <= DEBUG)
            android.util.Log.d(tag, msg, tr);
    }
    
    public static void i(String tag, String msg) {
        if (LOG_LEVER <= INFO || BuildConfig.DEBUG)
            android.util.Log.i(tag, msg);
    }
    
    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVER <= INFO || BuildConfig.DEBUG)
            android.util.Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        if (LOG_LEVER <= WARN || BuildConfig.DEBUG)
            android.util.Log.w(tag, msg);
    }
    
    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVER <= WARN || BuildConfig.DEBUG)
            android.util.Log.w(tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        if (LOG_LEVER <= ERROR || BuildConfig.DEBUG)
            android.util.Log.e(tag, msg);
    }
    
    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVER <= ERROR || BuildConfig.DEBUG)
            android.util.Log.e(tag, msg, tr);
    }

}
