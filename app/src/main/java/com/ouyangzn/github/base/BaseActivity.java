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

package com.ouyangzn.github.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.ouyangzn.github.App;
import com.trello.navi.component.support.NaviAppCompatActivity;

/**
 * @author Ouyang
 * Description：Activity基类,包含一些activity的通用操作
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends NaviAppCompatActivity
    implements BaseView<T> {

  protected String TAG = "BaseActivity";
  protected App mApp;
  protected Context mContext;
  protected T mPresenter;
  private com.ouyangzn.github.utils.Toast mToast;

  @Override protected final void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(getContentView());
    ButterKnife.bind(this);
    TAG = this.getClass().getSimpleName();
    mApp = (App) getApplication();
    mContext = this;
    mToast = com.ouyangzn.github.utils.Toast.getInstance(mContext);
    mPresenter = initPresenter();
    if (mPresenter != null) {
      mPresenter.onAttach((V) this);
    }
    initData();
    initView(savedInstanceState);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (mPresenter != null) {
      mPresenter.onDetach();
    }
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    // 点击界面隐藏软键盘
    if (null != this.getCurrentFocus()) {
      InputMethodManager mInputMethodManager =
          (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
          0);
    }
    return super.onTouchEvent(event);
  }

  public abstract T initPresenter();

  protected abstract int getContentView();

  /** 初始化数据 */
  protected abstract void initData();

  /** 初始化界面及控件 */
  protected abstract void initView(Bundle savedInstanceState);

  protected void toast(String content) {
    mToast.show(content, Toast.LENGTH_SHORT);
  }

  protected void toast(int resId) {
    mToast.show(resId, Toast.LENGTH_SHORT);
  }

  @Override public void finish() {
    super.finish();
    //        overridePendingTransition(R.anim.anim_push_in,
    //                R.anim.anim_push_out);
  }

  /**
   * 数据等加载指示器,默认空实现
   *
   * @param isActive 是否正在处理
   */
  @Override public void setLoadingIndicator(boolean isActive) {
  }
}
