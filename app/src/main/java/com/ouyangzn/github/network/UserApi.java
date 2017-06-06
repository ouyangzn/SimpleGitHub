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

package com.ouyangzn.github.network;

import com.ouyangzn.github.bean.apibean.Repository;
import com.ouyangzn.github.bean.apibean.User;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ouyangzn on 2017/5/18.<br/>
 * Description：用户相关API
 */
public interface UserApi {

  @GET("users/{user}/starred") Observable<List<Repository>> getStarred(
      @Path("user") String username, @Query("page") int page, @Query("per_page") int perPage);

  @GET("user/starred") Observable<List<Repository>> getStarred(@Query("page") int page,
      @Query("per_page") int perPage);

  /**
   * 登录
   *
   * @param authorization 字符串格式："Basic " + Base64.encodeToString((username + ":" +
   * password).getBytes(), Base64.NO_WRAP);
   * @return 登录用户的用户信息
   */
  @GET("user") Observable<User> login(@Header("Authorization") String authorization);
}
