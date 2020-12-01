package com.udacity.shoestore.ui.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseViewModel
import com.udacity.shoestore.db.entities.ShoeEntity
import com.udacity.shoestore.repositories.ShoeListRepository
import kotlinx.coroutines.launch

class ShoeListViewModel (
    private val repository: ShoeListRepository
) : BaseViewModel(repository) {

    private val _shoes : MutableLiveData<Resource<List<ShoeEntity>>> = MutableLiveData()
    val shoes: LiveData<Resource<List<ShoeEntity>>>
        get() = _shoes

    fun getShoes() = viewModelScope.launch {
        _shoes.value = Resource.Loading
        _shoes.value = repository.getShoes()
    }

}