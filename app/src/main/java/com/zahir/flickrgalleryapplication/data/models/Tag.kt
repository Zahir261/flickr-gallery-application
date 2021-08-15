package com.zahir.flickrgalleryapplication.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val frequency: Int,
    val createdAt: Date,
    val updatedAt: Date
)
