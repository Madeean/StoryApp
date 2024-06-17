package com.madeean.storyapp2.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.madeean.storyapp2.Utils.setVisibility
import com.madeean.storyapp2.data.response.DetailStoryResponseModel
import com.madeean.storyapp2.databinding.ActivityHomeBinding
import com.madeean.storyapp2.ui.ViewModelFactory
import com.madeean.storyapp2.ui.authentication.LoginActivity
import com.madeean.storyapp2.ui.home.HomeViewModel.AllStoryState
import com.madeean.storyapp2.ui.home.adapter.StoryListAdapter
import com.madeean.storyapp2.ui.home.adapter.listener.StoryListListener
import com.madeean.storyapp2.ui.home.detail.DetailActivity
import com.madeean.storyapp2.ui.home.tambah.TambahStoryActivity
import com.madeean.storyapp2.ui.preference.AuthenticationPreferences
import com.madeean.storyapp2.ui.preference.dataStore


class HomeActivity : AppCompatActivity() {
  private lateinit var binding: ActivityHomeBinding
  private lateinit var viewModel: HomeViewModel
  private val dataAdapter by lazy {
    StoryListAdapter()
  }
  private lateinit var token: String
  private var addStoryLauncher: ActivityResultLauncher<Intent>? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupActivityLauncher()
    setupViewModel()
    setupObserve()
    setupAdapter()
    setupRecycleView()
    setupButton()
  }

  private fun setupActivityLauncher() {
    addStoryLauncher = registerForActivityResult(
      StartActivityForResult()
    ) { result: ActivityResult ->
      if (result.resultCode == RESULT_OK) {
        viewModel.getAllStory(token)
      }
    }
  }

  private fun setupButton() {
    binding.run {
      ivLogout.setOnClickListener {
        binding.progressBar.setVisibility(true)
        viewModel.logout()
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
      }
      fabAdd.setOnClickListener {
        addStoryLauncher?.launch(Intent(this@HomeActivity, TambahStoryActivity::class.java))
      }
      swipeRefreshLayout.setOnRefreshListener {
        swipeRefreshLayout.isRefreshing = false
        viewModel.getAllStory(token)
      }
    }
  }

  private fun setupAdapter() {
    dataAdapter.setOnItemClickCallback(object : StoryListListener {
      override fun onItemClickListener(
        data: DetailStoryResponseModel,
        tvTitle: TextView,
        tvDescription: TextView,
        ivStory: ImageView,
        context: Context
      ) {
        val intent = DetailActivity.newIntent(this@HomeActivity, data)
        val optionsCompat: ActivityOptionsCompat =
          ActivityOptionsCompat.makeSceneTransitionAnimation(
            context as Activity,
            Pair(ivStory, "photoUrl"),
            Pair(tvTitle, "name"),
            Pair(tvDescription, "description"),
          )
        context.startActivity(intent, optionsCompat.toBundle())
      }
    })
  }

  private fun setupRecycleView() {
    binding.rvStory.run {
      layoutManager = LinearLayoutManager(this@HomeActivity)
      adapter = dataAdapter
    }
  }

  private fun setupObserve() {
    viewModel.getPrefToken().observe(this) {
      token = "Bearer $it"
    }

    viewModel.allStoryStatus.observe(this) {
      binding.run {
        when (it) {
          is AllStoryState.Loading -> {
            progressBar.setVisibility(true)
          }

          is AllStoryState.Success -> {
            vaStory.displayedChild = 0
            progressBar.setVisibility(false)
            dataAdapter.submitList(it.data.listStory)
            dataAdapter.notifyItemRangeChanged(0, it.data.listStory.size)
            rvStory.smoothScrollToPosition(0)
          }

          is AllStoryState.Error -> {
            progressBar.setVisibility(false)
            vaStory.displayedChild = 1
            tvError.text = it.data.message
          }

          is AllStoryState.Idle -> {
            progressBar.setVisibility(false)
            viewModel.getAllStory(token)
          }
        }
      }
    }
  }

  private fun setupViewModel() {
    val pref = AuthenticationPreferences.getInstance(application.dataStore)
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this, pref)
    viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
  }
}