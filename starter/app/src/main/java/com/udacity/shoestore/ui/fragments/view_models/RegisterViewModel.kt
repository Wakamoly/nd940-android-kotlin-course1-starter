package com.udacity.shoestore.ui.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseViewModel
import com.udacity.shoestore.db.entities.UserEntity
import com.udacity.shoestore.models.LoginResponse
import com.udacity.shoestore.repositories.RegisterRepository
import kotlinx.coroutines.launch

class RegisterViewModel (
    private val repository: RegisterRepository
) : BaseViewModel(repository) {

    private val _loginResponse : MutableLiveData<Resource<UserEntity>> = MutableLiveData()
    val loginResponse: LiveData<Resource<UserEntity>>
        get() = _loginResponse

    fun login (
        email: String,
        password: String,
        username: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(email, password, username)
    }


    /**
     *
     * using the same function for both, since we aren't actually getting info from a server
     *
     */

    private val _regResponse : MutableLiveData<Resource<UserEntity>> = MutableLiveData()
    val regResponse: LiveData<Resource<UserEntity>>
        get() = _regResponse

    fun register (
        email: String,
        password: String,
        username: String
    ) = viewModelScope.launch {
        _regResponse.value = Resource.Loading
        _regResponse.value = repository.login(email, password, username)
    }

}