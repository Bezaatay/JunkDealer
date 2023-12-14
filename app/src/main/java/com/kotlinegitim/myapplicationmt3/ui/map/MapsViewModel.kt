package com.kotlinegitim.myapplicationmt3.ui.map
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MapsViewModel : ViewModel() {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db = FirebaseFirestore.getInstance()
    private val user = firebaseAuth.currentUser
    private val uid = user?.uid

    // "users" koleksiyonunun altındaki "uid" dokümanına gidin
    private val userDocRef = uid?.let { db.collection("users").document(it) }

    // "location" koleksiyonunu oluşturun veya varsa alın
    private val locationCollectionRef = userDocRef?.collection("location")

    fun saveLatLng(latitude: Double, longitude: Double, time: Long) {
        val latLngValues = hashMapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "time" to time
        )
        locationCollectionRef?.add(latLngValues)
            ?.addOnSuccessListener {
                Log.e("success","depolama basairili")
            }
            ?.addOnFailureListener {
                Log.e("success","depolama basarisiz")
            }
    }
}
