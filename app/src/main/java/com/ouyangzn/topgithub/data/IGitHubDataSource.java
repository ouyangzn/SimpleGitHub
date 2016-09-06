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

package com.ouyangzn.topgithub.data;

import com.ouyangzn.topgithub.base.CommonConstants.GitHub;
import com.ouyangzn.topgithub.bean.SearchResult;
import rx.Observable;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public interface IGitHubDataSource {
  /**
   * 查询GitHub的数据
   *
   * @param keyword 关键字
   * @param language 搜索的语言
   * @param sort 查询的类别 {@link GitHub#SORT_STARS}、{@link GitHub#SORT_FORKS}、{@link
   * GitHub#SORT_UPDATED}
   * @param order 排序规则 {@link GitHub#ORDER_ASC}、{@link GitHub#ORDER_DESC}
   * @param perPage 每页的数据数
   * @param page 搜索页数
   */
  Observable<SearchResult> queryByKeyword(String keyword, String language, String sort,
      String order, int perPage, int page);

  Observable<SearchResult> queryByKeyword(String keyword, String language, int page);

  Observable<SearchResult> queryByKeyword(String keyword, int page);
}
