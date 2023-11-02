package com.dicoding.mystoryapp.ui.view.galery

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.mystoryapp.pref.UserModel
import com.dicoding.mystoryapp.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class GalleryViewModel(private val repository: UserRepository) : ViewModel() {
    fun uploadImage(file: MultipartBody.Part, description: RequestBody) =
        repository.uploadImage(file, description)

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}