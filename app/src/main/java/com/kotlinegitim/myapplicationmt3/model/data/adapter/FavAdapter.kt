package com.kotlinegitim.myapplicationmt3.model.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.model.data.FavDataClass

class FavAdapter(
    private val context: Context,
    private var favItem: List<FavDataClass>,
    private var listener: OnItemClickListener
)
    : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {

    inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val cardView1: CardView = itemView.findViewById(R.id.cardView1)
        val imageView1: ImageView = itemView.findViewById(R.id.imageView1)
        val priceTextView1: TextView = itemView.findViewById(R.id.textView1)
        val imageViewHeart: ImageView = itemView.findViewById(R.id.imageViewHeart)
        val productCategoryTextview: TextView = itemView.findViewById(R.id.product_info_textview)
        val distanceTextview: TextView = itemView.findViewById(R.id.distanceText)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : FavViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_card_view, parent, false)
            return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavAdapter.FavViewHolder, position: Int) {
        Glide.with(context).load(favItem[position].productUrl).into(holder.imageView1)
        holder.priceTextView1.text = favItem[position].productPrice
        holder.productCategoryTextview.text = favItem[position].productCategory
    }

    override fun getItemCount(): Int {
        return favItem.size
    }

}