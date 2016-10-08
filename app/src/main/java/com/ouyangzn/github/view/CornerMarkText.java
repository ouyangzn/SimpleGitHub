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

package com.ouyangzn.github.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.ouyangzn.github.R;
import com.ouyangzn.github.utils.ScreenUtil;

/**
 * Created by ouyangzn on 2016/9/14.<br/>
 * Description：角标text
 * e.g.:
 * **********************
 * *            *  n    *
 * *              *  e  *
 * *                *  w*
 * *                  * *
 * **********************
 */
public class CornerMarkText extends View {

  private final static String TAG = CornerMarkText.class.getSimpleName();

  private Paint mTextPaint;
  private Paint mTextBgPaint;
  private String mText;
  private float mTextSize;
  private int mTextColor;
  private int mTextBgColor;
  private int mMinSize;
  private int mMaxSize;

  private ViewGroup.LayoutParams mLayoutParams;

  public CornerMarkText(Context context) {
    this(context, null);
  }

  public CornerMarkText(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CornerMarkText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CornerMarkText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mMinSize = ScreenUtil.dp2px(context, 20);

    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CornerMarkText);
    mTextBgColor = ta.getColor(R.styleable.CornerMarkText_bg_color, 0x8000ff00);
    mText = ta.getString(R.styleable.CornerMarkText_text);
    mTextSize = ta.getDimensionPixelSize(R.styleable.CornerMarkText_text_size,
        ScreenUtil.sp2px(context, 10));
    mTextColor = ta.getColor(R.styleable.CornerMarkText_text_color,
        getResources().getColor(R.color.colorAccent));
    mMinSize = ta.getDimensionPixelOffset(R.styleable.CornerMarkText_min_size,
        ScreenUtil.dp2px(context, 20));
    mMaxSize = ta.getDimensionPixelOffset(R.styleable.CornerMarkText_max_size,
        ScreenUtil.dp2px(context, 40));
    ta.recycle();
    initTextPaint();
    initTextBgPaint();
  }

  public void setText(String text) {
    mText = text;
    // 固定大小不需要重新测量,否则要重新根据文字测量大小
    if (mLayoutParams == null || ((mLayoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT
        || mLayoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) || (mLayoutParams.width
        == ViewGroup.LayoutParams.WRAP_CONTENT
        || mLayoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT))) {
      requestLayout();
    } else {
      invalidate();
    }
  }

  public void setTextSize(float textSize) {
    mTextSize = textSize;
    invalidate();
  }

  public void setTextColor(int textColor) {
    mTextColor = textColor;
    invalidate();
  }

  public void setTextBgColor(int textBgColor) {
    mTextBgColor = textBgColor;
    invalidate();
  }

  public void setMinSize(int minSize) {
    this.mMinSize = minSize;
  }

  private void initTextBgPaint() {
    mTextBgPaint = new Paint();
    mTextBgPaint.setColor(mTextBgColor);
    mTextBgPaint.setStyle(Paint.Style.FILL);
    mTextBgPaint.setStrokeWidth(mTextSize);
    // 抗锯齿
    mTextBgPaint.setAntiAlias(true);
  }

  private void initTextPaint() {
    mTextPaint = new Paint();
    mTextPaint.setColor(mTextColor);
    mTextPaint.setTextSize(mTextSize);
    // 抗锯齿
    mTextPaint.setAntiAlias(true);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (TextUtils.isEmpty(mText)) {
      setMeasuredDimension(0, 0);
      return;
    }
    int size = measureSize(widthMeasureSpec, heightMeasureSpec);
    if (size < mMinSize) size = mMinSize;
    if (size > mMaxSize) size = mMaxSize;
    setMeasuredDimension(size, size);
  }

  @Override public void layout(int l, int t, int r, int b) {
    super.layout(l, t, r, b);
    mLayoutParams = getLayoutParams();
  }

  private int measureSize(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    return Math.max(width, height);
  }

  private int measureWidth(int measureSpec) {
    int widthMode = MeasureSpec.getMode(measureSpec);
    int widthSize = MeasureSpec.getSize(measureSpec);
    int width;
    if (widthMode == MeasureSpec.EXACTLY) {
      // Parent has told us how big to be. So be it.
      width = widthSize;
    } else {
      // 以正方形的对角线来写字，需把字的宽高 / 1.5 ，字写在对角线上，宽高要增加（1.5个字母）的宽度
      width = (int) (mTextPaint.measureText(mText) * 2 / 3 + mTextPaint.measureText("M") * 1.5 + (
          getPaddingRight()
              + getPaddingLeft()));
      if (widthMode == MeasureSpec.AT_MOST) {
        width = Math.min(widthSize, width);
      }
    }
    return width;
  }

  private int measureHeight(int measureSpec) {
    int heightMode = MeasureSpec.getMode(measureSpec);
    int heightSize = MeasureSpec.getSize(measureSpec);
    int height;
    if (heightMode == MeasureSpec.EXACTLY) {
      // Parent has told us how big to be. So be it.
      height = heightSize;
    } else {
      height = (int) mTextSize + getPaddingTop() + getPaddingBottom();

      if (heightMode == MeasureSpec.AT_MOST) {
        height = Math.min(height, heightSize);
      }
    }
    return height;
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }

  @Override protected void onDraw(Canvas canvas) {
    if (TextUtils.isEmpty(mText)) return;

    drawTextBg(canvas);
    drawText(canvas);
  }

  private void drawTextBg(Canvas canvas) {
    int width = getWidth();
    int height = getHeight();
    // 画一个梯形，即正方形对角线上方包含文字大小的区域
    Path path = new Path();
    path.moveTo(0, 0);
    path.lineTo(mTextSize * 1.5f, 0);
    path.lineTo(width, height - mTextSize * 1.5f);
    path.lineTo(width, height);
    path.lineTo(0, 0);
    path.close();
    canvas.drawPath(path, mTextBgPaint);
  }

  private void drawText(Canvas canvas) {
    // 画布的旋转为逆时针旋转，旋转完，再往下平移，最后drawText在整个控件的中心位置，完工后画布还原，文字就到了正方形对角线之上了
    canvas.save();
    // 从正中间旋转45°
    canvas.rotate(45, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
    // 画布往下移文字高度的一半
    canvas.translate(0, -mTextSize / 2);
    // 旋转到正方形的对角线上方
    // 文字画在正中间
    int width = getMeasuredWidth();
    width -= mTextPaint.measureText(mText);
    width = width / 2;
    float height = getMeasuredHeight() / 2 + mTextPaint.getFontMetrics().descent;
    // 解决大写字母偏上的问题
    if (mText.equals(mText.toUpperCase())) {
      height += ScreenUtil.dp2px(getContext(), 1);
    }
    canvas.drawText(mText, width, height, mTextPaint);
    canvas.restore();
  }
}
