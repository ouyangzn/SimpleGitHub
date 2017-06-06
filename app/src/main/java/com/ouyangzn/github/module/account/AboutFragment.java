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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.ImageLoader;
import com.ouyangzn.github.utils.UiUtils;

import static com.ouyangzn.github.utils.Actions.openUrl;

/**
 * Created by ouyangzn on 2017/6/6.<br/>
 * Description：
 */
public class AboutFragment extends BaseFragment {

  @BindView(R.id.img_about_photo) ImageView mImgPhoto;
  @BindView(R.id.tv_about_email) TextView mTvEmail;
  @BindView(R.id.tv_about_gmail) TextView mTvGmail;
  @BindView(R.id.tv_about_url) TextView mTvUrl;

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_about;
  }

  @Override public BasePresenter initPresenter() {
    return null;
  }

  @Override protected void initData(Bundle savedInstanceState) {

  }

  @Override protected void initView(View parent) {
    UiUtils.setCenterTitle(mToolbar, R.string.title_about);
    UiUtils.addWhiteBackBtn(mToolbar, getActivity());
    ImageLoader.loadAsCircle(mImgPhoto, R.drawable.ic_photo);
    UiUtils.addUnderLine(mTvEmail);
    UiUtils.addUnderLine(mTvGmail);
    UiUtils.addUnderLine(mTvUrl);
  }

  @OnClick({ R.id.tv_about_gmail, R.id.tv_about_url, R.id.tv_about_email })
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_about_url: {
        openUrl(getActivity(), mTvUrl.getText().toString());
        break;
      }
      case R.id.tv_about_gmail: {
        CommonUtils.copy(getContext(), mTvGmail.getText().toString());
        toast(R.string.tip_copy_success);
        break;
      }
      case R.id.tv_about_email: {
        CommonUtils.copy(getContext(), mTvEmail.getText().toString());
        toast(R.string.tip_copy_success);
        break;
      }
    }
  }

  @OnLongClick({ R.id.tv_about_gmail, R.id.tv_about_email }) public boolean onLongClick(View v) {
    switch (v.getId()) {
      case R.id.tv_about_gmail: {
        CommonUtils.copy(getContext(), mTvGmail.getText().toString());
        toast(R.string.tip_copy_success);
        break;
      }
      case R.id.tv_about_email: {
        CommonUtils.copy(getContext(), mTvEmail.getText().toString());
        toast(R.string.tip_copy_success);
        break;
      }
    }
    return false;
  }
}
