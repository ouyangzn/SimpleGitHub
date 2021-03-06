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

package com.ouyangzn.github.module.collect;

import com.ouyangzn.github.base.BasePresenter;
import com.ouyangzn.github.base.BaseView;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：
 */
public class CollectionsContract {

  interface ICollectionsView extends BaseView<ICollectionsPresenter> {

    void showCollect(List<CollectedRepo> repoList);

    void showErrorOnQueryFailure(String error);

    void showCollectQueryByKey(List<CollectedRepo> repoList);

    void showQueryByKeyFailure(String error);

    void showCollectionCanceled(CollectedRepo repo);

    void showCollectionCancelFailure();

  }

  abstract static class ICollectionsPresenter extends BasePresenter<ICollectionsView> {

    /**
     * 查询收藏的项目
     *
     * @param page 当前页
     * @param countEachPage 每页的数量
     */
    public abstract void queryCollect(int page, int countEachPage);

    public abstract void queryByKey(String key, int page, int limit);

    public abstract void cancelCollectRepo(CollectedRepo repo);
  }
}
