package com.example.connectapi.data.service

import com.example.connectapi.data.model.Product
import com.example.connectapi.data.model.Products
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): Response<Products>

    @GET("products/{id}")
    fun getProductById(@Path("id") id: Int): Call<Product>
}