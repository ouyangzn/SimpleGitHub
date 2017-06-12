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

package com.ouyangzn.github.module.repodetail;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.github.utils.UiUtils;
import com.ouyangzn.github.view.ListPopupWindow;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ouyangzn on 2017/6/12.<br/>
 * Description：详情
 */
public class RepoDetailFragment extends BaseFragment implements View.OnClickListener {

  public static final String KEY_REPO = "repo";

  @BindView(R.id.tv_repo_detail_owner) TextView mTvOwner;
  @BindView(R.id.tv_repo_detail_language) TextView mTvLanguage;
  @BindView(R.id.tv_repo_detail_url) TextView mTvUrl;
  @BindView(R.id.tv_repo_detail_latest_commit) TextView mTvLatestCommit;
  @BindView(R.id.tv_repo_detail_desc) TextView mTvDesc;
  private ListPopupWindow mPopupWindow;
  private ImageView mImgMore;
  private Repository mRepo;

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_repo_detail;
  }

  @Override public BasePresenter initPresenter() {
    return null;
  }

  @Override protected void initData(Bundle savedInstanceState) {
    mRepo = getArguments().getParcelable(KEY_REPO);
  }

  @Override protected void initView(View parent) {
    UiUtils.setCenterTitle(mToolbar, mRepo.getName());
    UiUtils.addWhiteBackBtn(mToolbar, getActivity());
    mImgMore = UiUtils.addToolbarRightBtn(mToolbar, R.drawable.ic_more);
    mImgMore.setId(R.id.id_toolbar_right_img);
    mImgMore.setOnClickListener(this);
    mTvOwner.setText(getString(R.string.repo_detail_owner, mRepo.getOwner().getAuthorName()));
    mTvLanguage.setText(getString(R.string.repo_detail_language, mRepo.getLanguage()));
    mTvUrl.setText(getString(R.string.repo_detail_url, mRepo.getHtmlUrl()));
    mTvLatestCommit.setText(getString(R.string.repo_detail_latest_commit, mRepo.getUpdatedAt()));
    mTvDesc.setText(mRepo.getDescription());
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.id_toolbar_right_img: {
        List<String> items =
            Arrays.asList(getResources().getStringArray(R.array.dialog_item_repo_detail_more));
        mPopupWindow = DialogUtils.showListPop(getContext(), items, mImgMore, (position) -> {
          toast("点击:" + items.get(position));
          //mPopupWindow.dismiss();
        });
        break;
      }
    }
  }
}
