package com.kotlinegitim.myapplicationmt3.ui.myFavs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.kotlinegitim.myapplicationmt3.model.data.adapter.ProductAdapter
import com.kotlinegitim.myapplicationmt3.databinding.FragmentMyFavsBinding
import com.kotlinegitim.myapplicationmt3.model.data.FavDataClass
import com.kotlinegitim.myapplicationmt3.model.data.adapter.FavAdapter

class MyFavsFragment : Fragment(), FavAdapter.OnItemClickListener {


    private lateinit var myFavsViewModel: MyFavsViewModel
    private lateinit var _binding : FragmentMyFavsBinding
    private val binding get() = _binding

    private var recyclerView: RecyclerView? = null
    private var favAdapter: FavAdapter? = null
    private var favItems = mutableListOf<FavDataClass>()

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val currentUser = auth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFavsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[MyFavsViewModel::class.java]
        _binding = FragmentMyFavsBinding.inflate(inflater, container, false)

        favItems =  ArrayList()

        recyclerView = binding.RecyclerView

        loadFavItems()

        return binding.root
    }
    private fun loadFavItems() {
        db.collection("users").document(currentUser).collection("myFavorites").get()
            .addOnSuccessListener { documents ->
                val tasks = mutableListOf<Task<QuerySnapshot>>()

                for (document in documents) {
                    val photoUrlInFav = document.getString("photoUrl")

                    val productQuery = db.collection("products").whereEqualTo("Product-Url", photoUrlInFav).get()
                    tasks.add(productQuery)
                }

                Tasks.whenAllSuccess<QuerySnapshot>(tasks)
                    .addOnSuccessListener { results ->
                        favItems.clear()

                        for (i in results.indices) {
                            val querySnapshot = results[i] as QuerySnapshot
                            if (!querySnapshot.isEmpty) {
                                val doc = querySnapshot.documents[0]

                                val productUrl = doc.getString("Product-Url")
                                val productPrize = doc.getString("Product-Prize")
                                val productCategory = doc.getString("Product-Categories")

                                favItems.add(FavDataClass(productUrl, productPrize, productCategory))
                            }
                        }

                        favAdapter = FavAdapter(requireActivity(), favItems, this)
                        val layoutManager: RecyclerView.LayoutManager =
                            GridLayoutManager(requireContext(), 2)

                        recyclerView!!.layoutManager = layoutManager
                        recyclerView!!.adapter = favAdapter
                    }
            }
            .addOnFailureListener {
                Log.e("hata nedeni loadFavItems", it.toString())
            }
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}