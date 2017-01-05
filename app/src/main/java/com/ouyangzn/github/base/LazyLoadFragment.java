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

package com.ouyangzn.github.base;

import android.view.View;

/**
 * Created by ouyangzn on 2016/10/25.<br/>
 * Description：fragment懒加载时继承的基类
 */
public abstract class LazyLoadFragment<V extends BaseView, T extends BasePresenter<V>>
    extends BaseFragment<V, T> {

  private boolean mIsVisible;
  private boolean mIsViewLoaded;

  @Override public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    mIsVisible = isVisibleToUser;
    if (mIsVisible && !mIsViewLoaded && mContentView != null) {
      lazyInitView(mContentView);
      mIsViewLoaded = true;
    }
  }

  @Override protected final void initView(View parent) {
    if (!mIsViewLoaded && mIsVisible && mContentView != null) {
      lazyInitView(parent);
      mIsViewLoaded = true;
    }
  }

  protected abstract void lazyInitView(View parent);

  @Override protected Status getCurrentStatus() {
    return Status.STATUS_LOADING;
  }
}
