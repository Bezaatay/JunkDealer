package com.kotlinegitim.myapplicationmt3.ui.Item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentItemBinding

class ItemFragment : Fragment(){

    private var _binding : FragmentItemBinding?= null
    private lateinit var itemViewModel: ItemViewModel

    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private fun showConfirmationDialog(productPrize: String, category: String, photoUrl: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("%5 İndirim müjdesi")
            .setMessage("Satın almak istediğiniz ürünü EskiciCüzdan ile daha ucuza ödeyin!")
            .setPositiveButton("Tamam") { _, _ ->

                findNavController().navigate(R.id.action_itemFragment_to_paymentFragment, Bundle().apply {
                    putString("prize",productPrize)
                    putString("url",photoUrl)
                    putString("category",category)
                })
            }
            .show()
    }
    private val callback = OnMapReadyCallback { googleMap ->

        itemViewModel.locationLiveData.observe(viewLifecycleOwner){
            if (it != null) {

                val sellerHome = it
                googleMap.addMarker(MarkerOptions().position(sellerHome).title("Home"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sellerHome,10f))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ItemViewModel::class.java]
        _binding = FragmentItemBinding.inflate(inflater, container, false)

        binding.urltxt.text = requireArguments().getString("url").toString()
        val url = binding.urltxt.text

        val productPhoto  : ImageView = binding.productPhoto
        val sellerPhoto : ImageView = binding.sellerPhoto
        val sellerUsername = binding.sellerUsername
        val productCategories = binding.productCategories
        val productDescription = binding.productDescription
        val productPrize = binding.productPrize

        itemViewModel.getItemProperties(
             url,
             productPhoto,
             sellerUsername,
             productCategories,
             productDescription,
             productPrize,
             sellerPhoto)

        binding.buyBtn.setOnClickListener {
            val prize = productPrize.text.toString()
            val category = productCategories.text.toString()
            val photoUrl = url.toString()
            showConfirmationDialog(prize, category, photoUrl)
        }

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}