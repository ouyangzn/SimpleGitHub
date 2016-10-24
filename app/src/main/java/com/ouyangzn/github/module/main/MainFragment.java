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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.base.CommonConstants;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.apibean.SearchResult;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.module.common.RepositoryAdapter;
import com.ouyangzn.github.utils.CommonUtil;
import com.ouyangzn.github.utils.DialogUtil;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import java.util.ArrayList;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.LIMIT_20;
import static com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import static com.ouyangzn.github.module.main.MainContract.IMainView;

/**
 * Created by ouyangzn on 2016/10/24.<br/>
 * Description：
 */
public class MainFragment extends BaseFragment<IMainView, IMainPresenter>
    implements IMainView, BaseRecyclerViewAdapter.OnLoadingMoreListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener {

  private static final int DATA_EACH_PAGE = LIMIT_20;
  @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.recycler) RecyclerView mRecyclerView;
  private RepositoryAdapter mAdapter;
  private SearchFactor mSearchFactor;
  // 重新加载或者加载下一页
  private boolean mIsRefresh = true;
  private int mCurrPage = 1;

  public static MainFragment getInstance(String language) {
    MainFragment fragment = new MainFragment();
    Bundle data = new Bundle();
    data.putString("language", language);
    fragment.setArguments(data);
    return fragment;
  }

  @Override protected Status getCurrentStatus() {
    return Status.STATUS_LOADING;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_main;
  }

  @Override protected void initData(Bundle savedInstanceState) {
    String language = getArguments().getString("language");
    mSearchFactor = new SearchFactor();
    mSearchFactor.language = language;
    search(false);
    mAdapter = new RepositoryAdapter(getContext(), new ArrayList<>(0));
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnRecyclerViewItemLongClickListener(this);
    mAdapter.setOnLoadingMoreListener(this);
  }

  @Override protected void initView(View parent) {
    mRefreshLayout = ButterKnife.findById(parent, R.id.refreshLayout);
    mRefreshLayout.setOnRefreshListener(() -> search(true));

    mRecyclerView = ButterKnife.findById(parent, R.id.recycler);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mAdapter.setLoadMoreView(mInflater.inflate(R.layout.item_load_more, mRecyclerView, false));
    mAdapter.setEmptyView(mInflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override public IMainPresenter initPresenter() {
    return new MainPresenter(getContext());
  }

  private void search(boolean isRefresh) {
    if (isRefresh) {
      mCurrPage = 1;
      mRefreshLayout.setRefreshing(true);
    }
    Log.d(TAG, "----------搜索数据:" + mSearchFactor);
    mPresenter.queryData(mSearchFactor, DATA_EACH_PAGE, mCurrPage);
    mIsRefresh = isRefresh;
  }

  @Override public void onItemClick(View view, int position) {
    Repository repository = mAdapter.getItem(position);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(repository.getHtmlUrl()));
    startActivity(intent);
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtil.getAlertDialog(getActivity());
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
    CommonUtil.copy(getContext(), url);
    toast(R.string.tip_copy_success);
  }

  private void collectProject(Repository repo) {
    mPresenter.collectRepo(repo);
  }

  @Override public void requestMoreData() {
    search(false);
  }

  @Override public void showErrorOnQueryData(String tips) {
    switchStatus(Status.STATUS_NORMAL);
    toast(tips);
    if (mIsRefresh) mRefreshLayout.setRefreshing(false);
    if (mAdapter.isLoadingMore()) mAdapter.loadMoreFinish(true, null);
  }

  @Override public void showQueryDataResult(SearchResult result) {
    switchStatus(Status.STATUS_NORMAL);
    Log.d(TAG, "----------result = " + result.toString());
    mCurrPage++;
    boolean hasMore = result.getRepositories().size() == DATA_EACH_PAGE;
    mAdapter.setHasMore(hasMore);
    mAdapter.setLanguageVisible(CommonConstants.GitHub.LANG_ALL.equals(mSearchFactor.language));
    if (mIsRefresh) {
      mRefreshLayout.setRefreshing(false);
      mAdapter.resetData(result.getRepositories());
    } else {
      if (mAdapter.isLoadingMore()) {
        mAdapter.loadMoreFinish(hasMore, result.getRepositories());
      } else {
        mAdapter.addData(result.getRepositories());
      }
    }
  }

  @Override public void showCollected() {
    toast(R.string.tip_collect_success);
  }

  @Override public void showCollectedFailure() {
    toast(R.string.error_collect_failure);
  }

  @Override public void setLoadingIndicator(boolean isActive) {

  }
}
