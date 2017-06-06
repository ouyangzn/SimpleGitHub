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
package com.ouyangzn.github.module.collect;

import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.data.ICollectDataSource;
import com.ouyangzn.github.data.local.CollectLocalDataSourceSource;
import com.ouyangzn.github.module.collect.CollectionsContract.ICollectionsPresenter;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.RxJavaUtils;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import rx.Observable;
import rx.Subscription;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：
 */
public class CollectionsPresenter extends ICollectionsPresenter {

  private final String TAG = CollectionsPresenter.class.getSimpleName();

  private ICollectDataSource mCollectData;
  private LifecycleProvider<FragmentEvent> mProvider;

  private Subscription mQueryAllSub;
  private Subscription mQueryByKeySub;

  public CollectionsPresenter(LifecycleProvider<FragmentEvent> provider) {
    mProvider = provider;
    mCollectData = new CollectLocalDataSourceSource();
  }

  @Override public void queryByKey(String key, int page, int limit) {
    if (mQueryByKeySub != null && mQueryByKeySub.isUnsubscribed()) mQueryByKeySub.unsubscribe();
    mQueryByKeySub = RxJavaUtils.wrapFragment(
        Observable.defer(() -> Observable.just(mCollectData.queryByKeyword(key, page, limit))),
            mProvider)
        .subscribe(results -> {
          mView.showCollectQueryByKey(results);
        }, error -> {
          Log.e(TAG, "----------查询收藏,queryByKey出错：", error);
          mView.showQueryByKeyFailure(App.getApp().getString(R.string.error_search_failure));
        });
  }

  @Override public void queryCollect(int page, int limit) {
    if (mQueryAllSub != null && mQueryAllSub.isUnsubscribed()) {
      mQueryAllSub.unsubscribe();
    }
    mQueryAllSub = RxJavaUtils.wrapFragment(
        Observable.defer(() -> Observable.just(mCollectData.queryCollectRepo(page, limit))),
        mProvider).subscribe(repoList -> mView.showCollect(repoList), error -> {
      Log.e(TAG, "----------查询收藏,queryByKey出错：", error);
      mView.showErrorOnQueryFailure(App.getApp().getString(R.string.error_search_failure));
        });
  }

  @Override public void cancelCollectRepo(CollectedRepo repo) {
    RxJavaUtils.wrapFragment(
        Observable.defer(() -> Observable.just(mCollectData.cancelRepo(repo.getId()))),
        mProvider).subscribe(success -> {
      if (success) {
        mView.showCollectionCanceled(repo);
      } else {
        mView.showCollectionCancelFailure();
      }
    }, error -> {
      Log.e(TAG, "取消收藏出错：", error);
      mView.showCollectionCancelFailure();
    });
  }

  @Override protected void onDestroy() {
    mCollectData = null;
  }

}
