package com.example.tobuy.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tobuy.room.dao.ItemEntityDao
import com.example.tobuy.room.model.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemEntityDao(): ItemEntityDao
}