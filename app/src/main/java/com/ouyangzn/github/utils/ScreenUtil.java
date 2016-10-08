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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 屏幕、软键盘、分辨率单位转换相关的辅助类
 *
 * @author ouyangzn
 */
public class ScreenUtil {

  private static final String TAG = ScreenUtil.class.getSimpleName();

  private ScreenUtil() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * dp转px
   *
   * @param context
   * @param dpVal dp值
   */
  public static int dp2px(Context context, float dpVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
        context.getResources().getDisplayMetrics());
  }

  /**
   * sp转px
   *
   * @param context
   * @param spVal sp值
   */
  public static int sp2px(Context context, float spVal) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
        context.getResources().getDisplayMetrics());
  }

  /**
   * px转dp
   *
   * @param context
   * @param pxVal px值
   */
  public static float px2dp(Context context, float pxVal) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (pxVal / scale);
  }

  /**
   * px转sp
   *
   * @param context
   * @param pxVal px值
   */
  public static float px2sp(Context context, float pxVal) {
    return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
  }

  /**
   * 获取屏幕宽度
   *
   * @param context
   */
  public static int getScreenWidth(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    return outMetrics.widthPixels;
  }

  /**
   * 获取屏幕高度
   *
   * @param context
   */
  public static int getScreenHeight(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    return outMetrics.heightPixels;
  }

  /**
   * 获得状态栏的高度
   *
   * @param context
   * @return 出错时返回0
   */
  public static int getStatusHeight(Context context) {
    int statusHeight = 0;
    try {
      Class<?> clazz = Class.forName("com.android.internal.R$dimen");
      Object object = clazz.newInstance();
      int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
      statusHeight = context.getResources().getDimensionPixelSize(height);
    } catch (Exception e) {
      Log.e(TAG, "获得状态栏的高度出错", e);
    }
    return statusHeight;
  }

  /**
   * 获取当前屏幕截图，包含状态栏
   *
   * @param activity
   */
  public static Bitmap snapShotWithStatusBar(Activity activity) {
    View view = activity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap bmp = view.getDrawingCache();
    int width = getScreenWidth(activity);
    int height = getScreenHeight(activity);
    Bitmap bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
    view.destroyDrawingCache();
    return bp;
  }

  /**
   * 获取当前屏幕截图，不包含状态栏
   *
   * @param activity
   */
  public static Bitmap snapShotWithoutStatusBar(Activity activity) {
    View view = activity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap bmp = view.getDrawingCache();
    Rect frame = new Rect();
    activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    int statusBarHeight = frame.top;

    int width = getScreenWidth(activity);
    int height = getScreenHeight(activity);
    Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
    view.destroyDrawingCache();
    return bp;
  }

  /**
   * 隐藏软键盘
   *
   * @param view 当前界面的view
   */
  public static void hideKeyBoard(View view) {
    InputMethodManager inputManager =
        (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  /**
   * 显示软键盘
   *
   * @param view 当前界面的view
   */
  public static void showKeyBoard(View view) {
    InputMethodManager inputManager =
        (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.showSoftInput(view, 0);
  }
}
