/*
 * Copyright (c) 2016.  ouyangzn   <ouyangzn@163.com>
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

package com.ouyangzn.github;

import android.app.Application;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.ouyangzn.github.db.DaoHelper;
import com.ouyangzn.github.json.DoubleAdapter;
import com.ouyangzn.github.json.IntegerAdapter;
import com.ouyangzn.github.json.LongAdapter;
import com.ouyangzn.github.utils.ImageLoader;
import com.ouyangzn.github.utils.SpUtils;

import static com.ouyangzn.github.utils.SpUtils.KEY_AUTHORIZATION;
import static com.ouyangzn.github.utils.SpUtils.KEY_USERNAME;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class App extends Application {

  private static App sApp;
  private static String sUsername;
  private static String sAuthorization;
  private Gson mGson;

  public static App getApp() {
    return sApp;
  }

  public static String getUsername() {
    if (TextUtils.isEmpty(sUsername)) {
      sUsername = SpUtils.getString(sApp, KEY_USERNAME);
    }
    return sUsername;
  }

  public static void setUsername(String username) {
    App.sUsername = username;
    SpUtils.put(sApp, KEY_USERNAME, username);
  }

  public static void login(String username, String password) {
    setAuthorization(
        "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));
  }

  public static String getAuthorization() {
    if (sAuthorization == null) {
      sAuthorization = SpUtils.getString(sApp, KEY_AUTHORIZATION);
    }
    return sAuthorization;
  }

  private static void setAuthorization(String authorization) {
    sAuthorization = authorization;
    SpUtils.put(sApp, KEY_AUTHORIZATION, sAuthorization);
  }

  public Gson getGson() {
    if (mGson == null) {
      mGson = new Gson();
      mGson = new GsonBuilder().registerTypeAdapterFactory(
          TypeAdapters.newFactory(int.class, Integer.class, new IntegerAdapter()))
          .registerTypeAdapterFactory(
              TypeAdapters.newFactory(double.class, Double.class, new DoubleAdapter()))
          .registerTypeAdapterFactory(
              TypeAdapters.newFactory(long.class, Long.class, new LongAdapter()))
          .create();
    }
    return mGson;
  }

  @Override public void onCreate() {
    super.onCreate();
    sApp = this;
    ImageLoader.init(sApp);
    DaoHelper.initDao(sApp);
  }

}
