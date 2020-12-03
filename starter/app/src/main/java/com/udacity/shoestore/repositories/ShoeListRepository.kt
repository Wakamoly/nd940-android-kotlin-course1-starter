package com.udacity.shoestore.repositories

import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseRepository
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.entities.ShoeEntity

class ShoeListRepository (
    private val shoeDao: ShoeDao
) : BaseRepository() {

    suspend fun getShoes() : Resource<List<ShoeEntity>> = safeApiCall { shoeDao.get() }

    suspend fun saveShoe(shoeEntity: ShoeEntity) = safeApiCall { shoeDao.insert(shoeEntity) }

}