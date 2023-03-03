package com.example.tobuy.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.tobuy.databinding.FragmentHomeBinding
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.ui.fragment.base.BaseFragment
import com.example.tobuy.ui.fragment.home.add.isOnItemSelectEdit
import com.example.tobuy.ui.fragment.home.epoxy.controller.HomeEpoxyController
import com.example.tobuy.ui.fragment.home.epoxy.model.ItemEntityEpoxyModel

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding by lazy { _binding!! }

    private val epoxyController by lazy { initHomeEpoxyController() }

    private fun initHomeEpoxyController() =
        HomeEpoxyController(::onBumpPriority, ::onItemSelect)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEpoxyRecyclerView()
        setupSwipeToDelete()
        setupObservers()
        setupClickListeners()

    }

    private fun setupEpoxyRecyclerView() {
        binding.homeEpoxyRV.setController(epoxyController)
    }

    private fun setupSwipeToDelete() {
        EpoxyTouchHelper.initSwiping(binding.homeEpoxyRV)
            .right()
            .withTarget(ItemEntityEpoxyModel::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<ItemEntityEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: ItemEntityEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val itemEntity = model?.itemWithCategoryEntity ?: return
                    sharedViewModel.deleteItem(itemEntity.itemEntity)
                }
            })
    }

    private fun setupObservers() {
        sharedViewModel.homeViewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            epoxyController.viewState = viewState
        }
    }

    private fun setupClickListeners() {

        binding.fab.setOnClickListener {
            val dest = HomeFragmentDirections.actionHomeFragmentToAddItemEntityFragment()
            navigateViaNavGraph(dest)
        }

    }

    private fun onBumpPriority(itemEntity: ItemEntity) {

        val newPriority = updatePriority(itemEntity)

        val updatedItemEntity = itemEntity.copy(priority = newPriority)
        isOnItemSelectEdit = false
        sharedViewModel.updateItem(updatedItemEntity)

    }

    private fun onItemSelect(itemEntity: ItemEntity) {
        val dest = HomeFragmentDirections.actionHomeFragmentToAddItemEntityFragment(itemEntity.id)
        navigateViaNavGraph(dest)
    }

    private fun updatePriority(itemEntity: ItemEntity): Int {

        val currentPriority = itemEntity.priority
        var newPriority = currentPriority + 1
        if (newPriority > 3) {
            newPriority = 1
        }

        return newPriority
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}