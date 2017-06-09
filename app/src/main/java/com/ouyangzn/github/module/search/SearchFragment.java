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
import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.data.IRepositoryDataSource;
import com.ouyangzn.github.data.remote.RepositoryRemoteDataSource;
import com.ouyangzn.github.module.common.SearchResultAdapter;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.RxJavaUtils;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.utils.UiUtils;
import com.ouyangzn.github.view.InputEdit;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;
import rx.android.schedulers.AndroidSchedulers;

import static com.ouyangzn.github.utils.Actions.openUrl;

/**
 * Created by ouyangzn on 2017/6/7.<br/>
 * Description：搜索
 */
public class SearchFragment extends BaseFragment
    implements BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener {

  @BindView(R.id.refresh_search) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.recycler_search) RecyclerView mRecyclerView;
  @BindView(R.id.view_search) InputEdit mSearchEdit;

  private IRepositoryDataSource mDataSource;
  private SearchFactor mSearchFactor;
  private SearchResultAdapter mAdapter;

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_search;
  }

  @Override public BasePresenter initPresenter() {
    return null;
  }

  @Override public void onDestroyView() {
    mRecyclerView.setAdapter(null);
    super.onDestroyView();
  }

  @Override protected void initData(Bundle savedInstanceState) {
    mDataSource = new RepositoryRemoteDataSource();
    mSearchFactor = new SearchFactor();
    mAdapter = new SearchResultAdapter(getContext(), new ArrayList<>(0));
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnRecyclerViewItemLongClickListener(this);
    mAdapter.setOnLoadingMoreListener(this);
  }

  @Override protected void initView(View parent) {
    requestNoToolbar();
    mSearchEdit.setOnEditorActionListener(event -> {
      if (event.actionId() == EditorInfo.IME_ACTION_SEARCH) {
        String keyword = mSearchEdit.getInputText().trim();
        if (TextUtils.isEmpty(keyword)) return;
        mSearchFactor.page = 1;
        mSearchFactor.keyword = keyword;
        requestData();
        ScreenUtils.hideKeyBoard(mSearchEdit.getEditText());
      }
    });
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAdapter.setEmptyView(mInflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    UiUtils.setRecyclerViewLoadMore(mAdapter, mRecyclerView);
    mRecyclerView.setAdapter(mAdapter);
  }

  private void requestData() {
    RxJavaUtils.wrapFragment(
        mDataSource.queryByKeyword(mSearchFactor, mSearchFactor.limit, mSearchFactor.page),
        // 不知道为什么，必须强转才能编译通过
        (LifecycleProvider<FragmentEvent>) mProvider)
        .doOnSubscribe(() -> mRefreshLayout.setRefreshing(true))
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(result -> {
          UiUtils.stopRefresh(mRefreshLayout);
          onResult(result.getRepositories());
        }, error -> {
          Log.e(TAG, "", error);
          UiUtils.stopRefresh(mRefreshLayout);
          onSearchFail();
        });
  }

  private void onResult(List<Repository> repoList) {
    mSearchFactor.page++;
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

  private void onSearchFail() {
    toast(R.string.error_network_error);
  }

  @OnClick({ R.id.tv_search_cancel }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_search_cancel: {
        getActivity().onBackPressed();
        break;
      }
    }
  }

  @Override public void requestMoreData() {
    requestData();
  }

  @Override public void onItemClick(View view, int position) {
    Repository repo = mAdapter.getItem(position);
    openUrl(this.getActivity(), repo.getHtmlUrl());
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtils.getAlertDialog(getContext());
    builder.setItems(R.array.long_click_main_dialog_item, (dialog, which) -> {
      Repository item = mAdapter.getItem(position);
      switch (which) {
        case 0:
          copyUrl(item.getHtmlUrl());
          break;
        case 1:
          //cancelCollectRepo(item);
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
}
