package com.example.nournet.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.nournet.R
import com.example.nournet.databinding.ReceiveRowLayoutBinding
import com.example.nournet.fragments.receive.ReceiveFragmentDirections
import com.example.nournet.model.Donation
import com.example.nournet.repository.NourNetRepository
import javax.inject.Inject

class ReceiveAdapter :
    ListAdapter < Donation, ReceiveAdapter.ReceiveViewHolder > (ReceiveViewHolder.ReceiveDiffUtil) {
    class ReceiveViewHolder(private val binding : ReceiveRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        object ReceiveDiffUtil : DiffUtil.ItemCallback < Donation > () {
            override fun areItemsTheSame(old : Donation, new : Donation) : Boolean {
                return old.donationID == new.donationID
            }

            override fun areContentsTheSame(old : Donation, new : Donation) : Boolean {
                return old == new
            }
        }

        @Inject
        lateinit var repo : NourNetRepository

        @SuppressLint("SetTextI18n")
        fun bind(donation : Donation?) {
            binding.donation.text = donation?.foodItem
            binding.description.text = donation?.description
            binding.donorPhoneNumber.setOnClickListener {
                val phoneNumber = donation?.phoneNumber
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel : $phoneNumber")
                binding.root.context.startActivity(intent)
            }
            binding.donorLocation.setOnClickListener {
                val action =
                    ReceiveFragmentDirections.actionReceiveFragmentToDonorLocationFragment(donation!!)
                binding.root.findNavController().navigate(action)
            }

            binding.receiveCheckBox.setOnClickListener {
                val alertDialog = AlertDialog.Builder(binding.root.context)
                    .setTitle("Are you sure?")
                    .setMessage("Marking this as received means that you have already contacted the donor and organized on how to receive the donation or you have already received it. \n\nProceed?")
                    .setIcon(R.drawable.ic_warning)
                    .setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        binding.receiveCheckBox.isChecked = true
                        binding.receiveCheckBox.isEnabled = false
                        binding.receiveCheckBox.text = "Received"

                        val hashMap = HashMap < String, Any > ()
                        hashMap["received"] = true
                        hashMap["id"] = donation?.donationID!!
                        hashMap["foodItem"] = donation.foodItem!!
                        hashMap["description"] = donation.description!!
                        hashMap["phoneNumber"] = donation.phoneNumber!!
                        hashMap["location"] = donation.location!!
                        hashMap["name"] = donation.name!!

                        val db = FirebaseFirestore.getInstance()
                        db.collection("donations").document(donation.donorID!!)
                            .update(hashMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    binding.root.context,
                                    "Donation marked as received.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnFailureListener { error ->
                                Toast.makeText(
                                    binding.root.context,
                                    "${error.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                    .setNegativeButton("No") { _, _ ->
                        binding.receiveCheckBox.isChecked = false
                        binding.receiveCheckBox.isEnabled = true
                    }.create()
                alertDialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ReceiveViewHolder {
        return ReceiveViewHolder(
            ReceiveRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder : ReceiveViewHolder, position : Int) {
        val item = getItem(position)
        if (item.received == true) {
            val cb = holder.itemView.findViewById < CheckBox > (R.id.receiveCheckBox)
            cb.isChecked = true
            cb.isEnabled = false
            cb.text = "Received"
        }
        holder.bind(item)
    }
}