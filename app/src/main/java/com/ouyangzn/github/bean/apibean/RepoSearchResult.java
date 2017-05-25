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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/6.<br/>
 * Description：搜索Repository返回的结果
 */
public class RepoSearchResult {

  @Expose @SerializedName("total_count") Integer totalCount;
  @Expose @SerializedName("incomplete_results") Boolean incompleteResults;
  @Expose @SerializedName("items") List<Repository> repositories = new ArrayList();

  public Boolean getIncompleteResults() {
    return this.incompleteResults;
  }

  public void setIncompleteResults(Boolean incompleteResults) {
    this.incompleteResults = incompleteResults;
  }

  public List<Repository> getRepositories() {
    return this.repositories;
  }

  public void setRepositories(List<Repository> repositories) {
    this.repositories = repositories;
  }

  public Integer getTotalCount() {
    return this.totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  @Override public String toString() {
    return "RepoSearchResult{" +
        "totalCount=" + totalCount +
        ", incompleteResults=" + incompleteResults +
        ", repositories=" + repositories +
        '}';
  }
}
