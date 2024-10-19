package com.example.connectapi.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Product (
    val id : Int,
    val title : String,
    val brand : String?,
    val description : String,
    val category : String,
    val price : Double,
    val stock : Int,
    val thumbnail : String
) : Parcelable