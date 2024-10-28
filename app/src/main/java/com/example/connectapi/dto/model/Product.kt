package com.example.connectapi.dto.model

data class Product (
    val id : Int,
    val title : String,
    val brand : String?,
    val description : String,
    val category : String,
    val price : Double,
    val stock : Int,
    val thumbnail : String,
    val rating : Double,
    val sku : String,
    val dimension : List<Dimension>,
    val reviews : List<Reviews>,
    val images : List<String>
)