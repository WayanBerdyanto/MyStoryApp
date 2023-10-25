package com.dicoding.mystoryapp.ui.view.signup

import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.repository.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(username: String, email: String, password: String) =repository.register(username, email, password)
}