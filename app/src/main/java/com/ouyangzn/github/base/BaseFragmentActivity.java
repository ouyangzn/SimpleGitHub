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

package com.ouyangzn.github.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import com.ouyangzn.github.R;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.ScreenUtil;

/**
 * Created by ouyangzn on 2017/5/3.<br/>
 * Description：内嵌一个fragment的activity基类
 */
public class BaseFragmentActivity extends BaseActivity {

  public static final String DATA_FRAGMENT_NAME = "fragmentName";

  private String mFragmentName;

  @Override public BasePresenter initPresenter() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.activity_base;
  }

  @Override protected void initData() {

  }

  @Override protected void initView(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      mFragmentName = savedInstanceState.getString(DATA_FRAGMENT_NAME);
    } else {
      mFragmentName = getIntent().getStringExtra(DATA_FRAGMENT_NAME);
    }
    if (TextUtils.isEmpty(mFragmentName)) {
      Log.w("BaseFragmentActivity", "start activity need a fragment, fragmentName cannot be null");
      finish();
      return;
    }

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    BaseFragment fragment = newFragment();
    assert fragment != null;
    ft.add(R.id.layout_fragment_container, fragment, getFragmentName()).commit();
  }

  public String getFragmentName() {
    return mFragmentName;
  }

  protected final BaseFragment newFragment() {
    String fragmentName = this.getFragmentName();
    return TextUtils.isEmpty(fragmentName) ? null
        : (BaseFragment) Fragment.instantiate(this, fragmentName, this.getIntent().getExtras());
  }

  @Override public void onBackPressed() {
    FragmentManager fm = getSupportFragmentManager();
    BaseFragment fragment = (BaseFragment) fm.findFragmentByTag(getFragmentName());
    if (!fragment.onBackPressed()) {
      super.onBackPressed();
    }
  }

  @Override public void finish() {
    View focus = getCurrentFocus();
    if (focus != null) ScreenUtil.hideKeyBoard(focus);
    super.finish();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(DATA_FRAGMENT_NAME, getFragmentName());
  }
}
