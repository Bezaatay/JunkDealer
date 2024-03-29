package com.kotlinegitim.myapplicationmt3.model.data.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.model.data.Shopping2DataClass

class Shopping2Adapter(
    private val context: Context,
    private var shopItem: List<Shopping2DataClass>,
    private var listener: OnItemClickListener
)
    : RecyclerView.Adapter<Shopping2Adapter.ShopViewHolder>() {

    inner class ShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val productPhoto: ImageView = itemView.findViewById(R.id.productPhotoIw)
        val productCategories: TextView = itemView.findViewById(R.id.productCategoryTw)
        val productPrize : TextView = itemView.findViewById(R.id.productPrizeTw)
        val shoppingDone :  TextView = itemView.findViewById(R.id.textView17)
        val doneView: ImageView = itemView.findViewById(R.id.imageView4)
        val prizeText : TextView = itemView.findViewById(R.id.textView13)

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
            : ShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shopping_card_view2, parent, false)
        return ShopViewHolder(view)
    }
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        Glide.with(context).load(shopItem[position].itemUrl).into(holder.productPhoto)
        holder.productCategories.text = shopItem[position].itemCategory
        holder.productPrize.text = shopItem[position].itemPrice.toString()

        if(shopItem[position].uploadF == true){
            holder.shoppingDone.visibility = View.INVISIBLE
            holder.doneView.visibility = View.INVISIBLE
        }
        else{
            holder.shoppingDone.visibility = View.VISIBLE
            holder.doneView.visibility = View.VISIBLE
        }
        if(shopItem[position].uploadS == true){
            holder.prizeText.text = "Kazancım :"
         }
    }
    override fun getItemCount(): Int {
        return shopItem.size
    }

}