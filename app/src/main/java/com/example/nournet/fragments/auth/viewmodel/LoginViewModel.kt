package com.example.nournet.fragments.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.nournet.repository.AuthRepository
import com.example.nournet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo : AuthRepository
) : ViewModel() {
    private val _loginRequest = MutableLiveData<Response<String>>()
    val loginRequest = _loginRequest as LiveData<Response<String>>

    fun login(email : String, password : String){
        viewModelScope.launch {
            _loginRequest.value = Response.Loading
            try {
                repo.login(email, password) { response ->
                    _loginRequest.value = response
                }
            } catch (e : Exception) {
                _loginRequest.value = Response.Error(e.message.toString())
            }
        }
    }
}