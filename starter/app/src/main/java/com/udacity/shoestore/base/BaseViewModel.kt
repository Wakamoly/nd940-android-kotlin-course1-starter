package com.udacity.shoestore.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel (
    private val repository: BaseRepository
): ViewModel()