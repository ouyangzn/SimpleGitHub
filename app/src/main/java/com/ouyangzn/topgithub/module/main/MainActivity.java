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

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.topgithub.R;
import com.ouyangzn.topgithub.base.BaseActivity;
import com.ouyangzn.topgithub.base.CommonConstants.ConfigSP;
import com.ouyangzn.topgithub.base.CommonConstants.GitHub;
import com.ouyangzn.topgithub.bean.Repository;
import com.ouyangzn.topgithub.bean.SearchResult;
import com.ouyangzn.topgithub.module.common.SearchResultAdapter;
import com.ouyangzn.topgithub.module.main.MainContract.IMainPresenter;
import com.ouyangzn.topgithub.module.main.MainContract.IMainView;
import com.ouyangzn.topgithub.utils.ImageLoader;
import com.ouyangzn.topgithub.utils.Log;
import java.util.ArrayList;

import static com.ouyangzn.topgithub.base.CommonConstants.NormalCons.LIMIT_20;

public class MainActivity extends BaseActivity<IMainView, IMainPresenter>
    implements IMainView, NavigationView.OnNavigationItemSelectedListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener {

  private SharedPreferences mConfigSp;
  private View mLoadingView;
  private SwipeRefreshLayout mRefreshLayout;
  private RecyclerView mRecyclerView;
  private SearchResultAdapter mAdapter;
  private DrawerLayout mDrawerLayout;
  private NavigationView mNavView;
  private String mKeyword;
  private String mLanguage;
  // 重新加载或者加载下一页
  private boolean mIsRefresh = true;
  private int mCurrPage = 1;

  @Override public IMainPresenter initPresenter() {
    return new MainPresenter(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mConfigSp.edit().putString(ConfigSP.KEY_LANGUAGE, mLanguage).apply();
  }

  @Override protected void initData() {
    mConfigSp = getSharedPreferences(ConfigSP.SP_NAME, MODE_PRIVATE);
    mLanguage = mConfigSp.getString(ConfigSP.KEY_LANGUAGE, GitHub.LANG_JAVA);
    mAdapter = new SearchResultAdapter(R.layout.item_search_result, new ArrayList<Repository>(0));
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnLoadingMoreListener(this);
    search(false);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    setTitle(GitHub.LANG_ALL.equals(mLanguage) ? getString(R.string.app_name) : mLanguage);

    mLoadingView = ButterKnife.findById(this, R.id.main_loading);
    mLoadingView.setVisibility(View.VISIBLE);

    Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
    setSupportActionBar(toolbar);
    // @BindView 找不到，NavigationView下的view直接find也找不到
    mNavView = ButterKnife.findById(this, R.id.nav_view);
    mNavView.setNavigationItemSelectedListener(this);
    initNavView();
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
        search(true);
      }
    });

    mRecyclerView = ButterKnife.findById(this, R.id.recycler);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    LayoutInflater inflater = getLayoutInflater();
    mAdapter.setLoadMoreView(inflater.inflate(R.layout.item_load_more, mRecyclerView, false));
    mRecyclerView.setAdapter(mAdapter);
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
      case R.id.nav_all: {
        mLanguage = GitHub.LANG_ALL;
        break;
      }
      case R.id.nav_java: {
        mLanguage = GitHub.LANG_JAVA;
        break;
      }
      case R.id.nav_oc: {
        mLanguage = GitHub.LANG_OC;
        break;
      }
      case R.id.nav_swift: {
        mLanguage = GitHub.LANG_SWIFT;
        break;
      }
      case R.id.nav_c: {
        mLanguage = GitHub.LANG_C;
        break;
      }
      case R.id.nav_cpp: {
        mLanguage = GitHub.LANG_CPP;
        break;
      }
      case R.id.nav_php: {
        mLanguage = GitHub.LANG_PHP;
        break;
      }
      case R.id.nav_js: {
        mLanguage = GitHub.LANG_JS;
        break;
      }
      case R.id.nav_python: {
        mLanguage = GitHub.LANG_PYTHON;
        break;
      }
      case R.id.nav_ruby: {
        mLanguage = GitHub.LANG_RUBY;
        break;
      }
      case R.id.nav_c_sharp: {
        mLanguage = GitHub.LANG_C_SHARP;
        break;
      }
      case R.id.nav_shell: {
        mLanguage = GitHub.LANG_SHELL;
        break;
      }
    }
    search(true);
    setTitle(GitHub.LANG_ALL.equals(mLanguage) ? getString(R.string.app_name) : mLanguage);
    mDrawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override public void showProgressDialog() {
  }

  @Override public void dismissProgressDialog() {
  }

  @Override public void showErrorOnQueryData(String tips) {
    toast(tips);
    mLoadingView.setVisibility(View.GONE);
    if (mIsRefresh) mRefreshLayout.setRefreshing(false);
  }

  @Override public void showQueryDataResult(SearchResult result) {
    Log.d(TAG, result.toString());
    mLoadingView.setVisibility(View.GONE);
    mCurrPage++;
    if (mIsRefresh) {
      mRefreshLayout.setRefreshing(false);
      mAdapter.resetData(result.getRepositories());
    } else {
      mAdapter.addData(result.getRepositories());
      mAdapter.loadMoreFinish(result.getRepositories().size() == LIMIT_20,
          result.getRepositories());
    }
  }

  private void search(boolean isRefresh) {
    if (isRefresh) {
      mCurrPage = 1;
      mRefreshLayout.setRefreshing(true);
    }
    mPresenter.queryData(mKeyword, mLanguage, mCurrPage);
    mIsRefresh = isRefresh;
  }

  @Override public void requestMoreData() {
    search(false);
  }

  @Override public void onItemClick(View view, int position) {
    Repository repository = mAdapter.getItem(position);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(repository.getHtmlUrl()));
    startActivity(intent);
  }

  private void initNavView() {
    if (GitHub.LANG_ALL.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_all);
    } else if (GitHub.LANG_JAVA.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_java);
    } else if (GitHub.LANG_OC.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_oc);
    } else if (GitHub.LANG_SWIFT.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_swift);
    } else if (GitHub.LANG_C.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_c);
    } else if (GitHub.LANG_CPP.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_cpp);
    } else if (GitHub.LANG_PHP.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_php);
    } else if (GitHub.LANG_JS.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_js);
    } else if (GitHub.LANG_PYTHON.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_ruby);
    } else if (GitHub.LANG_C_SHARP.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_c_sharp);
    } else if (GitHub.LANG_SHELL.equals(mLanguage)) {
      mNavView.setCheckedItem(R.id.nav_shell);
    }
  }
}
