package com.example.tobuy.ui.fragment.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.databinding.FragmentSortBottomSheetBinding
import com.example.tobuy.ui.fragment.home.bottomsheet.epoxy.controller.BottomSheetEpoxyController
import com.example.tobuy.ui.fragment.home.viewstate.HomeViewState
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SortBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSortBottomSheetBinding? = null
    private val binding by lazy { _binding!! }

    private val sharedViewModel: ToBuyViewModel by activityViewModels()

    private val epoxyController by lazy { initBottomSheetEpoxyController() }

    private fun initBottomSheetEpoxyController(): BottomSheetEpoxyController {
        return BottomSheetEpoxyController(HomeViewState.Sort.values()) {
            sharedViewModel.cuurentSort = it
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSortBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheetEpoxy()
    }

    private fun setupBottomSheetEpoxy() {
        binding.bottomSheetEpoxyRV.setControllerAndBuildModels(epoxyController)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}