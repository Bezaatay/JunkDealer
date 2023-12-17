package com.kotlinegitim.myapplicationmt3.ui.myPurse

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentMyPurseBinding

class MyPurseFragment : Fragment() {

    private lateinit var _binding : FragmentMyPurseBinding
    private lateinit var myPurseViewModel: MyPurseViewModel

    private val binding get() = _binding

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Onay")
            .setMessage("EskiciCüzdan'a paranız başarıyla yüklenmiştir. Güncel bakiyeniz ile alışverişe devam edebilirsiniz")
        val dialog = builder.create()

        dialog.show()

        val handler = Handler()
        val delayMillis: Long = 3000

        handler.postDelayed({
            dialog.dismiss()
        }, delayMillis)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myPurseViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MyPurseViewModel::class.java]
        _binding = FragmentMyPurseBinding.inflate(inflater, container, false)

        myPurseViewModel.getMoney(binding.coin)

        binding.loadMoney.setOnClickListener {
            myPurseViewModel.loadMoney(binding.editTextText)
            showConfirmationDialog()
            findNavController().navigate(R.id.action_myPurseFragment_to_navigation_profile)
        }

        return binding.root
    }
}