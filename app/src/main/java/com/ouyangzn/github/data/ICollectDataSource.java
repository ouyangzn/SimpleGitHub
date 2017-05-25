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

import com.ouyangzn.github.bean.localbean.CollectedRepo;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/30.<br/>
 * Description：
 */
public interface ICollectDataSource {

  /**
   * 查询所有收藏
   *
   * @param page 页码，从0开始
   * @param limit 每页数量
   * @return List<CollectedRepo>
   */
  List<CollectedRepo> queryCollectRepo(int page, int limit);

  /**
   * 根据关键字搜索
   *
   * @param keyword 关键字
   * @return List<CollectedRepo>
   */
  List<CollectedRepo> queryByKeyword(String keyword);

  boolean collectRepo(CollectedRepo repo);

  boolean cancelRepo(CollectedRepo repo);

  boolean cancelRepo(Long id);

}
