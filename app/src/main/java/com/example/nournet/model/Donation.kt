package com.example.nournet.model

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Donation(
    var donationID : String? = "",
    var name : String? = "",
    var foodItem : String? = "",
    var phoneNumber : String? = "",
    var description : String? = "",
    var location : @RawValue GeoPoint? = null,
    var received : Boolean? = false,
    var donorID : String? = ""
) : Parcelable