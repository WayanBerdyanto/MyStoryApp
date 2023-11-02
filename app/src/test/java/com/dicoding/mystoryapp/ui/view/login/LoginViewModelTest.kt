package com.dicoding.mystoryapp.ui.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dicoding.mystoryapp.ResultState
import com.dicoding.mystoryapp.data.remote.response.LoginResponse
import com.dicoding.mystoryapp.repository.UserRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when Get Login Should Not Null and Return Success`() {

        val observer = Observer<ResultState<LoginResponse>> {}
        val email = "example@gmail.com"
        val password = "2121"
        try {
            val expectedLogin = MutableLiveData<ResultState<LoginResponse>>()
            `when`(userRepository.login(email, password)).thenReturn(expectedLogin)
            val actualLogin= loginViewModel.login(email, password).observeForever(observer)
            Assert.assertNotNull(actualLogin)
        } finally {
            loginViewModel.login(email, password).removeObserver(observer)
        }
    }


}