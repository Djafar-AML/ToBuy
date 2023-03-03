package com.example.tobuy.ui.fragment.profile.epoxy.models

import androidx.appcompat.app.AlertDialog
import com.example.tobuy.R
import com.example.tobuy.databinding.ModelCategoryBinding
import com.example.tobuy.epxoybinding.ViewBindingKotlinModel
import com.example.tobuy.room.entities.CategoryEntity

data class CategoryEpoxyModel(
    val categoryEntity: CategoryEntity,
) : ViewBindingKotlinModel<ModelCategoryBinding>(R.layout.model_category) {

    override fun ModelCategoryBinding.bind() {
        textView.text = categoryEntity.name

        root.setOnClickListener {
        }

        root.setOnLongClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Delete ${categoryEntity.name}?")
                .setPositiveButton("Yes") { _, _ ->
                }
                .setNegativeButton("Cancel") { _, _ ->
                }
                .show()
            return@setOnLongClickListener true
        }
    }

//    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
//        return totalSpanCount
//    }
}