package com.example.tobuy.arch

import com.example.tobuy.room.AppDatabase
import com.example.tobuy.room.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

class ToButRepo(private val appDatabase: AppDatabase) {

    suspend fun insertItem(itemEntity: ItemEntity, insertCallback: (Boolean) -> Unit) {
        appDatabase.itemEntityDao().insert(itemEntity)
        insertCallback.invoke(true)
    }

    suspend fun deleteItem(itemEntity: ItemEntity) {
        appDatabase.itemEntityDao().delete(itemEntity)
    }

    fun getAllItems(): Flow<List<ItemEntity>> {
        return appDatabase.itemEntityDao().getAllItemEntities()
    }

}