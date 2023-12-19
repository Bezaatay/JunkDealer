package com.kotlinegitim.myapplicationmt3.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var _binding: FragmentSettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val binding get() = _binding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SettingsViewModel::class.java)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

}