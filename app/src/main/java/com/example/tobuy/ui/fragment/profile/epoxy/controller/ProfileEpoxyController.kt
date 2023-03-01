package com.example.tobuy.ui.fragment.profile.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.room.entity.CategoryEntity
import com.example.tobuy.ui.fragment.home.epoxy.model.HeaderEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.model.CategoryEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.model.EmptyButtonEpoxyModel

class ProfileEpoxyController(private val onCategoryEmptyStateClick: () -> Unit) :
    EpoxyController() {

    var categories: List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        HeaderEpoxyModel("Category").id("category_id").addTo(this)

        categories.forEach {
            CategoryEpoxyModel(it).id(it.id).addTo(this)
        }

        EmptyButtonEpoxyModel("Add Category", onCategoryEmptyStateClick).id("add_category")
            .addTo(this)

    }
}