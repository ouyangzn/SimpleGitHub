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

import android.os.Bundle;
import android.view.View;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.module.stars.StarsContract.IStarsPresenter;
import com.ouyangzn.github.module.stars.StarsContract.IStarsView;

/**
 * Created by ouyangzn on 2017/5/24.<br/>
 * Description：我star的项目
 */
public class StarsFragment extends BaseFragment<IStarsView, IStarsPresenter> implements IStarsView {

  @Override protected Status getCurrentStatus() {
    return null;
  }

  @Override protected int getContentView() {
    return R.layout.fragment_stars;
  }

  @Override protected void initData(Bundle savedInstanceState) {

  }

  @Override protected void initView(View parent) {

  }

  @Override public IStarsPresenter initPresenter() {
    return new StarsPresenter(mProvider);
  }
}
