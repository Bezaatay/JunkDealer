package com.kotlinegitim.myapplicationmt3.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.data.HomeDataClass

class ProductAdapter(private val context: Context, private var items: List<HomeDataClass>)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardView1: CardView = itemView.findViewById(R.id.cardView1)
        val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
        val priceTextView1: TextView = itemView.findViewById(R.id.textView1)
        val imageViewHeart: ImageView = itemView.findViewById(R.id.imageViewHeart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_card_view, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Glide.with(context).load(items[position].ProductUrl).into(holder.imageView1)
        holder.priceTextView1.text = items[position].ProductPrice
        holder.cardView1.setOnClickListener {
            Log.e("gösterildi","home geldii")
        }
        holder.imageViewHeart.setOnClickListener {
         //  var drawab=  holder.imageViewHeart.drawable
           // Log.e("gösterildi", drawab.toString())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
