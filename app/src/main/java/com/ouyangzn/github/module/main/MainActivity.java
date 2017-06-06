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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseActivity;
import com.ouyangzn.github.bean.apibean.User;
import com.ouyangzn.github.event.Event;
import com.ouyangzn.github.module.common.MainPagerAdapter;
import com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import com.ouyangzn.github.module.main.MainContract.IMainView;
import com.ouyangzn.github.utils.Actions;
import com.ouyangzn.github.utils.CommonUtils;
import com.ouyangzn.github.utils.ImageLoader;
import com.ouyangzn.github.utils.ScreenUtils;
import com.ouyangzn.github.utils.UiUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.ouyangzn.github.event.EventType.TYPE_LOGIN;
import static com.ouyangzn.github.event.EventType.TYPE_LOGOUT;

public class MainActivity extends BaseActivity<IMainView, IMainPresenter>
    implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

  private final int REQUEST_STARS_LOGIN = 1;
  private DrawerLayout mDrawerLayout;
  private NavigationView mNavView;
  private ImageView mImgAvatar;
  private TextView mTvEmail;

  @Override protected int getContentView() {
    return R.layout.activity_main;
  }

  @Override public IMainPresenter initPresenter() {
    return null;
  }

  @Override protected void initData() {
    EventBus.getDefault().register(this);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @Override protected void initView(Bundle savedInstanceState) {
    Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
    UiUtils.setCenterTitle(toolbar, R.string.app_name);
    ImageView collectImg =
        UiUtils.addImage2Toolbar(toolbar, R.drawable.selector_collect, Gravity.END,
            new int[] { 0, 0, ScreenUtils.dp2px(mContext, 15), 0 });
    collectImg.setId(R.id.id_toolbar_right_img);
    collectImg.setOnClickListener(this);
    // @BindView 找不到，NavigationView下的view直接find也找不到
    mNavView = ButterKnife.findById(this, R.id.nav_view);
    mNavView.setNavigationItemSelectedListener(this);
    View headerView = mNavView.getHeaderView(0);
    mImgAvatar = (ImageView) headerView.findViewById(R.id.img_photo);
    mTvEmail = (TextView) headerView.findViewById(R.id.tv_email);
    User user = App.getUser();
    if (user != null) {
      ImageLoader.loadAsCircle(mImgAvatar, R.drawable.ic_default_photo, user.getAvatarUrl());
      mTvEmail.setText(user.getEmail());
    } else {
      mImgAvatar.setImageResource(R.drawable.ic_default_photo);
      mTvEmail.setText(null);
    }

    // @BindView 找不到
    mDrawerLayout = ButterKnife.findById(this, R.id.drawer_layout);
    ActionBarDrawerToggle toggle =
        new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
    mDrawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    ViewPager viewPager = ButterKnife.findById(this, R.id.view_pager_main);
    TabLayout tabLayout = ButterKnife.findById(this, R.id.tabs);
    tabLayout.setupWithViewPager(viewPager);
    /** 页面list **/
    List<Fragment> fragmentList = new ArrayList<>();
    /** 页面title list **/
    String[] array = getResources().getStringArray(R.array.array_language);
    List<String> titleList = Arrays.asList(array);
    for (String title : titleList) {
      fragmentList.add(MainFragment.getInstance(title));
    }
    viewPager.setAdapter(
        new MainPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
    viewPager.setOffscreenPageLimit(fragmentList.size());
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onMessageEvent(Event event) {
    switch (event.getEventType()) {
      case TYPE_LOGIN: {
        User user = App.getUser();
        ImageLoader.loadAsCircle(mImgAvatar, R.drawable.ic_default_photo, user.getAvatarUrl());
        mTvEmail.setText(user.getEmail());
        break;
      }
      case TYPE_LOGOUT: {
        mImgAvatar.setImageResource(R.drawable.ic_default_photo);
        mTvEmail.setText(null);
        break;
      }
    }
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.id_toolbar_right_img: {
        Actions.gotoCollections(this);
        break;
      }
    }
  }

  @Override public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.nav_about: {
        Actions.gotoAbout(this);
        break;
      }
      case R.id.nav_account: {
        if (CommonUtils.canEdit()) {
          Actions.gotoUserInfo(this, App.getUser());
        } else {
          Actions.gotoLogin(this);
        }
        break;
      }
      case R.id.nav_my_stars: {
        if (CommonUtils.canBrowsing()) {
          Actions.gotoMineStars(this);
        } else {
          Actions.gotoLogin(this, REQUEST_STARS_LOGIN);
        }
        break;
      }
    }
    mDrawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_STARS_LOGIN && resultCode == RESULT_OK) {
      Actions.gotoMineStars(this);
    }
  }
}
