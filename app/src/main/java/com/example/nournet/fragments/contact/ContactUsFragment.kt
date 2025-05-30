package com.example.nournet.fragments.contact

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.databinding.FragmentContactUsBinding


@AndroidEntryPoint
class ContactUsFragment : Fragment() {
    private lateinit var binding : FragmentContactUsBinding
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)
        val view = binding.root

        val description = binding.descriptionError.editText?.text.toString().trim()

        binding.submit.setOnClickListener {
           when {
               binding.nameError.editText?.text.toString().isEmpty() -> {
                   binding.nameError.error = "Username Required!"
                   binding.nameError.isErrorEnabled = true
               }
               binding.emailError.editText?.text.toString().isEmpty() -> {
                   binding.emailError.error = "Email Required!"
                   binding.emailError.isErrorEnabled = true
                   return@setOnClickListener
               }
               binding.descriptionError.editText?.text.toString().isEmpty() -> {
                   binding.descriptionError.error = "Required"
                   binding.descriptionError.isErrorEnabled = true
                   return@setOnClickListener
               }
               else -> {
                       binding.nameError.isErrorEnabled = false
                       binding.emailError.isErrorEnabled = false
                       binding.descriptionError.isErrorEnabled = false
                   val emailIntent = Intent(
                       Intent.ACTION_SEND
                   )
                   emailIntent.action = Intent.ACTION_SEND
                   emailIntent.type = "message/rfc822"
                   emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("leencelidonde@gmail.com"))
                   emailIntent.putExtra(Intent.EXTRA_CC, "")
                   emailIntent.putExtra(Intent.EXTRA_BCC, "")
                   emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from NourNet")
                   emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n Message : \n $description")
                   emailIntent.type = "text/html"
                   startActivity(Intent.createChooser(emailIntent, "Send Mail Using : "))
               }
           }

        }
        return view
    }
}