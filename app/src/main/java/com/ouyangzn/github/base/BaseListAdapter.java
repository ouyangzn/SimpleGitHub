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

package com.ouyangzn.github.base;

import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouyangzn on 2016/11/24.<br/>
 * Description：ListView或者GridView的adapter的基类
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
  protected List<T> mData;

  public BaseListAdapter(List<T> list) {
    this.mData = list == null ? new ArrayList<T>(0) : list;
  }

  public List<T> getData() {
    return mData;
  }

  public void add(T data) {
    mData.add(data);
    notifyDataSetChanged();
  }

  public void add(List<T> data) {
    if (data != null && data.size() != 0) {
      mData.addAll(data);
      notifyDataSetChanged();
    }
  }

  public void remove(int index) {
    mData.remove(index);
    notifyDataSetChanged();
  }

  public void remove(T data) {
    mData.remove(data);
    notifyDataSetChanged();
  }

  public void remove(List<T> dataList) {
    mData.removeAll(dataList);
    notifyDataSetChanged();
  }

  public void resetData(List<T> list) {
    this.mData = list == null ? new ArrayList<T>(0) : list;
  }

  @Override public int getCount() {
    return mData.size();
  }

  @Override public Object getItem(int position) {
    return mData.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }
}
