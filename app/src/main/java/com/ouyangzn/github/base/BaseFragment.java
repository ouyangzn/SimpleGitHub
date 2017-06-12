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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.ouyangzn.github.R;
import com.trello.navi.component.support.NaviFragment;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.navi.NaviLifecycle;

public abstract class BaseFragment<V extends BaseView, T extends BasePresenter<V>>
    extends NaviFragment implements BaseView<T> {

  // 使用rxLifecycle方便控制rxJava事件的取消订阅时机
  protected final LifecycleProvider<FragmentEvent> mProvider =
      NaviLifecycle.createFragmentLifecycleProvider(this);

  protected String TAG = "BaseFragment";
  protected T mPresenter;
  protected LayoutInflater mInflater;
  protected View mContentView;
  protected View mLoadingView;
  protected View mErrorView;
  protected Toolbar mToolbar;
  private ViewGroup mRootView;
  private ViewGroup mContentContainer;
  private com.ouyangzn.github.utils.Toast mToast;
  private Status mStatus;
  private Unbinder mUnbinder;

  @Override public final void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    TAG = this.getClass().getSimpleName();
    mToast = com.ouyangzn.github.utils.Toast.getInstance(getContext());
    mPresenter = initPresenter();
    initData(savedInstanceState);
  }

  @Nullable @Override
  public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mInflater = inflater;
    mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_base_content, container, false);
    mContentContainer = (ViewGroup) mRootView.findViewById(R.id.layout_content_container);
    mLoadingView = mRootView.findViewById(R.id.stub_loading);
    mErrorView = mRootView.findViewById(R.id.stub_error);
    mContentView = inflater.inflate(getContentView(), container, false);
    if (mContentView == null) throw new UnsupportedOperationException("contentView == null");
    mContentContainer.addView(mContentView);
    mToolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
    requestToolbarOverlay(false);
    if (mStatus == null) {
      Status status = getCurrentStatus();
      switchStatus(status == null ? Status.STATUS_NORMAL : status);
    } else {
      switchStatus(mStatus);
    }
    mUnbinder = ButterKnife.bind(this, mContentView);
    return mRootView;
  }

  @Override public final void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (mPresenter != null) {
      mPresenter.onAttach((V) this);
    }
    initView(mContentView);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mPresenter != null) mPresenter.onDetach();
    mUnbinder.unbind();
  }

  public void requestToolbarOverlay(boolean overlay) {
    if (overlay) {
      mContentContainer.setPadding(0, 0, 0, 0);
    } else {
      mContentContainer.setPadding(0, getResources().getDimensionPixelSize(R.dimen.height_toolbar),
          0, 0);
    }
  }

  public void requestNoToolbar() {
    mToolbar.setVisibility(View.GONE);
    requestToolbarOverlay(true);
  }

  protected void setLoadingView(View loadingView) {
    loadingView.setVisibility(mLoadingView.getVisibility());
    mContentContainer.removeView(mLoadingView);
    mContentContainer.addView(loadingView);
    mLoadingView = loadingView;
  }

  protected void setErrorView(View errorView) {
    errorView.setVisibility(mErrorView.getVisibility());
    mContentContainer.removeView(mErrorView);
    mContentContainer.addView(errorView);
    mErrorView = errorView;
  }

  /**
   * 切换当前fragment状态
   *
   * @param status 当前状态
   * @see Status
   */
  protected void switchStatus(Status status) {
    mStatus = status;
    switch (status) {
      case STATUS_NORMAL:
        hideAllView();
        mContentView.setVisibility(View.VISIBLE);
        break;
      case STATUS_LOADING:
        hideAllView();
        mLoadingView.setVisibility(View.VISIBLE);
        break;
      case STATUS_ERROR:
        hideAllView();
        mErrorView.setVisibility(View.VISIBLE);
        break;
    }
  }

  protected void toast(String content) {
    mToast.show(content, Toast.LENGTH_SHORT);
  }

  protected void toast(int resId) {
    mToast.show(resId, Toast.LENGTH_SHORT);
  }

  /**
   * 当前状态是加载中还是其他的···,会根据状态显示对应的view
   *
   * @return 状态, 如果直接显示content，状态为{@link Status#STATUS_NORMAL}
   * @see Status
   */
  protected abstract Status getCurrentStatus();

  protected abstract int getContentView();

  public abstract T initPresenter();

  /**
   * 初始化一些数据,此时view还未创建完，
   * 如果是拿到数据马上显示的操作，应放到{@link #initView(View)}中
   *
   * @param savedInstanceState
   */
  protected abstract void initData(Bundle savedInstanceState);

  protected abstract void initView(View parent);

  private void hideAllView() {
    int childCount = mContentContainer.getChildCount();
    for (int i = 0; i < childCount; i++) {
      mContentContainer.getChildAt(i).setVisibility(View.GONE);
    }
  }

  /**
   * 默认空实现
   *
   * @param isActive 是否正在处理
   */
  @Override public void setLoadingIndicator(boolean isActive) {
  }

  /**
   * 返回键事件
   *
   * @return 是否继续传播
   */
  public boolean onBackPressed() {
    return false;
  }

  /**
   * fragment的数据等加载状态
   */
  public enum Status {
    STATUS_NORMAL, STATUS_LOADING, STATUS_ERROR
  }
}
