package com.kotlinegitim.myapplicationmt3.ui.personelInfos

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
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

   fun getMailandUsername(usernamehint: TextView,mailhint: TextView,){
      if (currentUserUid != null) {
         db.collection("users")
            .document(currentUserUid)
            .get()
            .addOnSuccessListener { document ->
               if (document != null) {
                  val email = document.getString("email")
                  val username = document.getString("username")
                  mailhint.hint=email.toString()
                  usernamehint.hint=username.toString()
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

   fun getPassword(password: TextView){
      if (currentUserUid != null) {
         db.collection("users")
            .document(currentUserUid)
            .get()
            .addOnSuccessListener { document ->
               if (document != null) {
                  val passwordDB = document.getString("password")
                  password.hint= passwordDB.toString()
                  println("Current username: $password")
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
   }
