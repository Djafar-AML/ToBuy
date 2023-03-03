package com.example.tobuy.ui.fragment.profile.epoxy.models

import com.example.tobuy.R
import com.example.tobuy.databinding.ModelEmptyButtonBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel

data class EmptyButtonEpoxyModel(
    val buttonText: String,
    val onCategoryEmptyStateClick: () -> Unit,
) : ViewBindingKotlinModel<ModelEmptyButtonBinding>(R.layout.model_empty_button) {

    override fun ModelEmptyButtonBinding.bind() {
        button.text = buttonText
        button.setOnClickListener { onCategoryEmptyStateClick.invoke() }
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}