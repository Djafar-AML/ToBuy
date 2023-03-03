package com.example.tobuy.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


fun <T> MutableLiveData<T>.asLiveData( ): LiveData<T> = this