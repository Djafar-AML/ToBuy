package com.example.tobuy.arch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.extensions.asLiveData
import com.example.tobuy.room.entities.CategoryEntity
import com.example.tobuy.room.entities.ItemEntity
import com.example.tobuy.room.entities.ItemWithCategoryEntity
import com.example.tobuy.ui.fragment.home.add.isOnItemSelectEdit
import com.example.tobuy.ui.fragment.home.viewstate.HomeViewState
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
    val itemEntitiesLiveData = _itemEntitiesLiveData.asLiveData()

    private val _allItemWithCategoryEntity = MutableLiveData<List<ItemWithCategoryEntity>>()
    val allItemWithCategoryEntity = _allItemWithCategoryEntity.asLiveData()

    // home page
    var currentSort = HomeViewState.Sort.NONE
        set(value) {
            field = value
            updateHomeViewState(allItemWithCategoryEntity.value!!)
        }

    private val _homeViewStateLiveData = MutableLiveData<HomeViewState>()
    val homeViewStateLiveData = _homeViewStateLiveData.asLiveData()

    private val _categoriesViewStateLiveData = MutableLiveData<CategoryViewState>()
    val categoriesViewState = _categoriesViewStateLiveData.asLiveData()

    private val _transactionInsertLiveData = MutableLiveData<Event<Boolean>>()
    val transactionInsertLiveData = _transactionInsertLiveData.asLiveData()

    private val _transactionUpdateLiveData = MutableLiveData<Event<Boolean>>()
    val transactionUpdateLiveData = _transactionUpdateLiveData.asLiveData()

    private val _categoriesLiveData = MutableLiveData<List<CategoryEntity>>()
    val categoriesLiveData = _categoriesLiveData.asLiveData()

    private val _transactionAddCategoryLiveData = MutableLiveData<Event<Boolean>>()
    val transactionAddCategoryLiveData = _transactionAddCategoryLiveData.asLiveData()

    init {

        initCoroutineScope()

        coroutineScope.launch {
            toBuyRepo.getAllItemWithCategoryEntity().collect { items ->
                _allItemWithCategoryEntity.postValue(items)

                updateHomeViewState(items)
            }

        }

        viewModelScope.launch {
            toBuyRepo.getAllCategories().collect { categories ->
                _categoriesLiveData.postValue(categories)
            }
        }
    }

    private fun updateHomeViewState(items: List<ItemWithCategoryEntity>) {

        val dataList = ArrayList<HomeViewState.DataItem<*>>()

        when (currentSort) {

            HomeViewState.Sort.NONE -> {

                var currentPriority = -1

                items
                    .sortedByDescending { it.itemEntity.priority }
                    .forEach { itemWithCategory ->

                        if (currentPriority != itemWithCategory.itemEntity.priority) {

                            currentPriority = itemWithCategory.itemEntity.priority

                            val headerItem =
                                HomeViewState.DataItem(
                                    data = headerTextBasedOnPriority(currentPriority),
                                    isHeader = true
                                )

                            dataList.add(headerItem)
                        }

                        val dataItem = HomeViewState.DataItem(data = itemWithCategory)
                        dataList.add(dataItem)

                    }
            }

            HomeViewState.Sort.CATEGORY -> {

                var currentCategoryId = "no_id"

                items
                    .sortedByDescending { it.itemEntity.priority }
                    .forEach { itemWithCategory ->

                        if (currentCategoryId != itemWithCategory.itemEntity.categoryId) {

                            currentCategoryId = itemWithCategory.itemEntity.categoryId

                            val headerItem =
                                HomeViewState.DataItem(
                                    data = itemWithCategory.categoryEntity?.name ?: "Unknown",
                                    isHeader = true
                                )

                            dataList.add(headerItem)
                        }

                        val dataItem = HomeViewState.DataItem(data = itemWithCategory)
                        dataList.add(dataItem)

                    }
            }

            HomeViewState.Sort.OLDEST -> {

                val headerItem = HomeViewState.DataItem("Oldest", isHeader = true)
                dataList.add(headerItem)

                items.sortedBy { it.itemEntity.createdAt }.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }

            HomeViewState.Sort.NEWEST -> {

                val headerItem = HomeViewState.DataItem("Newest", isHeader = true)
                dataList.add(headerItem)

                items.sortedByDescending { it.itemEntity.createdAt }.forEach {
                    val dataItem = HomeViewState.DataItem(data = it)
                    dataList.add(dataItem)
                }
            }

        }

        _homeViewStateLiveData.postValue(
            HomeViewState(
                dataList = dataList,
                isLoading = false,
                sort = currentSort
            )
        )
    }

    private fun headerTextBasedOnPriority(priority: Int): String {

        return when (priority) {
            1 -> "Low"
            2 -> "Medium"
            else -> "High"
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

        // Default category (un-selecting a category)
        viewStateItemList.add(
            CategoryViewState.Item(
                categoryEntity = CategoryEntity(name = "None"),
                isSelected = categoryId == ""
            )
        )

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