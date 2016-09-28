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

import com.ouyangzn.github.bean.localbean.LocalRepo;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by ouyangzn on 2016/9/22.<br/>
 * Description：数据库版本更新时会被调用
 */
public class GlobalRealmMigration implements RealmMigration {

  public static final long DB_VERSION = 2;

  @Override public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
    RealmSchema schema = realm.getSchema();
    if (oldVersion == 1) {
      updateVersionTo2(schema);
      oldVersion++;
    }
    if (oldVersion == 2) {

    }
  }

  private void updateVersionTo3(RealmSchema schema) {

  }

  private void updateVersionTo2(RealmSchema schema) {
    schema.get(LocalRepo.class.getSimpleName()).addField("collectTime", long.class);
  }

  @Override public int hashCode() {
    return 1;
  }

  @Override public boolean equals(Object obj) {
    return obj instanceof GlobalRealmMigration && obj.hashCode() == 1;
  }
}
