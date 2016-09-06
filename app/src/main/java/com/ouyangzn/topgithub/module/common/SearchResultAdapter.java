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

package com.ouyangzn.topgithub.module.common;

import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import com.ouyangzn.topgithub.R;
import com.ouyangzn.topgithub.bean.Repository;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public class SearchResultAdapter extends BaseRecyclerViewAdapter<Repository> {

  public SearchResultAdapter(int layoutResId, List<Repository> data) {
    super(layoutResId, data);
  }

  @Override protected void convert(BaseViewHolder holder, Repository item) {
    holder.setText(R.id.tv_project_name, item.getFullName());
    holder.setText(R.id.tv_project_desc, item.getDescription());
    holder.setText(R.id.tv_author, item.getOwner().getLogin());
  }
}
