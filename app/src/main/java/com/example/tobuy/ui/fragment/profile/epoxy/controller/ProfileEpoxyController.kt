package com.example.tobuy.ui.fragment.profile.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.prefs.Prefs
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.ui.fragment.home.epoxy.models.HeaderEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.models.CategoryEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.models.EmptyButtonEpoxyModel
import com.example.tobuy.ui.fragment.profile.epoxy.models.PriorityColorItemEpoxyModel
import java.util.*

class ProfileEpoxyController(
    private val onCategoryEmptyStateClick: () -> Unit,
    private val onCategoryDeleteCallback: (CategoryEntity) -> Unit,
    private val onPrioritySelectCallback: (String) -> Unit
) : EpoxyController() {

    var categories: List<CategoryEntity> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        addHeader("Category")

        addCategoriesModel()

        addEmptyModel()

        addHeader("Priorities")

        addColorPickerModels()

    }

    private fun addHeader(headerText: String) {

        HeaderEpoxyModel(headerText)
            .id("category_id ${UUID.randomUUID()}")
            .addTo(this)
    }

    private fun addCategoriesModel() {

        categories.forEach {
            CategoryEpoxyModel(it, onCategoryDeleteCallback)
                .id(it.id)
                .addTo(this)
        }
    }

    private fun addEmptyModel() {

        EmptyButtonEpoxyModel(
            "Add Category",
            onCategoryEmptyStateClick
        ).id("add_category ${UUID.randomUUID()}")
            .addTo(this)
    }

    private fun addColorPickerModels() {

        val highPriorityColor = Prefs.getHighPriorityColor()
        val mediumPriorityColor = Prefs.getMediumPriorityColor()
        val lowPriorityColor = Prefs.getLowPriorityColor()

        createPriorityColorEpoxyModel("High", highPriorityColor, onPrioritySelectCallback)
        createPriorityColorEpoxyModel("Medium", mediumPriorityColor, onPrioritySelectCallback)
        createPriorityColorEpoxyModel("Low", lowPriorityColor, onPrioritySelectCallback)

    }

    private fun createPriorityColorEpoxyModel(
        priorityName: String,
        priorityColor: Int,
        onPrioritySelectCallback: (String) -> Unit
    ) {
        PriorityColorItemEpoxyModel(priorityName, priorityColor, onPrioritySelectCallback)
            .id("$priorityName ${UUID.randomUUID()}").addTo(this)
    }

}