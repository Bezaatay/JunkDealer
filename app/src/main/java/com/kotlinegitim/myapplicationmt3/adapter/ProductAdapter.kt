package com.kotlinegitim.myapplicationmt3.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.data.HomeDataClass

class ProductAdapter(
    private val context: Context,
    private var items: List<HomeDataClass>,
    private var listener : OnItemClickListener
)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnClickListener{
        val cardView1: CardView = itemView.findViewById(R.id.cardView1)
        val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
        val priceTextView1: TextView = itemView.findViewById(R.id.textView1)
        val imageViewHeart: ImageView = itemView.findViewById(R.id.imageViewHeart)
        val productcategorytextview: TextView = itemView.findViewById(R.id.product_info_textview)

        init {
          itemView.setOnClickListener(this)
      }
        override fun onClick(p0: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.home_card_view, parent, false)
            return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Glide.with(context).load(items[position].ProductUrl).into(holder.imageView1)
        holder.priceTextView1.text = items[position].ProductPrice
        holder.productcategorytextview.text = items[position].ProductCategory
    }

    override fun getItemCount(): Int {
        return items.size
    }
}