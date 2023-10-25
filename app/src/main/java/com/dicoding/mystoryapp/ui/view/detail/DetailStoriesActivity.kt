package com.dicoding.mystoryapp.ui.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.mystoryapp.ResultState
import com.dicoding.mystoryapp.databinding.ActivityDetailStoriesBinding
import com.dicoding.mystoryapp.ui.StoryAdapter.Companion.USER_ID
import com.dicoding.mystoryapp.ui.ViewModelFactory

class DetailStoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoriesBinding

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getStringExtra(USER_ID).toString()

        viewModel.getSession().observe(this){
            if (it.isLogin){
                viewModel.getDetailStories(user).observe(this){result->

                    if (result != null){
                        when(result){
                            is ResultState.Loading -> showLoading(true)

                            is ResultState.Success->{
                                showLoading(false)
                                Glide.with(this)
                                    .load(result.data.story.photoUrl)
                                    .into(binding.ivPhoto)
                                with(binding){
                                    result.data.story.apply {
                                        tvUsername.text = name
                                        tvDescription.text = description
                                    }
                                }
                            }

                            is ResultState.Error -> {
                                showLoading(false)
                                showToast(result.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}