package com.example.nournet.repository

import com.example.nournet.model.User
import com.example.nournet.utils.Response

interface AuthRepository {
    suspend fun login(email : String, password : String, result : (Response < String >) -> Unit)
    suspend fun register(email : String, password : String, user : User,result : (Response < String >) -> Unit)
    suspend fun logout(result : () -> Unit)
}