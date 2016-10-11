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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.ouyangzn.github.db.GlobalRealmMigration;
import com.ouyangzn.github.json.DoubleAdapter;
import com.ouyangzn.github.json.IntegerAdapter;
import com.ouyangzn.github.json.LongAdapter;
import com.ouyangzn.github.utils.ImageLoader;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.ouyangzn.github.db.GlobalRealmMigration.DB_NAME;
import static com.ouyangzn.github.db.GlobalRealmMigration.DB_VERSION;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class App extends Application {

  private static App sApp;

  private Gson mGson;

  public static App getApp() {
    return sApp;
  }

  public Realm getGlobalRealm() {
    return Realm.getInstance(new RealmConfiguration.Builder().name(DB_NAME)
        // 密码必须64个字节
        .encryptionKey(getGlobalRealmKey())
        .schemaVersion(DB_VERSION)
        .migration(new GlobalRealmMigration())
        //.deleteRealmIfMigrationNeeded()
        .build());
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
    Realm.init(sApp);
  }

  protected byte[] getGlobalRealmKey() {
    return "abcdefghijklmnopqrstuvwxyz-ABCDEFGHIJKLMNOPQRSTUVWXYZ-0123456789".getBytes();
  }
}
