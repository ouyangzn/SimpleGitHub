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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import static com.ouyangzn.github.App.getAuthorization;
import static com.ouyangzn.github.App.getUsername;

/**
 * Created by ouyangzn on 2016/9/30.<br/>
 * Description：
 */
public class CommonUtils {

  /**
   * 复制文本
   *
   * @param context Context
   * @param content 文本内容
   */
  public static void copy(Context context, String content) {
    ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData data = ClipData.newPlainText(content, content);
    clip.setPrimaryClip(data);
  }

  /**
   * 是否能编辑 e.g. star、fork...
   *
   * @return true表示可以
   */
  public static boolean canEdit() {
    return !TextUtils.isEmpty(getAuthorization());
  }

  /**
   * 是否能浏览某个用户下的东西
   *
   * @return true表示可以
   */
  public static boolean canBrowsing() {
    return !TextUtils.isEmpty(getAuthorization()) || !TextUtils.isEmpty(getUsername());
  }

}
