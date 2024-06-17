package com.madeean.storyapp2.ui.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.madeean.storyapp2.R.string


class PasswordEditText : AppCompatEditText {

  private var passwordEditTextListener: CustomViewListener? = null

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

  fun setPasswordEditTextListener(listener: CustomViewListener) {
    passwordEditTextListener = listener
  }

  private fun init() {
    hint = context.getString(string.masukkan_password_anda)
    textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD


    addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        val password = s.toString()
        passwordEditTextListener?.onPasswordTextChanged(password)
        val isValidPassword = password.length >= 8
        error = if (!isValidPassword && password.isNotEmpty()) {
          context.getString(string.password_harus_memiliki_minimal_8_karakter)
        } else {
          null // Hapus error jika password valid
        }
      }

      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
  }

}