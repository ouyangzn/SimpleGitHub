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

import android.app.AlertDialog;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;
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
import com.ouyangzn.topgithub.utils.DialogUtil;
import com.ouyangzn.topgithub.utils.Formatter;
import com.ouyangzn.topgithub.utils.ImageLoader;
import com.ouyangzn.topgithub.utils.Log;
import com.ouyangzn.topgithub.utils.ScreenUtils;
import com.squareup.timessquare.CalendarPickerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.ouyangzn.topgithub.base.CommonConstants.NormalCons.LIMIT_20;

public class MainActivity extends BaseActivity<IMainView, IMainPresenter>
    implements IMainView, NavigationView.OnNavigationItemSelectedListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener {

  private View mLoadingView;
  private EditText mSearchEdit;
  private SwipeRefreshLayout mRefreshLayout;
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
    mPresenter.saveLanguage(mLanguage);
  }

  @Override protected void initData() {
    SharedPreferences configSp = getSharedPreferences(ConfigSP.SP_NAME, MODE_PRIVATE);
    mLanguage = configSp.getString(ConfigSP.KEY_LANGUAGE, GitHub.LANG_JAVA);
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
    final SearchView searchView = new SearchView(mContext);
    searchView.setQueryHint(getString(R.string.hint_input_keyword));
    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Gravity.RIGHT);
    searchView.setLayoutParams(params);
    toolbar.addView(searchView);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String s) {
        toast("准备搜索：" + s);
        searchView.clearFocus();
        return true;
      }

      @Override public boolean onQueryTextChange(String s) {
        return false;
      }
    });

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

    mSearchEdit = ButterKnife.findById(this, R.id.et_search);
    RxTextView.editorActionEvents(mSearchEdit).subscribe(new Action1<TextViewEditorActionEvent>() {
      @Override public void call(TextViewEditorActionEvent actionEvent) {
        if (EditorInfo.IME_ACTION_SEARCH == actionEvent.actionId()) {
          String keyword = mSearchEdit.getText().toString().trim();
          ScreenUtils.hideKeyBoard(mSearchEdit);
          mSearchEdit.clearFocus();
          if (!TextUtils.isEmpty(keyword)) {
            mKeyword = keyword;
            search(true);
          }
        }
      }
    });
    RecyclerView recyclerView = ButterKnife.findById(this, R.id.recycler);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    LayoutInflater inflater = getLayoutInflater();
    mAdapter.setLoadMoreView(inflater.inflate(R.layout.item_load_more, recyclerView, false));
    recyclerView.setAdapter(mAdapter);
    RxView.touches(recyclerView, new Func1<MotionEvent, Boolean>() {
      @Override public Boolean call(MotionEvent event) {
        ScreenUtils.hideKeyBoard(mSearchEdit);
        mSearchEdit.clearFocus();
        return false;
      }
    }).subscribe();

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
    boolean isAll = GitHub.LANG_ALL.equals(mLanguage);
    mDrawerLayout.closeDrawer(GravityCompat.START);
    // 搜索全部语言时，必须有创建时间限制，否则搜不到结果
    if (isAll) {
      Calendar nextYear = Calendar.getInstance();
      nextYear.add(Calendar.YEAR, -1);
      Date today = new Date();
      CalendarPickerView pickerView = new CalendarPickerView(mContext, null);
      pickerView.init(today, nextYear.getTime())
          .withSelectedDate(today)
          .inMode(CalendarPickerView.SelectionMode.SINGLE);
      final AlertDialog dialog = DialogUtil.getAlertDialog(mContext).setView(pickerView).show();
      dialog.setCancelable(false);
      pickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
        @Override public void onDateSelected(Date date) {
          dialog.dismiss();
          toast("选择了：" + Formatter.formatDate(date, Formatter.FORMAT_YYYY_MM_DD_CHINA));
        }

        @Override public void onDateUnselected(Date date) {

        }
      });
      return true;
    }
    setTitle(isAll ? getString(R.string.app_name) : mLanguage);
    search(true);
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
