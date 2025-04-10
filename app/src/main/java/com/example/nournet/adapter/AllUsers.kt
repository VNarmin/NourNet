package com.example.nournet.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nournet.databinding.UserItemBinding
import com.example.nournet.fragments.home.AdminHomeFragment
import com.example.nournet.model.User
import com.example.nournet.repository.NourNetRepositoryImpl
import com.example.nournet.utils.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllUsers(val instance : AdminHomeFragment) :
    ListAdapter < User, AllUsers.UsersViewHolder > (UsersDiffUtil) {
    class UsersViewHolder(private val binding : UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : User) {
            binding.username.text = item.name
            binding.phoneNumber.text = item.phone
        }
    }

    object UsersDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback < User > () {
        override fun areItemsTheSame(old : User, new : User) : Boolean {
            return old == new
        }

        override fun areContentsTheSame(old : User, new : User) : Boolean {
            return old.phone == new.phone
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : UsersViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder : UsersViewHolder, position : Int) {
        val item = getItem(position)
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to delete this user?")
            builder.setTitle("Alert!")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") { _ : DialogInterface?, _ : Int ->
                CoroutineScope(Dispatchers.IO).launch {
                    var userID = ""
                    NourNetRepositoryImpl.getInstance().getUserID(item.email!!) {
                        response -> userID = response
                    }
                    Log.d("UserID", userID)
                    NourNetRepositoryImpl.getInstance().deleteUser(userID) { response ->
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
}