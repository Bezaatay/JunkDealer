package com.kotlinegitim.myapplicationmt3.ui.shopping

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlinegitim.myapplicationmt3.adapter.Shopping2Adapter
import com.kotlinegitim.myapplicationmt3.data.Shopping2DataClass
import com.kotlinegitim.myapplicationmt3.databinding.FragmentShoppingBinding

class ShoppingFragment : Fragment(), Shopping2Adapter.OnItemClickListener {

    private lateinit var _binding : FragmentShoppingBinding
    private lateinit var shoppingViewModel: ShoppingViewModel


    private var recyclerView: RecyclerView? = null
    private var shopping2Adapter: Shopping2Adapter? = null
    private var shopItem = mutableListOf<Shopping2DataClass>()
    private val binding get() = _binding
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val currentUser = auth.currentUser?.uid
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoppingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ShoppingViewModel::class.java]
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)

        shopItem = ArrayList()

        recyclerView = binding.RecyclerViewID

        binding.purchasesText.setOnClickListener {
           //shoppingViewModel.getPurchases()
            getIBought()
        }
        binding.iSoldText.setOnClickListener {
            //shoppingViewModel.getPurchases()
            getISold()
        }
        getIBought()

        return binding.root
    }
    private fun adapterFun() {
        shopping2Adapter = Shopping2Adapter(requireActivity(), shopItem, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 1)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = shopping2Adapter

    }
    private fun getIBought(){
        shopItem.clear()

        db.collection("users").document(currentUser!!).collection("ProductsIBought").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val productUrl = document.getString("productPhotoUrl")
                    val productPrize = document.getDouble("discounted-price")
                    val productcategory = document.getString("category")
                    Log.e("urun fiyat",productPrize.toString())

                      shopItem.add(Shopping2DataClass(productUrl, productPrize, productcategory))
                }
                adapterFun()
            }
            .addOnFailureListener {
                Log.e("hata nedeni shopping ", it.toString())
            }
    }
    private fun getISold(){
        shopItem.clear()

        db.collection("users").document(currentUser!!).collection("ProductsISold").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val productUrl = document.getString("photoUrl")
                    val productPrize = document.getDouble("productPrice")
                    val productcategory = document.getString("category")

                    shopItem.add(Shopping2DataClass(productUrl, productPrize, productcategory))
                }
                adapterFun()
            }
            .addOnFailureListener {
                Log.e("hata nedeni shopping ", it.toString())
            }
    }
    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}