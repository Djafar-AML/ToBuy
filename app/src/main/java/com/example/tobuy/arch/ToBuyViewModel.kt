package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.room.entity.CategoryEntity
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.ui.fragment.home.add.isOnItemSelectEdit
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
            toBuyRepo.getAllItems().collect { items ->
                _itemEntitiesLiveData.postValue(items)
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

    private fun cancelTheCoroutineScope() {
        if (::coroutineScope.isInitialized)
            coroutineScope.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        cancelTheCoroutineScope()
    }

}