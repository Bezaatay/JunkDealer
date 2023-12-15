package com.kotlinegitim.myapplicationmt3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlinegitim.myapplicationmt3.adapter.ProductAdapter
import com.kotlinegitim.myapplicationmt3.data.HomeDataClass
import com.kotlinegitim.myapplicationmt3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var productAdapter: ProductAdapter? = null
    private var items = mutableListOf<HomeDataClass>()

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        items = ArrayList()

        recyclerView = binding.RecyclerViewID

     /*   binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

            }

            override fun onQueryTextChange(p0: String?): Boolean {

            }
        })*/

        loadAllItems()
        return root
    }

    private fun loadAllItems() {
        db.collection("products").get()
            .addOnSuccessListener { documents ->
                for (document in documents)  {
                    val productUrl = document.getString("Product-Url")
                    val productPrize = document.getString("Product-Prize")

                    if (productPrize != null && productUrl != null) {
                        items.add(HomeDataClass(productUrl,productPrize))
                    }
                }
                productAdapter = ProductAdapter(requireActivity(), items)
                val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)

                recyclerView!!.layoutManager = layoutManager
                recyclerView!!.adapter = productAdapter
            }
            .addOnFailureListener { exception ->
                // Hata durumu
                 }
            }

    private fun loadNearItems() {
        db.collection("products").get()
            .addOnSuccessListener { documents ->
                for (document in documents)  {
                    val productUrl = document.getString("Product-Url")
                    val productPrize = document.getString("Product-Prize")

                    if (productPrize != null && productUrl != null) {
                        items.add(HomeDataClass(productUrl,productPrize))
                    }
                }
                productAdapter = ProductAdapter(requireActivity(), items)
                val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)

                recyclerView!!.layoutManager = layoutManager
                recyclerView!!.adapter = productAdapter
            }
            .addOnFailureListener { exception ->
                // Hata durumu
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}