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

package com.ouyangzn.github.bean.localbean;

import com.google.gson.annotations.Expose;
import com.ouyangzn.github.bean.apibean.User;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：收藏的repo的拥有者
 */
public class CollectedRepoOwner {

  @Expose private Long id;
  /** 头像 */
  @Expose private String avatarUrl;
  /** 用户名 */
  @Expose private String name;

  public static CollectedRepoOwner convert(User owner) {
    CollectedRepoOwner cOwner = new CollectedRepoOwner();
    cOwner.id = owner.getId().longValue();
    cOwner.name = owner.getAuthorName();
    cOwner.avatarUrl = owner.getAvatarUrl();
    return cOwner;
  }

  @Override public String toString() {
    return "CollectedRepoOwner{" +
        "id=" + id +
        ", avatarUrl='" + avatarUrl + '\'' + ", name='" + name + '\'' +
        '}';
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAvatarUrl() {
    return this.avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
