package com.zahir.flickrgalleryapplication.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.zahir.flickrgalleryapplication.databinding.ActivityDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
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
        OptionsBottomSheetFragment.newInstance().show(supportFragmentManager, "OptionsFragment")
    }
}