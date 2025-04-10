package com.example.nournet.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nournet.databinding.DonationItemBinding
import com.example.nournet.fragments.home.AdminHomeFragment
import com.example.nournet.model.Donation
import com.example.nournet.repository.NourNetRepositoryImpl
import com.example.nournet.utils.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllDonations(val instance : AdminHomeFragment) :
    ListAdapter < Donation, AllDonations.DonationsViewHolder > (DonationDiffCallback) {
    object DonationDiffCallback : androidx.recyclerview.widget.DiffUtil.ItemCallback < Donation > () {
        override fun areItemsTheSame(old : Donation, new : Donation) : Boolean {
            return old.donationID == new.donationID
        }

        override fun areContentsTheSame(old : Donation, new : Donation) : Boolean {
            return old == new
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : DonationsViewHolder {
        return DonationsViewHolder.from(parent)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder : DonationsViewHolder, position : Int) {
        val item = getItem(position)
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to delete this Donation?")
            builder.setTitle("Alert!")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { _ : DialogInterface?, _ : Int ->
                CoroutineScope(Dispatchers.IO).launch {
                    val itemID = item.donationID
                    NourNetRepositoryImpl.getInstance().deleteDonation(itemID!!) { response ->
                        when (response) {
                            is Response.Error -> { }
                            is Response.Success -> {
                                Toast.makeText(context, response.data, Toast.LENGTH_SHORT).show()
                                notifyDataSetChanged()
                                instance.fetchData()
                            }
                            is Response.Loading -> { }
                        }
                    }
                }
            }

            builder.setNegativeButton("No") { dialog : DialogInterface, _ : Int ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
            true
        }
        holder.bind(item)
    }

    class DonationsViewHolder private constructor(private val binding : DonationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Donation) {
            binding.donation.text = item.name
            binding.description.text = item.foodItem
            binding.phoneNumber.text = item.phoneNumber
        }

        companion object {
            fun from(parent : ViewGroup) : DonationsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DonationItemBinding.inflate(layoutInflater, parent, false)
                return DonationsViewHolder(binding)
            }
        }
    }
}
