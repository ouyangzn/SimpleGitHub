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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.ouyangzn.github.App;
import com.ouyangzn.github.R;
import com.ouyangzn.github.base.BaseActivity;
import com.ouyangzn.github.module.collect.CollectActivity;
import com.ouyangzn.github.module.common.MainPagerAdapter;
import com.ouyangzn.github.module.main.MainContract.IMainPresenter;
import com.ouyangzn.github.module.main.MainContract.IMainView;
import com.ouyangzn.github.utils.Actions;
import com.ouyangzn.github.utils.DialogUtil;
import com.ouyangzn.github.utils.ImageLoader;
import com.ouyangzn.github.utils.ScreenUtil;
import com.ouyangzn.github.utils.UIUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity<IMainView, IMainPresenter>
    implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

  private DrawerLayout mDrawerLayout;
  private NavigationView mNavView;

  @Override protected int getContentView() {
    return R.layout.activity_main;
  }

  @Override public IMainPresenter initPresenter() {
    return null;
  }

  @Override protected void initData() {
  }

  @Override protected void initView(Bundle savedInstanceState) {
    Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
    toolbar.setTitle(R.string.app_name);
    toolbar.setTitleTextColor(Color.WHITE);
    setSupportActionBar(toolbar);
    ImageView collectImg =
        UIUtil.addImage2Toolbar(toolbar, R.drawable.selector_collect, Gravity.END,
            new int[] { 0, 0, ScreenUtil.dp2px(mContext, 15), 0 });
    collectImg.setId(R.id.id_toolbar_right_img);
    collectImg.setOnClickListener(this);
    // @BindView 找不到，NavigationView下的view直接find也找不到
    mNavView = ButterKnife.findById(this, R.id.nav_view);
    mNavView.setNavigationItemSelectedListener(this);
    ImageView img_photo = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.img_photo);
    ImageLoader.loadAsCircle(img_photo, R.drawable.ic_photo);

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

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.id_toolbar_right_img: {
        UIUtil.openActivity(this, CollectActivity.class);
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
        toast("点击about");
        break;
      }
      case R.id.nav_username: {
        showInputUsernameDialog(null);
        break;
      }
      case R.id.nav_my_stars: {
        String username = App.getUsername();
        if (TextUtils.isEmpty(username)) {
          showInputUsernameDialog(v -> Actions.gotoMineStars(this));
        } else {
          Actions.gotoMineStars(this);
        }
        break;
      }
    }
    mDrawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  /**
   * 显示输入用户名的dialog
   * @param onConfirmClick 点确定的回调
   */
  private void showInputUsernameDialog(View.OnClickListener onConfirmClick) {
    AlertDialog.Builder builder = DialogUtil.getAlertDialog(mContext);
    AlertDialog dialog = builder.setView(R.layout.dialog_input_view).create();
    dialog.show();
    TextView tvTitle = ButterKnife.findById(dialog, R.id.tv_dialog_input_title);
    tvTitle.setText(R.string.username_github);
    EditText etUsername = ButterKnife.findById(dialog, R.id.et_dialog_input);
    String user = App.getUsername();
    etUsername.setText(user);
    if (!TextUtils.isEmpty(user)) {
      etUsername.setSelection(user.length());
    }
    Button btnConfirm = ButterKnife.findById(dialog, R.id.btn_dialog_input_confirm);
    btnConfirm.setTag(dialog);
    btnConfirm.setOnClickListener(v -> {
      ScreenUtil.hideKeyBoard(v);
      dialog.dismiss();
      String username = etUsername.getText().toString().trim();
      if (TextUtils.isEmpty(username)) {
        toast(R.string.error_username_null);
        return;
      }
      App.setUsername(username);
      if (onConfirmClick != null) onConfirmClick.onClick(v);
    });
    Button btnCancel = ButterKnife.findById(dialog, R.id.btn_dialog_input_cancel);
    btnCancel.setTag(dialog);
    btnCancel.setOnClickListener(v -> {
      ScreenUtil.hideKeyBoard(v);
      dialog.dismiss();
    });
  }
}
