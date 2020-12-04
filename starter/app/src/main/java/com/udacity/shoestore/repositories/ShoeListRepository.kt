package com.udacity.shoestore.repositories

import com.google.gson.Gson
import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseRepository
import com.udacity.shoestore.db.DbConverters
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.entities.ShoeEntity

class ShoeListRepository (
    private val shoeDao: ShoeDao
) : BaseRepository() {

    suspend fun getShoes() : Resource<List<ShoeEntity>> = safeApiCall { shoeDao.get() }

    suspend fun saveShoe(shoeEntity: ShoeEntity) = safeApiCall { shoeDao.insert(shoeEntity) }

    suspend fun updateShoe(shoeEntity: ShoeEntity, id: Int): Resource<Int> {
        val name = shoeEntity.name
        val size = shoeEntity.size
        val company = shoeEntity.company
        val description = shoeEntity.description
        val images = Gson().toJson(shoeEntity.images)
        return safeApiCall { shoeDao.update(
            name,
            size,
            company,
            description,
            images,
            id
        )
        }
    }

}