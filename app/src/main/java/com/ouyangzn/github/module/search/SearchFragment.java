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

package com.ouyangzn.github.module.search;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import butterknife.BindView;
import butterknife.OnClick;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.bean.apibean.RepoSearchResult;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.module.search.SearchContract.ISearchPresenter;
import com.ouyangzn.github.module.search.SearchContract.ISearchView;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.utils.UiUtils;
import com.ouyangzn.github.view.InputEdit;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.PAGE_FIRST;
import static com.ouyangzn.github.utils.Actions.gotoRepoDetail;
import static com.ouyangzn.github.utils.UiUtils.initRefreshLayoutColor;
import static com.ouyangzn.github.utils.UiUtils.stopRefresh;

/**
 * Created by ouyangzn on 2017/6/7.<br/>
 * Description：搜索
 */
public class SearchFragment extends BaseFragment<ISearchView, ISearchPresenter>
    implements ISearchView, BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener {

  @BindView(R.id.refresh_search) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.recycler_search) RecyclerView mRecyclerView;
  @BindView(R.id.view_search) InputEdit mSearchEdit;

  private SearchFactor mSearchFactor;
  private SearchResultAdapter mAdapter;

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_search;
  }

  @Override public ISearchPresenter initPresenter() {
    return new SearchPresenter(getContext(), mProvider);
  }

  @Override public void onDestroyView() {
    mRecyclerView.setAdapter(null);
    super.onDestroyView();
  }

  @Override protected void initData(Bundle savedInstanceState) {
    mSearchFactor = new SearchFactor();
    mAdapter = new SearchResultAdapter(getContext(), new ArrayList<>(0));
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnRecyclerViewItemLongClickListener(this);
    mAdapter.setOnLoadingMoreListener(this);
  }

  @Override protected void initView(View parent) {
    requestNoToolbar();
    initRefreshLayoutColor(mRefreshLayout);
    mRefreshLayout.setOnRefreshListener(() -> requestData(true));
    mSearchEdit.setOnEditorActionListener(event -> {
      if (event.actionId() == EditorInfo.IME_ACTION_SEARCH) {
        String keyword = mSearchEdit.getInputText().trim();
        if (TextUtils.isEmpty(keyword)) return;
        mSearchFactor.keyword = keyword;
        requestData(true);
        ScreenUtils.hideKeyBoard(mSearchEdit.getEditText());
      }
    });
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAdapter.setEmptyView(mInflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    UiUtils.setRecyclerViewLoadMore(mAdapter, mRecyclerView);
    mRecyclerView.setAdapter(mAdapter);
  }

  @OnClick({ R.id.tv_search_cancel }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_search_cancel: {
        getActivity().onBackPressed();
        break;
      }
    }
  }

  @Override public void showErrorOnQueryData(String tips) {
    toast(tips);
    stopRefresh(mRefreshLayout);
    if (mAdapter.isLoadingMore()) {
      mAdapter.loadMoreFailure();
    }
  }

  @Override public void showQueryDataResult(RepoSearchResult result) {
    stopRefresh(mRefreshLayout);
    mSearchFactor.page++;
    List<Repository> repoList = result.getRepositories();
    boolean hasMore = repoList.size() == mSearchFactor.limit;
    if (!mAdapter.isLoadingMore()) {
      mAdapter.setHasMore(hasMore);
      mAdapter.resetData(repoList);
    } else {
      // loadMore获取数据速度太快了的时候，会crash：Cannot call this method(-->notifyDataSetChanged())
      // while RecyclerView is computing a layout or scrolling
      mRecyclerView.post(() -> mAdapter.loadMoreFinish(hasMore, repoList));
    }
  }

  @Override public void showCollected() {
    toast(R.string.tip_collect_success);
  }

  @Override public void showCollectedFailure() {
    toast(R.string.error_collect_failure);
  }

  @Override public void requestMoreData() {
    requestData(false);
  }

  @Override public void onItemClick(View view, int position) {
    Repository repo = mAdapter.getItem(position);
    gotoRepoDetail(this, repo);
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtils.getAlertDialog(getContext());
    builder.setItems(R.array.long_click_search_dialog_item, (dialog, which) -> {
      Repository item = mAdapter.getItem(position);
      switch (which) {
        case 0:
          copyUrl(item.getHtmlUrl());
          break;
        case 1:
          mPresenter.collectRepo(item);
          break;
      }
      dialog.dismiss();
    }).show();
    return true;
  }

  private void requestData(boolean refresh) {
    if (refresh) {
      mRefreshLayout.setRefreshing(true);
      mSearchFactor.page = PAGE_FIRST;
    }
    mPresenter.queryData(mSearchFactor);
  }

  private void copyUrl(String url) {
    CommonUtils.copy(getContext(), url);
    toast(R.string.tip_copy_success);
  }
}
