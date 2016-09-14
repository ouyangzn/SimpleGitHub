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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 各种格式化
 */
public class Formatter {

  public static final String FORMAT_YYYY = "yyyy";
  public static final String FORMAT_MM_DD = "MM-dd";
  public static final String FORMAT_MM_DD_CHINA = "MM月dd日";
  public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
  public static final String FORMAT_YYYY_MM_DD_CHINA = "yyyy年MM月dd日";
  public static final String FORMAT_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
  public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

  /**
   * 格式化时间
   *
   * @param date 时间
   * @param format 格式
   */
  public static String formatDate(Date date, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    return sdf.format(date);
  }

  /**
   * 格式化时间
   *
   * @param time 时间
   * @param format 格式
   */
  public static String formatDate(long time, String format) {
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    return sdf.format(new Date(time));
  }

  /**
   * 将日期转换为time
   *
   * @param date 时间字符串
   * @param format 时间格式
   * @return millisecond
   */
  public static long date2time(String date, String format) {
    long time = 0L;
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
    try {
      Date d = sdf.parse(date);
      time = d.getTime();
    } catch (ParseException e) {
      Log.e("Formatter", "---------将日期转换为time出错:", e);
    }
    return time;
  }
}
