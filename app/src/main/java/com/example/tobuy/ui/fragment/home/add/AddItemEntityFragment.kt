package com.example.tobuy.ui.fragment.home.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.navigation.fragment.navArgs
import com.example.tobuy.databinding.FragmentAddItemEntityBinding
import com.example.tobuy.room.entities.ItemEntity
import com.example.tobuy.room.entities.ItemWithCategoryEntity
import com.example.tobuy.ui.fragment.base.BaseFragment
import com.example.tobuy.ui.fragment.profile.epoxy.controller.CategoryViewStateEpoxyController
import java.util.*

var isOnItemSelectEdit: Boolean = false

class AddItemEntityFragment : BaseFragment() {


    private var _binding: FragmentAddItemEntityBinding? = null
    private val binding by lazy { _binding!! }

    private val safeArgs: AddItemEntityFragmentArgs by navArgs()
    private val selectedItemEntity: ItemWithCategoryEntity? by lazy { findSelectedItemEntityWithId() }
    private fun findSelectedItemEntityWithId() =
        sharedViewModel.allItemWithCategoryEntity.value?.find {
            it.itemEntity.id == safeArgs.entityId
        }

    private var isInEditMode: Boolean = false

    private lateinit var categoryViewStateEpoxyController: CategoryViewStateEpoxyController
    private var selectedCategoryId: String = ""

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
        setupCategoriesEpoxy()
        setupCategoryListData()

    }

    private fun onCategorySelected(categoryId: String) {
        selectedCategoryId = categoryId
        sharedViewModel.loadCategories(selectedCategoryId, sharedViewModel.categoryList())
    }

    private fun setupObservers() {

        sharedViewModel.transactionInsertLiveData.observe(viewLifecycleOwner) { completed ->

            completed?.getContent()?.let {
                resetAddItemEntityViews()
                showToastMessage("item saved!")
            }
        }

        sharedViewModel.transactionUpdateLiveData.observe(viewLifecycleOwner) { completed ->

            completed?.getContent()?.let {
                showToastMessage("item updated!")
            }
        }

        sharedViewModel.categoriesViewState.observe(viewLifecycleOwner) { viewState ->
            categoryViewStateEpoxyController.viewState = viewState
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

            quantitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {


                    val isValidInput = validateUserInput()

                    if (isValidInput.not()) {
                        return
                    }

                    val sanitizedText = modifyTextBasedOnSeekBar(progress)

                    binding.titleEditText.apply {
                        setText(sanitizedText)
                        setSelection(sanitizedText.length)
                    }

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun modifyTextBasedOnSeekBar(progress: Int): String {

        val currentTitleText = binding.titleEditText.text.toString().trim()
        val startIndex = currentTitleText.indexOf("<") - 1
        val newText = if (startIndex > 0) {
            "${currentTitleText.substring(0, startIndex)} <$progress>"
        } else {
            "$currentTitleText <$progress>"
        }

        return newText.replace(" <0>", "")
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
            categoryId = selectedCategoryId
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

        return selectedItemEntity?.itemEntity?.copy(
            title = binding.titleEditText.text.toString(),
            description = binding.descriptionEditText.text.toString(),
            priority = itemPriority(),
            categoryId = selectedCategoryId
        )
    }

    private fun updateTheItemEntity(updateItemEntity: ItemEntity?) {

        isOnItemSelectEdit = true
        updateItemEntity?.let { sharedViewModel.updateItem(updateItemEntity) }
    }

    private fun setupEditMode() {

        selectedItemEntity?.let { itemWithCategoryEntity ->

            isInEditMode = true

            binding.apply {

                titleEditText.setText(itemWithCategoryEntity.itemEntity.title)
                titleEditText.setSelection(itemWithCategoryEntity.itemEntity.title.length)
                descriptionEditText.setText(itemWithCategoryEntity.itemEntity.description)

                when (itemWithCategoryEntity.itemEntity.priority) {
                    1 -> radioGroup.check(radioButtonLow.id)
                    2 -> radioGroup.check(radioButtonMedium.id)
                    else -> radioGroup.check(radioButtonHigh.id)
                }

                setSeekBarQuantityCount(itemWithCategoryEntity.itemEntity)
                setSelectedCategoryUi(itemWithCategoryEntity.itemEntity.categoryId)
                saveButton.text = "Update"
            }
        }

    }

    private fun setSelectedCategoryUi(categoryId: String) {
        selectedCategoryId = categoryId
    }

    private fun setSeekBarQuantityCount(itemEntity: ItemEntity) {

        if (itemEntity.title.contains(" <")) {

            val startIndex = itemEntity.title.indexOf("<") + 1
            val endIndex = itemEntity.title.indexOf(">")
            try {
                val progress = itemEntity.title.substring(startIndex, endIndex).toInt()
                binding.quantitySeekBar.progress = progress
            } catch (e: Exception) {
                return
            }
        }
    }

    private fun setupCategoriesEpoxy() {

        categoryViewStateEpoxyController = CategoryViewStateEpoxyController(::onCategorySelected)

        binding.categoryEpoxyRecyclerView.setController(categoryViewStateEpoxyController)
    }

    private fun setupCategoryListData() {

        val categories = sharedViewModel.categoryList()

        if (categories.isNotEmpty()) {

            sharedViewModel.enableCategoriesLoadingState()

            val categoryId = selectedItemEntity?.categoryEntity?.id ?: ""
            sharedViewModel.loadCategories(categoryId, categories)

        } else {
            sharedViewModel.categoryEmptyList()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}