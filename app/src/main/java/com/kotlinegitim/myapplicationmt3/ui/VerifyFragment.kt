package com.kotlinegitim.myapplicationmt3.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotlinegitim.myapplicationmt3.databinding.FragmentVerifyBinding


class VerifyFragment : Fragment() {

    private lateinit var _binding: FragmentVerifyBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerifyBinding.inflate(inflater, container, false)

        return binding.root
    }
}