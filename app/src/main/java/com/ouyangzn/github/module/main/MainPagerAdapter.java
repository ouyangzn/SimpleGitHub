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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

/**
 * Created by ouyangzn on 2016/10/25.<br/>
 * Description：
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
  private List<Fragment> fragmentList;
  private List<String> titleList;

  public MainPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
    super(fm);
    this.fragmentList = fragmentList;
    this.titleList = titleList;
  }

  /**
   * 得到每个页面
   */
  @Override public Fragment getItem(int position) {
    return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(position);
  }

  /**
   * 每个页面的title
   */
  @Override public CharSequence getPageTitle(int position) {
    return (titleList.size() > position) ? titleList.get(position) : "";
  }

  /**
   * 页面的总个数
   */
  @Override public int getCount() {
    return fragmentList == null ? 0 : fragmentList.size();
  }
}
