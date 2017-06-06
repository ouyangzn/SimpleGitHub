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

package com.ouyangzn.github.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.ouyangzn.github.base.BaseFragment;
import com.ouyangzn.github.base.BaseFragmentActivity;
import com.ouyangzn.github.bean.apibean.User;
import com.ouyangzn.github.module.account.AboutFragment;
import com.ouyangzn.github.module.account.LoginFragment;
import com.ouyangzn.github.module.account.UserInfoFragment;
import com.ouyangzn.github.module.collect.CollectionsFragment;
import com.ouyangzn.github.module.stars.StarsFragment;

import static com.ouyangzn.github.module.account.UserInfoFragment.EXTRA_KEY_USER;

/**
 * Created by ouyangzn on 2017/5/24.<br/>
 * Description：ui跳转工具类
 */
public class Actions {

  /**
   * 跳转到用户信息
   * @param fragment 当前所在的fragment对象
   * @param user 用户
   */
  public static void gotoUserInfo(Fragment fragment, User user) {
    Bundle bundle = new Bundle(1);
    bundle.putParcelable(EXTRA_KEY_USER, user);
    startActivity(fragment, UserInfoFragment.class, bundle);
  }

  /**
   * 跳转到用户信息
   *
   * @param activity 当前所在的activity对象
   * @param user 用户
   */
  public static void gotoUserInfo(Activity activity, User user) {
    Bundle bundle = new Bundle(1);
    bundle.putParcelable(EXTRA_KEY_USER, user);
    startActivity(activity, UserInfoFragment.class, bundle);
  }

  /**
   * 跳转到我的stars
   *
   * @param fragment 当前所在的fragment对象
   */
  public static void gotoMineStars(Fragment fragment) {
    startActivity(fragment, StarsFragment.class);
  }

  /**
   * 跳转到我的stars
   *
   * @param activity 当前所在的activity对象
   */
  public static void gotoMineStars(Activity activity) {
    startActivity(activity, StarsFragment.class);
  }

  /**
   * 跳转到登录
   * @param fragment 当前所在的fragment对象
   */
  public static void gotoLogin(Fragment fragment) {
    startActivity(fragment, LoginFragment.class);
  }

  /**
   * 跳转到登录
   *
   * @param fragment 当前所在的fragment对象
   * @param requestCode 请求码
   */
  public static void gotoLogin(Fragment fragment, int requestCode) {
    startActivity(fragment, LoginFragment.class, requestCode);
  }

  /**
   * 跳转到登录
   *
   * @param activity 当前所在的activity对象
   */
  public static void gotoLogin(Activity activity) {
    startActivity(activity, LoginFragment.class);
  }

  /**
   * 跳转到登录
   *
   * @param activity 当前所在的activity对象
   * @param requestCode 请求码
   */
  public static void gotoLogin(Activity activity, int requestCode) {
    startActivity(activity, LoginFragment.class, requestCode);
  }

  /**
   * 跳转到我收藏
   *
   * @param fragment 当前所在的fragment对象
   */
  public static void gotoCollections(Fragment fragment) {
    startActivity(fragment, CollectionsFragment.class);
  }

  /**
   * 跳转到我收藏
   * @param activity 当前所在的activity对象
   */
  public static void gotoCollections(Activity activity) {
    startActivity(activity, CollectionsFragment.class);
  }

  /**
   * 跳转到关于
   *
   * @param activity 当前所在的activity对象
   */
  public static void gotoAbout(Activity activity) {
    startActivity(activity, AboutFragment.class);
  }

  /**
   * 打开一个链接
   *
   * @param activity 当前所在的activity对象
   * @param url url
   */
  public static void openUrl(Activity activity, String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    activity.startActivity(intent);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param fragment 当前所在的fragment对象
   * @param clazz 目标fragment
   */
  public static void startActivity(Fragment fragment, Class<? extends BaseFragment> clazz) {
    startActivity(fragment, clazz, null);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param fragment 当前所在的fragment对象
   * @param clazz 目标fragment
   * @param bundle 需要传过去的数据
   */
  public static void startActivity(Fragment fragment, Class<? extends BaseFragment> clazz,
      Bundle bundle) {
    startActivity(fragment, clazz, bundle, -1);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param fragment 当前所在的fragment对象
   * @param clazz 目标fragment
   * @param requestCode 请求码
   */
  public static void startActivity(Fragment fragment, Class<? extends BaseFragment> clazz,
      int requestCode) {
    startActivity(fragment, clazz, null, requestCode);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param fragment 当前所在的fragment对象
   * @param clazz 目标fragment
   * @param bundle 需要传过去的数据
   * @param requestCode 请求码
   */
  public static void startActivity(Fragment fragment, Class<? extends BaseFragment> clazz,
      Bundle bundle, int requestCode) {
    Intent intent = new Intent(fragment.getContext(), BaseFragmentActivity.class);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    intent.putExtra(BaseFragmentActivity.DATA_FRAGMENT_NAME, clazz.getName());
    fragment.startActivityForResult(intent, requestCode);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param activity 当前所在的activity对象
   * @param clazz 目标fragment
   */
  public static void startActivity(Activity activity, Class<? extends BaseFragment> clazz) {
    startActivity(activity, clazz, null);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param activity 当前所在的activity对象
   * @param clazz 目标fragment
   * @param bundle 需要传过去的数据
   */
  public static void startActivity(Activity activity, Class<? extends BaseFragment> clazz,
      Bundle bundle) {
    startActivity(activity, clazz, bundle, -1);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param activity 当前所在的activity对象
   * @param clazz 目标fragment
   * @param requestCode 请求码
   */
  public static void startActivity(Activity activity, Class<? extends BaseFragment> clazz,
      int requestCode) {
    startActivity(activity, clazz, null, requestCode);
  }

  /**
   * 跳转到另一个fragment
   *
   * @param activity 当前所在的activity对象
   * @param clazz 目标fragment
   * @param bundle 需要传过去的数据
   * @param requestCode 请求码
   */
  public static void startActivity(Activity activity, Class<? extends BaseFragment> clazz,
      Bundle bundle, int requestCode) {
    Intent intent = new Intent(activity.getApplicationContext(), BaseFragmentActivity.class);
    if (bundle != null) {
      intent.putExtras(bundle);
    }
    intent.putExtra(BaseFragmentActivity.DATA_FRAGMENT_NAME, clazz.getName());
    activity.startActivityForResult(intent, requestCode);
  }
}
