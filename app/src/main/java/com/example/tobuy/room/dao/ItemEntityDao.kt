package com.example.tobuy.room.dao

import androidx.room.*
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.room.entity.ItemWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemEntityDao {

    @Query("SELECT * FROM item_entity")
    fun getAllItemEntities(): Flow<List<ItemEntity>>

    @Transaction
    @Query("SELECT * FROM item_entity")
    fun getAllItemWithCategoryEntity(): Flow<List<ItemWithCategoryEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemEntity: ItemEntity)

    @Delete
    suspend fun delete(itemEntity: ItemEntity)
    @Update
    suspend fun update(itemEntity: ItemEntity)

}