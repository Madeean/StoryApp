package com.madeean.storyapp2.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import com.madeean.storyapp2.R.string

class EmailEditText : AppCompatEditText {

  private var emailEditTextListener: CustomViewListener? = null

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

  fun setEmailEditTextListener(listener: CustomViewListener) {
    emailEditTextListener = listener
  }

  private fun init() {
    hint = context.getString(string.masukkan_email_anda)
    textAlignment = TEXT_ALIGNMENT_VIEW_START
    inputType = EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

    addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        val email = s.toString()
        emailEditTextListener?.onEmailTextChanged(email)
        val isValidEmail = isValidEmail(email)
        error = if (!isValidEmail && email.isNotEmpty()) {
          context.getString(string.email_tidak_valid)
        } else {
          null // Hapus error jika email valid
        }
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
  }

  private fun isValidEmail(email: String): Boolean {
    return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
  }
}