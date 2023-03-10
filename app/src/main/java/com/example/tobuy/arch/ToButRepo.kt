package com.example.tobuy.arch

import com.example.tobuy.room.AppDatabase
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.room.entities.ItemEntity
import com.example.tobuy.room.entities.ItemWithCategoryEntity
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

    fun getAllItemWithCategoryEntity(): Flow<List<ItemWithCategoryEntity>> {
        return appDatabase.itemEntityDao().getAllItemWithCategoryEntity()
    }
    suspend fun updateItem(itemEntity: ItemEntity, updateCallback: (Boolean) -> Unit) {
        appDatabase.itemEntityDao().update(itemEntity)
        updateCallback.invoke(true)
    }

    suspend fun insertCategory(categoryEntity: CategoryEntity, insertCallback: (Boolean) -> Unit) {
        appDatabase.categoryEntityDao().insert(categoryEntity)
        insertCallback.invoke(true)
    }

    suspend fun deleteCategory(categoryEntity: CategoryEntity) {
        appDatabase.categoryEntityDao().delete(categoryEntity)
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return appDatabase.categoryEntityDao().getAllCategoryEntities()
    }

    suspend fun updateCategory(categoryEntity: CategoryEntity, updateCallback: (Boolean) -> Unit) {
        appDatabase.categoryEntityDao().update(categoryEntity)
        updateCallback.invoke(true)
    }

}