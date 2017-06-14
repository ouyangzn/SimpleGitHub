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
import com.ouyangzn.github.bean.localbean.CollectedRepoOwner;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public class User implements Parcelable {
  public static final Creator<User> CREATOR = new Creator<User>() {
    @Override public User createFromParcel(Parcel source) {
      return new User(source);
    }

    @Override public User[] newArray(int size) {
      return new User[size];
    }
  };
  @Expose private Integer id;
  @Expose private String type;
  @Expose private String url;
  @Expose private String email;
  @Expose @SerializedName("avatar_url") private String avatarUrl;
  @Expose @SerializedName("events_url") private String eventsUrl;
  @Expose @SerializedName("followers_url") private String followersUrl;
  @Expose @SerializedName("following_url") private String followingUrl;
  @Expose @SerializedName("gists_url") private String gistsUrl;
  @Expose @SerializedName("gravatar_id") private String gravatarId;
  @Expose @SerializedName("html_url") private String htmlUrl;
  @Expose @SerializedName("login") private String authorName;
  @Expose @SerializedName("organizations_url") private String organizationsUrl;
  @Expose @SerializedName("received_events_url") private String receivedEventsUrl;
  @Expose @SerializedName("repos_url") private String reposUrl;
  @Expose @SerializedName("site_admin") private Boolean siteAdmin;
  @Expose @SerializedName("starred_url") private String starredUrl;
  @Expose @SerializedName("subscriptions_url") private String subscriptionsUrl;

  public User() {
  }

  protected User(Parcel in) {
    this.avatarUrl = in.readString();
    this.eventsUrl = in.readString();
    this.followersUrl = in.readString();
    this.followingUrl = in.readString();
    this.gistsUrl = in.readString();
    this.gravatarId = in.readString();
    this.htmlUrl = in.readString();
    this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    this.authorName = in.readString();
    this.organizationsUrl = in.readString();
    this.receivedEventsUrl = in.readString();
    this.reposUrl = in.readString();
    this.siteAdmin = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.starredUrl = in.readString();
    this.subscriptionsUrl = in.readString();
    this.type = in.readString();
    this.url = in.readString();
    this.email = in.readString();
  }

  public static User convert(CollectedRepoOwner owner) {
    User user = new User();
    user.id = owner.getId().intValue();
    user.authorName = owner.getName();
    user.avatarUrl = owner.getAvatarUrl();
    return user;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAvatarUrl() {
    return this.avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getEventsUrl() {
    return this.eventsUrl;
  }

  public void setEventsUrl(String eventsUrl) {
    this.eventsUrl = eventsUrl;
  }

  public String getFollowersUrl() {
    return this.followersUrl;
  }

  public void setFollowersUrl(String followersUrl) {
    this.followersUrl = followersUrl;
  }

  public String getFollowingUrl() {
    return this.followingUrl;
  }

  public void setFollowingUrl(String followingUrl) {
    this.followingUrl = followingUrl;
  }

  public String getGistsUrl() {
    return this.gistsUrl;
  }

  public void setGistsUrl(String gistsUrl) {
    this.gistsUrl = gistsUrl;
  }

  public String getGravatarId() {
    return this.gravatarId;
  }

  public void setGravatarId(String gravatarId) {
    this.gravatarId = gravatarId;
  }

  public String getHtmlUrl() {
    return this.htmlUrl;
  }

  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAuthorName() {
    return authorName;
  }

  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  public String getOrganizationsUrl() {
    return this.organizationsUrl;
  }

  public void setOrganizationsUrl(String organizationsUrl) {
    this.organizationsUrl = organizationsUrl;
  }

  public String getReceivedEventsUrl() {
    return this.receivedEventsUrl;
  }

  public void setReceivedEventsUrl(String receivedEventsUrl) {
    this.receivedEventsUrl = receivedEventsUrl;
  }

  public String getReposUrl() {
    return this.reposUrl;
  }

  public void setReposUrl(String reposUrl) {
    this.reposUrl = reposUrl;
  }

  public Boolean getSiteAdmin() {
    return this.siteAdmin;
  }

  public void setSiteAdmin(Boolean siteAdmin) {
    this.siteAdmin = siteAdmin;
  }

  public String getStarredUrl() {
    return this.starredUrl;
  }

  public void setStarredUrl(String starredUrl) {
    this.starredUrl = starredUrl;
  }

  public String getSubscriptionsUrl() {
    return this.subscriptionsUrl;
  }

  public void setSubscriptionsUrl(String subscriptionsUrl) {
    this.subscriptionsUrl = subscriptionsUrl;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override public String toString() {
    return "User{"
        + "id="
        + id
        + ", type='"
        + type
        + '\''
        + ", url='"
        + url
        + '\''
        + ", email='"
        + email
        + '\''
        + ", avatarUrl='"
        + avatarUrl
        + '\''
        + ", eventsUrl='"
        + eventsUrl
        + '\''
        + ", followersUrl='"
        + followersUrl
        + '\''
        + ", followingUrl='"
        + followingUrl
        + '\''
        + ", gistsUrl='"
        + gistsUrl
        + '\''
        + ", gravatarId='"
        + gravatarId
        + '\''
        + ", htmlUrl='"
        + htmlUrl
        + '\''
        + ", authorName='"
        + authorName
        + '\''
        + ", organizationsUrl='"
        + organizationsUrl
        + '\''
        + ", receivedEventsUrl='"
        + receivedEventsUrl
        + '\''
        + ", reposUrl='"
        + reposUrl
        + '\''
        + ", siteAdmin="
        + siteAdmin
        + ", starredUrl='"
        + starredUrl
        + '\''
        + ", subscriptionsUrl='"
        + subscriptionsUrl
        + '\''
        + '}';
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.avatarUrl);
    dest.writeString(this.eventsUrl);
    dest.writeString(this.followersUrl);
    dest.writeString(this.followingUrl);
    dest.writeString(this.gistsUrl);
    dest.writeString(this.gravatarId);
    dest.writeString(this.htmlUrl);
    dest.writeValue(this.id);
    dest.writeString(this.authorName);
    dest.writeString(this.organizationsUrl);
    dest.writeString(this.receivedEventsUrl);
    dest.writeString(this.reposUrl);
    dest.writeValue(this.siteAdmin);
    dest.writeString(this.starredUrl);
    dest.writeString(this.subscriptionsUrl);
    dest.writeString(this.type);
    dest.writeString(this.url);
    dest.writeString(this.email);
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    return id != null ? id.equals(user.id) : user.id == null;
  }

  @Override public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
