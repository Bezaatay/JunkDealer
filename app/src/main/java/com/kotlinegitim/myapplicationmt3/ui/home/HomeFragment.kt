package com.kotlinegitim.myapplicationmt3.ui.home

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.model.data.adapter.ProductAdapter
import com.kotlinegitim.myapplicationmt3.databinding.FragmentHomeBinding
import com.kotlinegitim.myapplicationmt3.model.data.HomeDataClass
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class HomeFragment : Fragment(), ProductAdapter.OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var productAdapter: ProductAdapter? = null
    private var items = mutableListOf<HomeDataClass>()

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val currentUser = auth.currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        items = ArrayList()

        recyclerView = binding.RecyclerViewID

        loadAllItems()

        binding.allProducts.setOnClickListener {
            loadAllItems()
        }
        binding.searchBtn.setOnClickListener {
            val searchText = binding.searchView.text
            loadFilteredItems(searchText)
        }
        binding.nearProducts.setOnClickListener {
            loadNearItems()
        }

        return root
    }
    private fun adapterFun(){
        productAdapter = ProductAdapter(requireActivity(), items, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 2)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = productAdapter
    }
    private fun loadFilteredItems(searchText: Editable) {
        // Clear the existing items in the list
        items.clear()

        val dbSearchText = searchText.toString()
        db.collection("products").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val productcategory = document.getString("Product-Categories")

                    if (dbSearchText == productcategory) {
                        val productUrl = document.getString("Product-Url")
                        val productPrize = document.getString("Product-Prize")
                        val productCategory = document.getString("Product-Categories")
                    items.add(HomeDataClass(productUrl, productPrize, productCategory, null, false))
                    }
                }
                adapterFun()
            }
            .addOnFailureListener {
                Log.e("hata nedeni ", it.toString())
            }
    }
    private fun loadAllItems() {
        items.clear()

        val productTask = db.collection("products").get()
        val favoritesTask = db.collection("users").document(currentUser).collection("myFavorites").get()

        Tasks.whenAllSuccess<QuerySnapshot>(productTask, favoritesTask)
            .addOnSuccessListener { results ->
                val productDocuments = results[0] as QuerySnapshot
                val favoritesDocuments = results[1] as QuerySnapshot

                for (productDocument in productDocuments) {
                    val productUrl = productDocument.getString("Product-Url")
                    val productPrize = productDocument.getString("Product-Prize")
                    val productcategory = productDocument.getString("Product-Categories")

                    // Eşleşen belgeyi bul
                    val matchingFavoriteDocument = favoritesDocuments.firstOrNull { favoriteDoc ->
                        favoriteDoc.getString("photoUrl") == productUrl
                    }

                    // Belge varsa isFav değerini al, yoksa false olarak ata
                    val isFav = matchingFavoriteDocument?.getBoolean("isFavorite") ?: false

                    items.add(HomeDataClass(productUrl, productPrize, productcategory, null, isFav))
                }
                adapterFun()
            }
            .addOnFailureListener {
                Log.e("hata nedeni ", it.toString())
            }
    }

    private fun loadNearItems() {
        items.clear()

        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null) {
            db.collection("users").document(currentUserUid).collection("location")
                .document(currentUserUid + "location")
                .get().addOnSuccessListener { documentSnapshot ->
                    val userLatitude = documentSnapshot.getDouble("latitude")
                    val userLongitude = documentSnapshot.getDouble("longitude")

                    db.collection("products").get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                val productLatitude = document.getDouble("Product-Latitude")
                                val productLongitude = document.getDouble("Product-Longitude")

                                fun calculateDistance(
                                    userLatitude: Double,
                                    userLongitude: Double,
                                    productLatitude: Double,
                                    productLongitude: Double
                                ): Double {
                                    val radius = 6371 // Dünya yarıçapı (km), isteğe bağlı olarak başka bir değer de kullanabilirsiniz.

                                    val latDistance = Math.toRadians(productLatitude - userLatitude)
                                    val lonDistance = Math.toRadians(productLongitude - userLongitude)

                                    val a = sin(latDistance / 2) * sin(latDistance / 2) +
                                            cos(Math.toRadians(userLatitude)) * cos(Math.toRadians(productLatitude)) *
                                            sin(lonDistance / 2) * sin(lonDistance / 2)

                                    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

                                    return radius * c
                                }
                                val distance = calculateDistance(userLatitude!!, userLongitude!!, productLatitude!!, productLongitude!!)
                                val formattedDistanceStr = String.format("%.1f", distance)
                                val formattedDistance = formattedDistanceStr.toDouble()

                                if (distance < 10) {
                                    val productUrl = document.getString("Product-Url")
                                    val productPrize = document.getString("Product-Prize")
                                    val productcategory = document.getString("Product-Categories")

                                    if (productPrize != null && productUrl != null && productcategory != null) {
                                        items.add(HomeDataClass(productUrl, productPrize, productcategory, formattedDistance, null))
                                    }
                                    adapterFun()
                                }
                            }
                        }
                }
                .addOnFailureListener {
                    Log.e("addOnFailure", "$it")
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
    override fun onItemClick(position: Int) {

        productAdapter!!.notifyItemChanged(position)

        findNavController().navigate(R.id.action_navigation_home_to_itemFragment, Bundle().apply {
            putString("url",items[position].productUrl)
            putString("prize",items[position].productPrice)
            putString("category",items[position].productCategory)
        })
    }
}