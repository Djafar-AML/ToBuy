package com.example.tobuy.ui.fragment.profile.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.ui.fragment.home.epoxy.models.HeaderEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.models.CategoryEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.models.EmptyButtonEpoxyModel
import java.util.UUID

class ProfileEpoxyController(
    private val onCategoryEmptyStateClick: () -> Unit,
    private val onCategoryDeleteCallback: (CategoryEntity) -> Unit
) :
    EpoxyController() {

    var categories: List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        HeaderEpoxyModel("Category").id("category_id ${UUID.randomUUID()}").addTo(this)

        categories.forEach {
            CategoryEpoxyModel(it, onCategoryDeleteCallback).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category", onCategoryEmptyStateClick).id("add_category ${UUID.randomUUID()}")
            .addTo(this)

    }
}