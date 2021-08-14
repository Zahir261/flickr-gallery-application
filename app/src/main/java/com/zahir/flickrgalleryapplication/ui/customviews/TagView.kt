package com.zahir.flickrgalleryapplication.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zahir.flickrgalleryapplication.R
import com.zahir.flickrgalleryapplication.databinding.TagViewBinding

class TagView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) :
    ConstraintLayout(context, attributeSet, defStyle) {
    private val binding = DataBindingUtil.inflate<TagViewBinding>(
        LayoutInflater.from(context),
        R.layout.tag_view,
        this,
        true
    )
    private val autoCompleteAdapter =
        ArrayAdapter<String>(this.context, android.R.layout.simple_dropdown_item_1line)
    private val tagAdapter = TagAdapter()
    private val tagList = ArrayList<String>()
    private var listener: TagViewItemChangeListener? = null

    init {
        with(binding) {
            autoComplete.threshold = 1
            autoComplete.setAdapter(autoCompleteAdapter)
            autoComplete.setOnItemClickListener { parent, _, position, _ ->
                autoComplete.setText("")
                val selectedTag = parent.getItemAtPosition(position) as String
                addToTagList(selectedTag)
                listener?.onTagInserted(selectedTag)
            }

            rvTag.apply {
                layoutManager = StaggeredGridLayoutManager(
                    context.resources.getInteger(R.integer.span_count),
                    StaggeredGridLayoutManager.VERTICAL
                )
                adapter = this@TagView.tagAdapter
            }

            autoComplete.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    val enteredText = autoComplete.text.toString().trim()
                    autoComplete.setText("")
                    if (enteredText.isNotEmpty()) {
                        addToTagList(enteredText)
                        addToAutoCompleteList(enteredText)
                        listener?.onTagInserted(enteredText)
                    }
                }
                return@setOnKeyListener true
            }
        }

        tagAdapter.apply {
            listener = object : TagAdapter.TagCloseListener {
                override fun onTagCloseListener(position: Int) {
                    tagList.removeAt(position)
                    tagAdapter.tagList = tagList
                }
            }
        }
    }

    private fun addToTagList(tag: String) {
        if (!tagList.contains(tag)) {
            tagList.add(tag)
            tagAdapter.tagList = tagList
            binding.rvTag.scrollToPosition(tagList.size - 1)
        }
    }

    private fun addToAutoCompleteList(item: String) {
        autoCompleteAdapter.add(item)
    }

    fun setAutoCompleteItemList(itemList: List<String>) {
        autoCompleteAdapter.clear()
        autoCompleteAdapter.addAll(itemList)
    }

    fun setTagList(tagList: List<String>) {
        this.tagList.clear()
        this.tagList.addAll(tagList)
        tagAdapter.tagList = this.tagList
    }

    fun clearTagList() {
        tagList.clear()
        tagAdapter.tagList = tagList
    }

    interface TagViewItemChangeListener {
        fun onTagInserted(tag: String)
    }
}