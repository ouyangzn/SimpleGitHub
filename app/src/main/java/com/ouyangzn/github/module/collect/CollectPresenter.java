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
import com.ouyangzn.github.bean.localbean.LocalRepo;
import com.ouyangzn.github.module.collect.CollectContract.ICollectPresenter;
import com.ouyangzn.github.utils.Log;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：
 */
public class CollectPresenter extends ICollectPresenter {

  private final String TAG = CollectPresenter.class.getSimpleName();

  private App mApp;
  private RealmResults<LocalRepo> mCollectList;
  private RealmChangeListener<RealmResults<LocalRepo>> mCollectChangeListener;

  public CollectPresenter(Context context) {
    mApp = (App) context.getApplicationContext();
    mCollectChangeListener = new RealmChangeListener<RealmResults<LocalRepo>>() {
      @Override public void onChange(RealmResults<LocalRepo> element) {
        if (mView != null) {
          // todo 加上分页
          //element.subList()
          ArrayList<LocalRepo> list = new ArrayList<>();
          list.addAll(element);
          mView.showCollect(list);
        }
      }
    };
  }

  @Override void queryAllCollect() {
    mCollectList = mApp.getGlobalRealm()
        .where(LocalRepo.class)
        .findAllSortedAsync("collectTime", Sort.DESCENDING);
    mCollectList.addChangeListener(mCollectChangeListener);
  }

  @Override void cancelCollectRepo(final LocalRepo repo) {
    Observable.create(new Observable.OnSubscribe<Void>() {
      @Override public void call(Subscriber<? super Void> subscriber) {
        Realm realm = mApp.getGlobalRealm();
        try {
          realm.beginTransaction();
          realm.where(LocalRepo.class).equalTo("id", repo.id).findAll().deleteAllFromRealm();
          realm.commitTransaction();
          subscriber.onNext(null);
        } catch (Exception e) {
          subscriber.onError(e);
        }
      }
    }).subscribeOn(Schedulers.io()).subscribe(new Action1<Void>() {
      @Override public void call(Void aVoid) {
        if (mView != null) {
          mView.showNormalTips(mApp.getString(R.string.tip_collect_cancel_success));
        }
      }
    }, new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        Log.d(TAG, "---------取消收藏失败:", throwable);
        if (mView != null) {
          mView.showErrorTips(mApp.getString(R.string.error_collect_cancel_failure));
        }
      }
    });
  }

  @Override protected void onDestroy() {
    mApp = null;
    mCollectList.removeChangeListener(mCollectChangeListener);
    mCollectList = null;
  }
}
