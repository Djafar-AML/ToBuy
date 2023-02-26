package com.example.tobuy.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tobuy.room.entity.ItemEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToBuyViewModel @Inject constructor(private val toBuyRepo: ToButRepo) : ViewModel() {

    private val _itemEntitiesLiveData = MutableLiveData<List<ItemEntity>>()
    val itemEntitiesLiveData: LiveData<List<ItemEntity>> = _itemEntitiesLiveData

    init {

        viewModelScope.launch {

            // TODO: call db here 
//            val items = toBuyRepo.getAllItems()
//            _itemEntitiesLiveData.postValue(items)
        }
    }

    fun insertItem(itemEntity: ItemEntity) {
        toBuyRepo.insertItem(itemEntity)
    }

    fun deleteItem(itemEntity: ItemEntity) {
        toBuyRepo.deleteItem(itemEntity)
    }
}