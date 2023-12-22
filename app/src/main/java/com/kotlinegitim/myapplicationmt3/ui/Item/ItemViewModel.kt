package com.kotlinegitim.myapplicationmt3.ui.Item

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ItemViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference
    private val _locationLiveData = MutableLiveData<LatLng>()
    val locationLiveData: LiveData<LatLng> get() = _locationLiveData


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
                        val productUrldb = document.getString("Product-Url")
                        val productPrizedb = document.getString("Product-Prize")
                        val productCategorydb = document.getString("Product-Categories")
                        val productDescriptiondb = document.getString("Product-Description")
                        val sellerMaildb = document.getString("Seller-Mail")
                        val productLat = document.getDouble("Product-Latitude")
                        val productLng = document.getDouble("Product-Longitude")

                        productCategories.text = productCategorydb
                        productDescription.text = productDescriptiondb
                        productPrize.text = productPrizedb
                        Glide.with(sellerPhoto.context)
                            .load(productUrldb)
                            .into(productPhoto)
                        getSellerUsername(sellerMaildb, sellerUsername)
                        getSellerProfileImage(sellerMaildb, sellerPhoto)
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
}