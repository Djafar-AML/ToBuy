package com.example.tobuy.ui.fragment.home.epoxy.models

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.tobuy.R
import com.example.tobuy.databinding.ModelItemEntityBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel
import com.example.tobuy.prefs.Prefs
import com.example.tobuy.room.entities.ItemEntity
import com.example.tobuy.room.entities.ItemWithCategoryEntity

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

        val color = colorBasedOnPriority()
        priorityTextView.setBackgroundColor(color)
        root.strokeColor = color

        itemWithCategoryEntity.categoryEntity?.name.let {
            categoryNameTextView.visibility = View.VISIBLE
            categoryNameTextView.text = itemWithCategoryEntity.categoryEntity?.name
        }

        priorityTextView.setOnClickListener { onBumpPriority.invoke(itemWithCategoryEntity.itemEntity) }

        root.setOnClickListener { onItemSelect.invoke(itemWithCategoryEntity.itemEntity) }

    }

    private fun colorBasedOnPriority() = when (itemWithCategoryEntity.itemEntity.priority) {
        1 -> Prefs.getLowPriorityColor()
        2 -> Prefs.getMediumPriorityColor()
        3 -> Prefs.getHighPriorityColor()
        else -> R.color.gray_700
    }

}
