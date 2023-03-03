package com.example.tobuy.ui.fragment.profile.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.databinding.FragmentAddCategoryBinding
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.ui.fragment.base.BaseFragment
import java.util.*

class AddCategoryFragment : BaseFragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding by lazy { _binding!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyboard(binding.categoryNameEditText)
        setupClickListeners()
        setupObservers()
    }

    private fun setupObservers() {

        sharedViewModel.transactionAddCategoryLiveData.observe(viewLifecycleOwner) { completed ->
            completed?.getContent()?.let {
                navigateUp()
            }
        }
    }

    private fun setupClickListeners() {

        binding.apply {

            saveButton.setOnClickListener {
                val isUserInputValid = validateUserInput()
                if (isUserInputValid)
                    saveItemCategoryToDatabase(readCategoryEntityFromUserInput())

            }

        }
    }

    private fun saveItemCategoryToDatabase(categoryEntity: CategoryEntity) {
        sharedViewModel.insertCategory(categoryEntity)
    }

    private fun readCategoryEntityFromUserInput(): CategoryEntity {

        val categoryName = binding.categoryNameEditText.text.toString().trim()

        return CategoryEntity(
            id = UUID.randomUUID().toString(),
            name = categoryName
        )

    }


    private fun validateUserInput(): Boolean {

        val categoryName = binding.categoryNameEditText.text.toString().trim()

        if (categoryName.isEmpty()) {
            binding.categoryNameTextField.error = "* Required field"
            return false
        }

        binding.categoryNameTextField.error = null

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}