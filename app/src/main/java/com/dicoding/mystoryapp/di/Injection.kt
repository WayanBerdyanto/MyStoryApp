package com.dicoding.mystoryapp.di

import android.content.Context
import com.dicoding.mystoryapp.data.remote.retrofit.ApiConfig
import com.dicoding.mystoryapp.pref.UserPreference
import com.dicoding.mystoryapp.pref.dataStore
import com.dicoding.mystoryapp.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService =ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}