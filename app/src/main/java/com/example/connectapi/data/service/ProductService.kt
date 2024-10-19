package com.example.connectapi.data.service

import com.example.connectapi.data.model.Product
import com.example.connectapi.data.model.Products
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("products")
    fun getAll(): Call<Products>

    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int): Call<Product>
}