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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * dialog工具类
 *
 * @author ouyangzn
 */
public class DialogUtil {

  private DialogUtil() {
    throw new UnsupportedOperationException("cannot be instantiated");
  }

  /**
   * 显示一个进度圈
   *
   * @param context
   * @param message 对话框中的信息
   * @param cancelable 对话框能否取消
   * @return ProgressDialog
   */
  public static synchronized ProgressDialog showProgressDialog(Context context, String message,
      boolean cancelable) {
    ProgressDialog dialog = new ProgressDialog(context);
    // show需要在setContentView之前，否则会报requestFeature() must be called before adding content
    dialog.show();
    //        dialog.setContentView(R.layout.progress_dialog);
    //        ((TextView)dialog.findViewById(R.id.tv_progressbar_tips)).setText(Message);
    dialog.setMessage(message);
    dialog.setCancelable(cancelable);
    return dialog;
  }

  /**
   * 隐藏进度对话框
   */
  public static synchronized void dismissProgressDialog(ProgressDialog dialog) {
    if (dialog != null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  /**
   * 取消进度对话框,cancel时会回调OnCancelListener中的onCancel()
   */
  public static synchronized void cancelProgressDialog(ProgressDialog dialog) {
    if (dialog != null && dialog.isShowing()) {
      dialog.cancel();
    }
  }

  /**
   * 获取一个对话框AlertDialog
   *
   * @param context
   */
  public static synchronized AlertDialog.Builder getAlertDialog(Context context) {
    return new AlertDialog.Builder(context/*, R.style.BaseAlertDialog*/);
  }
}
