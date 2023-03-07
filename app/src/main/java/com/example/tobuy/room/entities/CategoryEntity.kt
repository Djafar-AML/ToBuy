package com.example.tobuy.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "category_entities")
data class CategoryEntity(
    @PrimaryKey
    val id: String = "",
    val name: String = ""
)