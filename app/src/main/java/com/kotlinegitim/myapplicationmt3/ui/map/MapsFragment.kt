package com.kotlinegitim.myapplicationmt3.ui.map

import android.content.Intent
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentMapsBinding
import com.kotlinegitim.myapplicationmt3.ui.activity.MainActivity

class MapsFragment : Fragment() {
    private lateinit var _binding: FragmentMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    private val binding get() = _binding
    private var isMarkerAdded = false

    private val callback = OnMapReadyCallback { googleMap ->
        // Önceki kodu buraya ekleyin
        googleMap.setOnMapClickListener { point ->
            if (!isMarkerAdded) {
                // LatLng'ten Location'a dönüşüm
                val location = Location("Test")
                location.latitude = point.latitude
                location.longitude = point.longitude
                location.time = java.util.Date().time
                Log.e("latlng", "${location.latitude} and ${location.longitude} and ${location.time}")

                // Location'dan LatLng'e dönüşüm
                val newLatLng = LatLng(location.latitude, location.longitude)

                // İşaretçi (marker) oluşturulması
                val markerOptions = MarkerOptions()
                    .position(newLatLng)
                    .title(newLatLng.toString())

                // Haritaya işaretçiyi ekleme
                googleMap.addMarker(markerOptions)

                // Haritayı tıklanan konuma ve belirli bir zoom düzeyine hareket ettirme
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLng, 17f))

                mapsViewModel.saveLatLng(location.latitude,location.longitude,location.time)

                // Bayrağı güncelle
                isMarkerAdded = true

                // SetOnMapClickListener'ı kaldır, böylece bir daha tıklanamaz.
                googleMap.setOnMapClickListener(null)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MapsViewModel::class.java]

        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}