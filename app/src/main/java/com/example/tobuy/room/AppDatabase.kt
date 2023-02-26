package com.example.tobuy.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tobuy.application.application
import com.example.tobuy.room.dao.ItemEntityDao
import com.example.tobuy.room.entity.ItemEntity

@Database(
    entities = [ItemEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemEntityDao(): ItemEntityDao
}

val appDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createRoomDatabase() }

private fun createRoomDatabase(): AppDatabase {
    return Room.databaseBuilder(application, AppDatabase::class.java, "to_buy_database").build()
}
