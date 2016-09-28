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
import com.ouyangzn.github.bean.apibean.SearchResult;
import com.ouyangzn.github.bean.localbean.LocalRepo;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.data.IGitHubDataSource;
import com.ouyangzn.github.data.remote.RemoteGitHubData;
import com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import com.ouyangzn.github.utils.Log;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class MainPresenter extends IMainPresenter {

  private final String TAG = MainPresenter.class.getSimpleName();

  private IGitHubDataSource mDataSource;
  private App mApp;
  private SharedPreferences mConfigSp;
  private Subscription mQueryDataSubscribe;

  public MainPresenter(Context context) {
    mApp = (App) context.getApplicationContext();
    mDataSource = new RemoteGitHubData();
    mConfigSp = mApp.getSharedPreferences(CommonConstants.ConfigSP.SP_NAME, Context.MODE_PRIVATE);
  }

  @Override protected void onDestroy() {
    mDataSource = null;
    mApp = null;
    mConfigSp = null;
    mQueryDataSubscribe = null;
  }

  @Override void queryData(SearchFactor factor, int perPage, int page) {
    if (mQueryDataSubscribe != null && !mQueryDataSubscribe.isUnsubscribed()) {
      mQueryDataSubscribe.unsubscribe();
    }
    mQueryDataSubscribe = mDataSource.queryByKeyword(factor, perPage, page)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(new Action0() {
          @Override public void call() {
            if (mView != null) mView.showProgressDialog();
          }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<SearchResult>() {
          @Override public void call(SearchResult searchResult) {
            if (mView != null) {
              mView.dismissProgressDialog();
              mView.showQueryDataResult(searchResult);
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "----------查询数据出错:" + throwable.getMessage());
            if (mView != null) {
              mView.dismissProgressDialog();
              mView.showErrorOnQueryData(mApp.getString(R.string.error_search_github));
            }
          }
        });
    addSubscription(mQueryDataSubscribe);
  }

  @Override void saveSearchFactor(SearchFactor factor) {
    Observable.just(factor).observeOn(Schedulers.io()).doOnNext(new Action1<SearchFactor>() {
      @Override public void call(SearchFactor factor) {
        mConfigSp.edit()
            .putString(CommonConstants.ConfigSP.KEY_LANGUAGE, App.getApp().getGson().toJson(factor))
            .apply();
      }
    }).subscribe();
  }

  @Override void collectRepo(final Repository repo) {
    // reaml原生态写法
    //Realm realm = mApp.getGlobalRealm();
    //realm.executeTransactionAsync(new Realm.Transaction() {
    //  @Override public void execute(Realm realm) {
    //    LocalRepo localRepo = new LocalRepo();
    //    localRepo.convert2Local(repo);
    //    realm.copyToRealmOrUpdate(localRepo);
    //  }
    //}, new Realm.Transaction.OnSuccess() {
    //  @Override public void onSuccess() {
    //    if (mView != null) {
    //      mView.showNormalTips(mApp.getString(R.string.tip_collect_success));
    //    }
    //  }
    //}, new Realm.Transaction.OnError() {
    //  @Override public void onError(Throwable error) {
    //    Log.d(TAG, "----------收藏失败：", error);
    //    if (mView != null) {
    //      mView.showErrorTips(mApp.getString(R.string.error_collect_failure));
    //    }
    //  }
    //});
    // rxJava写法
    Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(final Subscriber<? super Void> subscriber) {
        Realm realm = mApp.getGlobalRealm();
        try {
          realm.beginTransaction();
          LocalRepo localRepo = new LocalRepo();
          localRepo.collectTime = System.currentTimeMillis();
          localRepo.convert2Local(repo);
          realm.copyToRealmOrUpdate(localRepo);
          realm.commitTransaction();
          subscriber.onNext(null);
        } catch (Exception e) {
          if (realm.isInTransaction()) realm.cancelTransaction();
          subscriber.onError(e);
        }
      }
    }).subscribeOn(Schedulers.io()).subscribe(new Action1<Void>() {
      @Override public void call(Void aVoid) {
        if (mView != null) {
          mView.showNormalTips(mApp.getString(R.string.tip_collect_success));
        }
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Log.d(TAG, "----------收藏失败：", throwable);
        if (mView != null) mView.showErrorTips(mApp.getString(R.string.error_collect_failure));
      }
    });
  }

  //@Override void cancelCollectRepo(final LocalRepo repo) {
  //  Observable.create(new Observable.OnSubscribe<Void>() {
  //    @Override public void call(Subscriber<? super Void> subscriber) {
  //      mApp.getGlobalRealm().where(LocalRepo.class).equalTo("id", repo.id).findAll().deleteAllFromRealm();
  //    }
  //  }).subscribeOn(Schedulers.io()).subscribe(new Action1<Void>() {
  //    @Override public void call(Void aVoid) {
  //      if (mView != null) {
  //        mView.showNormalTips(mApp.getString(R.string.tip_collect_cancel_success));
  //      }
  //    }
  //  }, new Action1<Throwable>() {
  //    @Override public void call(Throwable throwable) {
  //      if (mView != null)
  //        mView.showErrorTips(mApp.getString(R.string.error_collect_cancel_failure));
  //    }
  //  });
  //}
}
