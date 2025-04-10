package com.example.nournet.fragments.receive

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
import com.example.nournet.adapter.ReceiveAdapter
import com.example.nournet.databinding.FragmentReceiveBinding
import com.example.nournet.utils.Response
import com.example.nournet.viewmodel.DonationsViewModel

@AndroidEntryPoint
class ReceiveFragment : Fragment() {
    private lateinit var binding : FragmentReceiveBinding
    private val adapter : ReceiveAdapter by lazy { ReceiveAdapter() }
    private val viewModel : DonationsViewModel by viewModels()

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentReceiveBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.receiveRv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getDonations()
        }

        viewModel.donations.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Response.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Response.Success -> {
                    binding.progressBar.isVisible = false
                    adapter.submitList(state.data)
                }
                is Response.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }
}