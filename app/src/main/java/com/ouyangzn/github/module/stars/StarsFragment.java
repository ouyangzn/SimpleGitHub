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

package com.ouyangzn.github.module.stars;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.module.common.StarsAdapter;
import com.ouyangzn.github.module.stars.StarsContract.IStarsPresenter;
import com.ouyangzn.github.module.stars.StarsContract.IStarsView;
import com.ouyangzn.github.utils.CommonUtil;
import com.ouyangzn.github.utils.DialogUtil;
import com.ouyangzn.github.utils.ScreenUtil;
import com.ouyangzn.github.utils.UIUtil;
import com.ouyangzn.github.view.InputEdit;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func1;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.LIMIT_10;

/**
 * Created by ouyangzn on 2017/5/24.<br/>
 * Description：我star的项目
 */
public class StarsFragment extends BaseFragment<IStarsView, IStarsPresenter>
    implements IStarsView, BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener,
    BaseRecyclerViewAdapter.OnLoadingMoreListener {

  private final int LIMIT = LIMIT_10;
  private final int FIRST_PAGE = 1;

  @BindView(R.id.refresh_stars) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.view_search_stars) InputEdit mSearchEdit;
  @BindView(R.id.recycler_stars) RecyclerView mRecyclerView;
  private StarsAdapter mAdapter;
  // 当前分页页数
  private int mCurrPage = FIRST_PAGE;

  @Override protected Status getCurrentStatus() {
    return Status.STATUS_LOADING;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_stars;
  }

  @Override protected void initData(Bundle savedInstanceState) {
    mAdapter = new StarsAdapter(new ArrayList<>(0));
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mAdapter.setOnRecyclerViewItemLongClickListener(this);
    mAdapter.setOnLoadingMoreListener(this);
    queryMineStars(true);
  }

  @Override public void onDestroyView() {
    mRecyclerView.setAdapter(null);
    super.onDestroyView();
  }

  @Override protected void initView(View parent) {
    getActivity().setTitle(R.string.title_stars);
    Context context = getContext();
    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        queryMineStars(true);
      }
    });
    //mRefreshLayout.setOnRefreshListener(() -> queryMineStars(true));
    mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    mAdapter.setEmptyView(mInflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    UIUtil.setRecyclerViewLoadMore(mAdapter, mRecyclerView);
    mRecyclerView.setAdapter(mAdapter);
    RxView.touches(mRecyclerView, new Func1<MotionEvent, Boolean>() {
      @Override public Boolean call(MotionEvent motionEvent) {
        ScreenUtil.hideKeyBoard(mSearchEdit);
        mSearchEdit.clearFocus();
        return false;
      }
    }).compose(mProvider.bindUntilEvent(FragmentEvent.DESTROY_VIEW)).subscribe();
    //RxView.touches(mRecyclerView, event -> {
    //  ScreenUtil.hideKeyBoard(mSearchEdit);
    //  mSearchEdit.clearFocus();
    //  return false;
    //}).compose(mProvider.bindUntilEvent(FragmentEvent.DESTROY_VIEW)).subscribe();
  }

  private void queryMineStars(boolean isRefresh) {
    if (isRefresh) mCurrPage = FIRST_PAGE;
    mPresenter.queryMineStars(mCurrPage, LIMIT);
  }

  @Override public IStarsPresenter initPresenter() {
    return new StarsPresenter(mProvider);
  }

  @Override public void showStars(List<Repository> repoList) {
    switchStatus(Status.STATUS_NORMAL);
    UIUtil.stopRefresh(mRefreshLayout);
    mCurrPage++;
    if (!mAdapter.isLoadingMore()) {
      mAdapter.setHasMore(repoList.size() == LIMIT);
      mAdapter.resetData(repoList);
    } else {
      mAdapter.loadMoreFinish(repoList.size() == LIMIT, repoList);
    }
  }

  @Override public void showOnQueryStarsFail(String error) {
    if (!mAdapter.isLoadingMore()) {
      UIUtil.stopRefresh(mRefreshLayout);
      switchStatus(Status.STATUS_ERROR);
      toast(error);
    } else {
      mAdapter.loadMoreFailure();
    }
  }

  @Override public void requestMoreData() {
    queryMineStars(false);
  }

  @Override public void onItemClick(View view, int position) {
    Repository repo = mAdapter.getItem(position);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(repo.getHtmlUrl()));
    startActivity(intent);
  }

  @Override public boolean onItemLongClick(View view, final int position) {
    AlertDialog.Builder builder = DialogUtil.getAlertDialog(getContext());
    builder.setItems(R.array.long_click_stars_dialog_item, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialog, int which) {
        Repository item = mAdapter.getItem(position);
        switch (which) {
          case 0:
            copyUrl(item.getHtmlUrl());
            break;
          case 1:
            CollectRepo(item);
            break;
        }
        dialog.dismiss();
      }
    }).show();
    return true;
    //builder.setItems(R.array.long_click_stars_dialog_item, (dialog, which) -> {
    //  Repository item = mAdapter.getItem(position);
    //  switch (which) {
    //    case 0:
    //      copyUrl(item.getHtmlUrl());
    //      break;
    //    case 1:
    //      CollectRepo(item);
    //      break;
    //  }
    //  dialog.dismiss();
    //}).show();
    //return true;
  }

  private void CollectRepo(Repository repo) {

  }

  private void copyUrl(String url) {
    CommonUtil.copy(getContext(), url);
    toast(R.string.tip_copy_success);
  }
}
