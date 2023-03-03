package com.example.tobuy.ui.fragment.home.epoxy.model

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.tobuy.R
import com.example.tobuy.databinding.ModelItemEntityBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.room.entity.ItemWithCategoryEntity

data class ItemEntityEpoxyModel(
    val itemWithCategoryEntity: ItemWithCategoryEntity,
    private val onBumpPriority: (ItemEntity) -> Unit,
    private val onItemSelect: (ItemEntity) -> Unit
) :
    ViewBindingKotlinModel<ModelItemEntityBinding>(
        R.layout.model_item_entity
    ) {

    override fun ModelItemEntityBinding.bind() {

        titleTextView.text = itemWithCategoryEntity.itemEntity.title

        if (itemWithCategoryEntity.itemEntity.description == null) {
            descriptionTextView.isGone = true
        } else {
            descriptionTextView.isVisible = true
            descriptionTextView.text = itemWithCategoryEntity.itemEntity.description
        }

        val colorRes = colorResBasedOnPriority()
        val color = ContextCompat.getColor(root.context, colorRes)
        priorityTextView.setBackgroundColor(color)
        root.strokeColor = color

        itemWithCategoryEntity.categoryEntity?.name .let {
            categoryNameTextView.visibility = View.VISIBLE
            categoryNameTextView.text = itemWithCategoryEntity.categoryEntity?.name
        }

        priorityTextView.setOnClickListener { onBumpPriority.invoke(itemWithCategoryEntity.itemEntity) }

        root.setOnClickListener { onItemSelect.invoke(itemWithCategoryEntity.itemEntity) }

    }

    private fun colorResBasedOnPriority() = when (itemWithCategoryEntity.itemEntity.priority) {
        1 -> android.R.color.holo_green_dark
        2 -> android.R.color.holo_orange_dark
        3 -> android.R.color.holo_red_dark
        else -> R.color.gray_700
    }

}
