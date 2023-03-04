package com.example.tobuy.ui.fragment.home.bottomsheet.epoxy.controller

import com.airbnb.epoxy.EpoxyController
import com.example.tobuy.ui.fragment.home.bottomsheet.epoxy.models.SortOrderItemEpoxyModel
import com.example.tobuy.ui.fragment.home.viewstate.HomeViewState
import java.util.*

class BottomSheetEpoxyController(
    private val sortOptions: Array<HomeViewState.Sort>,
    private val sortSelectCallback: (HomeViewState.Sort) -> Unit
) : EpoxyController() {

    override fun buildModels() {

        sortOptions.forEach {
            SortOrderItemEpoxyModel(
                it,
                sortSelectCallback
            ).id("${it.displayName} ${UUID.randomUUID()}").addTo(this)
        }
    }

}