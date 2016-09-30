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

package com.ouyangzn.github.data.remote;

import android.text.TextUtils;
import com.ouyangzn.github.base.CommonConstants.GitHub;
import com.ouyangzn.github.bean.apibean.SearchResult;
import com.ouyangzn.github.bean.localbean.SearchFactor;
import com.ouyangzn.github.data.IGitHubData;
import com.ouyangzn.github.network.Api;
import java.net.URLDecoder;
import rx.Observable;

import static com.ouyangzn.github.base.CommonConstants.NormalCons.LIMIT_20;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 * <p>eg:</p>
 * <li>no keyword-->https://api.github.com/search/repositories?q=+language:java&sort=stars&per_page=5&page=0</li>
 * <li>has keyword-->https://api.github.com/search/repositories?q=android+language:java&sort=stars&per_page=5&page=0</li>
 * <li>has keyword-->https://api.github.com/search/repositories?q=android+created:>2015-01-09+language:java&sort=stars&order=desc&per_page=3&page=1</li>
 */
public class RemoteGitHubData implements IGitHubData {

  @Override
  public Observable<SearchResult> queryByKeyword(SearchFactor factor, String sort, String order,
      int perPage, int page) {
    String q = "";
    if (factor.keyword != null) q = factor.keyword;
    if (!TextUtils.isEmpty(factor.getCreateDate())) {
      // 防止无keyword时，结果搜不到东西
      q = q.concat(URLDecoder.decode("+") + "created:>" + factor.getCreateDate());
    }
    if (!TextUtils.isEmpty(factor.language)) {
      // 防止无keyword时，结果搜不到东西
      q = q.concat(URLDecoder.decode("+") + "language:" + factor.language);
    }
    return Api.getSearchApi().query(q, sort, order, perPage, page);
  }

  @Override
  public Observable<SearchResult> queryByKeyword(SearchFactor factor, int perPage, int page) {
    return queryByKeyword(factor, GitHub.SORT_STARS, GitHub.ORDER_DESC, perPage, page);
  }

  @Override public Observable<SearchResult> queryByKeyword(SearchFactor factor, int page) {
    return queryByKeyword(factor, LIMIT_20, page);
  }
}
