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

import com.ouyangzn.github.bean.apibean.User;

/**
 * Created by ouyangzn on 2016/9/27.<br/>
 * Description：
 */
public class CollectedRepoOwner {

  private Long id;
  private String avatarUrl;
  private String login;

  public void convert(User owner) {
    this.id = owner.getId().longValue();
    this.login = owner.getAuthorName();
    this.avatarUrl = owner.getAvatarUrl();
  }

  @Override public String toString() {
    return "CollectedRepoOwner{" +
        "id=" + id +
        ", avatarUrl='" + avatarUrl + '\'' +
        ", login='" + login + '\'' +
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

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }
}
