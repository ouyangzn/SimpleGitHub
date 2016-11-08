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
import com.ouyangzn.github.base.CommonConstants;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.data.IGitHubData;
import com.ouyangzn.github.data.remote.RemoteGitHubData;
import com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import com.ouyangzn.github.utils.Log;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import io.realm.Realm;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class MainPresenter extends IMainPresenter {

  private final String TAG = MainPresenter.class.getSimpleName();

  private LifecycleProvider<FragmentEvent> mProvider;
  private IGitHubData mDataSource;
  private App mApp;
  private Realm mRealm;
  private SharedPreferences mConfigSp;
  private Subscription mQueryDataSubscribe;

  public MainPresenter(Context context, LifecycleProvider<FragmentEvent> provider) {
    mProvider = provider;
    mApp = (App) context.getApplicationContext();
    mRealm = mApp.getGlobalRealm();
    mDataSource = new RemoteGitHubData();
    mConfigSp = mApp.getSharedPreferences(CommonConstants.ConfigSP.SP_NAME, Context.MODE_PRIVATE);
  }

  @Override protected void onDestroy() {
    if (mRealm != null) {
      mRealm.close();
      mRealm = null;
    }
  }

  @Override public void queryData(SearchFactor factor, int perPage, int page) {
    if (mQueryDataSubscribe != null && !mQueryDataSubscribe.isUnsubscribed()) {
      mQueryDataSubscribe.unsubscribe();
    }
    mQueryDataSubscribe = mDataSource.queryByKeyword(factor, perPage, page)
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
    //addSubscription(mQueryDataSubscribe);
  }

  @Override public void saveSearchFactor(SearchFactor factor) {
    Observable.just(factor)
        .observeOn(Schedulers.io())
        .doOnNext(factor1 -> mConfigSp.edit()
            .putString(CommonConstants.ConfigSP.KEY_LANGUAGE,
                App.getApp().getGson().toJson(factor1))
            .apply())
        .subscribe();
  }

  @Override public void collectRepo(final Repository repo) {
    // realm原生态写法
    mRealm.executeTransactionAsync(bgRealm -> {
      CollectedRepo collectedRepo = new CollectedRepo();
      collectedRepo.convert(repo);
      collectedRepo.collectTime = System.currentTimeMillis();
      bgRealm.copyToRealmOrUpdate(collectedRepo);
    }, () -> {
      mView.showCollected();
    }, error -> {
      Log.d(TAG, "----------收藏失败：", error);
      mView.showCollectedFailure();
    });
    // rxJava写法
    //Observable.create((Observable.OnSubscribe<Void>) subscriber -> {
    //  try {
    //    mRealm.beginTransaction();
    //    CollectedRepo collectedRepo = new CollectedRepo();
    //    collectedRepo.collectTime = System.currentTimeMillis();
    //    collectedRepo.convert(repo);
    //    mRealm.copyToRealmOrUpdate(collectedRepo);
    //    mRealm.commitTransaction();
    //    subscriber.onNext(null);
    //    subscriber.onCompleted();
    //  } catch (Exception e) {
    //    if (mRealm.isInTransaction()) mRealm.cancelTransaction();
    //    subscriber.onError(e);
    //  }
    //}).subscribeOn(AndroidSchedulers.mainThread()).subscribe(aVoid -> {
    //  mView.showCollected();
    //}, throwable -> {
    //  Log.d(TAG, "----------收藏失败：", throwable);
    //  mView.showCollectedFailure();
    //});
  }

}
