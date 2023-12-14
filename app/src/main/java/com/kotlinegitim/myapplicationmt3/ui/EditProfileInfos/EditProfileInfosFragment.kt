package com.kotlinegitim.myapplicationmt3.ui.EditProfileInfos

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentEditProfileInfosBinding
import com.kotlinegitim.myapplicationmt3.ui.activity.MainActivity

class EditProfileInfosFragment : Fragment() {

    private lateinit var _binding : FragmentEditProfileInfosBinding
    private lateinit var editProfileInfosViewModel: EditProfileInfosViewModel
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        editProfileInfosViewModel =  ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(EditProfileInfosViewModel::class.java)

        _binding = FragmentEditProfileInfosBinding.inflate(inflater, container, false)

        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSaveChanges.setOnClickListener {
            val UsernameText = binding.editTextusername.text.toString()
            val MailText = binding.editTextMail.text.toString()
            val PasswordText = binding.editTextpassword.text.toString()

            editProfileInfosViewModel.SaveChanges(UsernameText,MailText,PasswordText)
        }

        editProfileInfosViewModel._isChange.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_editProfileInfosFragment_to_personelInfosFragment)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}

