package com.example.nournet.fragments.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.nournet.model.User
import com.example.nournet.repository.AuthRepository
import com.example.nournet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo : AuthRepository
) : ViewModel() {
    private val _registerRequest = MutableLiveData<Response<String>>()
    val registerRequest = _registerRequest as LiveData<Response<String>>

    fun register(email : String, password : String, user : User) {
        viewModelScope.launch {
            _registerRequest.value = Response.Loading
            try {
                repo.register(email, password, user) { response ->
                    _registerRequest.value = response
                }
            } catch (e : Exception) {
                _registerRequest.value = Response.Error(e.message.toString())
            }
        }
    }
}