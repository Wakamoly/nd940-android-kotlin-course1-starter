package com.udacity.shoestore.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.shoestore.db.entities.ShoeEntity

@Dao
interface ShoeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoeEntity: ShoeEntity) : Long

    @Query("SELECT * FROM shoe_table ORDER BY id DESC")
    suspend fun get(): List<ShoeEntity>

}