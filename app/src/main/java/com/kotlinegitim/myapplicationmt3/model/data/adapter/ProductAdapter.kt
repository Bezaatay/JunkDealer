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
import com.kotlinegitim.myapplicationmt3.model.data.HomeDataClass
import com.kotlinegitim.myapplicationmt3.firebase.FirebaseService

class ProductAdapter(
    private val context: Context,
    private var items: List<HomeDataClass>,
    private var listener : OnItemClickListener
)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val firebaseService = FirebaseService()
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    OnClickListener{
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
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_card_view, parent, false)
            return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Glide.with(context).load(items[position].productUrl).into(holder.imageView1)
        holder.priceTextView1.text = items[position].productPrice
        holder.productCategoryTextview.text = items[position].productCategory

        if (items[position].distance != null) {
            holder.distanceTextview.text = "${items[position].distance} KM"
        } else {
            holder.distanceTextview.text = ""
        }

            // Kalbin durumuna göre içi dolu veya boş kalp görüntüsüne geçiş yap
            if (items[position].isHeartFilled == true) {
                holder.imageViewHeart.setImageResource(R.drawable.filledheart)
            } else {
                holder.imageViewHeart.setImageResource(R.drawable.hollowheart)
            }

        /*
            holder.imageViewHeart.setOnClickListener {
                // Kalbin durumunu tersine çevir
                items[position].isHeartFilled = !items[position].isHeartFilled

                // Kalbin durumuna göre içi dolu veya boş kalp görüntüsüne geçiş yap
                if (items[position].isHeartFilled == true) {
                    holder.imageViewHeart.setImageResource(R.drawable.filledheart)
                } else {
                    holder.imageViewHeart.setImageResource(R.drawable.hollowheart)
                }
            }

         */
    }
    override fun getItemCount(): Int {
        return items.size
    }
}