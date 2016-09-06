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

package com.ouyangzn.topgithub.module.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.ouyangzn.topgithub.R;
import com.ouyangzn.topgithub.base.BaseActivity;
import com.ouyangzn.topgithub.base.CommonConstants.GitHub;
import com.ouyangzn.topgithub.bean.SearchResult;
import com.ouyangzn.topgithub.module.main.MainContract.IMainPresenter;
import com.ouyangzn.topgithub.module.main.MainContract.IMainView;
import com.ouyangzn.topgithub.utils.DialogUtil;
import com.ouyangzn.topgithub.utils.ImageLoader;
import com.ouyangzn.topgithub.utils.Log;

public class MainActivity extends BaseActivity<IMainView, IMainPresenter>
    implements IMainView, NavigationView.OnNavigationItemSelectedListener {

  /*@BindView(R.id.refreshLayout) */ SwipeRefreshLayout mRefreshLayout;
  private DrawerLayout mDrawerLayout;
  private NavigationView mNavView;
  private ProgressDialog mProgressDialog;
  private String mKeyword = "android";
  private String mLanguage = GitHub.LANG_JAVA;

  @Override public IMainPresenter initPresenter() {
    return new MainPresenter(this);
  }

  @Override protected void initData() {
    mPresenter.queryData(mKeyword, mLanguage, 0);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // @BindView 找不到，NavigationView下的view直接find也找不到
    mNavView = ButterKnife.findById(this, R.id.nav_view);
    mNavView.setNavigationItemSelectedListener(this);
    ImageView img_photo = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.img_photo);
    ImageLoader.loadAsCircle(img_photo, R.drawable.ic_photo);

    // @BindView 找不到
    mDrawerLayout = ButterKnife.findById(this, R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    mRefreshLayout = ButterKnife.findById(this, R.id.refreshLayout);
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        mPresenter.queryData(mKeyword, mLanguage, 0);
      }
    });

  }

  @Override public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.nav_java: {

        break;
      }
      case R.id.nav_oc: {

        break;
      }
      case R.id.nav_c: {

        break;
      }
      case R.id.nav_cpp: {

        break;
      }
      case R.id.nav_php: {

        break;
      }
      case R.id.nav_js: {

        break;
      }
      case R.id.nav_python: {

        break;
      }
      case R.id.nav_ruby: {

        break;
      }
      case R.id.nav_c_sharp: {

        break;
      }
      case R.id.nav_shell: {

        break;
      }
    }
    mDrawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override public void showProgressDialog() {
    mProgressDialog = DialogUtil.showProgressDialog(this, getString(R.string.loading), false);
  }

  @Override public void dismissProgressDialog() {
    DialogUtil.dismissProgressDialog(mProgressDialog);
  }

  @Override public void showErrorTips(String tips) {
    toast(tips);
  }

  @Override public void showResult(SearchResult result) {
    mRefreshLayout.setRefreshing(false);
    Log.d(TAG, result.toString());
  }
}
