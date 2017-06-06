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

package com.ouyangzn.github.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ouyangzn.github.R;
import com.ouyangzn.recyclerview.BaseRecyclerViewAdapter;

/**
 * Created by ouyangzn on 2016/10/8.<br/>
 * Description：UI操作相关工具类，包括界面（activity等）跳转
 */
public class UiUtils {

  /**
   * 给textView添加下划线
   *
   * @param textView TextView
   */
  public static void addUnderLine(TextView textView) {
    textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
  }

  /**
   * 给RecyclerView设置加载更多和加载更多失败时的属性
   *
   * @param mAdapter BaseRecyclerViewAdapter
   * @param recyclerView RecyclerView
   */
  public static void setRecyclerViewLoadMore(BaseRecyclerViewAdapter mAdapter,
      RecyclerView recyclerView) {
    LayoutInflater inflater = LayoutInflater.from(recyclerView.getContext());
    mAdapter.setLoadMoreView(inflater.inflate(R.layout.item_load_more, recyclerView, false));
    View loadMoreFail = inflater.inflate(R.layout.item_load_more_failure, recyclerView, false);
    loadMoreFail.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mAdapter.reloadMore();
      }
    });
    mAdapter.setLoadMoreFailureView(loadMoreFail);
  }

  /**
   * 给toolbar添加一张图片
   *
   * @param toolbar toolbar
   * @param resId 图片资源id
   * @param gravity 添加的位置，对应{@link Gravity#LEFT}、{@link Gravity#RIGHT}
   * @param margin 图片的四周边距{@link Toolbar.LayoutParams#setMargins(int, int, int, int)}
   * @return 被添加的ImageView
   */
  public static ImageView addImage2Toolbar(Toolbar toolbar, int resId, int gravity, int[] margin) {
    Context context = toolbar.getContext();
    ImageView img = new ImageView(context);
    img.setImageResource(resId);
    img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
        Toolbar.LayoutParams.WRAP_CONTENT);
    params.gravity = gravity | Gravity.CENTER;
    try {
      params.setMargins(margin[0], margin[1], margin[2], margin[3]);
    } catch (Exception e) {
      int margin_15 = ScreenUtils.dp2px(context, 15);
      params.setMargins(margin_15, 0, margin_15, 0);
    }
    img.setLayoutParams(params);
    toolbar.addView(img);
    return img;
  }

  /**
   * 添加返回键，点击后会调用activity的onBackPressed
   *
   * @param toolbar Toolbar
   * @param activity Activity
   */
  public static void addWhiteBackBtn(Toolbar toolbar, Activity activity) {
    toolbar.setNavigationIcon(R.drawable.ic_back_white);
    toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());
  }

  public static TextView setCenterTitle(Toolbar toolbar, @StringRes int resId) {
    return setCenterTitle(toolbar, toolbar.getContext().getString(resId));
  }

  public static TextView setCenterTitle(Toolbar toolbar, String title) {
    TextView titleView = new TextView(toolbar.getContext());
    titleView.setGravity(Gravity.CENTER);
    titleView.setTextAppearance(toolbar.getContext(), R.style.Toolbar_titleTextAppearance);
    titleView.setSingleLine();
    titleView.setEllipsize(TextUtils.TruncateAt.END);
    titleView.setText(title);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT);
    titleView.setLayoutParams(params);
    Toolbar.LayoutParams lp = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    lp.gravity = Gravity.CENTER;
    toolbar.addView(titleView, lp);
    return titleView;
  }

  public static void stopRefresh(SwipeRefreshLayout refreshLayout) {
    if (refreshLayout != null && refreshLayout.isRefreshing()) {
      refreshLayout.setRefreshing(false);
    }
  }

}
