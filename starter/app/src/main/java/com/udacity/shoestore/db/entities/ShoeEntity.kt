package com.udacity.shoestore.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoe_table")
data class ShoeEntity(
    var name: String,
    var size: Double,
    var company: String,
    var description: String,
    val images: List<String> = mutableListOf()
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
