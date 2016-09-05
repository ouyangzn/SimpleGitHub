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

package com.ouyangzn.topgithub.utils;

import android.content.Context;

/**
 * Created by Ouyang on 2015/11/30.
 * Description: 保持只有一个Toast实例，避免接二连三的显示多个toast
 */
public class Toast {

  private static android.widget.Toast toast;
  private static Toast mToast;

  private Toast() {
  }

  public synchronized static Toast getInstance(Context context) {
    if (toast == null) {
      toast = android.widget.Toast.makeText(context.getApplicationContext(), null,
          android.widget.Toast.LENGTH_SHORT);
      mToast = new Toast();
    }
    return mToast;
  }

  /**
   * show a toast
   *
   * @param duration {@link android.widget.Toast#LENGTH_SHORT} 或者  {@link
   * android.widget.Toast#LENGTH_LONG}
   * @param text
   */
  public void show(CharSequence text, int duration) {
    toast.setText(text);
    toast.setDuration(duration);
    toast.show();
  }

  /**
   * show a toast
   *
   * @param duration {@link android.widget.Toast#LENGTH_SHORT} 或者  {@link
   * android.widget.Toast#LENGTH_LONG}
   * @param resId
   */
  public void show(int resId, int duration) {
    toast.setText(resId);
    toast.setDuration(duration);
    toast.show();
  }
}
