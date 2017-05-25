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

package com.ouyangzn.github.data;

import com.ouyangzn.github.bean.apibean.Repository;
import java.util.List;
import rx.Observable;

/**
 * Created by ouyangzn on 2017/5/25.<br/>
 * Description：
 */
public interface IStarsDataSource {

  /**
   * 查询某个人的stars
   *
   * @param username 用户名
   * @return Observable<List<Repository>>
   */
  Observable<List<Repository>> querySomeoneStars(String username, int page, int limit);
}
