package com.example.tobuy.arch

import com.example.tobuy.room.AppDatabase
import com.example.tobuy.room.entity.ItemEntity

class ToButRepo(private val appDatabase: AppDatabase) {

    fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().insert(itemEntity)
    }

    fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    suspend fun getAllItems(): List<ItemEntity> {
        return appDatabase.itemEntityDao().getAllItemEntities()
    }

}