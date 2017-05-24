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

package com.ouyangzn.github.module.main;

import android.content.Context;
import android.content.SharedPreferences;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.data.ICollectData;
import com.ouyangzn.github.data.IGitHubData;
import com.ouyangzn.github.data.local.LocalCollectData;
import com.ouyangzn.github.data.remote.RemoteGitHubData;
import com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.RxJavaUtil;
import com.ouyangzn.github.utils.SpUtil;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ouyangzn.github.utils.SpUtil.KEY_LANGUAGE;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class MainPresenter extends IMainPresenter {

  private final String TAG = MainPresenter.class.getSimpleName();

  private LifecycleProvider<FragmentEvent> mProvider;
  private IGitHubData mDataSource;
  private ICollectData mCollectData;

  private App mApp;
  private SharedPreferences mConfigSp;
  private Subscription mQueryDataSubscribe;

  public MainPresenter(Context context, LifecycleProvider<FragmentEvent> provider) {
    mProvider = provider;
    mApp = (App) context.getApplicationContext();
    mDataSource = new RemoteGitHubData();
    mCollectData = new LocalCollectData();
    mConfigSp = SpUtil.getSp(context);
  }

  @Override protected void onDestroy() {
    mDataSource = null;
    mCollectData = null;
  }

  @Override public void queryData(SearchFactor factor) {
    if (mQueryDataSubscribe != null && !mQueryDataSubscribe.isUnsubscribed()) {
      mQueryDataSubscribe.unsubscribe();
    }
    mQueryDataSubscribe = mDataSource.queryByKeyword(factor, factor.limit, factor.page)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(() -> {
          mView.setLoadingIndicator(true);
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        // 使用rxlifecycle，自由指定取消订阅的时间点
        .compose(mProvider.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(searchResult -> {
          mView.setLoadingIndicator(false);
          mView.showQueryDataResult(searchResult);
        }, throwable -> {
          Log.e(TAG, "----------查询数据出错:" + throwable.getMessage());
          mView.setLoadingIndicator(false);
          mView.showErrorOnQueryData(mApp.getString(R.string.error_search_github));
        });
  }

  @Override public void saveSearchFactor(SearchFactor factor) {
    Observable.just(factor)
        .observeOn(Schedulers.io())
        .doOnNext(factor1 -> mConfigSp.edit().putString(KEY_LANGUAGE,
                App.getApp().getGson().toJson(factor1))
            .apply())
        .subscribe();
  }

  @Override public void collectRepo(final Repository repo) {
    RxJavaUtil.wrap(Observable.defer(() -> {
      CollectedRepo collectedRepo = new CollectedRepo();
      collectedRepo.convert(repo);
      collectedRepo.setCollectTime(System.currentTimeMillis());
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
