package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.room.entity.CategoryEntity
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.room.entity.ItemWithCategoryEntity
import com.example.tobuy.ui.fragment.home.add.isOnItemSelectEdit
import com.example.tobuy.ui.fragment.profile.add.CategoryViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToBuyViewModel @Inject constructor(private val toBuyRepo: ToButRepo) : ViewModel() {


    private lateinit var coroutineScope: CoroutineScope

    private val _itemEntitiesLiveData = MutableLiveData<List<ItemEntity>>()
    val itemEntitiesLiveData: LiveData<List<ItemEntity>> = _itemEntitiesLiveData

    private val _allItemWithCategoryEntity = MutableLiveData<List<ItemWithCategoryEntity>>()
    val allItemWithCategoryEntity: LiveData<List<ItemWithCategoryEntity>> =
        _allItemWithCategoryEntity

    private val _categoriesViewStateLiveData = MutableLiveData<CategoryViewState>()
    val categoriesViewState: LiveData<CategoryViewState> = _categoriesViewStateLiveData

    private val _transactionInsertLiveData = MutableLiveData<Event<Boolean>>()
    val transactionInsertLiveData: LiveData<Event<Boolean>> = _transactionInsertLiveData

    private val _transactionUpdateLiveData = MutableLiveData<Event<Boolean>>()
    val transactionUpdateLiveData: LiveData<Event<Boolean>> = _transactionUpdateLiveData

    private val _categoriesLiveData = MutableLiveData<List<CategoryEntity>>()
    val categoriesLiveData: LiveData<List<CategoryEntity>> = _categoriesLiveData

    private val _transactionAddCategoryLiveData = MutableLiveData<Event<Boolean>>()
    val transactionAddCategoryLiveData: LiveData<Event<Boolean>> = _transactionAddCategoryLiveData

    init {

        initCoroutineScope()

        coroutineScope.launch {
            toBuyRepo.getAllItemWithCategoryEntity().collect { items ->
                _allItemWithCategoryEntity.postValue(items)
            }

        }

        viewModelScope.launch {
            toBuyRepo.getAllCategories().collect { categories ->
                _categoriesLiveData.postValue(categories)
            }
        }
    }

    private fun initCoroutineScope() {
        coroutineScope = viewModelScope
    }

    // region item entity realm
    fun insertItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.insertItem(itemEntity) { complete ->
                _transactionInsertLiveData.postValue(Event(complete))
            }
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.deleteItem(itemEntity)
        }

    }

    fun updateItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.updateItem(itemEntity) { isSuccess ->
                if (isOnItemSelectEdit)
                    _transactionUpdateLiveData.postValue(Event(isSuccess))
            }
        }

    }
    //endregion

    // region Category entity

    fun insertCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            toBuyRepo.insertCategory(categoryEntity) { complete ->
                _transactionAddCategoryLiveData.postValue(Event(complete))
            }
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            toBuyRepo.deleteCategory(categoryEntity)
        }

    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            toBuyRepo.updateCategory(categoryEntity) { isSuccess ->
            }
        }

    }

    // endregion

    fun categoryList() = categoriesLiveData.value ?: emptyList()

    fun loadCategories(categoryId: String, categories: List<CategoryEntity>) {

        val viewStateItemList = ArrayList<CategoryViewState.Item>()

        categories.forEach {
            viewStateItemList.add(
                CategoryViewState.Item(
                    categoryEntity = it,
                    isSelected = it.id == categoryId
                )
            )
        }

        val viewState = CategoryViewState(itemList = viewStateItemList)
        _categoriesViewStateLiveData.postValue(viewState)

    }

    fun enableCategoriesLoadingState() {
        val loadingViewState = CategoryViewState(isLoading = true)
        _categoriesViewStateLiveData.value = (loadingViewState)
    }

    fun categoryEmptyList() {
        val viewState = CategoryViewState(isListEmpty = true)
        _categoriesViewStateLiveData.postValue(viewState)
    }

    private fun cancelTheCoroutineScope() {
        if (::coroutineScope.isInitialized)
            coroutineScope.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cancelTheCoroutineScope()
    }

}