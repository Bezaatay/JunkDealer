package com.kotlinegitim.myapplicationmt3.ui.profile


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kotlinegitim.myapplicationmt3.ui.activity.LoginSigninActivity
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentProfileBinding

class ProfileFragment() : Fragment() {

    private lateinit var _binding: FragmentProfileBinding
    private lateinit var profileViewModel: ProfileViewModel
    lateinit var downloadUri : Uri
    private val binding get() = _binding
    val storage = Firebase.storage
    private lateinit var auth: FirebaseAuth

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Kullanıcıya galeriye erişim izni istemek için izin kodu
    private val READ_EXTERNAL_STORAGE_PERMISSION = 123
    private val PICK_IMAGE_REQUEST = 123
    // İstenilen izinlerin sonuçları için onRequestPermissionsResult metodunu kontrol edin
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildi, galeriye erişim sağlanabilir
                openGallery()
            } else {
                // İzin reddedildi, galeriye erişim sağlanamaz
                Toast.makeText(requireContext(), "Galeriye erişim izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }
   // Galeri seçim sonucu için onActivityResult metodunu kontrol edin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data

            // Glide ile seçilen görseli ImageView'e yükleme

            Glide.with(this).load(selectedImageUri).into(binding.imageView)

            // Seçilen görseli Firebase Storage'a kaydetme
            val user = FirebaseAuth.getInstance().currentUser
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("users/${user?.email}/profile.jpg")

            val uploadTask = imageRef.putFile(selectedImageUri!!)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                // Görselin indirilebilir URL'sini alma
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadUri = task.result
                    Log.e("alındı","$downloadUri")
                    // Görselin indirilebilir URL'sini alınca yapılacak işlemler
                    // Örneğin, Firestore'a kaydetme gibi
                    // downloadUri.toString() kullanarak URL'yi alabilirsiniz
                } else {
                    Log.e("görsel","yüklenemedii")
                    // Görselin yüklenemediği durumlar için hata işlemleri
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ProfileViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        val  currentUser = auth.currentUser
        val storageRef = storage.reference

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val root: View = binding.root

        profileViewModel.getUsername(binding.usernameID)
        profileViewModel.getMoney(binding.myPurse)

        binding.logoutID.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), LoginSigninActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        binding.informationID.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_personelInfosFragment)
        }
        binding.settingsID.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_settingsFragment)
        }
        binding.myPurse.setOnClickListener {
            findNavController().navigate(R.id. action_navigation_profile_to_myPurseFragment)
        }
        binding.myPurseView.setOnClickListener {
            findNavController().navigate(R.id. action_navigation_profile_to_myPurseFragment)
        }


        //önceden profil potoğrafı yüklenmişse glide ile fotoğrafı getirme
        val email = currentUser?.email
        val profileImageView: ImageView = binding.imageView
        val imagePath = "users/$email/profile.jpg"

        storageRef.child(imagePath).downloadUrl.addOnSuccessListener {
            // Glide ile URL'yi ImageView'e yükle

                Glide.with(this).load(it).into(profileImageView)

        }
            .addOnFailureListener { exception ->
            Log.e("GlideError", "Fotoğraf yüklenirken hata oluştu: ${exception.message}")
        }

        binding.imageView.setOnClickListener {
            openGallery()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}