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
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewEditorActionEvent;
import com.ouyangzn.github.R;
import rx.functions.Action1;

/**
 * Created by ouyangzn on 2016/9/14.<br/>
 * Description：
 */
public class InputEdit extends FrameLayout implements View.OnClickListener, TextWatcher {

  private EditText mEditText;
  private ImageView mDelete;

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

  private void init(Context context, AttributeSet attrs) {
    View view = LayoutInflater.from(context).inflate(R.layout.view_input_edit, this, true);
    mEditText = ButterKnife.findById(view, R.id.et_input);
    mEditText.addTextChangedListener(this);
    mDelete = ButterKnife.findById(view, R.id.img_delete_input);
    mDelete.setOnClickListener(this);
    if (getInputText().length() == 0) {
      mDelete.setVisibility(GONE);
    }
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InputEdit);
    String hint = ta.getString(R.styleable.InputEdit_input_hint);
    setHint(hint);
    int imeOptions = ta.getInt(R.styleable.InputEdit_input_imeOptions, 1);
    switch (imeOptions) {
      case 1:
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        break;
      case 2:
        setImeOptions(EditorInfo.IME_ACTION_NEXT);
        break;
      case 3:
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        break;
      case 4:
        setImeOptions(EditorInfo.IME_ACTION_SEND);
        break;
    }
    ta.recycle();
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

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.img_delete_input:
        mEditText.setText(null);
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
}
