package com.madeean.storyapp2

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
  private val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
  private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

  val TERJADI_KESALAHAN_PADA_SERVER = "Terjadi kesalahan pada server"

  const val INTENT_DATA = "intent_data"

  fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) {
      this.visibility = View.VISIBLE
    } else {
      this.visibility = View.GONE
    }
  }

  fun <T> ArrayList<T>?.orEmpty(): ArrayList<T> = this ?: arrayListOf()

  fun ImageView.loadUseGifThumb(imageUrl: String, @DrawableRes resGif: Int) {
    Glide.with(this)
      .load(imageUrl)
      .thumbnail(Glide.with(this).load(resGif))
      .into(this)
  }

  fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
  }

  fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
  }

  fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
  }

  inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
  }

  inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
  }

  fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
      }
      uri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
      )
    }
    return uri ?: getImageUriForPreQ(context)
  }

  private fun getImageUriForPreQ(context: Context): Uri {
    val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(filesDir, "/MyCamera/$timeStamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
    return FileProvider.getUriForFile(
      context,
      "${BuildConfig.APPLICATION_ID}.fileprovider",
      imageFile
    )
  }

  fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()
    return myFile
  }

  private fun createCustomTempFile(context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
  }

  private const val MAXIMAL_SIZE = 1000000

  fun File.reduceFileImage(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).getRotatedBitmap(file)
    var compressQuality = 100
    var streamLength: Int
    do {
      val bmpStream = ByteArrayOutputStream()
      bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
      val bmpPicByteArray = bmpStream.toByteArray()
      streamLength = bmpPicByteArray.size
      compressQuality -= 5
    } while (streamLength > MAXIMAL_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
  }

  private fun Bitmap.getRotatedBitmap(file: File): Bitmap? {
    val orientation = ExifInterface(file).getAttributeInt(
      ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )
    return when (orientation) {
      ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90F)
      ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180F)
      ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270F)
      ExifInterface.ORIENTATION_NORMAL -> this
      else -> this
    }
  }

  private fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
      source, 0, 0, source.width, source.height, matrix, true
    )
  }

}
