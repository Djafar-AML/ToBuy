package com.example.tobuy.ui.fragment.home.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.ui.fragment.home.epoxy.model.ItemEntityEpoxyModel
import com.example.tobuy.ui.fragment.home.epoxy.model.LoadingEpoxyModel

class HomeEpoxyController(
    private val onDeleteItemCallback: (ItemEntity) -> Unit,
    private val onBumpPriority: (ItemEntity) -> Unit
) : EpoxyController() {

    var isLoading = true
        set(value) {
            field = value
            if (field)
                requestModelBuild()
        }

    var itemEntityList = ArrayList<ItemEntity>()
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

        if (itemEntityList.isEmpty()) {
            // todo empty state
            return
        }

        itemEntityList.forEach { itemEntity ->
            ItemEntityEpoxyModel(itemEntity, onDeleteItemCallback, onBumpPriority).id(itemEntity.id)
                .addTo(this)
        }

    }
}