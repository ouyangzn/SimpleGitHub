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

package com.ouyangzn.topgithub.module.main;

import com.ouyangzn.topgithub.base.BasePresenter;
import com.ouyangzn.topgithub.base.BaseView;
import com.ouyangzn.topgithub.bean.SearchResult;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public interface MainContract {

  interface IMainView extends BaseView<MainPresenter> {

    void showErrorTips(String tips);

    void showResult(SearchResult result);
  }

  abstract class IMainPresenter extends BasePresenter<IMainView> {
    /**
     * 搜索github上的数据
     *
     * @param keyword 关键字，可为null
     * @param language 搜索的语言
     * @param page 当前页
     */
    abstract void queryData(String keyword, String language, int page);
  }
}
