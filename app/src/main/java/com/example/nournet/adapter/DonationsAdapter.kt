package com.example.nournet.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nournet.R
import com.example.nournet.databinding.DonationsRowLayoutBinding
import com.example.nournet.fragments.donations.DonationsFragmentDirections
import com.example.nournet.model.Donation

class DonationsAdapter : ListAdapter < Donation, DonationsAdapter.DonationsViewHolder > (COMPARATOR) {
    class DonationsViewHolder(private var binding : DonationsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(donation : Donation?) {
            binding.donatedFoodItem.text = donation?.foodItem
            binding.donatedFoodItemDescription.text = donation?.description
            binding.donorPhoneNumber.setOnClickListener {
                val phoneNumber = donation?.phoneNumber
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel : $phoneNumber")
                binding.root.context.startActivity(intent)
            }

            binding.donorLocation.setOnClickListener {
                val action =
                    DonationsFragmentDirections.actionDonationsFragmentToDonorLocationFragment(donation!!)
                binding.root.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : DonationsViewHolder {
        return DonationsViewHolder(
            DonationsRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder : DonationsViewHolder, position : Int) {
        val item = getItem(position)
        if (item.received == true) {
            val tv = holder.itemView.findViewById < TextView > (R.id.receiveTextView)
            tv.text = "Received"
            val phoneIcon = holder.itemView.findViewById < ImageView > (R.id.donorPhoneNumber)
            phoneIcon.isVisible = false
        }
        holder.bind(item)
    }

    object COMPARATOR : DiffUtil.ItemCallback < Donation > () {
        override fun areItemsTheSame(old : Donation, new : Donation) : Boolean {
            return old.id == new.id
        }

        override fun areContentsTheSame(old : Donation, new : Donation) : Boolean {
            return old == new
        }
    }
}