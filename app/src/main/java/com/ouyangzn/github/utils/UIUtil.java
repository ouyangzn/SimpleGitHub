/*
 * Copyright (c) 2016.  ouyangzn   <email : ouyangzn@163.com>
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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.ImageView;

/**
 * Created by ouyangzn on 2016/10/8.<br/>
 * Description：UI操作相关工具类，包括界面（activity等）跳转
 */
public class UIUtil {

  /**
   * 给toolbar添加一张图片
   *
   * @param toolbar toolbar
   * @param resId 图片资源id
   * @param gravity 添加的位置，对应{@link Gravity#LEFT}、{@link Gravity#RIGHT}
   * @param margin 图片的四周边距{@link Toolbar.LayoutParams#setMargins(int, int, int, int)}
   * @return 被添加的ImageView
   */
  public static ImageView addImage2Toolbar(Toolbar toolbar, int resId, int gravity, int[] margin) {
    Context context = toolbar.getContext();
    ImageView img = new ImageView(context);
    img.setImageResource(resId);
    img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
        Toolbar.LayoutParams.WRAP_CONTENT);
    params.gravity = gravity | Gravity.CENTER;
    try {
      params.setMargins(margin[0], margin[1], margin[2], margin[3]);
    } catch (Exception e) {
      int margin_15 = ScreenUtil.dp2px(context, 15);
      params.setMargins(margin_15, 0, margin_15, 0);
    }
    img.setLayoutParams(params);
    toolbar.addView(img);
    return img;
  }

  /**
   * 打开一个activity
   *
   * @param currActivity 当前的activity
   * @param target 要打开的activity
   */
  public static void openActivity(Activity currActivity, Class target) {
    openActivity(currActivity, target, null);
  }

  /**
   * 打开一个activity
   *
   * @param currActivity 当前的activity
   * @param target 要打开的activity
   * @param data 要携带过去的数据
   */
  public static void openActivity(Activity currActivity, Class target, Bundle data) {
    openActivityForResult(currActivity, target, -1, data);
  }

  /**
   * 打开一个activity
   *
   * @param currActivity 当前的activity
   * @param target 要打开的activity
   * @param requestCode 请求码
   */
  public static void openActivityForResult(Activity currActivity, Class target, int requestCode) {
    openActivityForResult(currActivity, target, requestCode, null);
  }

  /**
   * 打开一个activity
   *
   * @param currActivity 当前的activity
   * @param target 要打开的activity
   * @param requestCode 请求码
   * @param data 要携带过去的数据
   */
  public static void openActivityForResult(Activity currActivity, Class target, int requestCode,
      Bundle data) {
    Intent intent = new Intent(currActivity, target);
    if (data != null) intent.putExtras(data);
    currActivity.startActivityForResult(intent, requestCode);
    //        overridePendingTransition(R.anim.anim_slide_in,
    //                R.anim.anim_slide_out);
  }
}
