package com.kotlinegitim.myapplicationmt3.ui.shopping

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.adapter.ProductAdapter
import com.kotlinegitim.myapplicationmt3.adapter.ShoppingItemAdapter
import com.kotlinegitim.myapplicationmt3.data.HomeDataClass
import com.kotlinegitim.myapplicationmt3.data.ItemsDataClass
import com.kotlinegitim.myapplicationmt3.databinding.FragmentHomeBinding
import com.kotlinegitim.myapplicationmt3.databinding.FragmentShoppingBinding
import com.kotlinegitim.myapplicationmt3.ui.home.HomeViewModel

class ShoppingFragment : Fragment(), ShoppingItemAdapter.OnItemClickListener {

    private lateinit var _binding : FragmentShoppingBinding
    private lateinit var shoppingViewModel: ShoppingViewModel


    private var recyclerView: RecyclerView? = null
    private var shoppingItemAdapter: ShoppingItemAdapter? = null
    private var article = mutableListOf<ItemsDataClass>()

    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        shoppingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ShoppingViewModel::class.java]
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)

        article = ArrayList()

        recyclerView = binding.RecyclerViewID

        binding.purchasesText.setOnClickListener {
            //getPurchases()
        }

        return binding.root
    }

    private fun adapterFun(){
        shoppingItemAdapter = ShoppingItemAdapter(requireActivity(), article, this)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 2)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = shoppingItemAdapter
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}