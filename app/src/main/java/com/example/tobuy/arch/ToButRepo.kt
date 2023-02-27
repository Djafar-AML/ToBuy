package com.example.tobuy.arch

import com.example.tobuy.room.AppDatabase
import com.example.tobuy.room.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

class ToButRepo(private val appDatabase: AppDatabase) {

    suspend fun insertItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().insert(itemEntity)
    }

    suspend fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    fun getAllItems(): Flow<List<ItemEntity>> {
        return appDatabase.itemEntityDao().getAllItemEntities()
    }

}