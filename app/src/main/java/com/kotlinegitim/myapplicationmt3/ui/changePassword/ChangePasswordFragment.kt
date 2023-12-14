package com.kotlinegitim.myapplicationmt3.ui.changePassword

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentChangePasswordBinding
import com.kotlinegitim.myapplicationmt3.databinding.FragmentSettingsBinding
import com.kotlinegitim.myapplicationmt3.ui.settings.SettingsViewModel

class ChangePasswordFragment : Fragment() {

    private lateinit var _binding: FragmentChangePasswordBinding
    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    companion object {
        fun newInstance() = ChangePasswordFragment()
    }

    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        changePasswordViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(ChangePasswordViewModel::class.java)
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

    return root
    }


}