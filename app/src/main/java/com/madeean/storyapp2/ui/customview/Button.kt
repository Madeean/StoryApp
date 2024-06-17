package com.madeean.storyapp2.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.madeean.storyapp2.R
import com.madeean.storyapp2.R.string

class Button : AppCompatButton {

  private lateinit var enabledBackground: Drawable
  private lateinit var disabledBackground: Drawable
  private var txtColor: Int = 0
  private var buttonText = ""

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

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    background = if (isEnabled) enabledBackground else disabledBackground
    setTextColor(txtColor)
    textSize = 12f
    gravity = Gravity.CENTER
    text = if (isEnabled) buttonText else context.getString(string.isi_dulu)
    isAllCaps = false
  }

  fun updateButtonText(text: String) {
    buttonText = text
  }

  private fun init() {
    txtColor = ContextCompat.getColor(context, android.R.color.background_light)
    enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
    disabledBackground =
      ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
  }
}