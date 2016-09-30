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

import android.content.Context;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.module.collect.CollectContract.ICollectPresenter;
import com.ouyangzn.github.utils.Log;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import rx.Subscription;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：
 */
public class CollectPresenter extends ICollectPresenter {

  private final String TAG = CollectPresenter.class.getSimpleName();

  private App mApp;
  private Realm mRealm;
  private Subscription mQueryCollectSub;
  private RealmResults<CollectedRepo> mCollectList;
  private RealmChangeListener<RealmResults<CollectedRepo>> mCollectChangeListener;

  public CollectPresenter(Context context) {
    mApp = (App) context.getApplicationContext();
    mRealm = mApp.getGlobalRealm();
    mCollectChangeListener = new RealmChangeListener<RealmResults<CollectedRepo>>() {
      @Override public void onChange(RealmResults<CollectedRepo> element) {
        if (mView != null) {
          // todo 加上分页
          //element.subList()
          ArrayList<CollectedRepo> list = new ArrayList<>();
          list.addAll(element);
          mView.showCollect(list);
        }
      }
    };
  }

  @Override void queryAllCollect() {
    mCollectList =
        mRealm.where(CollectedRepo.class).findAllSortedAsync("collectTime", Sort.DESCENDING);
    mCollectList.addChangeListener(mCollectChangeListener);
    // ----->mCollectList.addChangeListener(mCollectChangeListener);不能在rxJava的io线程调用，暂时不知道怎么解决
    //if (mQueryCollectSub != null && mQueryCollectSub.isUnsubscribed()) mQueryCollectSub.unsubscribe();
    //mQueryCollectSub = Observable.create(new Observable.OnSubscribe<Void>() {
    //  @Override public void call(Subscriber<? super Void> subscriber) {
    //    Log.d(TAG, "------------------queryAllCollect--------------------------");
    //    Realm realm = mApp.getGlobalRealm();
    //    try {
    //      realm.beginTransaction();
    //      mCollectList = realm.where(CollectedRepo.class).findAllSorted("collectTime", Sort.DESCENDING);
    //      mCollectList.addChangeListener(mCollectChangeListener);
    //      Log.d(TAG, "------------------queryAllCollect.mCollectList = " + mCollectList);
    //      realm.commitTransaction();
    //      subscriber.onNext(null);
    //    } catch (Exception e) {
    //      subscriber.onError(e);
    //      if (realm.isInTransaction()) realm.cancelTransaction();
    //    }
    //  }
    //})
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(RxJavaUtil.<Void>discardResult(), new Action1<Throwable>() {
    //      @Override public void call(Throwable throwable) {
    //        Log.e(TAG, "----------查询已收藏repo出错：", throwable);
    //        if (mView != null) mView.showErrorOnQueryFailure();
    //      }
    //    });
  }

  @Override void cancelCollectRepo(final CollectedRepo repo) {
    try {
      // 由于realm在主线程创建，所以只能在主线程操作
      mRealm.executeTransaction(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          realm.where(CollectedRepo.class).equalTo("id", repo.id).findAll().deleteAllFromRealm();
          if (mView != null) {
            mView.showNormalTips(mApp.getString(R.string.tip_collect_cancel_success));
          }
        }
      });
    } catch (Exception e) {
      Log.e(TAG, "---------取消收藏失败:", e);
      if (mView != null) {
        mView.showErrorTips(mApp.getString(R.string.error_collect_cancel_failure));
      }
    }
    // -------------------错误的方式，会抛异常-----------------------
    //mRealm.executeTransactionAsync(new Realm.Transaction() {
    //  @Override public void execute(Realm realm) {
    //    Log.d(TAG, "---------------------------------------------");
    //    // 此操作也不行，会抛异常：Realm access from incorrect thread
    //    repo.collectTime = 0;
    //    // 此操作不会抛异常
    //    CollectedRepo collectedRepo = new CollectedRepo();
    //    collectedRepo.id = 1;
    //    realm.copyToRealm(collectedRepo);
    //    Log.d(TAG, "---------------------------------------------");
    //    // 此操作却会抛异常：Realm access from incorrect thread
    //    realm.where(CollectedRepo.class).equalTo("id", repo.id).findAll().deleteAllFromRealm();
    //  }
    //}, new Realm.Transaction.OnSuccess() {
    //  @Override public void onSuccess() {
    //    if (mView != null) {
    //      mView.showNormalTips(mApp.getString(R.string.tip_collect_cancel_success));
    //    }
    //  }
    //}, new Realm.Transaction.OnError() {
    //  @Override public void onError(Throwable error) {
    //    Log.e(TAG, "---------取消收藏失败:", error);
    //    if (mView != null) {
    //      mView.showErrorTips(mApp.getString(R.string.error_collect_cancel_failure));
    //    }
    //  }
    //});
    // ----------rxJava方式----------
    //Observable.create(new Observable.OnSubscribe<Void>() {
    //  @Override public void call(Subscriber<? super Void> subscriber) {
    //    Realm realm = mApp.getGlobalRealm();
    //    try {
    //      realm.beginTransaction();
    //      realm.where(CollectedRepo.class).equalTo("id", repo.id).findAll().deleteAllFromRealm();
    //      realm.commitTransaction();
    //      subscriber.onNext(null);
    //    } catch (Exception e) {
    //      if (realm.isInTransaction()) realm.cancelTransaction();
    //      subscriber.onError(e);
    //    }
    //  }
    //})
    //    // 因为realm在main线程获取，所以只能在main线程
    //    .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
    //  @Override public void call(Void aVoid) {
    //    if (mView != null) {
    //      mView.showNormalTips(mApp.getString(R.string.tip_collect_cancel_success));
    //    }
    //  }
    //}, new Action1<Throwable>() {
    //  @Override public void call(Throwable throwable) {
    //    Log.e(TAG, "---------取消收藏失败:", throwable);
    //    if (mView != null) {
    //      mView.showErrorTips(mApp.getString(R.string.error_collect_cancel_failure));
    //    }
    //  }
    //});
  }

  @Override protected void onDestroy() {
    if (mCollectList != null) {
      mCollectList.removeChangeListener(mCollectChangeListener);
      mCollectList = null;
    }
    if (mRealm != null) {
      mRealm.close();
      mRealm = null;
    }
  }
}
