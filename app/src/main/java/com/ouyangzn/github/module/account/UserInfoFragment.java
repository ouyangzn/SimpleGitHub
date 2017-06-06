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

package com.ouyangzn.github.module.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.bean.apibean.User;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.ImageLoader;
import com.ouyangzn.github.utils.UiUtils;

import static com.ouyangzn.github.utils.Actions.openUrl;

/**
 * Created by ouyangzn on 2017/6/1.<br/>
 * Description：用户信息
 */
public class UserInfoFragment extends BaseFragment {

  public static final String EXTRA_KEY_USER = "user";

  @BindView(R.id.img_user_info_photo) ImageView mImgPhoto;
  @BindView(R.id.tv_user_info_name) TextView mTvName;
  @BindView(R.id.tv_user_info_email) TextView mTvEmail;
  @BindView(R.id.tv_user_info_url) TextView mTvUrl;
  @BindView(R.id.btn_user_info_logout) Button mBtnLogout;
  private User mUser;

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override public BasePresenter initPresenter() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_user_info;
  }

  @Override protected void initData(Bundle savedInstanceState) {
    mUser = getArguments().getParcelable(EXTRA_KEY_USER);
  }

  @Override protected void initView(View parent) {
    UiUtils.setCenterTitle(mToolbar, R.string.title_user_info);
    mToolbar.setNavigationIcon(R.drawable.ic_back_white);
    mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    mBtnLogout.setVisibility(mUser.equals(App.getUser()) ? View.VISIBLE : View.GONE);
    ImageLoader.loadAsCircle(mImgPhoto, R.drawable.ic_default_photo, mUser.getAvatarUrl());
    mTvName.setText(mUser.getAuthorName());
    mTvEmail.setText(mUser.getEmail());
    UiUtils.addUnderLine(mTvEmail);
    mTvUrl.setText(mUser.getHtmlUrl());
    UiUtils.addUnderLine(mTvUrl);
  }

  @OnClick({ R.id.btn_user_info_logout, R.id.tv_user_info_url, R.id.tv_user_info_email })
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_user_info_url: {
        openUrl(getActivity(), mUser.getHtmlUrl());
        break;
      }
      case R.id.btn_user_info_logout: {
        App.onLogout();
        getActivity().onBackPressed();
        break;
      }
      case R.id.tv_user_info_email: {
        String email = mUser.getEmail();
        if (!TextUtils.isEmpty(email)) {
          CommonUtils.copy(getContext(), email);
          toast(R.string.tip_copy_success);
        }
        break;
      }
    }
  }

  @OnLongClick({ R.id.tv_user_info_email }) public boolean onLongClick(View v) {
    switch (v.getId()) {
      case R.id.tv_user_info_email: {
        String email = mUser.getEmail();
        if (!TextUtils.isEmpty(email)) {
          CommonUtils.copy(getContext(), email);
          toast(R.string.tip_copy_success);
        }
        break;
      }
    }
    return false;
  }
}
