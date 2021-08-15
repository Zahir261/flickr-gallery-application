package com.zahir.flickrgalleryapplication.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zahir.flickrgalleryapplication.databinding.FragmentOptionsBottomSheetBinding

/**
 * This fragment is responsible for showing the options that can be performed on an image
 * like sharing via email, save to galley and open in browser
 */
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
        /**
         * Perform task when Share via email button is clicked
         */
        fun onShareButtonClick()

        /**
         * Perform action when save to gallery button is clicked
         */
        fun onSaveButtonClick()

        /**
         * Perform action when open to browser button is clickec
         */
        fun onOpenToBrowserClick()
    }
}