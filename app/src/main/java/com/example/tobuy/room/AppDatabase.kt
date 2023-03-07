package com.example.tobuy.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tobuy.application.application
import com.example.tobuy.room.dao.CategoryEntityDao
import com.example.tobuy.room.dao.ItemEntityDao
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.room.entities.ItemEntity
import com.example.tobuy.room.migration.MIGRATION_1_2

@Database(
    entities = [ItemEntity::class, CategoryEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemEntityDao(): ItemEntityDao
    abstract fun categoryEntityDao(): CategoryEntityDao
}

val appDatabase by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { createRoomDatabase() }

private fun createRoomDatabase(): AppDatabase {
    return Room.databaseBuilder(
        application, AppDatabase::class.java,
        name = "to_buy_database"
    ).addMigrations(MIGRATION_1_2).build()
}
