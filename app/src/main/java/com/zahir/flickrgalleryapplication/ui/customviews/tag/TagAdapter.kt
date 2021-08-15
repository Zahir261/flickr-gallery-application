package com.zahir.flickrgalleryapplication.ui.customviews.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zahir.flickrgalleryapplication.databinding.ItemTagBinding

class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    var tagList = ArrayList<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: TagCloseListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(tagList[position], position)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    inner class TagViewHolder(val binding: ItemTagBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String, position: Int) {
            binding.tag = tag
            binding.ivClose.setOnClickListener {
                listener?.onTagClosed(position, tag)
            }
        }
    }

    interface TagCloseListener {
        fun onTagClosed(position: Int, tag: String)
    }
}