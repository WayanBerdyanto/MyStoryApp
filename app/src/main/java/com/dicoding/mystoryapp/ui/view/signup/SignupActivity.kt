package com.dicoding.mystoryapp.ui.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.ResultState
import com.dicoding.mystoryapp.databinding.ActivitySignupBinding
import com.dicoding.mystoryapp.ui.ViewModelFactory
import com.dicoding.mystoryapp.ui.customView.MyButton
import com.dicoding.mystoryapp.ui.customView.MyEditTextEmail
import com.dicoding.mystoryapp.ui.customView.MyEditTextPassword

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var myButton: MyButton
    private lateinit var myEditText: MyEditTextPassword
    private lateinit var myEditTextEmail: MyEditTextEmail

    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myButton = binding.signupButton
        myEditText = binding.passwordEditText
        myEditTextEmail = binding.emailEditText


        setupView()
        register()
        playAnimation()
        setMyButtonEnable()

        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.signupButton.setOnClickListener{register()}
        
    }

    private fun register(){
        binding.let {
            val username = it.nameEditText.text.toString()
            val email = it.emailEditText.text.toString()
            val password = it.passwordEditText.text.toString()
            it.signupButton.setOnClickListener {
                viewModel.register(username, email, password).observe(this){result->
                    if (result != null) {
                        when (result) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Success -> {
                                showToast(result.data.message)
                                AlertDialog.Builder(this).apply {
                                    setTitle(R.string.succeed)
                                    setMessage(R.string.succeed_message)
                                    setPositiveButton(R.string.continues) { _, _ ->
                                        finish()
                                    }
                                    create()
                                    show()
                                }
                                showLoading(false)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)

        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(500)

        val email = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(500)

        val password = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEdit = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(500)

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)

        val togetherName = AnimatorSet().apply {
            playTogether(nameEditLayout, nameEdit)
        }

        val togetherEmail = AnimatorSet().apply {
            playTogether(emailEditLayout, emailEdit)
        }

        val togetherPassword = AnimatorSet().apply {
            playTogether(passwordEditLayout, passwordEdit)
        }

        AnimatorSet().apply {
            playSequentially(title,name, togetherName, email, togetherEmail, password, togetherPassword, signup)
            start()
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setMyButtonEnable() {
        val result = myEditText.text
        myButton.isEnabled = result.toString().length >= 8
    }
}