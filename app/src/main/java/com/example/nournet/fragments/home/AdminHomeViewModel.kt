package com.example.nournet.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.nournet.model.Donation
import com.example.nournet.model.User
import com.example.nournet.repository.NourNetRepository
import com.example.nournet.utils.Response
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminHomeViewModel @Inject constructor(
    private val repo : NourNetRepository
) : ViewModel() {

    private val _users = MutableLiveData<Response<List<User>>>()
    val users = _users as LiveData<Response<List<User>>>

    private val _totalDonations = MutableLiveData<Response<Int>>()
    val totalDonations = _totalDonations as LiveData<Response<Int>>

    private val _totalUser = MutableLiveData<Response<Int>>()
    val totalUsers = _totalUser as LiveData<Response<Int>>

    private val _totalDonors = MutableLiveData<Response<Int>>()
    val totalDonors = _totalDonors as LiveData<Response<Int>>

    private val _allDonations = MutableLiveData<Response<List<Donation>>>()
    val allDonations = _allDonations as LiveData<Response<List<Donation>>>

    private val _deleteUser = MutableLiveData<Response<String>>()
    val deleteUser = _deleteUser as  LiveData<Response<String>>

    private val _deleteDonation = MutableLiveData<Response<String>>()
    val deleteDonation = _deleteDonation as  LiveData<Response<String>>

    fun getAllUsers() {
        viewModelScope.launch {
            _users.value = Response.Loading
            try {
                repo.getAllUsers { result ->
                    _users.value = result
                }
            } catch (e : Exception) {
                _users.value = Response.Error(e.message.toString())
            }
        }
    }

    fun getAllUsersTotalNumber() {
        viewModelScope.launch {
            _totalUser.value = Response.Loading
            try {
                repo.getAllUsersTotalNumber { result ->
                    _totalUser.value = result
                }
            } catch (e : Exception) {
                _totalUser.value = Response.Error(e.message.toString())
            }
        }
    }

    fun getAllDonorsTotalNumber() {
        viewModelScope.launch {
            _totalDonors.value = Response.Loading
            try {
                repo.getTotalDonors { result ->
                    _totalDonors.value = result
                }
            } catch (e : Exception) {
                _totalDonors.value = Response.Error(e.message.toString())
            }
        }
    }

    fun getTotalDonations() {
        viewModelScope.launch {
            _totalDonations.value = Response.Loading
            try {
                repo.getTotalDonations { result ->
                    _totalDonations.value = result
                }
            } catch (e : Exception) {
                _totalDonations.value = Response.Error(e.message.toString())
            }
        }
    }

    fun getAllDonations() {
        viewModelScope.launch {
            _allDonations.value = Response.Loading
            try {
                repo.getAllDonations { result ->
                    _allDonations.value = result
                }
            } catch (e : Exception) {
                _allDonations.value = Response.Error(e.message.toString())
            }
        }
    }
}