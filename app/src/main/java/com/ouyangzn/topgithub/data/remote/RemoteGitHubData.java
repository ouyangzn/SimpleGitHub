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

package com.ouyangzn.topgithub.data.remote;

import android.text.TextUtils;
import com.ouyangzn.topgithub.base.CommonConstants.GitHub;
import com.ouyangzn.topgithub.bean.SearchResult;
import com.ouyangzn.topgithub.data.IGitHubDataSource;
import com.ouyangzn.topgithub.network.Api;
import java.net.URLDecoder;
import rx.Observable;

import static com.ouyangzn.topgithub.base.CommonConstants.NormalCons.LIMIT_20;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 * <p>eg:</p>
 * <li>no keyword-->https://api.github.com/search/repositories?q=+language:java&sort=stars&per_page=5&page=0</li>
 * <li>has keyword-->https://api.github.com/search/repositories?q=android+language:java&sort=stars&per_page=5&page=0</li>
 * <li>has keyword-->https://api.github.com/search/repositories?q=android+created:>2015-01-09+language:java&sort=stars&order=desc&per_page=3&page=1</li>
 */
public class RemoteGitHubData implements IGitHubDataSource {

  @Override
  public Observable<SearchResult> queryByKeyword(String keyword, String createDate, String language,
      String sort, String order, int perPage, int page) {
    if (keyword == null) keyword = "";
    if (!TextUtils.isEmpty(createDate)) {
      // 防止无keyword时，结果搜不到东西
      keyword = keyword.concat(URLDecoder.decode("+") + "created:>" + createDate);
    }
    if (!TextUtils.isEmpty(language)) {
      // 防止无keyword时，结果搜不到东西
      keyword = keyword.concat(URLDecoder.decode("+") + "language:" + language);
    }
    return Api.getSearchApi().query(keyword, sort, order, perPage, page);
  }

  @Override
  public Observable<SearchResult> queryByKeyword(String keyword, String createDate, String language,
      int perPage, int page) {
    return queryByKeyword(keyword, createDate, language, GitHub.SORT_STARS, GitHub.ORDER_DESC,
        perPage, page);
  }

  @Override
  public Observable<SearchResult> queryByKeyword(String keyword, String createDate, String language,
      int page) {
    return queryByKeyword(keyword, createDate, language, LIMIT_20, page);
  }

  @Override
  public Observable<SearchResult> queryByKeyword(String keyword, String language, int perPage,
      int page) {
    return queryByKeyword(keyword, null, language, perPage, page);
  }

  @Override
  public Observable<SearchResult> queryByKeyword(String keyword, String language, int page) {
    return queryByKeyword(keyword, language, LIMIT_20, page);
  }
}
