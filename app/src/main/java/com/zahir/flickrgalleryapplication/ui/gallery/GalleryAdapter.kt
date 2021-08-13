package com.zahir.flickrgalleryapplication.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.databinding.ItemGalleryBinding

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    var items = emptyList<ImageDetail>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var clickListener: OnImageClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GalleryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class GalleryViewHolder(private val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageDetail: ImageDetail) {
            binding.imageDetail = imageDetail
            binding.root.setOnClickListener {
                clickListener?.onClick(imageDetail)
            }
        }
    }

    interface OnImageClickListener {
        fun onClick(imageDetail: ImageDetail)
    }
}