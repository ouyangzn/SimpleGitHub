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

package com.ouyangzn.github.module.repodetail;

import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.data.ICollectDataSource;
import com.ouyangzn.github.data.local.CollectLocalDataSourceSource;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.RxJavaUtils;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import rx.Observable;

/**
 * Created by ouyangzn on 2017/6/14.<br/>
 * Description：
 */
public class RepoDetailPresenter extends RepoDetailContract.IRepoDetailPresenter {

  private final String TAG = RepoDetailPresenter.class.getSimpleName();

  private LifecycleProvider<FragmentEvent> mProvider;
  private ICollectDataSource mCollectData;

  public RepoDetailPresenter(LifecycleProvider<FragmentEvent> provider) {
    mProvider = provider;
    mCollectData = new CollectLocalDataSourceSource();
  }

  @Override protected void onDestroy() {

  }

  @Override public void collectRepo(Repository repo) {
    RxJavaUtils.wrap(Observable.defer(() -> {
      CollectedRepo collectedRepo = CollectedRepo.convert(repo);
      if (repo.getCollectTime() == 0) {
        collectedRepo.setCollectTime(System.currentTimeMillis());
      }
      return Observable.just(mCollectData.collectRepo(collectedRepo));
    })).subscribe(success -> {
      if (success) {
        mView.showCollected();
      } else {
        Log.e(TAG, "----------未知原因收藏失败-----------");
        mView.showCollectedFailure();
      }
    }, error -> {
      Log.e(TAG, "----------收藏失败：", error);
      mView.showCollectedFailure();
    });
  }
}
