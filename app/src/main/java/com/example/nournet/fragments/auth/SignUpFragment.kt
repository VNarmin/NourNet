package com.example.nournet.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.R
import com.example.nournet.activities.MainActivity
import com.example.nournet.model.User
import com.example.nournet.databinding.FragmentSignUpBinding
import com.example.nournet.fragments.auth.viewmodel.SignUpViewModel
import com.example.nournet.utils.CheckInternet
import com.example.nournet.utils.Response
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    @Inject lateinit var auth : FirebaseAuth
    private  val viewModel : SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as MainActivity).supportActionBar?.hide()

        binding.loginTv.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        binding.btnRegister.setOnClickListener {
            val name = binding.nameInputLayout.editText?.text.toString().trim()
            val email = binding.emailInputLayout.editText?.text.toString().trim()
            val phone = binding.phoneNumberInputLayout.editText?.text.toString().trim()
            val password = binding.passInputLayout.editText?.text.toString().trim()
            val confirmPassword = binding.confPassInputLayout.editText?.text.toString().trim()

            when {
                !(binding.btnResturant.isChecked || binding.btnOrganization.isChecked ||
                        binding.btnAdmin.isChecked) -> {
                    Toast.makeText(
                        requireContext(),
                        "User Type Required!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                name.isEmpty() -> {
                    binding.nameInputLayout.error = "Username Required!"
                    binding.nameInputLayout.isErrorEnabled = true
                }
                email.isEmpty() -> {
                    binding.emailInputLayout.error = "Email Required!"
                    binding.emailInputLayout.isErrorEnabled = true
                }
                phone.isEmpty() -> {
                    binding.phoneNumberInputLayout.error = "Phone Required!"
                    binding.phoneNumberInputLayout.isErrorEnabled = true
                }
                password.isEmpty() -> {
                    binding.passInputLayout.error = "Password Required!"
                    binding.passInputLayout.isErrorEnabled = true
                }
                confirmPassword.isEmpty() -> {
                    binding.confPassInputLayout.error = "Confirm Password Required!"
                    binding.confPassInputLayout.isErrorEnabled = true
                }
                password != confirmPassword -> {
                    binding.passInputLayout.error = "Passwords do not match!"
                    binding.confPassInputLayout.error = "Passwords do not match!"
                    binding.passInputLayout.isErrorEnabled = true
                }
                else -> {
                    binding.nameInputLayout.isErrorEnabled = false
                    binding.emailInputLayout.isErrorEnabled = false
                    binding.phoneNumberInputLayout.isErrorEnabled = false
                    binding.passInputLayout.isErrorEnabled = false
                    binding.confPassInputLayout.isErrorEnabled = false
                    binding.btnRegister.isEnabled = false

                    val selectedItemID = binding.radioGroup.checkedRadioButtonId
                    val selectedItem = binding.radioGroup.findViewById<RadioButton>(selectedItemID)
                    val userType = selectedItem.text.toString()

                    if (CheckInternet.isConnected(requireContext())) {
                        binding.progressCircular.isVisible = true
                        val user = User(email, name, phone, userType)
                        viewModel.register(email, password, user)

                        viewModel.registerRequest.observe(viewLifecycleOwner) { response ->
                            when (response) {
                                is Response.Loading -> {
                                    binding.progressCircular.isVisible = true
                                }
                                is Response.Error -> {
                                    Toast.makeText(
                                        requireContext(),
                                        response.errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                is Response.Success -> {
                                    Toast.makeText(
                                        requireContext(),
                                        response.data,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }
                            }
                        }
                    }
                }
            }
        }
        return view
    }
}