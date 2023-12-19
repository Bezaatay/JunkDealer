package com.kotlinegitim.myapplicationmt3.ui.shopping

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentHomeBinding
import com.kotlinegitim.myapplicationmt3.databinding.FragmentShoppingBinding
import com.kotlinegitim.myapplicationmt3.ui.home.HomeViewModel

class ShoppingFragment : Fragment() {

    private lateinit var _binding : FragmentShoppingBinding

    private lateinit var shoppingViewModel: ShoppingViewModel

    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoppingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ShoppingViewModel::class.java]
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)


        return binding.root
    }
}