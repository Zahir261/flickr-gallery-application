package com.zahir.flickrgalleryapplication.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zahir.flickrgalleryapplication.databinding.ActivityDetailsBinding
import com.zahir.flickrgalleryapplication.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * This activity shows the full image and the image description
 * and performs different actions on the image.
 */
@AndroidEntryPoint
class DetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var viewModel: DetailsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailsActivityViewModel::class.java)
        with(binding) {
            lifecycleOwner = this@DetailsActivity
            activty = this@DetailsActivity
            viewModel = this@DetailsActivity.viewModel
        }
        viewModel.init(intent.getParcelableExtra("imageDetail"))
        binding.root.setOnClickListener {
            viewModel.toggleDetailsVisibility()
        }
    }

    fun onThreeDotsMenuClick() {
        OptionsBottomSheetFragment.newInstance().apply {
            listener = object : OptionsBottomSheetFragment.OptionsClickListener {
                override fun onShareButtonClick() {
                    viewModel.shareViaEmail(this@DetailsActivity)
                }

                override fun onSaveButtonClick() {
                    viewModel.saveToGallery(this@DetailsActivity)
                }

                override fun onOpenToBrowserClick() {
                    viewModel.openToBrowser(this@DetailsActivity)
                }

            }
        }
            .show(supportFragmentManager, "OptionsFragment")
    }
}