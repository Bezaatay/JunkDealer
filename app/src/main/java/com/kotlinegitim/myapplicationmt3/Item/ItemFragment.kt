package com.kotlinegitim.myapplicationmt3.Item

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.adapter.ProductAdapter
import com.kotlinegitim.myapplicationmt3.data.HomeDataClass
import com.kotlinegitim.myapplicationmt3.data.ItemsDataClass
import com.kotlinegitim.myapplicationmt3.databinding.FragmentHomeBinding
import com.kotlinegitim.myapplicationmt3.databinding.FragmentItemBinding
import com.kotlinegitim.myapplicationmt3.ui.home.HomeViewModel

class ItemFragment : Fragment() {

    private var _binding : FragmentItemBinding?= null
    private lateinit var itemViewModel: ItemViewModel

    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var productAdapter: ProductAdapter? = null
    private var stuff = mutableListOf<ItemsDataClass>()

    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ItemViewModel::class.java]
        _binding = FragmentItemBinding.inflate(inflater, container, false)

        stuff = ArrayList()

        binding.urltxt.text = requireArguments().getString("url").toString()

        return  binding.root
    }

}