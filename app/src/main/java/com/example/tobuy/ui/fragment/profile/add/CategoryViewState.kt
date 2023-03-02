package com.example.tobuy.ui.fragment.profile.add

import com.example.tobuy.room.entity.CategoryEntity

data class CategoryViewState(
    val isLoading: Boolean = false,
    val isListEmpty: Boolean = false,
    val itemList: List<Item> = emptyList()
) {

    data class Item(
        val categoryEntity: CategoryEntity = CategoryEntity(),
        var isSelected: Boolean = false
    )
}
