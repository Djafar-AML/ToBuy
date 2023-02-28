package com.example.tobuy.ui.fragment.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.ui.fragment.base.BaseFragment
import java.util.*

class AddItemEntityFragment : BaseFragment() {

    private var _binding: FragmentAddItemEntityBinding? = null
    private val binding by lazy { _binding!! }

    private val safeArgs: AddItemEntityFragmentArgs by navArgs()
    private val selectedItemEntity: ItemEntity? by lazy { findSelectedItemEntityWithId() }
    private fun findSelectedItemEntityWithId() = sharedViewModel.itemEntitiesLiveData.value?.find {
        it.id == safeArgs.entityId
    }

    private var isInEditMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemEntityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSoftKeyboard(binding.titleEditText)
        setupObservers()
        setupClickListeners()
        setupEditMode()
    }


    private fun setupObservers() {

        sharedViewModel.transactionInsertLiveData.observe(viewLifecycleOwner) { complete ->

            if (complete) {

                showToastMessage("item saved!")
                resetAddItemEntityViews()
                resetUpsertTransactionLiveDataState()
            }
        }

        sharedViewModel.transactionUpdateLiveData.observe(viewLifecycleOwner) { complete ->

            if (complete) {
                showToastMessage("item updated!")
                resetUpsertTransactionLiveDataState()
            }

        }
    }

    private fun resetAddItemEntityViews() {

        binding.apply {

            titleEditText.text = null
            titleEditText.requestFocus()

            descriptionEditText.text = null
            radioGroup.check(radioButtonLow.id)
        }
    }

    private fun resetUpsertTransactionLiveDataState() {
        sharedViewModel.resetUpsertTransactionLiveDataState()
    }

    private fun setupClickListeners() {

        binding.apply {

            saveButton.setOnClickListener {

                val isUserInputValid = validateUserInput()

                if (isUserInputValid && !isInEditMode)
                    saveItemEntityToDatabase(readItemEntityFromUserInput())
                else if (isUserInputValid) {
                    updateTheItemEntity(readUpdateDItemDataFromUserInput())
                }
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

        val itemPriority = itemPriority()

        return ItemEntity(
            id = UUID.randomUUID().toString(),
            title = itemTitle,
            description = itemDescription,
            priority = itemPriority,
            createdAt = System.currentTimeMillis(),
            categoryId = "" //todo update this later
        )

    }

    private fun itemPriority() = when (binding.radioGroup.checkedRadioButtonId) {
        binding.radioButtonHigh.id -> 3
        binding.radioButtonMedium.id -> 2
        binding.radioButtonLow.id -> 1
        else -> 0
    }

    private fun saveItemEntityToDatabase(itemEntity: ItemEntity) {
        sharedViewModel.insertItem(itemEntity)
    }

    private fun readUpdateDItemDataFromUserInput(): ItemEntity? {

        return selectedItemEntity?.copy(
            title = binding.titleEditText.text.toString(),
            description = binding.descriptionEditText.text.toString(),
            priority = itemPriority()
        )
    }

    private fun updateTheItemEntity(updateItemEntity: ItemEntity?) {

        updateItemEntity?.let { sharedViewModel.updateItem(updateItemEntity) }
    }

    private fun setupEditMode() {

        selectedItemEntity?.let { itemEntity ->

            isInEditMode = true

            binding.apply {

                titleEditText.setText(itemEntity.title)
                titleEditText.setSelection(itemEntity.title.length)
                descriptionEditText.setText(itemEntity.description)

                when (itemEntity.priority) {
                    1 -> radioGroup.check(radioButtonLow.id)
                    2 -> radioGroup.check(radioButtonMedium.id)
                    else -> radioGroup.check(radioButtonHigh.id)
                }

                saveButton.text = "Update"
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}