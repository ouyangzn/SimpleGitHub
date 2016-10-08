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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;

/**
 * Created by ouyangzn on 2016/9/7.<br/>
 * Description：
 */
public abstract class BaseProgressActivity<V, T extends BasePresenter<V>>
    extends AppCompatActivity {

  public static final int STATUS_NORMAL = 1;
  public static final int STATUS_LOADING = 2;
  protected String TAG = "BaseProgressActivity";
  protected App mApp;
  protected Context mContext;
  protected T mPresenter;
  private ViewGroup mContentView;
  private View mLoadingView;
  private com.ouyangzn.github.utils.Toast mToast;

  @Override public final void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_content);
    mContentView = ButterKnife.findById(this, R.id.layout_content);
    mLoadingView = ButterKnife.findById(this, R.id.layout_loading);
    getLayoutInflater().inflate(getContentView(), mContentView, true);
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

    setContentView(R.layout.activity_base_content);
    mContentView = ButterKnife.findById(this, R.id.layout_content);
    mLoadingView = ButterKnife.findById(this, R.id.layout_loading);
    getLayoutInflater().inflate(getContentView(), mContentView, true);
    ButterKnife.bind(this);
  }

  public abstract T initPresenter();

  protected abstract int getContentView();

  /** 初始化数据 */
  protected abstract void initData();

  /** 初始化界面及控件 */
  protected abstract void initView(Bundle savedInstanceState);

  /**
   * 切换当前显示的contentView
   *
   * @param status 当前的状态{@link #STATUS_LOADING}、{@link #STATUS_NORMAL}
   */
  protected void switchContent(int status) {
    switch (status) {
      case STATUS_NORMAL:
        mContentView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        break;
      case STATUS_LOADING:
        mContentView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        break;
    }
  }

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

}
