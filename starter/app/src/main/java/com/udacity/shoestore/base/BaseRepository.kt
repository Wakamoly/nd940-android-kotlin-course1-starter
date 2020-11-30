package com.udacity.shoestore.base

import com.udacity.shoestore.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
            apiCall: suspend () -> T
    ) : Resource<T> {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(apiCall.invoke())
            }catch(throwable: Throwable){
                Resource.Failure(true, null, null)
            }
        }
    }

}