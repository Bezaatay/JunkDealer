package com.kotlinegitim.myapplicationmt3.ui.profile

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {

    var _isVerify : MutableLiveData<Boolean> = MutableLiveData(false)

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db = FirebaseFirestore.getInstance()
    private val uid = firebaseAuth.currentUser?.uid.toString()
    private val storageRef = FirebaseStorage.getInstance().reference
    val  currentUser = firebaseAuth.currentUser

    fun checkEmailVerifiedOrNot() {
        val currentUserverified = firebaseAuth.currentUser!!.isEmailVerified
        Log.e("verified","$currentUserverified")

      /*  // Kullanıcı oturum açmışsa ve e-posta doğrulama işlemi tamamlandıysa
        if (currentUser != null && currentUser.isEmailVerified) {
            _isVerify.value = true
        } else {
            // Eğer oturum açmış bir kullanıcı yoksa veya e-posta doğrulama işlemi tamamlanmadıysa
            // E-posta doğrulama işleminin tamamlanması için bekleyin ve tekrar kontrol edin
            Handler(Looper.getMainLooper()).postDelayed({
                checkEmailVerifiedOrNot()
            }, 5000)  // 5 saniye bekleyip tekrar kontrol et, bu süreyi ihtiyaca göre ayarlayabilirsiniz
        }*/
    }

    fun verifyMail(){
        currentUser!!.sendEmailVerification().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("email yollandı","please check email for verification")
            }else{
                Log.e("email yollanamadı","please check email for verification")
            }
        }.addOnFailureListener {
            Log.e("email","email yollanamadı")
        }
    }
    fun createMoneyDb(myPurse: TextView) {
        val userDocRef = uid.let { db.collection("users").document(it) }
        val moneyCollectionRef = userDocRef.collection("money")

        moneyCollectionRef.document(uid + "money").get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Belge zaten var, işlem yapmaya gerek yok
                    Log.e("createMoneyDb", "Belge zaten var.")
                    getMoney(myPurse)
                } else {
                    // Belge yok, oluştur
                    moneyCollectionRef.document(uid + "money").set(mapOf("money" to 0.0))
                        .addOnSuccessListener {
                            Log.e("createMoneyDb", "Belge oluşturma başarılı.")
                            getMoney(myPurse)
                        }
                        .addOnFailureListener {
                            Log.e("createMoneyDb", "Belge oluşturma başarısız.")
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("createMoneyDb", "Belge varlığı kontrol edilirken hata: $exception")
            }
    }

    private fun getMoney(coin: TextView) {
        db.collection("users").document(uid).collection("money").document(uid+"money")
            .get().addOnSuccessListener {
                val money = it.getDouble("money").toString()
                coin.text = "$money TL"
            }
    }
    fun getProfilePhoto(imageView: ImageView) {
        // Önceden profil fotoğrafı yüklenmişse Glide ile fotoğrafı getirme
        val email = currentUser?.email
        val imagePath = "users/$email/profile.jpg"

        storageRef.child(imagePath).downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()

            Glide.with(imageView.context)
                .load(imageUrl)
                .into(imageView)
        }
            .addOnFailureListener { exception ->
                Log.e("GlideError", "Fotoğraf yüklenirken hata oluştu: ${exception.message}")
            }
    }

    fun getUsername(usernameText: TextView) {
            val currentUserUid = firebaseAuth.currentUser?.uid
            if (currentUserUid != null) {
                db.collection("users")
                    .document(currentUserUid)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val username = document.getString("username")
                            usernameText.text = username.toString()
                            if (username != null) {
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
    }