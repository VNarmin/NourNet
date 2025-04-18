package com.example.nournet.fragments.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.databinding.FragmentSplashBinding
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding
    @Inject lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root

        val user = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (user != null) {
                val sharedPrefs = requireActivity().getSharedPreferences("userType", Context.MODE_PRIVATE)
                val userType = sharedPrefs.getString("user_type", null)
                val action = when (userType) {
                    "Admin" -> SplashFragmentDirections.actionSplashFragmentToAdminHomeFragment()
                    "Organization" -> SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                    "Restaurant" -> SplashFragmentDirections.actionSplashFragmentToDonorsHomeFragment()
                    else -> SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                }
                findNavController().navigate(action)
            } else {
                val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }, 2000)
        return view
    }
}