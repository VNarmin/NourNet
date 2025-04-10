package com.example.nournet.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.databinding.FragmentResetPasswordBinding
import com.example.nournet.utils.CheckInternet

@AndroidEntryPoint
class ResetPasswordFragment : DialogFragment() {
    private lateinit var binding : FragmentResetPasswordBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()

        binding.dialogConfirm.setOnClickListener {
            val email = binding.userEmil.editText?.text.toString().trim()

            if (email.isEmpty()) {
                binding.userEmil.error = "Enter your email!"
                binding.userEmil.requestFocus()
                return@setOnClickListener
            }
            else {
                binding.progressBar6.isVisible = true
                auth.sendPasswordResetEmail(email)
                    .addOnCanceledListener {
                        binding.progressBar6.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnCompleteListener { response ->
                        if (response.isSuccessful && CheckInternet.isConnected(requireContext())) {
                            binding.progressBar6.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                "A reset link has been sent to your email.\nCheck your inbox.",
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                        else {
                            binding.progressBar6.isVisible = false
                            Toast.makeText(
                                requireContext(),
                                "Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { error ->
                        binding.progressBar6.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
        return view
    }
}