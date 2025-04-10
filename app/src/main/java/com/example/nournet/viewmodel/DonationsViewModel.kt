package com.example.nournet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.nournet.model.Donation
import com.example.nournet.repository.NourNetRepository
import com.example.nournet.utils.Response
import javax.inject.Inject

@HiltViewModel
class DonationsViewModel @Inject constructor(
    private val repository: NourNetRepository,
    private val database: FirebaseFirestore
) : ViewModel() {
    private  val TAG = "DonationsViewModel"
    private val _donations = MutableLiveData<Response<List<Donation>>>()
    val donations : LiveData<Response<List<Donation>>> = _donations

    suspend fun getDonations()  {
        _donations.value = Response.Loading
        repository.getDonations { donations->
            _donations.value = donations
        }
    }

    private val _donate = MutableLiveData<Response<List<Donation>>>()
    val donate : LiveData<Response<List<Donation>>> = _donate

    suspend fun donate(donation : Donation){
        _donate.value = Response.Loading
        repository.donate(donation){ donate ->
            _donate.value = donate
        }
    }

    private val _history = MutableLiveData<Response<List<Donation>>>()
    val history : LiveData<Response<List<Donation>>> = _history

    suspend fun getHistory() {
        _history.value = Response.Loading
            repository.fetchHistory { history ->
            _history.value = history
        }
    }

    /*private val _listener = MutableLiveData<Resource<List<Donation>>>()
    val listener : LiveData<Resource<List<Donation>>> = _listener

     fun listenToChanges(){
        _listener.value = Resource.Loading
        database.collection("Donations").addSnapshotListener { value, error ->
            if (error != null){
                Log.d(TAG, "listenToChanges: ${error.message}")
            }
            if (value != null){
                val donations = ArrayList<Donation>()
                val documents = value.documents
                documents.forEach {
                    val donation = it.toObject(Donation::class.java)
                    if (donation != null){
                        donations.add(donation)
                    }
                }
                _listener.value = Resource.Success(donations)
            }
        }
    }*/
}