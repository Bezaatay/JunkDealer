package com.kotlinegitim.myapplicationmt3.adapter

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
import com.kotlinegitim.myapplicationmt3.ui.Item.ItemFragment
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.data.ItemsDataClass

class ItemAdapter(
    private val context: Context,
    private var article: List<ItemsDataClass>,
    private var listener: OnItemClickListener
)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnClickListener{

        val itemcardView: CardView = itemView.findViewById(R.id.itemcardView)
        val productPhoto: ImageView = itemView.findViewById(R.id.product_photo)
        val productCategories: TextView = itemView.findViewById(R.id.product_categories)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val productPrize : TextView = itemView.findViewById(R.id.product_prize)
        val sellerPhoto: ImageView = itemView.findViewById(R.id.seller_photo)
        val sellerUsername : TextView = itemView.findViewById(R.id.seller_username)
        val imageViewHeart: ImageView = itemView.findViewById(R.id.imageViewHeart)
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
     //   Glide.with(context).load(article[position].ItemUrl).into(holder.productPhoto)
        holder.productCategories.text = article[position].ItemCategory
      //  holder.productDescription.text = article[position].ItemDescription
      //  holder.productPrize.text = article[position].ItemPrice
      //  Glide.with(context).load(article[position].sellerUrl).into(holder.sellerPhoto)
      //  holder.productPrize.text = article[position].sellerName
       // holder.textView13.text = article[position].
    }

    override fun getItemCount(): Int {
        return article.size
    }

}