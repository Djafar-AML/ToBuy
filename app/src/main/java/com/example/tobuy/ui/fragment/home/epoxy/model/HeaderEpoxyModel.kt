package com.example.tobuy.ui.fragment.home.epoxy.model

import com.example.tobuy.R
import com.example.tobuy.databinding.ModelHeaderItemBinding
import com.example.tobuy.ui.fragment.home.epoxy.ViewBindingKotlinModel

data class HeaderEpoxyModel(val headerText: String) :
    ViewBindingKotlinModel<ModelHeaderItemBinding>(R.layout.model_header_item) {

    override fun ModelHeaderItemBinding.bind() {
        textView.text = headerText
    }
}
