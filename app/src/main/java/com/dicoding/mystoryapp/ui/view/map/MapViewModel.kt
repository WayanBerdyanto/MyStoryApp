package com.dicoding.mystoryapp.ui.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.mystoryapp.pref.UserModel
import com.dicoding.mystoryapp.repository.UserRepository

class MapViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
    fun getStoriesWithLocation(location: Int) = repository.getStoriesWithLocation(location)

    fun getAllStory() = repository.getAllStory()
}