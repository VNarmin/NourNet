package com.example.nournet.repository

import com.example.nournet.model.Donation
import com.example.nournet.model.User
import com.example.nournet.utils.Resource

interface MainRepository {
    suspend fun getDonations(result: (Resource<List<Donation>>) -> Unit)
    suspend fun donate(donation: Donation, result: (Resource<List<Donation>>) -> Unit)
    suspend fun fetchHistory(result: (Resource<List<Donation>>) -> Unit)
    suspend fun updateDonation(donation: Donation, data: HashMap<String,Any>, result: (Resource<List<Donation>>) -> Unit)
    suspend fun getAllUsersTotalNumber(result: (Resource<Int>) -> Unit)
    suspend fun getTotalDonations(result: (Resource<Int>) -> Unit)
    suspend fun getTotalDonors(result: (Resource<Int>) -> Unit)
    suspend fun getAllDonations(result: (Resource<List<Donation>>) -> Unit)
    suspend fun getAllUsers(result: (Resource<List<User>>) -> Unit)
    suspend fun deleteUser(userId: String, result: (Resource<String>) -> Unit)
    suspend fun deleteDonation(donationId: String,result: (Resource<String>) -> Unit)
    suspend fun getUserId(email: String, result: (String) -> Unit)
    suspend fun getCurrentUserEmail(result: (String) -> Unit)
}