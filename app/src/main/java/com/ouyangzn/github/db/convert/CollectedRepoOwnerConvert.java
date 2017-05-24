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

package com.ouyangzn.github.db.convert;

import com.ouyangzn.github.App;
import com.ouyangzn.github.bean.localbean.CollectedRepoOwner;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by ouyangzn on 2017/5/24.<br/>
 * Description：
 */
public class CollectedRepoOwnerConvert implements PropertyConverter<CollectedRepoOwner, String> {
  @Override public CollectedRepoOwner convertToEntityProperty(String databaseValue) {
    return App.getApp().getGson().fromJson(databaseValue, CollectedRepoOwner.class);
  }

  @Override public String convertToDatabaseValue(CollectedRepoOwner entityProperty) {
    return App.getApp().getGson().toJson(entityProperty);
  }
}
