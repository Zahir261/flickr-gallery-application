package com.zahir.flickrgalleryapplication.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahir.flickrgalleryapplication.databinding.FragmentOptionsBottomSheetBinding

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentOptionsBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = OptionsBottomSheetFragment()
    }
}