package com.example.nournet.repository

import com.example.nournet.model.Donation
import com.example.nournet.model.User
import com.example.nournet.utils.Response

interface NourNetRepository {
    suspend fun getDonations(result : (Response<List<Donation>>) -> Unit)
    suspend fun donate(donation : Donation, result : (Response<List<Donation>>) -> Unit)
    suspend fun fetchHistory(result : (Response<List<Donation>>) -> Unit)
    suspend fun updateDonation(donation : Donation,
                               data : HashMap<String,Any>,
                               result : (Response<List<Donation>>) -> Unit)
    suspend fun getAllUsersTotalNumber(result : (Response<Int>) -> Unit)
    suspend fun getTotalDonations(result : (Response<Int>) -> Unit)
    suspend fun getTotalDonors(result : (Response<Int>) -> Unit)
    suspend fun getAllDonations(result : (Response<List<Donation>>) -> Unit)
    suspend fun getAllUsers(result : (Response<List<User>>) -> Unit)
    suspend fun deleteUser(userID : String, result : (Response<String>) -> Unit)
    suspend fun deleteDonation(donationID : String, result : (Response<String>) -> Unit)
    suspend fun getUserID(email : String, result : (String) -> Unit)
    suspend fun getCurrentUserEmail(result : (String) -> Unit)
}