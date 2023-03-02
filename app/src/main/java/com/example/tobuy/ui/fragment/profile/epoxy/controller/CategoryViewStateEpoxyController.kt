package com.example.tobuy.ui.fragment.profile.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.ui.fragment.home.epoxy.model.LoadingEpoxyModel
import com.example.tobuy.ui.fragment.profile.add.CategoryViewState
import com.example.tobuy.ui.fragment.profile.epoxy.model.CategoryItemSelectionEpoxyModel

class CategoryViewStateEpoxyController(
    private val onCategorySelected: (String) -> Unit
) : EpoxyController() {

    var viewState = CategoryViewState()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if (viewState.isLoading) {
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        if (viewState.isListEmpty) {
            // empty list
            return
        }

        viewState.itemList.forEach { item ->
            CategoryItemSelectionEpoxyModel(item, onCategorySelected).id(item.categoryEntity.id)
                .addTo(this)
        }
    }
}