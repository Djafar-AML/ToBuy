package com.example.tobuy.ui.fragment.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.ui.fragment.base.BaseFragment
import java.util.*

class AddItemEntityFragment : BaseFragment() {

    private var _binding: FragmentAddItemEntityBinding? = null
    private val binding by lazy { _binding!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemEntityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.apply {
            saveButton.setOnClickListener {
                val isUserInputValid = validateUserInput()
                if (isUserInputValid)
                    saveItemEntityToDatabase(readItemEntityFromUserInput())
            }
        }
    }

    private fun validateUserInput(): Boolean {

        val itemTitle = binding.titleEditText.text.toString().trim()

        if (itemTitle.isEmpty()) {
            binding.titleTextField.error = "* Required field"
            return false
        }

        binding.titleTextField.error = null

        return true
    }


    private fun readItemEntityFromUserInput(): ItemEntity {

        val itemTitle = binding.titleEditText.text.toString().trim()
        val itemDescription = binding.descriptionEditText.text.toString().trim()

        val itemPriority = when (binding.radioGroup.checkedRadioButtonId) {
            binding.radioButtonHigh.id -> 3
            binding.radioButtonMedium.id -> 2
            binding.radioButtonLow.id -> 1
            else -> {
                0
            }
        }

        return ItemEntity(
            id = UUID.randomUUID().toString(),
            title = itemTitle,
            description = itemDescription,
            priority = itemPriority,
            createdAt = System.currentTimeMillis(),
            categoryId = "" //todo update this later
        )
    }

    private fun saveItemEntityToDatabase(itemEntity: ItemEntity) {
        sharedViewModel.insertItem(itemEntity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}