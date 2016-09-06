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

package com.ouyangzn.topgithub.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public class Repository {

  @Expose @SerializedName("created_at") String createdAt;
  @Expose String description;
  @Expose @SerializedName("full_name") String fullName;
  @Expose String homepage;
  @Expose @SerializedName("html_url") String htmlUrl;
  @Expose Integer id;
  @Expose String language;
  @Expose String name;
  @Expose User owner;
  @Expose Double score;
  @Expose @SerializedName("stargazers_count") Integer stargazersCount;
  @Expose @SerializedName("updated_at") String updatedAt;
  @Expose Integer watchers;

  public String getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFullName() {
    return this.fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getHomepage() {
    return this.homepage;
  }

  public void setHomepage(String homepage) {
    this.homepage = homepage;
  }

  public String getHtmlUrl() {
    return this.htmlUrl;
  }

  public void setHtmlUrl(String paramString) {
    this.htmlUrl = paramString;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String paramString) {
    this.name = paramString;
  }

  public User getOwner() {
    return this.owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public Double getScore() {
    return this.score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public Integer getStargazersCount() {
    return this.stargazersCount;
  }

  public void setStargazersCount(Integer stargazersCount) {
    this.stargazersCount = stargazersCount;
  }

  public String getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Integer getWatchers() {
    return this.watchers;
  }

  public void setWatchers(Integer watchers) {
    this.watchers = watchers;
  }

  @Override public String toString() {
    return "Repository{" +
        "createdAt='" + createdAt + '\'' +
        ", description='" + description + '\'' +
        ", fullName='" + fullName + '\'' +
        ", homepage='" + homepage + '\'' +
        ", htmlUrl='" + htmlUrl + '\'' +
        ", id=" + id +
        ", language='" + language + '\'' +
        ", name='" + name + '\'' +
        ", owner=" + owner +
        ", score=" + score +
        ", stargazersCount=" + stargazersCount +
        ", updatedAt='" + updatedAt + '\'' +
        ", watchers=" + watchers +
        '}';
  }
}
