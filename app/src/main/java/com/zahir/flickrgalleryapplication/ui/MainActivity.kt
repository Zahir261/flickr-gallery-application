package com.zahir.flickrgalleryapplication.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.zahir.flickrgalleryapplication.ui.gallery.GalleryActivity
import com.zahir.flickrgalleryapplication.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateAsNewTask(createIntent<GalleryActivity>())
        }, 1500)
    }
}