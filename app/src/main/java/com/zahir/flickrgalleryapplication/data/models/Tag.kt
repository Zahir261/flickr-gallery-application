package com.zahir.flickrgalleryapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Local database Tag entity model
 */
@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val frequency: Int,
    val createdAt: Date,
    val updatedAt: Date
)
