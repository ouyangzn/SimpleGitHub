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
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.ouyangzn.github.json.DoubleAdapter;
import com.ouyangzn.github.json.IntegerAdapter;
import com.ouyangzn.github.json.LongAdapter;
import com.ouyangzn.github.utils.ImageLoader;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class App extends Application {

  private static Context sContext;

  private static Gson sGson;

  public static Context getContext() {
    return sContext;
  }

  public static Gson getGson() {
    if (sGson == null) {
      sGson = new Gson();
      sGson = new GsonBuilder().registerTypeAdapterFactory(
          TypeAdapters.newFactory(int.class, Integer.class, new IntegerAdapter()))
          .registerTypeAdapterFactory(
              TypeAdapters.newFactory(double.class, Double.class, new DoubleAdapter()))
          .registerTypeAdapterFactory(
              TypeAdapters.newFactory(long.class, Long.class, new LongAdapter()))
          .create();
    }
    return sGson;
  }

  @Override public void onCreate() {
    super.onCreate();
    sContext = this;
    ImageLoader.init(sContext);
  }
}
