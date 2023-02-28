package com.example.tobuy.ui.fragment.home.epoxy.model

import com.airbnb.epoxy.EpoxyModel
import com.example.tobuy.R
import com.example.tobuy.databinding.ModelEmptyStateBinding
import com.example.tobuy.ui.fragment.home.epoxy.ViewBindingKotlinModel

class EmptyStateExpoyModel: ViewBindingKotlinModel<ModelEmptyStateBinding>(R.layout.model_empty_state) {

    override fun ModelEmptyStateBinding.bind() {
        // nothing to do
    }
}