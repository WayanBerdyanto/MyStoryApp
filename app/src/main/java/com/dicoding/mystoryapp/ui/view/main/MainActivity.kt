package com.dicoding.mystoryapp.ui.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.ResultState
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.ui.LoadingStateAdapter
import com.dicoding.mystoryapp.ui.StoryAdapter
import com.dicoding.mystoryapp.ui.ViewModelFactory
import com.dicoding.mystoryapp.ui.view.galery.GaleryActivity
import com.dicoding.mystoryapp.ui.view.login.LoginActivity
import com.dicoding.mystoryapp.ui.view.map.MapsActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: StoryAdapter

    private lateinit var binding: ActivityMainBinding

    private var isDarkModeActive = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getList()
        buttonTopAppBar()
        getThemeSetting()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, GaleryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun buttonTopAppBar() {
        binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.menu_logout -> {
                    AlertDialog.Builder(this).apply {
                        setTitle(R.string.logout)
                        setMessage(R.string.sure_logout)
                        setPositiveButton(R.string.continues) { _, _ ->
                            viewModel.logout()

                        }
                        create()
                        show()
                    }
                    true
                }

                R.id.menu_lightMode -> {
                    if (isDarkModeActive) {
                        menu.setIcon(R.drawable.baseline_light_mode_24)
                        viewModel.saveThemeSetting(isDarkModeActive)
                    } else {
                        menu.setIcon(R.drawable.baseline_dark_mode_24)
                        viewModel.saveThemeSetting(isDarkModeActive)
                    }
                    true
                }

                R.id.menu_settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }

                R.id.menu_map -> {
                    val intent = Intent(this@MainActivity, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun getList() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            if (user.isLogin) {
                viewModel.getAllStory().observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                setAllStory()
                                showLoading(false)
                                showToast(result.data.message)
                            }

                            is ResultState.Error -> {
                                showToast(result.error)
                                showLoading(false)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setAllStory() {
        adapter = StoryAdapter()
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvListStory.layoutManager = layoutManager
        binding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

    }

    private fun getThemeSetting() {
        viewModel.getThemeSetting().observe(this) { mode: Boolean ->
            isDarkModeActive = mode
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                isDarkModeActive = false

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                isDarkModeActive = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSession()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}