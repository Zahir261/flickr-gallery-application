package com.zahir.flickrgalleryapplication.ui.gallery

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.zahir.flickrgalleryapplication.R
import com.zahir.flickrgalleryapplication.data.models.ImageDetail
import com.zahir.flickrgalleryapplication.data.models.SearchFilter
import com.zahir.flickrgalleryapplication.databinding.ActivityGalleryBinding
import com.zahir.flickrgalleryapplication.ui.BaseActivity
import com.zahir.flickrgalleryapplication.ui.details.DetailsActivity
import com.zahir.flickrgalleryapplication.ui.filter.FilterBottomSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryActivity : BaseActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var viewModel: GalleryActivityViewModel
    private val adapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GalleryActivityViewModel::class.java)

        adapter.apply {
            clickListener = object : GalleryAdapter.OnImageClickListener {
                override fun onClick(imageDetail: ImageDetail) {
                    val intent = createIntent<DetailsActivity>().apply {
                        putExtra("imageDetail", imageDetail)
                    }
                    navigate(intent)
                }
            }
        }

        with(binding) {
            lifecycleOwner = this@GalleryActivity
            activity = this@GalleryActivity
            viewModel = this@GalleryActivity.viewModel
            rvGallery.apply {
                addItemDecoration(GalleryItemDecoration(this@GalleryActivity))
                layoutManager = GridLayoutManager(
                    this@GalleryActivity,
                    resources.getInteger(R.integer.span_count)
                )
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

    fun onFilterClick(){
        FilterBottomSheetFragment.newInstance(viewModel.searchFilter).apply {
            listener = object : FilterBottomSheetFragment.SearchButtonClickListener{
                override fun onSearchButtonClick(searchFilter: SearchFilter) {
                    viewModel.updateSearchFilter(searchFilter)
                    viewModel.getImages()
                }

            }
        }
            .show(supportFragmentManager, "Filter")
    }
}