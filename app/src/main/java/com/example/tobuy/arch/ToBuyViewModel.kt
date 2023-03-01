package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.room.entity.CategoryEntity
import com.example.tobuy.room.entity.ItemEntity
import com.example.tobuy.ui.fragment.add.isOnItemSelectEdit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToBuyViewModel @Inject constructor(private val toBuyRepo: ToButRepo) : ViewModel() {

    private lateinit var coroutineScope: CoroutineScope

    private val _itemEntitiesLiveData = MutableLiveData<List<ItemEntity>>()
    val itemEntitiesLiveData: LiveData<List<ItemEntity>> = _itemEntitiesLiveData

    private val _transactionInsertLiveData = MutableLiveData<Boolean>()
    val transactionInsertLiveData: LiveData<Boolean> = _transactionInsertLiveData

    private val _transactionUpdateLiveData = MutableLiveData<Boolean>()
    val transactionUpdateLiveData: LiveData<Boolean> = _transactionUpdateLiveData

    private val _categoriesLiveData = MutableLiveData<List<CategoryEntity>>()
    val categoriesLiveData: LiveData<List<CategoryEntity>> = _categoriesLiveData

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
                _transactionInsertLiveData.postValue(complete)
            }
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.deleteItem(itemEntity)
        }

    }

    fun resetUpsertTransactionLiveDataState() {
        _transactionInsertLiveData.postValue(false)
        _transactionUpdateLiveData.postValue(false)
    }

    fun updateItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.updateItem(itemEntity) { isSuccess ->
                if (isOnItemSelectEdit)
                    _transactionUpdateLiveData.postValue(isSuccess)
            }
        }

    }
    //endregion

    // region Category entity

    fun insertCategory(categoryEntity: CategoryEntity) {
        coroutineScope.launch {
            toBuyRepo.insertCategory(categoryEntity) { complete ->
                _transactionInsertLiveData.postValue(complete)
            }
        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        coroutineScope.launch {
            toBuyRepo.deleteCategory(categoryEntity)
        }

    }

    fun updateCategory(categoryEntity: CategoryEntity) {
        coroutineScope.launch {
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