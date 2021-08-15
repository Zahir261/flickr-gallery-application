package com.zahir.flickrgalleryapplication.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahir.flickrgalleryapplication.databinding.FragmentOptionsBottomSheetBinding

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentOptionsBottomSheetBinding
    var listener: OptionsClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOptionsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.fragment = this
    }

    companion object {
        fun newInstance() = OptionsBottomSheetFragment()
    }

    fun onShareButtonClick() {
        listener?.onShareButtonClick()
        dismiss()
    }

    fun onSaveButtonClick() {
        listener?.onSaveButtonClick()
        dismiss()
    }

    fun onOpenToBrowserButtonClick() {
        listener?.onOpenToBrowserClick()
        dismiss()
    }

    interface OptionsClickListener {
        fun onShareButtonClick()
        fun onSaveButtonClick()
        fun onOpenToBrowserClick()
    }
}