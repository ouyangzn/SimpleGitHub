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

package com.ouyangzn.github.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public class ImageLoader {

  @SuppressLint("StaticFieldLeak") private static Context sContext;

  private ImageLoader() {}

  public static void init(Context context) {
    sContext = context.getApplicationContext();
  }

  public static void loadAsCircle(ImageView target, int resId) {
    Glide.with(sContext).load(resId).bitmapTransform(new CropCircleTransformation(sContext)).into(target);
  }

  public static void load(ImageView target, String url) {
    Glide.with(sContext).load(url).into(target);
  }
}
