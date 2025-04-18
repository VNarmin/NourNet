package com.example.nournet.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.nournet.model.Donation
import com.example.nournet.model.User
import com.example.nournet.utils.Response
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NourNetRepositoryImpl @Inject constructor(
    private val db : FirebaseFirestore,
    private val auth : FirebaseAuth,
) : NourNetRepository {
    private val tag = "RepositoryImpl"

    companion object{
        @Volatile
        private var instance : NourNetRepositoryImpl? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: NourNetRepositoryImpl(
                FirebaseFirestore.getInstance(),
                FirebaseAuth.getInstance()
            ).also { impl -> instance = impl }
        }
    }

    override suspend fun getDonations(result : (Response<List<Donation>>) -> Unit) {
        db.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList < Donation > ()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Response.Success(donations)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun donate(donation : Donation, result : (Response<List<Donation>>) -> Unit) {
        val donationID = donation.donorID!!
        db.collection("donations").document(donationID).set(donation)
            .addOnSuccessListener {
                result.invoke(
                    Response.Success(listOf(donation))
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun fetchHistory(result : (Response<List<Donation>>) -> Unit) {
        val userID = auth.currentUser!!.uid
        db.collection("donations").whereEqualTo("donationID", userID)
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList < Donation > ()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Response.Success(donations)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun updateDonation(
        donation : Donation,
        data : HashMap < String,Any >,
        result : (Response<List<Donation>>) -> Unit
    ) {
        db.collection("donation")
            .document(donation.donorID!!)
            .update(data)
            .addOnSuccessListener {
                result.invoke(
                    Response.Success(listOf(donation))
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun getAllUsersTotalNumber(result : (Response<Int>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = ArrayList < User > ()
                for (data in snapshot.documents) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                result.invoke(
                    Response.Success(users.size)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun getTotalDonations(result : (Response<Int>) -> Unit) {
        db.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList < Donation > ()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Response.Success(donations.size)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun getTotalDonors(result : (Response<Int>) -> Unit) {
        db.collection("users")
            .whereEqualTo("userType", "Restaurant")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList < Donation > ()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Response.Success(donations.size)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun getAllDonations(result : (Response<List<Donation>>) -> Unit) {
        db.collection("donations")
            .get()
            .addOnSuccessListener { snapshot ->
                val donations = ArrayList < Donation > ()
                for (data in snapshot.documents) {
                    val donation = data.toObject(Donation::class.java)
                    if (donation != null) {
                        donations.add(donation)
                    }
                }
                result.invoke(
                    Response.Success(donations)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun getAllUsers(result : (Response<List<User>>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { snapshot ->
                val users = ArrayList < User > ()
                for (data in snapshot.documents) {
                    val user = data.toObject(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                result.invoke(
                    Response.Success(users)
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun deleteUser(userID : String, result : (Response<String>) -> Unit) {
        db.collection("users")
            .document(userID)
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    Response.Success("User deleted.")
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun deleteDonation(donationID : String, result : (Response<String>) -> Unit) {
        db.collection("donations")
            .document(donationID)
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    Response.Success("Deleted Successfully.")
                )
            }
            .addOnFailureListener { error ->
                result.invoke(
                    Response.Error(error.message.toString())
                )
            }
    }

    override suspend fun getUserID(email : String, result : (String) -> Unit) {
       val query =  db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()
        for (i in query.documents) {
            val id = i.id
            result.invoke(id)
        }
    }

    override suspend fun getCurrentUserEmail(result : (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.e(tag, "No authenticated user found.")
            return
        }

        db.collection("users")
            .document(currentUser.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.toObject(User::class.java)
                if (user?.email != null) {
                    result.invoke(user.email!!)
                } else {
                    Log.e(tag, "User or user.email is null.")
                }
            }
            .addOnFailureListener { error ->
                Log.e(tag, "Error fetching user data : ${error.message}")
            }
    }
}