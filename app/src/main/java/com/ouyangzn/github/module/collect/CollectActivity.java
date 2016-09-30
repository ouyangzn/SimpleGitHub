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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import butterknife.BindView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseActivity;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.module.common.CollectAdapter;
import com.ouyangzn.github.utils.CommonUtil;
import com.ouyangzn.github.utils.DialogUtil;
import com.ouyangzn.github.utils.Log;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.view.InputEdit;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.ouyangzn.github.module.collect.CollectContract.ICollectPresenter;
import static com.ouyangzn.github.module.collect.CollectContract.ICollectView;

public class CollectActivity extends BaseActivity<ICollectView, ICollectPresenter>
    implements ICollectView, BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener,
    BaseRecyclerViewAdapter.OnRecyclerViewItemLongClickListener {

  @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.view_search) InputEdit mSearchEdit;
  @BindView(R.id.recycler_collect) RecyclerView mRecyclerView;
  @BindView(R.id.layout_loading) View mLoadingView;

  private CollectAdapter mCollectAdapter;
  // 重新加载或者加载下一页
  private boolean mIsRefresh = true;

  @Override protected int getContentView() {
    return R.layout.activity_collect;
  }

  @Override public ICollectPresenter initPresenter() {
    return new CollectPresenter(mContext);
  }

  @Override protected void initData() {
    mCollectAdapter = new CollectAdapter(mContext, new ArrayList<CollectedRepo>(0));
    mCollectAdapter.setOnRecyclerViewItemClickListener(this);
    mCollectAdapter.setOnRecyclerViewItemLongClickListener(this);
    queryAllCollect(true);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    setTitle(R.string.title_collect);
    mLoadingView.setVisibility(View.VISIBLE);
    LayoutInflater inflater = getLayoutInflater();
    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    mCollectAdapter.setEmptyView(inflater.inflate(R.layout.item_no_data, mRecyclerView, false));
    mRecyclerView.setAdapter(mCollectAdapter);
    RxView.touches(mRecyclerView, new Func1<MotionEvent, Boolean>() {
      @Override public Boolean call(MotionEvent event) {
        ScreenUtils.hideKeyBoard(mSearchEdit);
        mSearchEdit.clearFocus();
        return false;
      }
    }).subscribe();

    mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        queryAllCollect(true);
      }
    });

    mSearchEdit.setOnClearTextListener(new InputEdit.OnClearTextListener() {
      @Override public void onClearText() {

      }
    });
    mSearchEdit.setOnEditorActionListener(new Action1<TextViewEditorActionEvent>() {
      @Override public void call(TextViewEditorActionEvent actionEvent) {
        if (EditorInfo.IME_ACTION_SEARCH == actionEvent.actionId()) {
          String keyword = mSearchEdit.getInputText().trim();
          ScreenUtils.hideKeyBoard(mSearchEdit);
          mSearchEdit.clearFocus();
          if (!TextUtils.isEmpty(keyword)) {
            // todo 从收藏中搜索

          }
        }
      }
    });

  }

  private void queryAllCollect(boolean isRefresh) {
    if (isRefresh) {
      mRefreshLayout.setRefreshing(true);
    }
    mIsRefresh = isRefresh;
    mPresenter.queryAllCollect();
  }

  @Override public void showCollect(List<CollectedRepo> repoList) {
    Log.d(TAG, "----------repoList = " + repoList);
    mLoadingView.setVisibility(View.GONE);
    if (mIsRefresh) {
      mRefreshLayout.setRefreshing(false);
      mCollectAdapter.resetData(repoList);
    } else {
      mCollectAdapter.addData(repoList);
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
}
