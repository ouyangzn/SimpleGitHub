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

package com.ouyangzn.topgithub.base;

/**
 * Description：常量类
 */
public class CommonConstants {

  /**
   * GitHub搜索需要用到的常量
   */
  public static class GitHub {
    // -----------搜索的类别---------
    public static final String SORT_UPDATED = "updated";
    public static final String SORT_STARS = "stars";
    public static final String SORT_FORKS = "forks";
    // -----------搜索结果排序方式---------
    public static final String ORDER_DESC = "desc";
    public static final String ORDER_ASC = "asc";
    // -----------搜索的语言-----------
    public static final String LANG_ALL = "";
    public static final String LANG_JAVA = "Java";
    public static final String LANG_OC = "Object-C";
    public static final String LANG_C = "C";
    public static final String LANG_CPP = "C++";
    public static final String LANG_PHP = "PHP";
    public static final String LANG_JS = "JavaScript";
    public static final String LANG_PYTHON = "Python";
    public static final String LANG_RUBY = "Ruby";
    public static final String LANG_C_SHARP = "C#";
    public static final String LANG_SHELL = "Shell";
  }

  public static class NormalCons {
    /** 分页--每页20条数据 */
    public static final int LIMIT_20 = 20;
  }
}
