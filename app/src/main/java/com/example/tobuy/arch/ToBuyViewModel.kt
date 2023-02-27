package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tobuy.room.entity.ItemEntity
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

    init {
        initCoroutineScope()
        coroutineScope.launch {
            toBuyRepo.getAllItems().collect { items ->
                _itemEntitiesLiveData.postValue(items)
            }
        }
    }

    fun insertItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.insertItem(itemEntity)
        }
    }

    fun deleteItem(itemEntity: ItemEntity) {
        coroutineScope.launch {
            toBuyRepo.deleteItem(itemEntity)
        }
    }

    private fun initCoroutineScope() {
        coroutineScope = CoroutineScope(Dispatchers.Default)
    }

    override fun onCleared() {
        super.onCleared()
        cancelTheCoroutineScope()
    }

    private fun cancelTheCoroutineScope() {
        if (::coroutineScope.isInitialized)
            coroutineScope.cancel()
    }

}