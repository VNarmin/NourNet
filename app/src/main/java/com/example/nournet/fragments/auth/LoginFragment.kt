package com.example.nournet.fragments.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nournet.R
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.databinding.FragmentLoginBinding
import com.example.nournet.fragments.auth.viewmodel.LoginViewModel
import com.example.nournet.utils.CheckInternet
import com.example.nournet.utils.Response

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val viewModel : LoginViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.forgotPasswordTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailInputLayout.editText?.text.toString()
            val password = binding.passwordInputLayout.editText?.text.toString()

            when {
                email.isEmpty() -> {
                    binding.emailInputLayout.error = "Email Required!!"
                    binding.emailInputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.passwordInputLayout.error = "Password Required!!"
                    binding.passwordInputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.emailInputLayout.error = "Invalid Email Format!!"
                    binding.emailInputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                else -> {
                    binding.emailInputLayout.isErrorEnabled = false
                    binding.passwordInputLayout.isErrorEnabled = false
                    binding.btnLogin.isEnabled = false

                    if (CheckInternet.isConnected(requireActivity())) {
                        binding.emailInputLayout.isEnabled = false
                        binding.passwordInputLayout.isEnabled = false
                        binding.btnLogin.isEnabled = false
                        binding.btnLogin.text = "Loading..."
                        viewModel.login(email, password)
                        viewModel.loginRequest.observe(viewLifecycleOwner) { response ->
                            when (response) {
                                is Response.Loading -> {
                                    binding.progressCircular.isVisible = true
                                }
                                is Response.Error -> {
                                    binding.progressCircular.isVisible = false
                                    binding.emailInputLayout.isEnabled = true
                                    binding.passwordInputLayout.isEnabled = true
                                    Toast.makeText(
                                        requireContext(),
                                        response.errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Response.Success -> {
                                    binding.progressCircular.isVisible = false
                                    val result = response.data
                                    val sharedPref = requireActivity()
                                        .getSharedPreferences("userType", Context.MODE_PRIVATE)
                                    val editor = sharedPref.edit()
                                    editor.putString("user_type", result)
                                    editor.apply()

                                    when (result) {
                                        "Organization" -> {
                                            findNavController()
                                                .navigate(R.id.action_loginFragment_to_homeFragment)
                                            Toast.makeText(
                                                requireContext(),
                                                "Logged in as Organization",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        "Restaurant" -> {
                                            findNavController()
                                                .navigate(R.id.action_loginFragment_to_donorsHomeFragment)
                                            Toast.makeText(
                                                requireContext(),
                                                "Logged in as Restaurant",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        "Admin" -> {
                                            findNavController()
                                                .navigate(R.id.action_loginFragment_to_adminHomeFragment)
                                            Toast.makeText(
                                                requireContext(),
                                                "Logged in as Admin",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        else -> {
                                            Toast.makeText(
                                                requireContext(),
                                                "You are not registered yet, or an error occurred.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                }
                            }
                        }
                    }
                    else {
                        Toast.makeText(
                            activity,
                            "No Internet Connection!!",
                            Toast.LENGTH_SHORT).show()
                        binding.progressCircular.isVisible = false
                        binding.btnLogin.isEnabled = true
                        binding.btnLogin.text = "LOGIN"
                    }
                }
            }
        }
        return view
    }
}