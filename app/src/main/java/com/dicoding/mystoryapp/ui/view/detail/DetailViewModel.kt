package com.dicoding.mystoryapp.ui.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.mystoryapp.pref.UserModel
import com.dicoding.mystoryapp.repository.UserRepository

class DetailViewModel(private val repository: UserRepository) : ViewModel()  {

    fun getDetailStories(id: String) = repository.getDetailStories(id)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}