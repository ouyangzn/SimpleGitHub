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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.ouyangzn.github.App;
import com.ouyangzn.github.dao.DaoMaster;
import com.ouyangzn.github.dao.DaoSession;
import org.greenrobot.greendao.database.Database;

/**
 * Created by ouyangzn on 2017/5/23.<br/>
 * Description：
 */
public class DaoHelper {

  private static DaoSession sDaoSession;

  private DaoHelper() {
  }

  public static void initDao(Context context) {
    DaoMaster.OpenHelper helper = new DBOpenHelper(context, "open-resource");
    Database db = helper.getWritableDb();
    sDaoSession = new DaoMaster(db).newSession();
  }

  public static DaoSession getDaoSession() {
    if (sDaoSession == null) {
      initDao(App.getApp());
    }
    return sDaoSession;
  }

  private static class DBOpenHelper extends DaoMaster.OpenHelper {

    private DBOpenHelper(Context context, String name) {
      super(context, name);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // 数据库版本升级
      switch (oldVersion) {
        case 1: {
          onUpgrade1(db);
          break;
        }
      }
    }

    private void onUpgrade1(SQLiteDatabase db) {
      // 收藏表增加列"标签"
      db.execSQL("ALTER TABLE COLLECTED_REPO ADD COLUMN 'LABEL' TEXT;");
    }
  }
}
