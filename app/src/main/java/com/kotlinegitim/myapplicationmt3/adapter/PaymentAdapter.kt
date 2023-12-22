package com.kotlinegitim.myapplicationmt3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.data.PaymentDataClass

class PaymentAdapter(
    private val context: Context,
    private var item: List<PaymentDataClass>
): RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {
    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val productPhoto: ImageView = itemView.findViewById(R.id.productPhotoIw)
        val productCategories: TextView = itemView.findViewById(R.id.productCategoryTw)
        val productPrize : TextView = itemView.findViewById(R.id.productPrizeTw)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : PaymentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shopping_card_view, parent, false)
        return PaymentViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        Glide.with(context).load(item[position].itemUrl).into(holder.productPhoto)
        holder.productCategories.text = item[position].itemCategory
        holder.productPrize.text = item[position].itemPrice
    }
}