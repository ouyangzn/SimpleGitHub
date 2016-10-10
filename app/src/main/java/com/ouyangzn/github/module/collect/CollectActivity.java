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

import android.content.DialogInterface;
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
import android.view.inputmethod.EditorInfo;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
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
import java.util.ArrayList;
import java.util.List;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.LIMIT_10;
import static com.ouyangzn.github.module.collect.CollectContract.ICollectPresenter;
import static com.ouyangzn.github.module.collect.CollectContract.ICollectView;

public class CollectActivity extends BaseActivity<ICollectView, ICollectPresenter>
    implements ICollectView, BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener {

  private final int COUNT_EACH_PAGE = LIMIT_10;

  @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.view_search) InputEdit mSearchEdit;
  @BindView(R.id.recycler_collect) RecyclerView mRecyclerView;
  @BindView(R.id.layout_loading) View mLoadingView;

  private CollectAdapter mCollectAdapter;
  // 重新加载或者加载下一页
  private boolean mIsRefresh = true;
  // 当前分页页数
  private int mCurrPage = 0;

  @Override protected int getContentView() {
    return R.layout.activity_collect;
  }

  @Override public ICollectPresenter initPresenter() {
    return new CollectPresenter(mContext);
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
    }).subscribe();

    mRefreshLayout.setOnRefreshListener(() -> queryCollect(true));

    mSearchEdit.setOnClearTextListener(() -> {

    });

    mSearchEdit.setOnEditorActionListener(actionEvent -> {
      if (EditorInfo.IME_ACTION_SEARCH == actionEvent.actionId()) {
        String keyword = mSearchEdit.getInputText().trim();
        ScreenUtil.hideKeyBoard(mSearchEdit);
        mSearchEdit.clearFocus();
        if (!TextUtils.isEmpty(keyword)) {
          // todo 从收藏中搜索

        }
      }
    });
  }

  private void queryCollect(boolean isRefresh) {
    if (isRefresh) {
      mRefreshLayout.setRefreshing(true);
      mCurrPage = 0;
    }
    mIsRefresh = isRefresh;
    //mPresenter.queryAllCollect();
    mPresenter.queryCollect(mCurrPage, COUNT_EACH_PAGE);
  }

  @Override public void showCollect(List<CollectedRepo> repoList) {
    Log.d(TAG, "----------repoList.size = " + repoList.size());
    mLoadingView.setVisibility(View.GONE);
    boolean hasMore = repoList.size() == COUNT_EACH_PAGE;
    mCollectAdapter.setHasMore(hasMore);
    mCurrPage++;
    if (mIsRefresh) {
      mRefreshLayout.setRefreshing(false);
      // ------------很大几率crash,暂未解决,exception：Inconsistency detected. Invalid view holder adapter positionViewHolder---------------
      //// 使用DiffUtil计算有变化的数据进行局部刷新
      //List<CollectedRepo> oldData = mCollectAdapter.getData();
      //DiffUtil.DiffResult diffResult =
      //    DiffUtil.calculateDiff(new CollectDiffCallback(repoList, oldData));
      ///*
      //需要先替换adapter中的数据但不刷新，刷新通知交由diffResult.dispatchUpdatesTo()来做
      //<pre>
      //     List oldList = mAdapter.getData();
      //     DiffResult result = DiffUtil.calculateDiff(new MyCallback(oldList, newList));
      //     mAdapter.setData(newList);
      //     result.dispatchUpdatesTo(mAdapter);
      //</pre>
      //*/
      //oldData.clear();
      //oldData.addAll(repoList);
      //diffResult.dispatchUpdatesTo(mCollectAdapter);
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

  @Override public void showErrorOnQueryFailure() {
    mLoadingView.setVisibility(View.GONE);
    if (mIsRefresh) mRefreshLayout.setRefreshing(false);

  }

  @Override public void showNormalTips(String tips) {
    toast(tips);
  }

  @Override public void showErrorTips(String tips) {
    toast(tips);
  }

  @Override public void showProgressDialog() {
  }

  @Override public void dismissProgressDialog() {
  }

  @Override public void onItemClick(View view, int position) {
    CollectedRepo repo = mCollectAdapter.getItem(position);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(repo.htmlUrl));
    startActivity(intent);
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtil.getAlertDialog(mContext);
    builder.setItems(R.array.long_click_collect_dialog_item, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
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
      }
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
