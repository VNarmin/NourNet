package com.example.nournet.repository

import com.example.nournet.model.User
import com.example.nournet.utils.Resource

interface AuthRepository {
    //suspend fun saveUserDetails(user: User, result: (Resource<User>) -> Unit)
    suspend fun login(email: String, password: String, result: (Resource<String>) -> Unit)
    suspend fun register(email: String, password: String, user: User,result: (Resource<String>) -> Unit)
    suspend fun logout(result: () -> Unit)
}