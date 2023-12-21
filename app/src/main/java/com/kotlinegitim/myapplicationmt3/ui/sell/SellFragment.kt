package com.kotlinegitim.myapplicationmt3.ui.sell

import android.app.Activity
import android.app.AlertDialog
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentSellBinding

class SellFragment : Fragment() {

    private lateinit var _binding: FragmentSellBinding
    private lateinit var sellViewModel: SellViewModel
    private lateinit var auth: FirebaseAuth
    var selectedImageUri : Uri? = null
    var selectedBitmap : Bitmap? = null
    private val ListOfProducts = ArrayList<String>()
    private lateinit var dataAdapter : ArrayAdapter<String>


    // Kullanıcıya galeriye erişim izni istemek için izin kodu
    private val READ_EXTERNAL_STORAGE_PERMISSION = 123
    private val PICK_IMAGE_REQUEST = 123
    private val binding get() = _binding

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Onay")
            .setMessage("Ürün başarılı şekilde yüklendi.")
            .show()
        findNavController().navigate(R.id.action_navigation_sell_to_navigation_home)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sellViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SellViewModel::class.java]
        _binding = FragmentSellBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        sellViewModel._isUpload.observe(viewLifecycleOwner) {
            if (it) {
               showConfirmationDialog()
            }
        }

        ListOfProducts.add("ayakkabi")
        ListOfProducts.add("pijama")
        ListOfProducts.add("kazak")
        ListOfProducts.add("pantolon")
        ListOfProducts.add("sapka")
        ListOfProducts.add("buzdolabi")
        ListOfProducts.add("firin")
        ListOfProducts.add("camasir makinesi")
        ListOfProducts.add("sac kurutma makinesi")

        dataAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,android.R.id.text1,ListOfProducts)

        binding.spinner.adapter = dataAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val product_categories = ListOfProducts[p2]
               binding.textView11.text = product_categories
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        sellViewModel.getMail(binding.mailtxt)
        sellViewModel.getLocation(binding.latitudeValue,binding.longitudeValue)

        binding.UploadProductBtn.setOnClickListener {
            val ProductCategories=binding.textView11.text.toString()
            val ProductDescription= binding.editTextProductDescription.text.toString()
            val ProductPrize= binding.editTextProductPrize.text.toString()
            val SellerMail = binding.mailtxt.text.toString()
            val ProductLocationLatitude=binding.latitudeValue.text.toString()
            val ProductLocationLongitude=binding.longitudeValue.text.toString()

            sellViewModel.uploadUrl(selectedImageUri,
                ProductCategories,
                ProductDescription,
                ProductPrize,
                SellerMail,
                ProductLocationLatitude,
                ProductLocationLongitude)
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
            Log.e("uri","$selectedImageUri")

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
