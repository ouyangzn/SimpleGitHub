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

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.ouyangzn.github.R;
import com.ouyangzn.github.view.ListPopupWindow;
import java.util.List;

/**
 * dialog工具类
 *
 * @author ouyangzn
 */
public class DialogUtils {

  private DialogUtils() {
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
    return new AlertDialog.Builder(context, R.style.BaseAlertDialog);
  }

  /**
   * 获取一个PopupWindow
   *
   * @param contentView 内容view
   * @return PopupWindow
   */
  public static synchronized PopupWindow getPopupWindow(View contentView) {
    PopupWindow mPopupWindow = new PopupWindow(contentView);
    mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
    mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    mPopupWindow.setOutsideTouchable(true);
    mPopupWindow.setFocusable(true);
    mPopupWindow.setAnimationStyle(R.style.PopupWindowLoadAnimation);
    return mPopupWindow;
  }

  /**
   * 显示一个选择项PopupWindow
   *
   * @param context Context
   * @param items 选项
   * @param anchorView 挂靠的view
   * @param listener 点击选项的监听
   */
  public static ListPopupWindow showListPop(Context context, List<String> items, View anchorView,
      ListPopupWindow.OnItemClickListener listener) {
    ListPopupWindow window = new ListPopupWindow(context, items, listener);
    //window.showAsDropDown(anchorView, ScreenUtils.dp2px(context, -4), anchorView.getHeight() / 2);
    // 我也不知道为什么是anchorView.getBottom() * 3 / 2，但是这样确实显示在了这个控件下方
    window.showAtLocation(anchorView, Gravity.TOP | Gravity.RIGHT, ScreenUtils.dp2px(context, 4),
        anchorView.getBottom() * 3 / 2);
    return window;
  }

}
