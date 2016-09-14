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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：
 */
public class User {
  @Expose @SerializedName("avatar_url") String avatarUrl;
  @Expose @SerializedName("events_url") String eventsUrl;
  @Expose @SerializedName("followers_url") String followersUrl;
  @Expose @SerializedName("following_url") String followingUrl;
  @Expose @SerializedName("gists_url") String gistsUrl;
  @Expose @SerializedName("gravatar_id") String gravatarId;
  @Expose @SerializedName("html_url") String htmlUrl;
  @Expose Integer id;
  @Expose String login;
  @Expose @SerializedName("organizations_url") String organizationsUrl;
  @Expose @SerializedName("received_events_url") String receivedEventsUrl;
  @Expose @SerializedName("repos_url") String reposUrl;
  @Expose @SerializedName("site_admin") Boolean siteAdmin;
  @Expose @SerializedName("starred_url") String starredUrl;
  @Expose @SerializedName("subscriptions_url") String subscriptionsUrl;
  @Expose String type;
  @Expose String url;

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

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
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
}
