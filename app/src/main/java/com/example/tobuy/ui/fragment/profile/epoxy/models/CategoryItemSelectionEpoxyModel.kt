package com.example.tobuy.ui.fragment.profile.epoxy.models

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.example.tobuy.R
import com.example.tobuy.databinding.ModelCategoryItemSelectionBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel
import com.example.tobuy.ui.fragment.profile.add.CategoryViewState

data class CategoryItemSelectionEpoxyModel(
    val item: CategoryViewState.Item,
    val onCategorySelected: (String) -> Unit
) :
    ViewBindingKotlinModel<ModelCategoryItemSelectionBinding>(R.layout.model_category_item_selection) {

    override fun ModelCategoryItemSelectionBinding.bind() {

        categoryNameTextView.text = item.categoryEntity.name

        colorize()

        root.setOnClickListener { onCategorySelected(item.categoryEntity.id) }
    }

    private fun ModelCategoryItemSelectionBinding.colorize() {

        val colorRes =
            if (!item.isSelected) R.color.primaryColor else R.color.secondaryColor

        val context = root.context

        val color = ContextCompat.getColor(context, colorRes)

        categoryNameTextView.setTextColor(color)
        root.setStrokeColor(ColorStateList.valueOf(color))
    }
}