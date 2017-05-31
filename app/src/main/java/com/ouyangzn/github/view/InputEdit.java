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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;
import com.ouyangzn.github.R;
import com.ouyangzn.github.utils.ScreenUtils;
import rx.functions.Action1;

/**
 * Created by ouyangzn on 2016/9/14.<br/>
 * Description：
 */
public class InputEdit extends FrameLayout implements View.OnClickListener, TextWatcher {

  private EditText mEditText;
  private ImageView mDelete;

  private OnClearTextListener mClearTextListener;

  public InputEdit(Context context) {
    this(context, null);
  }

  public InputEdit(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public InputEdit(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public InputEdit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  public OnClearTextListener getOnClearTextListener() {
    return mClearTextListener;
  }

  public void setOnClearTextListener(OnClearTextListener clearTextListener) {
    mClearTextListener = clearTextListener;
  }

  public EditText getEditText() {
    return mEditText;
  }

  public ImageView getDeleteView() {
    return mDelete;
  }

  public void setOnEditorActionListener(Action1<TextViewEditorActionEvent> action) {
    RxTextView.editorActionEvents(mEditText).subscribe(action);
  }

  public void setOnEditorActionListener(TextView.OnEditorActionListener listener) {
    mEditText.setOnEditorActionListener(listener);
  }

  public void clearFocus() {
    mEditText.clearFocus();
  }

  public String getInputText() {
    return mEditText.getText().toString();
  }

  public void setHint(CharSequence hint) {
    mEditText.setHint(hint);
  }

  public void setImeOptions(int imeOptions) {
    mEditText.setImeOptions(imeOptions);
  }

  private void setInputType(int type) {
    mEditText.setInputType(type);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.img_delete_input:
        mEditText.setText(null);
        if (mClearTextListener != null) {
          mClearTextListener.onClearText();
        }
        break;
    }
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override public void afterTextChanged(Editable s) {
    if (s.length() == 0) {
      mDelete.setVisibility(GONE);
    } else {
      mDelete.setVisibility(VISIBLE);
    }
  }

  private void init(Context context, AttributeSet attrs) {
    LayoutParams params =
        new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    setLayoutParams(params);
    inflate(context, R.layout.view_input_edit, this);
    mEditText = ButterKnife.findById(this, R.id.et_input);
    mEditText.addTextChangedListener(this);
    mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          if (getInputText().length() == 0) {
            mDelete.setVisibility(GONE);
          } else {
            mDelete.setVisibility(VISIBLE);
          }
        } else {
          mDelete.setVisibility(GONE);
        }
      }
    });
    mDelete = ButterKnife.findById(this, R.id.img_delete_input);
    mDelete.setOnClickListener(this);
    if (getInputText().length() == 0) {
      mDelete.setVisibility(GONE);
    }
    // todo 增加几个基础属性
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputEdit);
    String hint = ta.getString(R.styleable.InputEdit_input_hint);
    setHint(hint);
    Drawable drawable = ta.getDrawable(R.styleable.InputEdit_editBg);
    if (drawable != null) {
      mEditText.setBackgroundDrawable(drawable);
    }
    drawable = ta.getDrawable(R.styleable.InputEdit_deleteImg);
    int paddingRight = ScreenUtils.dp2px(context, 40);
    int padding = ScreenUtils.dp2px(context, 10);
    if (drawable != null) {
      mDelete.setImageDrawable(drawable);
      // 最后一个字离删除图标留一定的距离
      paddingRight = drawable.getIntrinsicWidth() + padding + ScreenUtils.dp2px(context, 4);
    }
    mEditText.setPadding(padding, padding, paddingRight, padding);
    initImeOptions(ta);
    boolean singleLine = ta.getBoolean(R.styleable.InputEdit_singleLine, true);
    if (singleLine) {
      mEditText.setSingleLine();
      mEditText.setGravity(Gravity.CENTER_VERTICAL);
    } else {
      mEditText.setSingleLine(false);
      mEditText.setGravity(Gravity.START | Gravity.TOP);
    }
    // singleLine会导致password的inputType无效，所以需要先singleLine
    initInputType(ta, singleLine);
    ta.recycle();
  }

  /**
   * 初始化输入类型，解决了单行与密码类型互相冲突的问题
   *
   * @param ta TypedArray
   * @param singleLine 是否设置了单行
   */
  private void initInputType(TypedArray ta, boolean singleLine) {
    int inputType = ta.getInt(R.styleable.InputEdit_inputType, 1);
    switch (inputType) {
      default:
      case 1:
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | (singleLine ? InputType.TYPE_NULL
            : InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        break;
      case 2:
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER | (singleLine ? InputType.TYPE_NULL
            : InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        break;
      case 3:
        mEditText.setInputType(
            InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | (singleLine
                ? InputType.TYPE_NULL : InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        break;
      case 4:
        mEditText.setInputType(InputType.TYPE_CLASS_PHONE | (singleLine ? InputType.TYPE_NULL
            : InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        break;
      case 5:
        mEditText.setInputType(
            InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD | (singleLine
                ? InputType.TYPE_NULL : InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        break;
    }
  }

  private void initImeOptions(TypedArray ta) {
    int imeOptions = ta.getInt(R.styleable.InputEdit_input_imeOptions, 1);
    switch (imeOptions) {
      case 1:
        mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        break;
      case 2:
        mEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        break;
      case 3:
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        break;
      case 4:
        mEditText.setImeOptions(EditorInfo.IME_ACTION_SEND);
        break;
    }
  }

  public interface OnClearTextListener {
    void onClearText();
  }
}
