package com.example.connectapi.data.model

data class Reviews (
    val rating : Int,
    val comment : String,
    val date : String,
    val reviewerName : String,
    val reviewerEmail: String
)