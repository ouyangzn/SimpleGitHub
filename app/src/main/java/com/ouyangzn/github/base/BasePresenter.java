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

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by ouyangzn on 2016/7/1.<br/>
 * Description：Presenter的基类，如果有使用rxJava进行耗时操作，需调用{@link #addSubscription(Subscription)}
 * @since 2016/11/8 改为使用rxLifecycle来自由控制取消订阅
 */
public abstract class BasePresenter<T> {

  protected T mView;
  private CompositeSubscription mSubscriptions = new CompositeSubscription();

  public void onAttach(T view) {
    this.mView = view;
  }

  public void onDetach() {
    // 使用rxLifecycle来自由进行取消订阅的控制
    // 先取消订阅，然后再置空view，就不会出现刚好出结果但是view被置空了的情况
    mSubscriptions.clear();
    this.mView = null;
    this.onDestroy();
  }

  /**
   * 对应的view销毁时调用的清理资源方法
   */
  protected abstract void onDestroy();

  /**
   * @param subscription
   * @see <a href="https://github.com/trello/RxLifecycle">RxLifecycle</a>
   * @deprecated 使用rxLifecycle来自由进行取消订阅的控制
   */
  @Deprecated() protected void addSubscription(Subscription subscription) {
    this.mSubscriptions.add(subscription);
  }
}
