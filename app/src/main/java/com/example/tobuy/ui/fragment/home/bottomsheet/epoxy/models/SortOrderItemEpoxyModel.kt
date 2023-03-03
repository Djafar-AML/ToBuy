package com.example.tobuy.ui.fragment.home.bottomsheet.epoxy.models

import com.example.tobuy.R
import com.example.tobuy.databinding.ModelSortOrderItemBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel
import com.example.tobuy.ui.fragment.home.viewstate.HomeViewState

data class SortOrderItemEpoxyModel(
    private val sort: HomeViewState.Sort,
    private val sortSelectCallback: (HomeViewState.Sort) -> Unit
) : ViewBindingKotlinModel<ModelSortOrderItemBinding>(R.layout.model_sort_order_item) {

    override fun ModelSortOrderItemBinding.bind() {

        sortTextView.text = sort.displayName

        root.setOnClickListener {
            sortSelectCallback.invoke(sort)
        }

    }

}
