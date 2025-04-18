package com.example.nournet.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.databinding.FragmentAboutUsBinding

@AndroidEntryPoint
class AboutUsFragment : DialogFragment() {
    private lateinit var binding : FragmentAboutUsBinding
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}