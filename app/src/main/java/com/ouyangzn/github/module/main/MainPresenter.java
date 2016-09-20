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
import com.ouyangzn.github.bean.apibean.SearchResult;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.data.IGitHubDataSource;
import com.ouyangzn.github.data.remote.RemoteGitHubData;
import com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import com.ouyangzn.github.utils.Log;
import rx.Observable;
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
  private Context mContext;
  private SharedPreferences mConfigSp;
  private Subscription mQueryDataSubscribe;

  public MainPresenter(Context context) {
    this.mContext = context.getApplicationContext();
    mDataSource = new RemoteGitHubData();
    mConfigSp =
        mContext.getSharedPreferences(CommonConstants.ConfigSP.SP_NAME, Context.MODE_PRIVATE);
  }

  @Override protected void onDestroy() {
    mDataSource = null;
    mContext = null;
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
              mView.showErrorOnQueryData(mContext.getString(R.string.error_search_github));
            }
          }
        });
    addSubscription(mQueryDataSubscribe);
  }

  @Override void saveSearchFactor(SearchFactor factor) {
    Observable.just(factor).observeOn(Schedulers.io()).doOnNext(new Action1<SearchFactor>() {
      @Override public void call(SearchFactor factor) {
        mConfigSp.edit()
            .putString(CommonConstants.ConfigSP.KEY_LANGUAGE, App.getGson().toJson(factor))
            .apply();
      }
    }).subscribe();
  }
}
