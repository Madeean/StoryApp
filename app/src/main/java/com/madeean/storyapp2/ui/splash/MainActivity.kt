package com.madeean.storyapp2.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.madeean.storyapp2.databinding.ActivityMainBinding
import com.madeean.storyapp2.ui.ViewModelFactory
import com.madeean.storyapp2.ui.authentication.AuthenticationViewModel
import com.madeean.storyapp2.ui.authentication.LoginActivity
import com.madeean.storyapp2.ui.home.HomeActivity
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import com.madeean.storyapp2.ui.preference.dataStore

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var viewModel: AuthenticationViewModel
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    Handler(Looper.getMainLooper()).postDelayed({
      setupViewModel()
      viewModel.getPrefToken().observe(this) {
        if (it.isEmpty()) {
          startActivity(Intent(this, LoginActivity::class.java))
          finish()
        } else {
          startActivity(Intent(this, HomeActivity::class.java))
          finish()
        }
      }
    }, 2000)
  }

  private fun setupViewModel() {
    val pref = AuthenticationPreferences.getInstance(application.dataStore)
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    viewModel = ViewModelProvider(this, factory)[AuthenticationViewModel::class.java]
  }

}