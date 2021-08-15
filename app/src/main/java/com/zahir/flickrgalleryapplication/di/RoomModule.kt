package com.zahir.flickrgalleryapplication.di

import android.content.Context
import androidx.room.Room
import com.zahir.flickrgalleryapplication.data.database.LocalDatabase
import com.zahir.flickrgalleryapplication.data.database.dao.TagDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    fun providesLocalDatabase(@ApplicationContext context: Context): LocalDatabase {
        return Room.databaseBuilder(context, LocalDatabase::class.java, "flickr_app_db")
            .build()
    }

    @Provides
    fun providesTagDao(localDatabase: LocalDatabase): TagDao {
        return localDatabase.tagDao()
    }
}