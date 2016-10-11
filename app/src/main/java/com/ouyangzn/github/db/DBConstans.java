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

package com.ouyangzn.github.db;

/**
 * Created by ouyangzn on 2016/9/30.<br/>
 * Description：
 */
public interface DBConstans {

  /** CollectedRepo表对应的列名 */
  interface CollectedRepoFields {
    /** 项目repository的id */
    String FIELD_ID = "id";
    /** 收藏时间 */
    String FIELD_COLLECT_TIME = "collectTime";
    /** 项目url */
    String FIELD_HTML_URL = "htmlUrl";
    /** 项目全名称 */
    String FIELD_FULL_NAME = "fullName";
    /** 项目所用语言 */
    String FIELD_LANGUAGE = "language";
    /** 项目的star数 */
    String FIELD_STARGAZERS_COUNT = "stargazersCount";
    /** 项目所有者 */
    String FIELD_OWNER = "owner";
    /** 项目描述 */
    String FIELD_DESCRIPTION = "description";
  }

  interface CollectedUser {
    /** 用户id */
    String FIELD_ID = "id";
    /** 用户头像地址 */
    String FIELD_AVATAR_URL = "avatarUrl";
    /** 用户名称 */
    String FIELD_NAME = "login";
  }
}
