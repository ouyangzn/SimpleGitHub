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

import android.content.Context;
import android.widget.ImageView;
import com.ouyangzn.github.R;
import com.ouyangzn.github.bean.localbean.CollectedRepo;
import com.ouyangzn.github.bean.localbean.CollectedRepoOwner;
import com.ouyangzn.github.utils.ImageLoader;
import com.ouyangzn.github.view.CornerMarkText;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public class CollectAdapter extends BaseRecyclerViewAdapter<CollectedRepo> {

  private Context mContext;

  public CollectAdapter(Context context, List<CollectedRepo> data) {
    super(R.layout.item_repo, data);
    this.mContext = context;
  }

  @Override protected void convert(BaseViewHolder holder, CollectedRepo repo) {
    holder.setText(R.id.tv_project_name, repo.getFullName());
    holder.setText(R.id.tv_project_desc, repo.getDescription());
    CollectedRepoOwner owner = repo.getOwner();
    holder.setText(R.id.tv_author, owner.getLogin());
    holder.setText(R.id.tv_stars, mContext.getString(R.string.stars, repo.getStargazersCount()));
    ImageView photo = (ImageView) holder.getConvertView().findViewById(R.id.img_author_photo);
    ImageLoader.load(photo, owner.getAvatarUrl());
    holder.setVisible(R.id.tv_language, true);
    CornerMarkText text = (CornerMarkText) holder.getConvertView().findViewById(R.id.tv_language);
    text.setText(repo.getLanguage());
  }
}
