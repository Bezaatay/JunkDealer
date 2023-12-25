package com.kotlinegitim.myapplicationmt3.ui.payment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.adapter.PaymentAdapter
import com.kotlinegitim.myapplicationmt3.data.PaymentDataClass
import com.kotlinegitim.myapplicationmt3.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private lateinit var  _binding : FragmentPaymentBinding
    private lateinit var paymentViewModel: PaymentViewModel

    private var recyclerView: RecyclerView? = null
    private var paymentItemAdapter: PaymentAdapter? = null
    private var item = mutableListOf<PaymentDataClass>()

    private val binding get() = _binding

    private fun showNotEnoughMoneyDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Yetersiz Kredi")
            .setMessage("Cüzdanınızda yeterli bakiye yok. Yüklemek ister misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                findNavController().navigate(R.id.action_paymentFragment_to_myPurseFragment)
            }
            .setNegativeButton("Hayır") { dialog, _ ->
                // Hayır'a basıldığında yapılacak işlemler
                dialog.dismiss()
            }
            .show()
    }

    private fun enoughMoneyDialogShown() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alışveriş Tamamlandı.")
            .setMessage("Ödemeniz başarıyla gerçekleşti. Keyifli alışverişler dileriz <3")
            .setPositiveButton("Evet") { _, _ ->
                findNavController().navigate(R.id.action_paymentFragment_to_shoppingFragment)
            }
            .setNegativeButton("Hayır") { dialog, _ ->
                // Hayır'a basıldığında yapılacak işlemler
                dialog.dismiss()
            }
            .show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        paymentViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[PaymentViewModel::class.java]
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)

        item = ArrayList()

        recyclerView = binding.RecyclerView

        val prize = requireArguments().getString("prize").toString()
        val category = requireArguments().getString("category").toString()
        val photoUrl = requireArguments().getString("url").toString()
        val description = requireArguments().getString("description").toString()
        val sellerUsername = requireArguments().getString("sellerUsername").toString()

        item.add(PaymentDataClass(photoUrl,prize,category))

        // TL sembolünü ve boşlukları kaldırarak sadece sayısal değeri elde etme
        val numericValue = prize.replace("[^\\d.]".toRegex(), "")

        // Elde edilen sayısal değeri double'a çevirme
        val prizeDbl = numericValue.toDouble()

        val paymentTotal = prizeDbl * 0.95
        val discountAmount = prizeDbl * 0.05

        val paymentTotalText = "$paymentTotal TL"
        val discountedText = "$discountAmount TL"
        Log.e("paymenttotal",paymentTotalText)

        binding.totalPaymentTxt.text = paymentTotalText
        binding.discountText.text = discountedText

        paymentItemAdapter = PaymentAdapter(requireActivity(), item)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(requireContext(), 1)

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.adapter = paymentItemAdapter

        paymentViewModel.getMoney(binding.moneyTxt)

        binding.payBtn.setOnClickListener {
            val totalPaymentTxt = binding.totalPaymentTxt.text.toString()
            val purseTxt = binding.moneyTxt.text.toString()
            paymentViewModel.buyProduct(totalPaymentTxt,purseTxt,photoUrl,category,sellerUsername,description)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        paymentViewModel.notEnoughMoneyDialog.observe(viewLifecycleOwner, Observer { showDialog ->
            if (showDialog) {
                showNotEnoughMoneyDialog()
                paymentViewModel.notEnoughMoneyDialogShown()
            }
        })
        paymentViewModel.enoughMoneyDialog.observe(viewLifecycleOwner, Observer { showDialog ->
            if (showDialog) {
                enoughMoneyDialogShown()
                paymentViewModel.enoughMoneyDialogShown()
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }
}