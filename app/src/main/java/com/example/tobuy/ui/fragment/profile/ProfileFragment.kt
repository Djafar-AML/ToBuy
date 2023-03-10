package com.example.tobuy.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.databinding.FragmentProfileBinding
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.ui.fragment.base.BaseFragment
import com.example.tobuy.ui.fragment.profile.epoxy.controller.ProfileEpoxyController

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding by lazy { _binding!! }

    private val profileEpoxyController =
        ProfileEpoxyController(
            ::onCategoryEmptyStateClick,
            ::onCategoryDeleteCallback,
            ::onPrioritySelectCallback
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEpoxy()
        setupObservers()
    }

    private fun setupEpoxy() {
        binding.epoxyRecyclerView.setController(profileEpoxyController)
    }

    private fun setupObservers() {

        sharedViewModel.categoriesLiveData.observe(viewLifecycleOwner) { categories ->
            profileEpoxyController.categories = categories
        }

    }

    private fun onCategoryEmptyStateClick() {
        val direction = ProfileFragmentDirections.actionProfileFragmentToAddCategoryFragment()
        navigateViaNavGraph(direction)
    }

    private fun onCategoryDeleteCallback(categoryEntity: CategoryEntity) {
        sharedViewModel.deleteCategory(categoryEntity)
    }

    private fun onPrioritySelectCallback(priorityName: String) {

        val direction =
            ProfileFragmentDirections.actionProfileFragmentToColorPickerFragment(priorityName)
        navigateViaNavGraph(direction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}