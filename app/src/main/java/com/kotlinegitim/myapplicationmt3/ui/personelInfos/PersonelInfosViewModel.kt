package com.kotlinegitim.myapplicationmt3.ui.personelInfos

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PersonelInfosViewModel : ViewModel() {
   val db = FirebaseFirestore.getInstance()
   private val firebaseAuth: FirebaseAuth by lazy {
      FirebaseAuth.getInstance()
   }
   private val currentUserUid=firebaseAuth.currentUser?.uid

   private val _locationLiveData = MutableLiveData<LatLng>()
   val locationLiveData: LiveData<LatLng> get() = _locationLiveData

   fun getInformations(usernamehint: TextView,mailhint: TextView,passwordhint: TextView){
      if (currentUserUid != null) {
         db.collection("users")
            .document(currentUserUid)
            .get()
            .addOnSuccessListener { document ->
               if (document != null) {
                  val email = document.getString("email")
                  val username = document.getString("username")
                  val password = document.getString("password")
                  mailhint.hint=email.toString()
                  usernamehint.hint=username.toString()
                  passwordhint.hint=password.toString()
                  if (username != null || email != null) {
                     // Kullanıcı adını kullanarak işlemlerinizi gerçekleştirin
                     println("Current username: $username")
                  }
               } else {
                  println("Belirtilen UID ile ilgili belge bulunamadı.")
               }
            }
            .addOnFailureListener { exception ->
               println("Firestore'dan veri alınamadı: $exception")
            }
      } else {
         println("Oturum açmış bir kullanıcı bulunamadı.")
      }
   }
   fun getLocation() {
      if(currentUserUid != null) {
         db.collection("users").document(currentUserUid).collection("location").document(currentUserUid+"location")
            .get().addOnSuccessListener { documentSnapshot ->
               val latitude = documentSnapshot.getDouble("latitude")
               val longitude = documentSnapshot.getDouble("longitude")

               if (latitude != null && longitude != null) {
                  // LiveData'ya yeni bir LatLng nesnesi gönderme
                  _locationLiveData.value = LatLng(latitude, longitude)
               } else {
                  // Eğer latitude veya longitude değeri null ise, hatayı log'a yazabilirsiniz
                  Log.e("PersonelInfosViewModel", "Latitude veya longitude değeri null.")
               }
            }
      }
   }
   }
