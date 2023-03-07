package com.example.tobuy.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MIGRATION_1_2: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(" CREATE TABLE IF NOT EXISTS `category_entity` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`)) " )
    }
}