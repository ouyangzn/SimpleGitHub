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
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import butterknife.ButterKnife;
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
   * @param context Context
   */
  public static synchronized AlertDialog.Builder getAlertDialog(Context context) {
    return getAlertDialog(context, R.style.BaseAlertDialog);
  }

  /**
   * 获取一个对话框AlertDialog
   *
   * @param context Context
   * @param style dialog的样式
   */
  public static synchronized AlertDialog.Builder getAlertDialog(Context context,
      @StyleRes int style) {
    return new AlertDialog.Builder(context, style);
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
   * @param listener 点击选项的监听
   */
  public static ListPopupWindow getListPopupWindow(Context context, List<String> items,
      ListPopupWindow.OnItemClickListener listener) {
    return new ListPopupWindow(context, items, listener);
  }

  /**
   * 显示一个可输入对话框
   *
   * @param activity 所在的activity
   * @param title 输入框title
   * @param contentHint 输入框提示信息
   * @param content 输入框内容,可为null
   * @param onConfirmClick 点击确定
   * @param onCancelClick 点击取消
   */
  public static void showInputDialog(Activity activity, String title, String contentHint,
      String content, OnConfirmClickListener onConfirmClick, OnClickListener onCancelClick) {
    AlertDialog.Builder builder = DialogUtils.getAlertDialog(activity, R.style.DialogNoBackground);
    AlertDialog dialog = builder.setView(R.layout.dialog_input_view).create();
    dialog.show();
    TextView tvTitle = ButterKnife.findById(dialog, R.id.tv_dialog_input_title);
    tvTitle.setText(title);
    EditText editText = ButterKnife.findById(dialog, R.id.et_dialog_input);
    editText.setHint(contentHint);
    if (!TextUtils.isEmpty(content)) {
      editText.setText(content);
      editText.setSelection(content.length());
    }
    Button btnConfirm = ButterKnife.findById(dialog, R.id.btn_dialog_input_confirm);
    btnConfirm.setTag(dialog);
    btnConfirm.setOnClickListener(v -> {
      if (onConfirmClick != null) onConfirmClick.onConfirm(dialog, editText.getText().toString());
    });
    Button btnCancel = ButterKnife.findById(dialog, R.id.btn_dialog_input_cancel);
    btnCancel.setTag(dialog);
    btnCancel.setOnClickListener(v -> {
      ScreenUtils.hideKeyBoard(v);
      dialog.dismiss();
      if (onCancelClick != null) onCancelClick.onClick(v);
    });
  }

  public interface OnConfirmClickListener {
    /**
     * 点击确定按钮的回调
     *
     * @param dialog dialog
     * @param content 填写的内容
     */
    void onConfirm(AlertDialog dialog, String content);
  }
}
