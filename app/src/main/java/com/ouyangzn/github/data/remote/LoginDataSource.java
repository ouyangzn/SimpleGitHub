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

package com.ouyangzn.github.data.remote;

import android.util.Base64;
import com.ouyangzn.github.bean.apibean.User;
import com.ouyangzn.github.network.Api;
import rx.Observable;

/**
 * Created by ouyangzn on 2017/6/1.<br/>
 * Description：登录相关
 */
public class LoginDataSource {
  public static Observable<User> login(String username, String password) {
    String authorization =
        "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
    return Api.getUserApi().login(authorization);
  }
}
