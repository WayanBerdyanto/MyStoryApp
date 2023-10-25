package com.dicoding.mystoryapp.ui.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.pref.UserModel
import com.dicoding.mystoryapp.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getAllStory() = repository.getAllStory()

    fun getThemeSetting(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(saveThemeSetting: Boolean){
        viewModelScope.launch {
            repository.saveThemeSetting(saveThemeSetting)
        }
    }





}