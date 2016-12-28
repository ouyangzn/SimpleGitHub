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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseActivity;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.module.common.CollectAdapter;
import com.ouyangzn.github.utils.CommonUtil;
import com.ouyangzn.github.utils.DialogUtil;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.ScreenUtil;
import com.ouyangzn.github.view.InputEdit;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.navi.NaviLifecycle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.android.schedulers.AndroidSchedulers;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.LIMIT_10;
import static com.ouyangzn.github.module.collect.CollectContract.ICollectPresenter;
import static com.ouyangzn.github.module.collect.CollectContract.ICollectView;

public class CollectActivity extends BaseActivity<ICollectView, ICollectPresenter>
    implements ICollectView, BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener {

  // 使用rxLifecycle方便控制rxJava事件的取消订阅时机
  protected final LifecycleProvider<ActivityEvent> mProvider =
      NaviLifecycle.createActivityLifecycleProvider(this);

  private final int COUNT_EACH_PAGE = LIMIT_10;
  private final int FIRST_PAGE = 0;

  @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.view_search) InputEdit mSearchEdit;
  @BindView(R.id.recycler_collect) RecyclerView mRecyclerView;
  @BindView(R.id.layout_loading) View mLoadingView;

  private CollectAdapter mCollectAdapter;
  // 重新加载或者加载下一页
  private boolean mIsRefresh = true;
  // 当前分页页数
  private int mCurrPage = FIRST_PAGE;

  @Override protected int getContentView() {
    return R.layout.activity_collect;
  }

  @Override public ICollectPresenter initPresenter() {
    return new CollectPresenter(mContext, mProvider);
  }

  @Override protected void initData() {
    mCollectAdapter = new CollectAdapter(mContext, new ArrayList<>(0));
    mCollectAdapter.setOnRecyclerViewItemClickListener(this);
    mCollectAdapter.setOnRecyclerViewItemLongClickListener(this);
    mCollectAdapter.setOnLoadingMoreListener(this);
    queryCollect(true);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setTitle(R.string.title_collect);
    mLoadingView.setVisibility(View.VISIBLE);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    LayoutInflater inflater = getLayoutInflater();
    mCollectAdapter.setEmptyView(inflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    mCollectAdapter.setLoadMoreView(
        inflater.inflate(R.layout.item_load_more, mRecyclerView, false));
    mRecyclerView.setAdapter(mCollectAdapter);
    RxView.touches(mRecyclerView, event -> {
      ScreenUtil.hideKeyBoard(mSearchEdit);
      mSearchEdit.clearFocus();
      return false;
    }).compose(mProvider.bindUntilEvent(ActivityEvent.DESTROY)).subscribe();

    mRefreshLayout.setOnRefreshListener(() -> queryCollect(true));

    RxTextView.textChanges(mSearchEdit.getEditText())
        .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
        .compose(mProvider.bindUntilEvent(ActivityEvent.DESTROY))
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
              queryByKey(keyword);
            }
          }
        });
  }

  private void onClearKeyword() {
    mCurrPage = FIRST_PAGE;
    queryCollect(true);
  }

  private void queryByKey(String keyword) {
    mPresenter.queryByKey(keyword);
  }

  private void queryCollect(boolean isRefresh) {
    if (isRefresh) {
      mRefreshLayout.setRefreshing(true);
      mCurrPage = FIRST_PAGE;
    }
    mIsRefresh = isRefresh;
    mPresenter.queryCollect(mCurrPage, COUNT_EACH_PAGE);
  }

  @Override public void showCollect(List<CollectedRepo> repoList) {
    int listSize = repoList.size();
    Log.d(TAG, "----------repoList.size = " + listSize);
    mLoadingView.setVisibility(View.GONE);
    boolean hasMore = listSize == COUNT_EACH_PAGE;
    mCollectAdapter.setHasMore(hasMore);
    // 没有数据的话，没必要增加当前页数,其实是为了解决realm.findAllAsync时会先返回一个空集合的问题
    if (listSize > 0) {
      mCurrPage++;
    }
    if (mIsRefresh) {
      mRefreshLayout.setRefreshing(false);
      //calculateDiffAndRefresh(mCollectAdapter, repoList);
      mCollectAdapter.resetData(repoList);
    } else {
      if (mCollectAdapter.isLoadingMore()) {
        // loadMore获取数据速度太快了的时候，会crash：Cannot call this method(-->notifyDataSetChanged())
        // while RecyclerView is computing a layout or scrolling
        mRecyclerView.post(() -> mCollectAdapter.loadMoreFinish(hasMore, repoList));
      } else {
        mCollectAdapter.addData(repoList);
      }
    }
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

  @Override public void showErrorOnQueryFailure() {
    mLoadingView.setVisibility(View.GONE);
    if (mIsRefresh) mRefreshLayout.setRefreshing(false);
  }

  @Override public void showCollectQueryByKey(List<CollectedRepo> repoList) {
    mCollectAdapter.resetData(repoList);
  }

  @Override public void showQueryByKeyFailure() {
    mCollectAdapter.resetData(new ArrayList<>(0));
  }

  @Override public void showCollectionCanceled() {
    toast(R.string.tip_collect_cancel_success);
  }

  @Override public void showCollectionCancelFailure() {
    toast(R.string.error_collect_cancel_failure);
  }

  @Override public void setLoadingIndicator(boolean isActive) {
    //mRefreshLayout.setRefreshing(isActive);
  }

  @Override public void onItemClick(View view, int position) {
    CollectedRepo repo = mCollectAdapter.getItem(position);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(repo.htmlUrl));
    startActivity(intent);
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtil.getAlertDialog(mContext);
    builder.setItems(R.array.long_click_collect_dialog_item, (dialog, which) -> {
      CollectedRepo item = mCollectAdapter.getItem(position);
      switch (which) {
        case 0:
          copyUrl(item.htmlUrl);
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
    CommonUtil.copy(mContext, url);
    toast(R.string.tip_copy_success);
  }

  private void cancelCollectRepo(CollectedRepo repo) {
    mPresenter.cancelCollectRepo(repo);
  }

  @Override public void requestMoreData() {
    queryCollect(false);
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
      return mOldRepoList.get(oldItemPosition).id.equals(mNewRepoList.get(newItemPosition).id);
    }

    @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
      CollectedRepo oldRepo = mOldRepoList.get(oldItemPosition);
      CollectedRepo newRepo = mNewRepoList.get(newItemPosition);
      return oldRepo.collectTime == newRepo.collectTime
          || oldRepo.stargazersCount == newRepo.stargazersCount;
    }
  }
}
