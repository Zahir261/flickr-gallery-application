package com.zahir.flickrgalleryapplication.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zahir.flickrgalleryapplication.data.models.Tag
import java.util.*

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Query("UPDATE tag SET updatedAt = :updateTime, frequency = frequency + 1 WHERE title = :tag")
    suspend fun updateTag(tag: String, updateTime: Date)

    @Query("SELECT * FROM tag ORDER BY updatedAt DESC, frequency DESC, title ASC")
    fun getAllTags(): LiveData<List<Tag>>
}