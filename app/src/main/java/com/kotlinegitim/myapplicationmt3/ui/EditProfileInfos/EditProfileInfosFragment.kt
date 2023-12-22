package com.kotlinegitim.myapplicationmt3.ui.EditProfileInfos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentEditProfileInfosBinding

class EditProfileInfosFragment : Fragment() {

    private lateinit var _binding: FragmentEditProfileInfosBinding
    private lateinit var editProfileInfosViewModel: EditProfileInfosViewModel
    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->

        editProfileInfosViewModel.locationLiveData.observe(viewLifecycleOwner) {
            if (it != null) {

                val MyHome = it
                googleMap.addMarker(MarkerOptions().position(MyHome).title("Home"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyHome, 10f))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editProfileInfosViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[EditProfileInfosViewModel::class.java]

        _binding = FragmentEditProfileInfosBinding.inflate(inflater, container, false)

        editProfileInfosViewModel.getInformations(binding.editTextusername,binding.editTextMail,binding.editTextpassword)
        editProfileInfosViewModel.getLocation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSaveChanges.setOnClickListener {
            val usernameText = binding.editTextusername.text.toString()
            val mailText = binding.editTextMail.text.toString()
            val passwordText = binding.editTextpassword.text.toString()

            editProfileInfosViewModel.SaveChanges(usernameText, mailText, passwordText)
        }

        editProfileInfosViewModel._isChange.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_editProfileInfosFragment_to_personelInfosFragment)
            }
        }
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding

    }
}

