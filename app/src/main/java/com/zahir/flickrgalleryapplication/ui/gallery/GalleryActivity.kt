package com.zahir.flickrgalleryapplication.ui.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zahir.flickrgalleryapplication.R
import com.zahir.flickrgalleryapplication.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var viewModel: GalleryActivityViewModel
    private val adapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GalleryActivityViewModel::class.java)
        with(binding) {
            lifecycleOwner = this@GalleryActivity
            viewModel = this@GalleryActivity.viewModel
            rvGallery.apply {
                addItemDecoration(GalleryItemDecoration(this@GalleryActivity))
                layoutManager = GridLayoutManager(this@GalleryActivity, resources.getInteger(R.integer.span_count))
                adapter = this@GalleryActivity.adapter
            }
        }

        with(viewModel) {
            getImages()

            imageList.observe(this@GalleryActivity, { imageList ->
                adapter.items = imageList
            })


        }
    }
}