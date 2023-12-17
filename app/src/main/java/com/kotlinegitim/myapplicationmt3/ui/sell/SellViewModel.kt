package com.kotlinegitim.myapplicationmt3.ui.sell

import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class SellViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val db = FirebaseFirestore.getInstance()
    val currentUserUid = firebaseAuth.currentUser?.uid
    val storageRef = FirebaseStorage.getInstance().reference


    fun getLocation(latitudeValue: TextView, longitudeValue: TextView) {
        if(currentUserUid != null) {
            db.collection("users").document(currentUserUid).collection("location").document(currentUserUid+"location")
                .get().addOnSuccessListener { documentSnapshot ->
                    val latitude = documentSnapshot.getDouble("latitude")
                    val longitude = documentSnapshot.getDouble("longitude")

                    if (latitude != null && longitude != null) {
                      latitudeValue.text= latitude.toString()
                        longitudeValue.text= longitude.toString()
                    }
                    else {
                        // Eğer latitude veya longitude değeri null ise, hatayı log'a yazabilirsiniz
                        Log.e("PersonelInfosViewModel", "Latitude veya longitude değeri null.")
                    }
                }
        }
    }

    // Seçilen görseli Firebase Storage'a kaydetme
   fun UploadUrl(
        selectedImageUri: Uri?,
        ProductInfos: String,
        ProductDescription: String,
        ProductPrize: String,
        ProductLocationLatitude: String,
        ProductLocationLongitude: String,
    ) {
        if(selectedImageUri != null) {

            val user = FirebaseAuth.getInstance().currentUser
            val uuid = UUID.randomUUID()
            val imageID = "${uuid}.jpg"
            val imageRef = storageRef.child("productsPhotos/${user?.email}/myProducts/$imageID")
            imageRef.putFile(selectedImageUri).addOnSuccessListener {   task ->
                val dowlandImageRef = imageRef
                dowlandImageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    UploadProduct(ProductInfos,ProductDescription,ProductPrize,downloadUrl,ProductLocationLatitude,ProductLocationLongitude)
                }.addOnFailureListener {
                    Log.e("hata url1-SellVM",it.toString())
                }
            }.addOnFailureListener{exception ->
                Log.e("hata url2-SellVM",exception.toString())
            }
        }
        Log.e("dfgd","$selectedImageUri")
   }

    fun UploadProduct(
        ProductInfos: String,
        ProductDescription: String,
        ProductPrize: String,
        ProductUrl: String,
        ProductLocationLatitude: String,
        ProductLocationLongitude: String
    ){
        var productMap = hashMapOf(
            "Product-Info" to ProductInfos,
            "Product-Description" to ProductDescription,
            "Product-Prize" to ProductPrize+"TL",
            "Product-Url" to ProductUrl,
            "Product-Latitude" to ProductLocationLatitude,
            "Product-Longitude" to ProductLocationLongitude

        )
        if (currentUserUid != null) {
            db.collection("products").document().set(productMap)
                .addOnSuccessListener {
                    Log.e("firestora database","firestore db success")
                }.addOnFailureListener {
                    Log.e("firestora database","firestore db unsuccessfull")
                }
            UploadUserProduct(currentUserUid,ProductInfos, ProductDescription, ProductPrize,ProductUrl)
        }
    }

    fun UploadUserProduct(currentUser:String,editTextProductInfos:String, editTextProductDescription:String, editTextProductPrize:String,ProductUrl : String){
        val userProductMap = hashMapOf(
            "Product-Info" to editTextProductInfos,
            "Product-Description" to editTextProductDescription,
            "Product-Prize" to editTextProductPrize+"TL",
            "Product-Url" to ProductUrl
        )
        db.collection("users").document(currentUser).collection("ProductsIUpload").add(userProductMap)
    }
}