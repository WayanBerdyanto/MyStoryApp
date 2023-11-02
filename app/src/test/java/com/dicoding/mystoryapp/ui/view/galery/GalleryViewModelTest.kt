package com.dicoding.mystoryapp.ui.view.galery

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import com.dicoding.mystoryapp.MainDispatcherRule
import com.dicoding.mystoryapp.ResultState
import com.dicoding.mystoryapp.data.remote.response.PostStoriesResponse
import com.dicoding.mystoryapp.reduceFileImage
import com.dicoding.mystoryapp.repository.UserRepository
import com.dicoding.mystoryapp.uriToFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GalleryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var galleryViewModel: GalleryViewModel
    private var currentImageUri: Uri? = null

    @Before
    fun setUp() {
        galleryViewModel = GalleryViewModel(userRepository)
    }

    @Test
    fun `when post Story Should Not Null and Return Data`() = runTest{
        val observer = Observer<ResultState<PostStoriesResponse>> {}
        currentImageUri?.let { uri->
            val context = InstrumentationRegistry.getInstrumentation().context
            val imageFile = uriToFile(uri, context).reduceFileImage()
            val desc = "lorem"

            val requestBody = desc.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())

            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val expectedPostStory = MutableLiveData<ResultState<PostStoriesResponse>>()
                Mockito.`when`(userRepository.uploadImage(multipartBody, requestBody)).thenReturn(expectedPostStory)
                val actualLogin= galleryViewModel.uploadImage(multipartBody, requestBody).observeForever(observer)
                Assert.assertNotNull(actualLogin)
            } finally {
                galleryViewModel.uploadImage(multipartBody, requestBody).removeObserver(observer)
            }
        }
    }
}