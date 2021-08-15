package com.zahir.flickrgalleryapplication.ui.customviews.tag

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
    var listener: TagViewItemChangeListener? = null

    init {
        with(binding) {
            autoComplete.threshold = 1 // start giving suggestion from first character
            autoComplete.setAdapter(autoCompleteAdapter)
            autoComplete.setOnItemClickListener { parent, _, position, _ ->
                autoComplete.setText("")
                val selectedTag = parent.getItemAtPosition(position) as String
                addToTagList(selectedTag, Action.Selection)
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
                        addToTagList(enteredText, Action.Insertion)
                    }
                }
                return@setOnKeyListener true
            }
        }

        tagAdapter.apply {
            listener = object : TagAdapter.TagCloseListener {
                override fun onTagClosed(position: Int, tag: String) {
                    tagList.removeAt(position)
                    tagAdapter.tagList = tagList
                    this@TagView.listener?.onTagDeleted(tagList)
                }
            }
        }
    }

    /**
     * Add an item to the tag list and update the tag recyclerview.
     * Call [TagViewItemChangeListener.onTagInserted] or [TagViewItemChangeListener.onTagSelected] based on the action.
     *
     * @param tag String - tag to be added
     * @param action [Action] to be performed
     */
    private fun addToTagList(tag: String, action: Action) {
        if (!tagList.contains(tag)) {
            tagList.add(tag)
            tagAdapter.tagList = tagList
            binding.rvTag.scrollToPosition(tagList.size - 1)
            when (action) {
                Action.Selection -> listener?.onTagSelected(tag, tagList)
                Action.Insertion -> listener?.onTagInserted(tag, tagList)
            }
        }
    }

    /**
     * Get the text from auto complete text view
     *
     * @return String - text from auto complete text view
     */
    fun getEnteredTag(): String {
        return binding.autoComplete.text.toString()
    }

    /**
     * discard previous list and assign to the new list
     *
     * @param itemList List<String>
     */
    fun setAutoCompleteItemList(itemList: List<String>) {
        autoCompleteAdapter.clear()
        autoCompleteAdapter.addAll(itemList)
    }

    /**
     * discard previous list of tag recyclerview and set a new list
     *
     * @param tagList List<String>
     */
    fun setTagList(tagList: List<String>) {
        this.tagList.clear()
        this.tagList.addAll(tagList)
        tagAdapter.tagList = this.tagList
        binding.rvTag.scrollToPosition(tagList.size - 1)
    }

    /**
     * Clear the selected tag list
     */
    fun clearTagList() {
        tagList.clear()
        tagAdapter.tagList = tagList
    }

    /**
     * An interface to detect the tag insertion, selection and deletion event
     */
    interface TagViewItemChangeListener {
        /**
         * Perform action when a tag is inserted
         *
         * @param tag String Selected tag
         * @param tagList List<String> - currently selected tag list
         */
        fun onTagInserted(tag: String, tagList: List<String>)

        /**
         * Perform action when a tag is selected
         *
         * @param tag String Selected tag
         * @param tagList List<String> - currently selected tag list
         */
        fun onTagSelected(tag: String, tagList: List<String>)

        /**
         * Perform action when a tag is deleted
         *
         * @param tagList List<String> - currently selected tag list
         */
        fun onTagDeleted(tagList: List<String>)
    }

    /**
     * Tag selection or insertion aciton
     */
    sealed class Action {
        object Selection : Action()
        object Insertion : Action()
    }
}