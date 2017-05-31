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

package com.ouyangzn.github.module.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.view.InputEdit;

/**
 * Created by ouyangzn on 2017/5/27.<br/>
 * Description：登录
 */
public class LoginFragment extends BaseFragment {

  @BindView(R.id.input_login_username) InputEdit mInputUsername;
  @BindView(R.id.input_login_password) InputEdit mInputPassword;

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_login;
  }

  @Override protected void initData(Bundle savedInstanceState) {

  }

  @Override protected void initView(View parent) {
    requestNoToolbar();
  }

  @Override public BasePresenter initPresenter() {
    return null;
  }

  @OnClick({ R.id.btn_login, R.id.tv_login_just_browsing }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_login: {
        String username = mInputUsername.getInputText().trim();
        String password = mInputPassword.getInputText().trim();
        if (TextUtils.isEmpty(username)) {
          toast(R.string.error_username_null);
          return;
        }
        if (TextUtils.isEmpty(password)) {
          toast(R.string.error_password_null);
          return;
        }
        App.login(username, password);
        onLogin();
        break;
      }
      case R.id.tv_login_just_browsing: {
        showInputUsernameDialog(view -> onLogin());
        break;
      }
    }
  }

  private void onLogin() {
    FragmentActivity activity = getActivity();
    activity.setResult(Activity.RESULT_OK);
    activity.finish();
  }

  /**
   * 显示输入用户名的dialog
   *
   * @param onConfirmClick 点确定的回调
   */
  private void showInputUsernameDialog(View.OnClickListener onConfirmClick) {
    AlertDialog.Builder builder = DialogUtils.getAlertDialog(getContext());
    AlertDialog dialog = builder.setView(R.layout.dialog_input_view).create();
    dialog.show();
    TextView tvTitle = ButterKnife.findById(dialog, R.id.tv_dialog_input_title);
    tvTitle.setText(R.string.username_github);
    EditText etUsername = ButterKnife.findById(dialog, R.id.et_dialog_input);
    String user = App.getUsername();
    etUsername.setText(user);
    if (!TextUtils.isEmpty(user)) {
      etUsername.setSelection(user.length());
    }
    Button btnConfirm = ButterKnife.findById(dialog, R.id.btn_dialog_input_confirm);
    btnConfirm.setTag(dialog);
    btnConfirm.setOnClickListener(v -> {
      ScreenUtils.hideKeyBoard(v);
      dialog.dismiss();
      String username = etUsername.getText().toString().trim();
      if (TextUtils.isEmpty(username)) {
        toast(R.string.error_username_null);
        return;
      }
      App.setUsername(username);
      if (onConfirmClick != null) onConfirmClick.onClick(v);
    });
    Button btnCancel = ButterKnife.findById(dialog, R.id.btn_dialog_input_cancel);
    btnCancel.setTag(dialog);
    btnCancel.setOnClickListener(v -> {
      ScreenUtils.hideKeyBoard(v);
      dialog.dismiss();
    });
  }
}
