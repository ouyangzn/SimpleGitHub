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

package com.ouyangzn.github.bean.apibean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ouyangzn.github.bean.localbean.CollectedRepo;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public class Repository implements Parcelable {

  public static final Creator<Repository> CREATOR = new Creator<Repository>() {
    @Override public Repository createFromParcel(Parcel source) {
      return new Repository(source);
    }

    @Override public Repository[] newArray(int size) {
      return new Repository[size];
    }
  };
  @Expose @SerializedName("created_at") private String createdAt;
  @Expose private String description;
  @Expose @SerializedName("full_name") private String fullName;
  @Expose private String homepage;
  @Expose @SerializedName("html_url") private String htmlUrl;
  @Expose private Integer id;
  @Expose private String language;
  @Expose private String name;
  @Expose private User owner;
  @Expose private Double score;
  @Expose @SerializedName("stargazers_count") private Integer stargazersCount;
  @Expose @SerializedName("updated_at") private String updatedAt;
  @Expose private Integer watchers;
  /** 以下变量为本地使用，不是api接口返回，不需要解析 */
  private String label;
  private long collectTime;

  public Repository() {
  }

  protected Repository(Parcel in) {
    this.createdAt = in.readString();
    this.description = in.readString();
    this.fullName = in.readString();
    this.homepage = in.readString();
    this.htmlUrl = in.readString();
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.language = in.readString();
    this.name = in.readString();
    this.owner = in.readParcelable(User.class.getClassLoader());
    this.score = (Double) in.readValue(Double.class.getClassLoader());
    this.stargazersCount = (Integer) in.readValue(Integer.class.getClassLoader());
    this.updatedAt = in.readString();
    this.watchers = (Integer) in.readValue(Integer.class.getClassLoader());
    this.label = in.readString();
    this.collectTime = in.readLong();
  }

  public static Repository convert(CollectedRepo collectedRepo) {
    Repository repo = new Repository();
    repo.id = collectedRepo.getId().intValue();
    repo.htmlUrl = collectedRepo.getHtmlUrl();
    repo.fullName = collectedRepo.getFullName();
    repo.language = collectedRepo.getLanguage();
    repo.stargazersCount = collectedRepo.getStargazersCount();
    repo.description = collectedRepo.getDescription();
    repo.label = collectedRepo.getLabel();
    repo.owner = User.convert(collectedRepo.getOwner());
    repo.collectTime = collectedRepo.getCollectTime();
    return repo;
  }

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

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public long getCollectTime() {
    return collectTime;
  }

  public void setCollectTime(long collectTime) {
    this.collectTime = collectTime;
  }

  @Override public String toString() {
    return "Repository{"
        + "createdAt='"
        + createdAt
        + '\''
        + ", description='"
        + description
        + '\''
        + ", fullName='"
        + fullName
        + '\''
        + ", homepage='"
        + homepage
        + '\''
        + ", htmlUrl='"
        + htmlUrl
        + '\''
        + ", id="
        + id
        + ", language='"
        + language
        + '\''
        + ", name='"
        + name
        + '\''
        + ", owner="
        + owner
        + ", score="
        + score
        + ", stargazersCount="
        + stargazersCount
        + ", updatedAt='"
        + updatedAt
        + '\''
        + ", watchers="
        + watchers
        + ", label='"
        + label
        + '\''
        + ", collectTime="
        + collectTime
        + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.createdAt);
    dest.writeString(this.description);
    dest.writeString(this.fullName);
    dest.writeString(this.homepage);
    dest.writeString(this.htmlUrl);
    dest.writeValue(this.id);
    dest.writeString(this.language);
    dest.writeString(this.name);
    dest.writeParcelable(this.owner, flags);
    dest.writeValue(this.score);
    dest.writeValue(this.stargazersCount);
    dest.writeString(this.updatedAt);
    dest.writeValue(this.watchers);
    dest.writeString(this.label);
    dest.writeLong(this.collectTime);
  }
}
