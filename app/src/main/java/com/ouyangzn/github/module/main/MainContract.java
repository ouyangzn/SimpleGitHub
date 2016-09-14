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

package com.ouyangzn.github.module.main;

import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.base.BaseView;
import com.ouyangzn.github.bean.apibean.SearchResult;
import com.ouyangzn.github.bean.localbean.SearchFactor;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public interface MainContract {

  interface IMainView extends BaseView<MainPresenter> {

    void showErrorOnQueryData(String tips);

    void showQueryDataResult(SearchResult result);
  }

  abstract class IMainPresenter extends BasePresenter<IMainView> {
    /**
     * 搜索github上的数据
     *
     * @param factor 搜索因子（即各种搜索条件）
     * @param page 搜索第几页
     */
    abstract void queryData(SearchFactor factor, int page);

    /**
     * 保存搜索因子
     *
     * @param factor 搜索因子
     */
    abstract void saveSearchFactor(SearchFactor factor);
  }
}
