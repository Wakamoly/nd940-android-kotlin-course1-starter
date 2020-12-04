package com.udacity.shoestore.ui.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseViewModel
import com.udacity.shoestore.db.entities.ShoeEntity
import com.udacity.shoestore.repositories.ShoeListRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class ShoeListViewModel (
    private val repository: ShoeListRepository
) : BaseViewModel(repository) {

    private val _shoes : MutableLiveData<Resource<List<ShoeEntity>>> = MutableLiveData()
    val shoes: LiveData<Resource<List<ShoeEntity>>>
        get() = _shoes

    private val _result : MutableLiveData<Resource<*>> = MutableLiveData()
    val result: LiveData<Resource<*>>
        get() = _result

    private val _saveFinishedEvent = MutableLiveData<Boolean>()
    val saveFinishedEvent : LiveData<Boolean>
        get() = _saveFinishedEvent


    init {
        _saveFinishedEvent.value = false
    }


    fun getShoes() = viewModelScope.launch {
        _shoes.value = Resource.Loading
        _shoes.value = repository.getShoes()
    }


    fun saveShoe(shoeEntity: ShoeEntity) = viewModelScope.launch {
        _result.value = Resource.Loading
        _result.value = repository.saveShoe(shoeEntity)
    }

    fun updateShoe(shoeEntity: ShoeEntity, id: Int) = viewModelScope.launch {
        _result.value = Resource.Loading
        Timber.d("ID of shoe updated: $id")
        _result.value = repository.updateShoe(shoeEntity, id)
    }

    fun saveFinished(){
        _saveFinishedEvent.value = true
        _result.value = null
        _saveFinishedEvent.value = false
    }

}