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

import android.os.Bundle;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseActivity;
import com.ouyangzn.github.bean.localbean.LocalRepo;
import com.ouyangzn.github.utils.Log;
import java.util.List;

import static com.ouyangzn.github.module.collect.CollectContract.ICollectPresenter;
import static com.ouyangzn.github.module.collect.CollectContract.ICollectView;

public class CollectActivity extends BaseActivity<ICollectView, ICollectPresenter>
    implements ICollectView {

  @Override protected int getContentView() {
    return R.layout.activity_collect;
  }

  @Override public ICollectPresenter initPresenter() {
    return new CollectPresenter(mContext);
  }

  @Override protected void initData() {
    mPresenter.queryAllCollect();
  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @Override public void showCollect(List<LocalRepo> repoList) {
    Log.d(TAG, "----------repoList = " + repoList);
  }

  @Override public void showErrorOnQueryFailure() {

  }

  @Override public void showNormalTips(String tips) {
    toast(tips);
  }

  @Override public void showErrorTips(String tips) {
    toast(tips);
  }

  @Override public void showProgressDialog() {
  }

  @Override public void dismissProgressDialog() {
  }
}
