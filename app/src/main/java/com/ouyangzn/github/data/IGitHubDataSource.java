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

package com.ouyangzn.github.data;

import com.ouyangzn.github.base.CommonConstants.GitHub;
import com.ouyangzn.github.bean.apibean.SearchResult;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import rx.Observable;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public interface IGitHubDataSource {
  /**
   * 查询GitHub的数据
   *
   * @param factor 搜索因子（即搜索条件）
   * @param sort 查询的类别 {@link GitHub#SORT_STARS}、{@link GitHub#SORT_FORKS}、{@link
   * GitHub#SORT_UPDATED}
   * @param order 排序规则 {@link GitHub#ORDER_ASC}、{@link GitHub#ORDER_DESC}
   * @param perPage 每页的数据量
   * @param page 搜索页数
   */
  Observable<SearchResult> queryByKeyword(SearchFactor factor, String sort, String order,
      int perPage, int page);

  Observable<SearchResult> queryByKeyword(SearchFactor factor, int perPage, int page);

  Observable<SearchResult> queryByKeyword(SearchFactor factor, int page);

}
