package com.example.nournet.fragments.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.adapter.HistoryAdapter
import com.example.nournet.databinding.FragmentHistoryBinding
import com.example.nournet.utils.Response
import com.example.nournet.fragments.donations.viewmodel.DonationsViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBinding
    private val viewModel by viewModels<DonationsViewModel>()
    private val adapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.receiveRV.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getHistory()
        }
        viewModel.history.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(response.data)
                }
                is Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Response.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.errorMessage.let { message ->
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }
}