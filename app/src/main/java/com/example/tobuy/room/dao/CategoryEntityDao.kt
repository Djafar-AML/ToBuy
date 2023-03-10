package com.example.tobuy.room.dao

import androidx.room.*
import com.example.tobuy.room.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryEntityDao {

    @Query("SELECT * FROM category_entities")
    fun getAllCategoryEntities(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

}