package com.example.nournet.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.R
import com.example.nournet.adapter.AllDonations
import com.example.nournet.adapter.AllUsers
import com.example.nournet.databinding.FragmentAdminHomeBinding
import com.example.nournet.utils.Response
import javax.inject.Inject

@AndroidEntryPoint
class AdminHomeFragment : Fragment(), MenuProvider {
    private lateinit var binding : FragmentAdminHomeBinding
    private val viewModel by viewModels<AdminHomeViewModel>()
    private val instance = this
    private val usersAdapter by lazy { AllUsers(instance) }
    private val donationsAdapter by lazy { AllDonations(instance) }
    @Inject lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        requireActivity().addMenuProvider(this, viewLifecycleOwner )
        (activity as AppCompatActivity).supportActionBar?.show()
        binding.allUsers.adapter = usersAdapter
        binding.allDonations.adapter = donationsAdapter
        fetchData()
        return view
    }

     @SuppressLint("SetTextI18n")
     fun fetchData() {
        viewModel.getAllUsers()
        viewModel.getAllDonations()
        viewModel.getAllUsersTotalNumber()
        viewModel.getTotalDonations()
        viewModel.getAllDonorsTotalNumber()

        viewModel.totalDonors.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.totalDonors.text = response.data.toString()
                }
                is Response.Error -> {
                    Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Response.Loading -> { }
            }
        }

        viewModel.totalDonations.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.totalDonations.text = response.data.toString()
                }
                is Response.Error -> {
                    Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Response.Loading -> { }
            }
        }

        viewModel.totalUsers.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.totalUsers.text = response.data.toString()
                }
                is Response.Error -> {
                    Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Response.Loading -> { }
            }
        }

        viewModel.allDonations.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    donationsAdapter.submitList(response.data)
                    binding.progressBarRV2.visibility = View.GONE
                }
                is Response.Error -> {
                    Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressBarRV2.visibility = View.GONE
                }
                is Response.Loading -> {
                    binding.progressBarRV2.visibility = View.VISIBLE
                }
            }
        }

        viewModel.users.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    usersAdapter.submitList(response.data)
                    binding.progressBarRV.visibility = View.GONE
                }
                is Response.Error -> {
                    Toast.makeText(requireContext(), response.errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressBarRV.visibility = View.GONE
                }
                is Response.Loading -> {
                    binding.progressBarRV.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateMenu(menu : Menu, menuInflater : MenuInflater) {
        menuInflater.inflate(R.menu.admin_menu, menu)
        menu.findItem(R.id.action_logout).setOnMenuItemClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_adminHomeFragment_to_loginFragment)
            true
        }
    }

    override fun onMenuItemSelected(menuItem : MenuItem) : Boolean {
        when (menuItem.itemId) {
            R.id.action_logout -> {
                auth.signOut()
                findNavController().navigate(R.id.action_adminHomeFragment_to_loginFragment)
            }
        }
        return true
    }
}