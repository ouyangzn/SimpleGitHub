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

package com.ouyangzn.github.bean.localbean;

import com.ouyangzn.github.bean.apibean.Repository;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：
 */
public class LocalRepo extends RealmObject {

  @PrimaryKey public Integer id;
  public long collectTime;
  public String htmlUrl;
  public String fullName;
  public String language;
  public int stargazersCount;
  public LocalUser owner;
  public String description;

  public void convert2Local(Repository repo) {
    this.id = repo.getId();
    this.htmlUrl = repo.getHtmlUrl();
    this.fullName = repo.getFullName();
    this.language = repo.getLanguage();
    this.stargazersCount = repo.getStargazersCount();
    this.description = repo.getDescription();
    this.owner = new LocalUser();
    this.owner.convert2Local(repo.getOwner());
  }

  @Override public String toString() {
    return "LocalRepo{" +
        "id=" + id +
        ", collectTime=" + collectTime +
        ", htmlUrl='" + htmlUrl + '\'' +
        ", fullName='" + fullName + '\'' +
        ", language='" + language + '\'' +
        ", stargazersCount=" + stargazersCount +
        ", owner=" + owner +
        ", description='" + description + '\'' +
        '}';
  }
}
