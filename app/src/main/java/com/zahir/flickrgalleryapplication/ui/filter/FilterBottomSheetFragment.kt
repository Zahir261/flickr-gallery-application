package com.zahir.flickrgalleryapplication.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahir.flickrgalleryapplication.R
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.data.models.TagMode
import com.zahir.flickrgalleryapplication.databinding.FragmentFilterBottomSheetBinding
import com.zahir.flickrgalleryapplication.ui.customviews.tag.TagView
import dagger.hilt.android.AndroidEntryPoint

/**
 * This fragment is responsible for selecting the filters for image search.
 */
@AndroidEntryPoint
class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBottomSheetBinding
    private lateinit var viewModel: FilterFragmentViewModel
    private var searchFilter: SearchFilter = SearchFilter()
    var listener: SearchButtonClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        arguments?.let {
            searchFilter = it.getParcelable(SEARCH_FILTER_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(FilterFragmentViewModel::class.java)
        viewModel.init(searchFilter)
        with(binding) {
            lifecycleOwner = this@FilterBottomSheetFragment
            fragment = this@FilterBottomSheetFragment
            viewModel = this@FilterBottomSheetFragment.viewModel

            // set initial tag list
            if (searchFilter.tags.isNotEmpty()) {
                binding.tagView.setTagList(searchFilter.tags)
            }

            // Check tag mode
            val id = when (searchFilter.tagMode) {
                TagMode.All -> R.id.rb_all
                TagMode.Any -> R.id.rb_any
            }
            radioGroup.check(id)

            // Populate language list and set language selection
            val languageList = resources.getStringArray(R.array.languages)
            val languageAdapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                languageList
            ).apply {
                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            }
            spinnerLanguage.apply {
                adapter = languageAdapter
                val selectedPosition = languageList.indexOf(searchFilter.language.name)
                setSelection(if (selectedPosition == -1) 0 else selectedPosition)
            }

            tagView.apply {
                listener = object : TagView.TagViewItemChangeListener {
                    override fun onTagInserted(tag: String, tagList: List<String>) {
                        this@FilterBottomSheetFragment.viewModel.onTagInserted(tag, tagList)
                    }

                    override fun onTagSelected(tag: String, tagList: List<String>) {
                        this@FilterBottomSheetFragment.viewModel.onTagSelected(tag, tagList)
                    }

                    override fun onTagDeleted(tagList: List<String>) {
                        this@FilterBottomSheetFragment.viewModel.onTagDeleted(tagList)
                    }

                }
            }
        }

        // update the autoCompleteList in the tag view
        viewModel.autoCompleteList.observe(viewLifecycleOwner, {
            val tagList = it.map { tag ->
                tag.title
            }
            binding.tagView.setAutoCompleteItemList(tagList)
        })

    }

    /**
     * Reset filters to initial state
     */
    fun resetFilters() {
        binding.tagView.clearTagList()
        binding.radioGroup.check(R.id.rb_all)
        binding.spinnerLanguage.setSelection(0)
        viewModel.resetFilters()
    }

    /**
     * Build search params based on the selection of the filters and
     * send to gallery activity for searching again
     */
    fun onSearchButtonClick() {
        // add to tagList if last entered text is not empty
        val enteredTag = binding.tagView.getEnteredTag()
        if (enteredTag.trim().isNotEmpty() && !viewModel.tagList.contains(enteredTag)) {
            viewModel.addToTagList(enteredTag)
        }
        // update the tag mode selection
        val checkedRadioButtonId = binding.radioGroup.checkedRadioButtonId
        val selectedRadioButton = binding.root.findViewById<RadioButton>(checkedRadioButtonId)
        val selectedText = selectedRadioButton.text ?: "All tags"
        viewModel.updateTagMode(selectedText.toString())
        // update language selection
        val language = binding.spinnerLanguage.selectedItem.toString()
        viewModel.updateLanguage(language)
        // build the updated filter
        val searchFilter = SearchFilter(viewModel.tagList, viewModel.tagMode, viewModel.language)

        listener?.onSearchButtonClick(searchFilter)
        dismiss()
    }

    companion object {
        private const val SEARCH_FILTER_KEY = "searchFilter"

        /**
         * Create new instance of the [FilterBottomSheetFragment]
         *
         * @param searchFilter SearchFilter - initial filter values for this fragment
         */
        fun newInstance(searchFilter: SearchFilter) = FilterBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable(SEARCH_FILTER_KEY, searchFilter)
            }
        }
    }

    interface SearchButtonClickListener {
        fun onSearchButtonClick(searchFilter: SearchFilter)
    }
}