package com.kotlinegitim.myapplicationmt3.ui.sell

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.kotlinegitim.myapplicationmt3.databinding.FragmentSellBinding

class SellFragment : Fragment() {

    private lateinit var _binding: FragmentSellBinding
    private lateinit var sellViewModel: SellViewModel
    private lateinit var auth: FirebaseAuth
    var selectedImageUri : Uri? = null
    var selectedBitmap : Bitmap? = null

    // Kullanıcıya galeriye erişim izni istemek için izin kodu
    private val READ_EXTERNAL_STORAGE_PERMISSION = 123
    private val PICK_IMAGE_REQUEST = 123
    private val binding get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sellViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(SellViewModel::class.java)
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        binding.UploadProductBtn.setOnClickListener {
            val ProductInfos= binding.editTextProductInfos.text.toString()
            val ProductDescription= binding.editTextProductDescription.text.toString()
            val ProductPrize= binding.editTextProductPrize.text.toString()

            sellViewModel.UploadUrl(selectedImageUri,ProductInfos,ProductDescription,ProductPrize)

        }
        binding.imageViewAddPhoto1.setOnClickListener {
            openGallery()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    // İstenilen izinlerin sonuçları için onRequestPermissionsResult metodunu kontrol edin
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // İzin verildi, galeriye erişim sağlanabilir
                openGallery()
            } else {
                // İzin reddedildi, galeriye erişim sağlanamaz
            }
        }
    }

    // Galeri seçim sonucu için onActivityResult metodunu kontrol edin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data

            if(selectedImageUri != null){

                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(requireActivity().contentResolver,selectedImageUri!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    binding.imageViewAddPhoto1.setImageBitmap(selectedBitmap)
                }
                else{
                    selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,selectedImageUri)
                    binding.imageViewAddPhoto1.setImageBitmap(selectedBitmap)
                }
            }
        }
    }
}
