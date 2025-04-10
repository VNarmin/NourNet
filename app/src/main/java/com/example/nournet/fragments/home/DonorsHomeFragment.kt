package com.example.nournet.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.R
import com.example.nournet.databinding.FragmentDonorsHomeBinding
import javax.inject.Inject

@AndroidEntryPoint
class DonorsHomeFragment : Fragment() {
    private lateinit var binding : FragmentDonorsHomeBinding
    @Inject lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentDonorsHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as AppCompatActivity).supportActionBar?.show()
        binding.cardLogout.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_donorsHomeFragment_to_loginFragment)
        }
        binding.cardDonate.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_donateFragment)
        }
        binding.cardHistory.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_historyFragment)
        }
        binding.cardAboutus.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_aboutUsFragment)
        }
        binding.cardContact.setOnClickListener {
            findNavController().navigate(R.id.action_donorsHomeFragment_to_contactUsFragment)
        }
        return view
    }
}