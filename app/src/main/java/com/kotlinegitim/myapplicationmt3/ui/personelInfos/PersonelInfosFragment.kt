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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentPersonelInfosBinding

class PersonelInfosFragment : Fragment() {

    private lateinit var _binding : FragmentPersonelInfosBinding
    private lateinit var personelInfosViewModel : PersonelInfosViewModel
    private lateinit var auth : FirebaseAuth
    val storage = Firebase.storage
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        personelInfosViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )
            .get(PersonelInfosViewModel::class.java)

        _binding = FragmentPersonelInfosBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val root: View = binding.root

        personelInfosViewModel.getMailandUsername(binding.editTextusername,binding.editTextMail)
        personelInfosViewModel.getPassword(binding.editTextpassword)

        val  currentUser = auth.currentUser
        val storageRef = storage.reference

            val email = currentUser?.email
            val profileImageView: ImageView = binding.imageViewProfilePhoto

            storageRef.child("users/$email/profile.jpg").downloadUrl.addOnSuccessListener { uri ->
                // Glide ile URL'yi ImageView'e yükle
                Glide.with(this).load(uri).into(profileImageView)
            }
                .addOnFailureListener { exception ->
                    Log.e("GlideError", "Fotoğraf yüklenirken hata oluştu: ${exception.message}")
                }

        binding.imageViewEdit.setOnClickListener {
            findNavController().navigate(R.id.action_personelInfosFragment_to_editProfileInfosFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}