package com.example.nournet.fragments.donations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.adapter.DonationsAdapter
import com.example.nournet.databinding.FragmentDonationsBinding
import com.example.nournet.utils.Response
import com.example.nournet.viewmodel.DonationsViewModel

@AndroidEntryPoint
class DonationsFragment : Fragment() {
    private lateinit var binding : FragmentDonationsBinding
    private val viewModel : DonationsViewModel by viewModels()
    private val adapter by lazy { DonationsAdapter() }

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentDonationsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Loading -> {
                    binding.progressCircular.isVisible = true
                }
                is Response.Success -> {
                    binding.progressCircular.isVisible = false
                    adapter.submitList(state.data)
                }
                is Response.Error -> {
                    binding.progressCircular.isVisible = false
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}