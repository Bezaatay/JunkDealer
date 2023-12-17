package com.kotlinegitim.myapplicationmt3.ui.EditProfileInfos

import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class EditProfileInfosViewModel : ViewModel() {

    var _isChange : MutableLiveData<Boolean> = MutableLiveData(false)

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db = FirebaseFirestore.getInstance()
    private val user = firebaseAuth.currentUser
    private val uid = user?.uid
    private val currentUserUid=firebaseAuth.currentUser?.uid

    private val _locationLiveData = MutableLiveData<LatLng>()
    val locationLiveData: LiveData<LatLng> get() = _locationLiveData

    fun getInformations(usernamehint: TextView, mailhint: TextView, passwordhint: TextView){
        if (currentUserUid != null) {
            db.collection("users")
                .document(currentUserUid)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val email = document.getString("email")
                        val username = document.getString("username")
                        val password = document.getString("password")
                        mailhint.text=email.toString()
                        usernamehint.text=username.toString()
                        passwordhint.text=password.toString()
                        if (username != null || email != null) {
                            // Kullanıcı adını kullanarak işlemlerinizi gerçekleştirin
                            Log.e("","Current username: $username")
                        }
                    } else {
                        Log.e("","Belirtilen UID ile ilgili belge bulunamadı.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("","Firestore'dan veri alınamadı: $exception")
                }
        } else {
            Log.e("","Oturum açmış bir kullanıcı bulunamadı.")
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

    fun SaveChanges(username: String, Mail: String, Password: CharSequence){

        val updateDataUserMap = hashMapOf(
            "username" to username,
            "email" to Mail,
            "password" to Password
        )
        if (uid != null) {
            db.collection("users").document(uid).update(updateDataUserMap as Map<String, Any>)
                .addOnSuccessListener {
                    Log.e("firestora database","firestore update db success")
                _isChange.value = true
                }.addOnFailureListener {
                    Log.e("firestora database","firestore update db unsuccessfull")
                }
        }
    }
}