package com.example.tobuy.ui.fragment.profile.epoxy.models

import com.example.tobuy.R
import com.example.tobuy.databinding.ModelPriorityColorItemBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel

data class PriorityColorItemEpoxyModel(
    val displayText: String,
    val displayColor: Int,
    val onPrioritySelectCallback: (String) -> Unit
) : ViewBindingKotlinModel<ModelPriorityColorItemBinding>(R.layout.model_priority_color_item) {

    override fun ModelPriorityColorItemBinding.bind() {

        priorityTextView.text = displayText

        priorityImageView.apply {
            setBackgroundColor(displayColor)
            setOnClickListener { onPrioritySelectCallback.invoke(displayText) }
        }

        root.strokeColor = displayColor

    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}