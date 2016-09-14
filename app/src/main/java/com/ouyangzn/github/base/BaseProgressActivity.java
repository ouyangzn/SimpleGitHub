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

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ouyangzn.github.R;

/**
 * Created by ouyangzn on 2016/9/7.<br/>
 * Description：
 */
public abstract class BaseProgressActivity<V, T extends BasePresenter<V>> extends BaseActivity {

  public static final int STATUS_NORMAL = 1;
  public static final int STATUS_LOADING = 2;
  //private ViewGroup mContentView;
  //private View mLoadingView;
  protected T mPresenter;
  @BindView(R.id.layout_content) ViewGroup mContentView;
  @BindView(R.id.layout_loading) View mLoadingView;

  @Override public void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_base_content);
    ButterKnife.bind(this);
    getLayoutInflater().inflate(setContentView(), mContentView, true);

    mPresenter = initPresenter();
    if (mPresenter != null) {
      mPresenter.onAttach((V) this);
    }
    super.onCreate(savedInstanceState);
  }

  public abstract T initPresenter();

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

  /**
   * 设置自身的内容View
   *
   * @return layoutResId viewId
   */
  protected abstract int setContentView();
}
