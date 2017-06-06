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

package com.ouyangzn.github.network;

import com.ouyangzn.github.bean.apibean.RepoSearchResult;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by ouyangzn on 2016/9/5.<br/>
 * Description：
 */
public interface SearchApi {

  @GET("/search/repositories") Observable<RepoSearchResult> query(@Query("q") String keyword,
      @Query("sort") String sort, @Query("order") String order, @Query("per_page") int perPage,
      @Query("page") int page);
}
