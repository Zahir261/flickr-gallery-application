package com.zahir.flickrgalleryapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahir.flickrgalleryapplication.R
import com.zahir.flickrgalleryapplication.databinding.FragmentFilterBottomSheetBinding

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBottomSheetBinding
    private val autoCompleteList = mutableListOf("Bangladesh", "India", "Pakistan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tagView.setAutoCompleteItemList(autoCompleteList)
    }

    companion object {
        fun newInstance() = FilterBottomSheetFragment()
    }
}