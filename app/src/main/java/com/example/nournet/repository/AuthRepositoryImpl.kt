package com.example.nournet.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.nournet.model.User
import com.example.nournet.utils.Resource
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val db : FirebaseFirestore,
    private val auth : FirebaseAuth,
) : AuthRepository {

    override suspend fun login(
        email : String,
        password : String,
        result : (Resource < String > ) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val currentUser = auth.currentUser
                if (currentUser!!.isEmailVerified) {
                    db.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .addOnSuccessListener { success ->
                            val userType = success.get("userType") as String
                            when (userType) {
                                "Admin" -> result.invoke(Resource.Success("Admin"))
                                "Organization" -> result.invoke(Resource.Success("Organization"))
                                else -> result.invoke(Resource.Success("Restaurant"))
                            }
                        }
                }
                else result.invoke(Resource.Error("Email not verified!"))
            }
            .addOnFailureListener { error ->
                result.invoke(Resource.Error(error.message.toString()))
            }
    }

    override suspend fun register(
        email : String,
        password : String,
        user : User,
        result : (Resource < String >) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                auth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        db.collection("users")
                            .document(auth.uid.toString())
                            .set(user)
                            .addOnSuccessListener {
                                result.invoke(Resource.Success(
                                    "Account created successfully!\nCheck your email to verify your account."))
                            }
                    }
            }
            .addOnFailureListener { error ->
                result.invoke(Resource.Error(error.message.toString()))
            }
    }

    override suspend fun logout(result : () -> Unit) {
        auth.signOut()
        result.invoke()
    }
}