package com.example.tobuy.ui.fragment.home.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.room.entity.CategoryEntity
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.room.entity.ItemWithCategoryEntity
import com.example.tobuy.ui.fragment.home.epoxy.model.EmptyStateExpoyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.HeaderEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.ItemEntityEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.LoadingEpoxyModel

class HomeEpoxyController(
    private val onBumpPriority: (ItemEntity) -> Unit,
    private val onItemSelect: (ItemEntity) -> Unit
) : EpoxyController() {

    var isLoading = true
        set(value) {
            field = value
            if (field)
                requestModelBuild()
        }

//    var itemEntityList = ArrayList<ItemEntity>()
//        set(value) {
//            field = value
//            isLoading = false
//            requestModelBuild()
//        }

    var itemWithCategoryList = ArrayList<ItemWithCategoryEntity>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }
    override fun buildModels() {

        if (isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if (itemWithCategoryList.isEmpty()) {
            EmptyStateExpoyModel().id("empty_state").addTo(this)
            return
        }

        var currentPriority = -1

        itemWithCategoryList
            .sortedByDescending { it.itemEntity.createdAt }
            .sortedByDescending { it.itemEntity.priority }
            .forEach { itemWithCategory ->

                if (currentPriority != itemWithCategory.itemEntity.priority) {

                    currentPriority = itemWithCategory.itemEntity.priority
                    val headerText = headerTextBasedOnPriority(currentPriority)
                    HeaderEpoxyModel(headerText).id(headerText).addTo(this)

                }

                ItemEntityEpoxyModel(itemWithCategory, onBumpPriority, onItemSelect).id(itemWithCategory.itemEntity.id)
                    .addTo(this)
            }

    }

    private fun headerTextBasedOnPriority(priority: Int): String {

        return when (priority) {
            1 -> "Low"
            2 -> "Medium"
            else -> "High"
        }
    }

}