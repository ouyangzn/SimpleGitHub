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

package com.ouyangzn.github.module.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.CommonConstants;
import com.ouyangzn.github.base.LazyLoadFragment;
import com.ouyangzn.github.bean.apibean.RepoSearchResult;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.utils.UiUtils;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;

import static com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import static com.ouyangzn.github.module.main.MainContract.IMainView;
import static com.ouyangzn.github.utils.Actions.openUrl;
import static com.ouyangzn.github.utils.UiUtils.initRefreshLayoutColor;
import static com.ouyangzn.github.utils.UiUtils.stopRefresh;

/**
 * Created by ouyangzn on 2016/10/24.<br/>
 * Description：
 */
public class MainFragment extends LazyLoadFragment<IMainView, IMainPresenter>
    implements IMainView, BaseRecyclerViewAdapter.OnLoadingMoreListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener, View.OnClickListener {

  private static final String LANGUAGE = "language";
  @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.recycler) RecyclerView mRecyclerView;
  private RepositoryAdapter mAdapter;
  private SearchFactor mSearchFactor;

  public static MainFragment getInstance(String language) {
    MainFragment fragment = new MainFragment();
    Bundle data = new Bundle();
    data.putString(LANGUAGE, language);
    fragment.setArguments(data);
    return fragment;
  }

  @Override protected Status getCurrentStatus() {
    return Status.STATUS_LOADING;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_main;
  }

  @Override public IMainPresenter initPresenter() {
    return new MainPresenter(getContext(), mProvider);
  }

  @Override protected void initData(Bundle savedInstanceState) {
    String language = getArguments().getString(LANGUAGE);
    mSearchFactor = new SearchFactor();
    mSearchFactor.language = language;

    mAdapter = new RepositoryAdapter(getContext(), new ArrayList<>(0));
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnRecyclerViewItemLongClickListener(this);
    mAdapter.setOnLoadingMoreListener(this);
  }

  @Override public void onDestroyView() {
    mRecyclerView.setAdapter(null);
    super.onDestroyView();
  }

  @Override protected void lazyInitView(View parent) {
    search(false);
    requestNoToolbar();
    initRefreshLayoutColor(mRefreshLayout);
    mRefreshLayout.setOnRefreshListener(() -> search(true));

    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    RxView.touches(mRecyclerView, motionEvent -> {
      ScreenUtils.hideKeyBoard(mRecyclerView);
      return false;
    }).compose(mProvider.bindUntilEvent(FragmentEvent.DESTROY_VIEW)).subscribe();
    mAdapter.setEmptyView(mInflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    UiUtils.setRecyclerViewLoadMore(mAdapter, mRecyclerView);
    mRecyclerView.setAdapter(mAdapter);

    View errorView = mInflater.inflate(R.layout.view_error_main, (ViewGroup) parent, false);
    errorView.findViewById(R.id.layout_main_loading_failure).setOnClickListener(this);
    setErrorView(errorView);

  }

  private void search(boolean isRefresh) {
    if (isRefresh) {
      mSearchFactor.page = 1;
      mRefreshLayout.setRefreshing(true);
    }
    Log.d(TAG, "----------搜索数据:" + mSearchFactor);
    mPresenter.queryData(mSearchFactor);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.layout_main_loading_failure:
        switchStatus(Status.STATUS_LOADING);
        search(false);
        break;
      case R.id.tv_reload_more: {
        mAdapter.reloadMore();
        break;
      }
    }
  }

  @Override public void onItemClick(View view, int position) {
    Repository repository = mAdapter.getItem(position);
    openUrl(this.getActivity(), repository.getHtmlUrl());
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtils.getAlertDialog(getActivity());
    builder.setItems(R.array.long_click_main_dialog_item, (dialog, which) -> {
      Repository item = mAdapter.getItem(position);
      switch (which) {
        case 0:
          copyUrl(item.getHtmlUrl());
          break;
        case 1:
          collectProject(item);
          break;
      }
      dialog.dismiss();
    }).show();
    return true;
  }

  private void copyUrl(String url) {
    CommonUtils.copy(getContext(), url);
    toast(R.string.tip_copy_success);
  }

  private void collectProject(Repository repo) {
    mPresenter.collectRepo(repo);
  }

  @Override public void requestMoreData() {
    search(false);
  }

  @Override public void showErrorOnQueryData(String tips) {
    toast(tips);
    stopRefresh(mRefreshLayout);
    if (mAdapter.isLoadingMore()) {
      switchStatus(Status.STATUS_NORMAL);
      mAdapter.loadMoreFailure();
    } else {
      switchStatus(Status.STATUS_ERROR);
    }
  }

  @Override public void showQueryDataResult(RepoSearchResult result) {
    switchStatus(Status.STATUS_NORMAL);
    stopRefresh(mRefreshLayout);
    mSearchFactor.page++;
    List<Repository> repoList = result.getRepositories();
    boolean hasMore = repoList.size() == mSearchFactor.limit;
    mAdapter.setHasMore(hasMore);
    mAdapter.setLanguageVisible(CommonConstants.GitHub.LANG_ALL.equals(mSearchFactor.language));
    if (mAdapter.isLoadingMore()) {
      mAdapter.loadMoreFinish(hasMore, repoList);
    } else {
      mAdapter.resetData(repoList);
    }
  }

  @Override public void showCollected() {
    toast(R.string.tip_collect_success);
  }

  @Override public void showCollectedFailure() {
    toast(R.string.error_collect_failure);
  }

}
