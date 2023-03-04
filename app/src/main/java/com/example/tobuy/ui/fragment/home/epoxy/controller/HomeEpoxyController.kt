package com.example.tobuy.ui.fragment.home.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.room.entities.ItemEntity
import com.example.tobuy.room.entities.ItemWithCategoryEntity
import com.example.tobuy.ui.fragment.home.epoxy.models.EmptyStateExpoyModel
import com.example.tobuy.ui.fragment.home.epoxy.models.HeaderEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.models.ItemEntityEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.models.LoadingEpoxyModel
import com.example.tobuy.ui.fragment.home.viewstate.HomeViewState
import java.util.UUID

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
            LoadingEpoxyModel().id("loading_state ${UUID.randomUUID()}").addTo(this)
            return
        }

        if (viewState.dataList.isEmpty()) {
            EmptyStateExpoyModel().id("empty_state ${UUID.randomUUID()}").addTo(this)
            return
        }

        viewState.dataList.forEach { dataItem ->

            if (dataItem.isHeader) {
                HeaderEpoxyModel(dataItem.data as String).id("header ${UUID.randomUUID()}").addTo(this)
                return@forEach
            }

            val itemWithCategoryEntity = dataItem.data as ItemWithCategoryEntity
            ItemEntityEpoxyModel(itemWithCategoryEntity, onBumpPriority, onItemSelect)
                .id(itemWithCategoryEntity.itemEntity.categoryId)
                .addTo(this)
        }

    }

}