package com.zahir.flickrgalleryapplication.ui.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zahir.flickrgalleryapplication.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}