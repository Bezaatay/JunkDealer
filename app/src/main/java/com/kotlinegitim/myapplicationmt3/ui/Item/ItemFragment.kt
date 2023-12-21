package com.kotlinegitim.myapplicationmt3.ui.Item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

        val productLat = binding.latitudetxt.text.toString()
        val productLng = binding.longitudetxt.text.toString()
        itemViewModel.getLocation(productLat,productLng)

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

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}