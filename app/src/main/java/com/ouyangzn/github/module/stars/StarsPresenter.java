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

package com.ouyangzn.github.module.stars;

import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.data.IStarsDataSource;
import com.ouyangzn.github.data.remote.StarsRemoteDataSource;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.RxJavaUtil;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;

/**
 * Created by ouyangzn on 2017/5/24.<br/>
 * Description：
 */
public class StarsPresenter extends StarsContract.IStarsPresenter {

  private static final String TAG = StarsPresenter.class.getSimpleName();

  private LifecycleProvider<FragmentEvent> mProvider;
  private IStarsDataSource mStarsDataSource;

  public StarsPresenter(LifecycleProvider<FragmentEvent> provider) {
    mProvider = provider;
    mStarsDataSource = new StarsRemoteDataSource();
  }

  @Override protected void onDestroy() {
    mStarsDataSource = null;
    mProvider = null;
  }

  @Override public void queryMineStars(int page, int limit) {
    RxJavaUtil.wrapFragment(mStarsDataSource.querySomeoneStars(App.getUsername(), page, limit),
        mProvider).subscribe(result -> mView.showStars(result), error -> {
      Log.e(TAG, "查询我的star出错：", error);
      mView.showOnQueryStarsFail(App.getApp().getString(R.string.error_network_error));
    });
  }
}
