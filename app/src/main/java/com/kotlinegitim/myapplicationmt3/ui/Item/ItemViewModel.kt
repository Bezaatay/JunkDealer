package com.kotlinegitim.myapplicationmt3.ui.Item

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ItemViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val _locationLiveData = MutableLiveData<LatLng>()
    var _isFav : MutableLiveData<Boolean> = MutableLiveData(false)
    val locationLiveData: LiveData<LatLng> get() = _locationLiveData

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val currentUser = auth.currentUser?.uid

    fun getItemProperties(
        urlTxt: CharSequence,
        productPhoto: ImageView,
        sellerUsername: TextView,
        productCategories: TextView,
        productDescription: TextView,
        productPrize: TextView,
        sellerPhoto: ImageView
    ) {
        val url = urlTxt.toString()
        db.collection("products").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val productUrl = document.getString("Product-Url").toString()

                    if (url == productUrl) {
                        val productUrlDb = document.getString("Product-Url")
                        val productPrizeDb = document.getString("Product-Prize")
                        val productCategoryDb = document.getString("Product-Categories")
                        val productDescriptionDb = document.getString("Product-Description")
                        val sellerMailDb = document.getString("Seller-Mail")
                        val productLat = document.getDouble("Product-Latitude")
                        val productLng = document.getDouble("Product-Longitude")

                        productCategories.text = productCategoryDb
                        productDescription.text = productDescriptionDb
                        productPrize.text = productPrizeDb
                        Glide.with(sellerPhoto.context)
                            .load(productUrlDb)
                            .into(productPhoto)
                        getSellerUsername(sellerMailDb, sellerUsername)
                        getSellerProfileImage(sellerMailDb, sellerPhoto)
                        getLocation(productLat, productLng)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("hata nedeni ", it.toString())
            }

    }
    private fun getSellerUsername(sellerMailDb: String?, sellerUsername: TextView) {
        db.collection("users").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val sellermaildb = document.getString("email")

                    if (sellerMailDb == sellermaildb) {
                        val usernameDb = document.getString("username")
                        sellerUsername.text = usernameDb
                    }
                }
            }.addOnFailureListener {
                Log.e("hata nedeni ", it.toString())
            }
    }
    private fun getSellerProfileImage(sellerMailDb: String?, sellerPhoto: ImageView) {

        storageRef.child("users/$sellerMailDb/profile.jpg").downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(sellerPhoto.context)
                    .load(uri)
                    .into(sellerPhoto)
            }
            .addOnFailureListener {
                Log.e("GlideError", "Fotoğraf yüklenirken hata oluştu: ${it.message}")
            }
    }
    private fun getLocation(productLat: Double?, productLng: Double?) {
        _locationLiveData.value = LatLng(productLat!!, productLng!!)
    }
    fun myFavorites(url: CharSequence) {
        val photoUrl = url.toString()
        val favoritesRef = db.collection("users").document(currentUser!!).collection("myFavorites")

        val favMap = hashMapOf(
            "photoUrl" to photoUrl,
            "isFavorite" to true
        )
            favoritesRef.document().set(favMap)
            .addOnSuccessListener {
                Log.e("fava eklendi", photoUrl)
                _isFav.value = true
                Log.e("isfav = ","$_isFav")
            }
            .addOnFailureListener {
                Log.e("fav ekleme hatası itemvm", "HATA NEDENİ -> $it")
            }
    }
}