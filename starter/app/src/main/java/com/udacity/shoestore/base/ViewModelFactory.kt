@file:Suppress("UNCHECKED_CAST")

package com.udacity.shoestore.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.shoestore.repositories.RegisterRepository
import com.udacity.shoestore.ui.fragments.view_models.RegisterViewModel

class ViewModelFactory(
        private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository as RegisterRepository) as T
            else -> throw IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}