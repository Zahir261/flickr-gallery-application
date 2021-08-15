package com.zahir.flickrgalleryapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zahir.flickrgalleryapplication.data.database.converters.DateConverter
import com.zahir.flickrgalleryapplication.data.database.dao.TagDao
import com.zahir.flickrgalleryapplication.data.models.Tag

@Database(entities = [Tag::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun tagDao(): TagDao
}