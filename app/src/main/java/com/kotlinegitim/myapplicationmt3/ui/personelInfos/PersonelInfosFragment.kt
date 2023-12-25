package com.kotlinegitim.myapplicationmt3.ui.personelInfos

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentPersonelInfosBinding

class PersonelInfosFragment : Fragment() {

    private lateinit var _binding : FragmentPersonelInfosBinding
    private lateinit var personelInfosViewModel : PersonelInfosViewModel

    private lateinit var auth : FirebaseAuth
    private val storage = Firebase.storage

    private val binding get() = _binding

    private val callback = OnMapReadyCallback { googleMap ->

        personelInfosViewModel.locationLiveData.observe(viewLifecycleOwner){
            if (it != null) {

                val MyHome = it
                googleMap.addMarker(MarkerOptions().position(MyHome).title("Home"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyHome,17f))
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        personelInfosViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[PersonelInfosViewModel::class.java]

        _binding = FragmentPersonelInfosBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        personelInfosViewModel.getInformations(binding.editTextusername,binding.editTextMail,binding.editTextpassword)
        personelInfosViewModel.getLocation()

        val currentUser = auth.currentUser
        val storageRef = storage.reference

        val email = currentUser?.email
        val profileImageView: ImageView = binding.imageViewProfilePhoto

        storageRef.child("users/$email/profile.jpg").downloadUrl
            .addOnSuccessListener { uri ->
            Glide.with(this).load(uri).into(profileImageView)
        }
            .addOnFailureListener { exception ->
                Log.e("GlideError", "Fotoğraf yüklenirken hata oluştu: ${exception.message}")
            }

        binding.imageViewEdit.setOnClickListener {
            findNavController().navigate(R.id.action_personelInfosFragment_to_editProfileInfosFragment)
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}