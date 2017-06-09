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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.utils.UiUtils;
import com.ouyangzn.github.view.InputEdit;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.LIMIT_10;
import static com.ouyangzn.github.utils.Actions.openUrl;
import static com.ouyangzn.github.utils.UiUtils.initRefreshLayoutColor;

/**
 * Created by ouyangzn on 2017/5/27.<br/>
 * Description：收藏
 */
public class CollectionsFragment extends
    BaseFragment<CollectionsContract.ICollectionsView, CollectionsContract.ICollectionsPresenter>
    implements CollectionsContract.ICollectionsView,
    BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener {

  private final int LIMIT = LIMIT_10;
  private final int FIRST_PAGE = 0;

  @BindView(R.id.refresh_collections) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.recycler_collections) RecyclerView mRecyclerView;
  @BindView(R.id.view_search_collections) InputEdit mSearchEdit;

  private CollectAdapter mCollectAdapter;
  // 当前分页页数
  private int mCurrPage = FIRST_PAGE;

  @Override protected Status getCurrentStatus() {
    return Status.STATUS_LOADING;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_collections;
  }

  @Override protected void initData(Bundle savedInstanceState) {
    mCollectAdapter = new CollectAdapter(getContext(), new ArrayList<>(0));
    mCollectAdapter.setOnRecyclerViewItemClickListener(this);
    mCollectAdapter.setOnRecyclerViewItemLongClickListener(this);
    mCollectAdapter.setOnLoadingMoreListener(this);
    queryCollect(true);
  }

  @Override public void onDestroyView() {
    // 不置空可能会导致内存泄漏
    mRecyclerView.setAdapter(null);
    super.onDestroyView();
  }

  @Override public CollectionsContract.ICollectionsPresenter initPresenter() {
    return new CollectionsPresenter(mProvider);
  }

  @Override protected void initView(View parent) {
    UiUtils.setCenterTitle(mToolbar, R.string.title_collect);
    mToolbar.setNavigationIcon(R.drawable.ic_back_white);
    mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    mCollectAdapter.setEmptyView(mInflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    UiUtils.setRecyclerViewLoadMore(mCollectAdapter, mRecyclerView);
    mRecyclerView.setAdapter(mCollectAdapter);
    RxView.touches(mRecyclerView, event -> {
      ScreenUtils.hideKeyBoard(mSearchEdit);
      mSearchEdit.clearFocus();
      return false;
    }).compose(mProvider.bindUntilEvent(FragmentEvent.DESTROY_VIEW)).subscribe();

    initRefreshLayoutColor(mRefreshLayout);
    mRefreshLayout.setOnRefreshListener(() -> {
      // 下拉刷新的话清除搜索关键字
      String keyword = mSearchEdit.getInputText().trim();
      if (TextUtils.isEmpty(keyword)) {
        queryCollect(true);
      } else {
        mSearchEdit.clearFocus();
        mSearchEdit.getEditText().setText(null);
      }
    });

    RxTextView.textChanges(mSearchEdit.getEditText())
        .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .compose(mProvider.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        // 初始化时，rxView会先调用一次textChanged事件
        .skip(1)
        .subscribe(text -> {
          String keyword = text.toString();
          // 清空搜索条件，为搜索全部收藏
          if (TextUtils.isEmpty(keyword)) {
            onClearKeyword();
          } else {
            keyword = keyword.trim();
            if (!TextUtils.isEmpty(keyword)) {
              queryByKey(keyword, true);
            }
          }
        });
  }

  private void onClearKeyword() {
    mCurrPage = FIRST_PAGE;
    queryCollect(true);
  }

  /**
   * @param keyword keyword
   */
  private void queryByKey(String keyword, boolean newKeyword) {
    if (newKeyword) mCurrPage = FIRST_PAGE;
    mPresenter.queryByKey(keyword, mCurrPage, LIMIT);
  }

  private void queryCollect(boolean isRefresh) {
    if (isRefresh) {
      mCurrPage = FIRST_PAGE;
    }
    mPresenter.queryCollect(mCurrPage, LIMIT);
  }

  @Override public void showCollect(List<CollectedRepo> repoList) {
    switchStatus(Status.STATUS_NORMAL);
    UiUtils.stopRefresh(mRefreshLayout);
    mCurrPage++;
    boolean hasMore = repoList.size() == LIMIT;
    if (!mCollectAdapter.isLoadingMore()) {
      mCollectAdapter.setHasMore(hasMore);
      //calculateDiffAndRefresh(mCollectAdapter, repoList);
      mCollectAdapter.resetData(repoList);
    } else {
      // loadMore获取数据速度太快了的时候，会crash：Cannot call this method(-->notifyDataSetChanged())
      // while RecyclerView is computing a layout or scrolling
      mRecyclerView.post(() -> mCollectAdapter.loadMoreFinish(hasMore, repoList));
    }
  }

  @Override public void showErrorOnQueryFailure(String error) {
    toast(error);
    if (!mCollectAdapter.isLoadingMore()) {
      switchStatus(Status.STATUS_NORMAL);
      UiUtils.stopRefresh(mRefreshLayout);
    } else {
      mCollectAdapter.loadMoreFailure();
    }
  }

  @Override public void showCollectQueryByKey(List<CollectedRepo> repoList) {
    showCollect(repoList);
  }

  @Override public void showQueryByKeyFailure(String error) {
    showErrorOnQueryFailure(error);
  }

  @Override public void showCollectionCanceled(CollectedRepo repo) {
    toast(R.string.tip_collect_cancel_success);
    mCollectAdapter.remove(repo);
  }

  @Override public void showCollectionCancelFailure() {
    toast(R.string.error_collect_cancel_failure);
  }

  @Override public void setLoadingIndicator(boolean isActive) {
    //mRefreshLayout.setRefreshing(isActive);
  }

  @Override public void requestMoreData() {
    String keyword = mSearchEdit.getInputText().trim();
    if (TextUtils.isEmpty(keyword)) {
      queryCollect(false);
    } else {
      queryByKey(keyword, false);
    }
  }

  @Override public void onItemClick(View view, int position) {
    CollectedRepo repo = mCollectAdapter.getItem(position);
    openUrl(this.getActivity(), repo.getHtmlUrl());
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtils.getAlertDialog(getContext());
    builder.setItems(R.array.long_click_collect_dialog_item, (dialog, which) -> {
      CollectedRepo item = mCollectAdapter.getItem(position);
      switch (which) {
        case 0:
          copyUrl(item.getHtmlUrl());
          break;
        case 1:
          cancelCollectRepo(item);
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

  private void cancelCollectRepo(CollectedRepo repo) {
    mPresenter.cancelCollectRepo(repo);
  }

  private void calculateDiffAndRefresh(BaseRecyclerViewAdapter<CollectedRepo> adapter,
      List<CollectedRepo> repoList) {
    // ------------很大几率crash,暂未解决,exception：Inconsistency detected. Invalid view holder adapter positionViewHolder---------------
    // 使用DiffUtil计算有变化的数据进行局部刷新
    List<CollectedRepo> oldData = adapter.getData();
    DiffUtil.DiffResult diffResult =
        DiffUtil.calculateDiff(new CollectDiffCallback(repoList, oldData));
    /*
    需要先替换adapter中的数据但不刷新，刷新通知交由diffResult.dispatchUpdatesTo()来做
    <pre>
         List oldList = mAdapter.getData();
         DiffResult result = DiffUtil.calculateDiff(new MyCallback(oldList, newList));
         mAdapter.setData(newList);
         result.dispatchUpdatesTo(mAdapter);
    </pre>
    */
    oldData.clear();
    oldData.addAll(repoList);
    diffResult.dispatchUpdatesTo(adapter);
  }

  public static class CollectDiffCallback extends DiffUtil.Callback {

    private List<CollectedRepo> mNewRepoList;
    private List<CollectedRepo> mOldRepoList;

    public CollectDiffCallback(List<CollectedRepo> newRepoList, List<CollectedRepo> oldRepoList) {
      if (newRepoList == null) newRepoList = new ArrayList<>(0);
      if (oldRepoList == null) oldRepoList = new ArrayList<>(0);
      this.mNewRepoList = newRepoList;
      this.mOldRepoList = oldRepoList;
    }

    @Override public int getOldListSize() {
      return mOldRepoList.size();
    }

    @Override public int getNewListSize() {
      return mNewRepoList.size();
    }

    @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
      return mOldRepoList.get(oldItemPosition)
          .getId()
          .equals(mNewRepoList.get(newItemPosition).getId());
    }

    @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
      CollectedRepo oldRepo = mOldRepoList.get(oldItemPosition);
      CollectedRepo newRepo = mNewRepoList.get(newItemPosition);
      return oldRepo.getCollectTime() == newRepo.getCollectTime()
          || oldRepo.getStargazersCount() == newRepo.getStargazersCount();
    }
  }
}
