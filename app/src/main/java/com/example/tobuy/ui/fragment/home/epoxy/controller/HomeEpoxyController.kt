package com.example.tobuy.ui.fragment.home.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.room.entity.ItemWithCategoryEntity
import com.example.tobuy.ui.fragment.home.epoxy.model.EmptyStateExpoyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.HeaderEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.ItemEntityEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.LoadingEpoxyModel
import com.example.tobuy.ui.fragment.home.viewstate.HomeViewState

class HomeEpoxyController(
    private val onBumpPriority: (ItemEntity) -> Unit,
    private val onItemSelect: (ItemEntity) -> Unit
) : EpoxyController() {

    var viewState = HomeViewState(isLoading = true)
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if (viewState.isLoading) {
            LoadingEpoxyModel().id("loading_state").addTo(this)
            return
        }

        if (viewState.dataList.isEmpty()) {
            EmptyStateExpoyModel().id("empty_state").addTo(this)
            return
        }

        viewState.dataList.forEach { dataItem ->

            if (dataItem.isHeader) {
                HeaderEpoxyModel(dataItem.data as String).id("header ${dataItem.data}").addTo(this)
                return@forEach
            }

            val itemWithCategoryEntity = dataItem.data as ItemWithCategoryEntity
            ItemEntityEpoxyModel(itemWithCategoryEntity, onBumpPriority, onItemSelect)
                .id(itemWithCategoryEntity.itemEntity.categoryId)
                .addTo(this)
        }

    }

}