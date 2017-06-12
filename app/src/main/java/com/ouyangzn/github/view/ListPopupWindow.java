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

package com.ouyangzn.github.view;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.ouyangzn.github.R;
import com.ouyangzn.github.utils.DialogUtils;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener;
import com.ouyangzn.recyclerview.BaseViewHolder;
import java.util.List;

import static com.ouyangzn.github.utils.UiUtils.dimWhenPop;

/**
 * Created by ouyangzn on 2017/6/12.<br/>
 * Description：选项PopupWindow
 */
public class ListPopupWindow implements OnRecyclerViewItemClickListener {

  private Context mContext;
  private PopupWindow mPopupWindow;
  private List<String> mItems;
  private RecyclerView mRecyclerView;
  private ItemAdapter mAdapter;
  private OnItemClickListener mItemClickListener;

  public ListPopupWindow(Context context, List<String> items, OnItemClickListener listener) {
    mItems = items;
    mAdapter = new ItemAdapter(items);
    mItemClickListener = listener;
    mRecyclerView =
        (RecyclerView) LayoutInflater.from(context).inflate(R.layout.view_list_pop, null);
    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.addItemDecoration(
        new DividerItemDecoration(context, layoutManager.getOrientation()));
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setOnRecyclerViewItemClickListener(this);
    mPopupWindow = DialogUtils.getPopupWindow(mRecyclerView);
  }

  public void showAsDropDown(View anchorView) {
    mPopupWindow.showAsDropDown(anchorView);
    dimWhenPop(mPopupWindow);
  }

  public void showAsDropDown(View anchorView, int xoff, int yoff) {
    mPopupWindow.showAsDropDown(anchorView, xoff, yoff);
    dimWhenPop(mPopupWindow);
  }

  public void showAtLocation(View parent, int gravity, int x, int y) {
    mPopupWindow.showAtLocation(parent, gravity, x, y);
    dimWhenPop(mPopupWindow);
  }

  public void dismiss() {
    mPopupWindow.dismiss();
  }

  public PopupWindow getPopupWindow() {
    return mPopupWindow;
  }

  @Override public void onItemClick(View view, int position) {
    dismiss();
    mItemClickListener.onItemClick(position);
  }

  public interface OnItemClickListener {
    void onItemClick(int position);
  }

  private static class ItemAdapter extends BaseRecyclerViewAdapter<String> {

    public ItemAdapter(List<String> data) {
      super(R.layout.item_list_pop, data);
    }

    @Override protected void convert(BaseViewHolder holder, String item) {
      holder.setText(R.id.tv_list_pop_item, item);
    }
  }
}
