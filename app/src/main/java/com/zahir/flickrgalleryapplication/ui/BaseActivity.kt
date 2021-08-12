package com.zahir.flickrgalleryapplication.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    inline fun <reified A : Activity> createIntent() = Intent(this, A::class.java)

    fun navigate(intent: Intent, requestCode: Int? = null) {
        when (requestCode) {
            null -> startActivity(intent)
            else -> startActivityForResult(intent, requestCode)
        }
    }

    fun navigateAsNewTask(intent: Intent) {
        startActivity(intent)
        finish()
    }

}