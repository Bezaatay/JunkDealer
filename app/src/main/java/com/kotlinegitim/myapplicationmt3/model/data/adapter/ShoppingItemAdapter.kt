package com.kotlinegitim.myapplicationmt3.model.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.model.data.ItemsDataClass

class ShoppingItemAdapter(
    private val context: Context,
    private var article: List<ItemsDataClass>,
    private var listener: OnItemClickListener
)
    : RecyclerView.Adapter<ShoppingItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnClickListener{

        val productPhoto: ImageView = itemView.findViewById(R.id.productPhotoIw)
        val productCategories: TextView = itemView.findViewById(R.id.productCategoryTw)
        val date: TextView = itemView.findViewById(R.id.dateTw)
        val productPrize : TextView = itemView.findViewById(R.id.productPrizeTw)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_card_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Glide.with(context).load(article[position].itemUrl).into(holder.productPhoto)
        holder.productCategories.text = article[position].itemCategory
        holder.productPrize.text = article[position].itemPrice
    }

    override fun getItemCount(): Int {
        return article.size
    }

}