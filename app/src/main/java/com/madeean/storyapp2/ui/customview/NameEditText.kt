package com.madeean.storyapp2.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.madeean.storyapp2.R.string


class NameEditText : AppCompatEditText {

  constructor(context: Context) : super(context) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    init()
  }

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  ) {
    init()
  }

  private fun init() {
    hint = context.getString(string.masukkan_nama_anda)
    textAlignment = View.TEXT_ALIGNMENT_VIEW_START
  }

}